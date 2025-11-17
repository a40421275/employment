package com.shera.framework.employment.employment.modules.job.service.impl;

import com.shera.framework.employment.employment.modules.job.dto.JobTagDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobTagDetailDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobTagQueryDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobTag;
import com.shera.framework.employment.employment.modules.job.entity.JobTagRelation;
import com.shera.framework.employment.employment.modules.job.repository.JobTagRepository;
import com.shera.framework.employment.employment.modules.job.repository.JobTagRelationRepository;
import com.shera.framework.employment.employment.modules.job.service.JobTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位标签服务实现类（多对多关系版本）
 */
@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {
    
    private final JobTagRepository jobTagRepository;
    private final JobTagRelationRepository jobTagRelationRepository;
    
    @Override
    @Transactional
    public JobTag createTag(String tagName) {
        // 检查标签是否已存在
        List<JobTag> existingTags = jobTagRepository.findByTagName(tagName);
        if (!existingTags.isEmpty()) {
            return existingTags.get(0);
        }
        
        JobTag jobTag = new JobTag();
        jobTag.setTagName(tagName);
        jobTag.setTagType(1); // 默认为自定义标签
        jobTag.setTagColor(generateRandomColor());
        jobTag.setDescription("自定义标签: " + tagName);
        return jobTagRepository.save(jobTag);
    }
    
    @Override
    @Transactional
    public JobTag addTagToJob(Long jobId, String tagName) {
        // 检查标签是否已存在
        List<JobTag> existingTags = jobTagRepository.findByTagName(tagName);
        JobTag tag;
        
        if (existingTags.isEmpty()) {
            // 创建新标签
            tag = new JobTag();
            tag.setTagName(tagName);
            tag.setTagType(1); // 自定义标签
            tag.setTagColor(generateRandomColor());
            tag.setDescription("自定义标签: " + tagName);
            tag = jobTagRepository.save(tag);
        } else {
            // 使用现有标签
            tag = existingTags.get(0);
        }
        
        // 检查关联关系是否已存在
        if (!jobTagRelationRepository.existsByJobIdAndTagId(jobId, tag.getId())) {
            // 创建关联关系
            JobTagRelation relation = new JobTagRelation();
            relation.setJobId(jobId);
            relation.setTagId(tag.getId());
            jobTagRelationRepository.save(relation);
            
            // 更新标签使用次数
            tag.setUseCount(tag.getUseCount() + 1);
            jobTagRepository.save(tag);
        }
        
        return tag;
    }
    
    @Override
    @Transactional
    public List<JobTag> addTagsToJob(Long jobId, List<String> tagNames) {
        List<JobTag> result = new ArrayList<>();
        for (String tagName : tagNames) {
            JobTag jobTag = addTagToJob(jobId, tagName);
            result.add(jobTag);
        }
        return result;
    }
    
    @Override
    @Transactional
    public void removeTagFromJob(Long jobId, String tagName) {
        // 查找标签
        List<JobTag> tags = jobTagRepository.findByTagName(tagName);
        if (!tags.isEmpty()) {
            JobTag tag = tags.get(0);
            // 删除关联关系
            jobTagRelationRepository.deleteByJobIdAndTagId(jobId, tag.getId());
            
            // 更新标签使用次数
            if (tag.getUseCount() > 0) {
                tag.setUseCount(tag.getUseCount() - 1);
                jobTagRepository.save(tag);
            }
        }
    }
    
    @Override
    @Transactional
    public void removeTagsFromJob(Long jobId, List<String> tagNames) {
        for (String tagName : tagNames) {
            removeTagFromJob(jobId, tagName);
        }
    }
    
    @Override
    public List<JobTag> getTagsByJobId(Long jobId) {
        // 获取岗位的所有关联关系
        List<JobTagRelation> relations = jobTagRelationRepository.findByJobId(jobId);
        // 获取对应的标签
        List<Long> tagIds = relations.stream()
                .map(JobTagRelation::getTagId)
                .collect(Collectors.toList());
        return jobTagRepository.findAllById(tagIds);
    }
    
    @Override
    public List<String> getTagNamesByJobId(Long jobId) {
        List<JobTag> tags = getTagsByJobId(jobId);
        return tags.stream()
                .map(JobTag::getTagName)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Long> getJobIdsByTagName(String tagName) {
        // 查找标签
        List<JobTag> tags = jobTagRepository.findByTagName(tagName);
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        
        JobTag tag = tags.get(0);
        // 获取关联关系
        List<JobTagRelation> relations = jobTagRelationRepository.findByTagId(tag.getId());
        return relations.stream()
                .map(JobTagRelation::getJobId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Long> getJobIdsByTagNames(List<String> tagNames) {
        // 查找所有标签
        List<JobTag> tags = jobTagRepository.findByTagNameIn(tagNames);
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 获取所有标签ID
        List<Long> tagIds = tags.stream()
                .map(JobTag::getId)
                .collect(Collectors.toList());
        
        // 获取关联关系
        List<JobTagRelation> relations = jobTagRelationRepository.findByTagIdIn(tagIds);
        return relations.stream()
                .map(JobTagRelation::getJobId)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Long> searchJobIdsByTagName(String tagName) {
        // 查找包含关键词的标签
        List<JobTag> tags = jobTagRepository.findAll().stream()
                .filter(tag -> tag.getTagName().contains(tagName))
                .collect(Collectors.toList());
        
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 获取所有标签ID
        List<Long> tagIds = tags.stream()
                .map(JobTag::getId)
                .collect(Collectors.toList());
        
        // 获取关联关系
        List<JobTagRelation> relations = jobTagRelationRepository.findByTagIdIn(tagIds);
        return relations.stream()
                .map(JobTagRelation::getJobId)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Object[]> getHotTags(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<JobTag> hotTags = jobTagRepository.findHotTags(pageable);
        return hotTags.stream()
                .map(tag -> new Object[]{tag.getTagName(), tag.getUseCount()})
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Long> countTagsUsage() {
        List<JobTag> allTags = jobTagRepository.findAll();
        Map<String, Long> result = new HashMap<>();
        for (JobTag tag : allTags) {
            result.put(tag.getTagName(), (long) tag.getUseCount());
        }
        return result;
    }
    
    @Override
    public List<String> getAllTags() {
        List<JobTag> allTags = jobTagRepository.findAll();
        return allTags.stream()
                .map(JobTag::getTagName)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getSystemTags() {
        return Arrays.stream(JobTag.CommonTags.values())
                .map(JobTag.CommonTags::getName)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isTagExists(Long jobId, String tagName) {
        // 查找标签
        List<JobTag> tags = jobTagRepository.findByTagName(tagName);
        if (tags.isEmpty()) {
            return false;
        }
        
        JobTag tag = tags.get(0);
        // 检查关联关系是否存在
        return jobTagRelationRepository.existsByJobIdAndTagId(jobId, tag.getId());
    }
    
    @Override
    @Transactional
    public JobTag updateTag(Long tagId, JobTagDTO jobTagDTO) {
        Optional<JobTag> optionalJobTag = jobTagRepository.findById(tagId);
        if (optionalJobTag.isPresent()) {
            JobTag jobTag = optionalJobTag.get();
            jobTag.setTagName(jobTagDTO.getTagName());
            jobTag.setTagType(jobTagDTO.getTagType());
            jobTag.setTagColor(jobTagDTO.getTagColor());
            jobTag.setDescription(jobTagDTO.getDescription());
            return jobTagRepository.save(jobTag);
        }
        throw new RuntimeException("标签不存在: " + tagId);
    }
    
    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        // 先删除所有关联关系
        jobTagRelationRepository.deleteByTagId(tagId);
        // 再删除标签
        jobTagRepository.deleteById(tagId);
    }
    
    @Override
    @Transactional
    public void batchDeleteTags(List<Long> tagIds) {
        // 先删除所有关联关系
        jobTagRelationRepository.deleteByTagIdIn(tagIds);
        // 再删除标签
        jobTagRepository.deleteAllById(tagIds);
    }
    
    @Override
    public List<JobTag> getTagsByType(Integer tagType) {
        List<JobTag> allTags = jobTagRepository.findAll();
        return allTags.stream()
                .filter(tag -> tagType.equals(tag.getTagType()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getTagStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总标签数
        long totalTags = jobTagRepository.count();
        stats.put("totalTags", totalTags);
        
        // 热门标签
        List<Object[]> hotTags = getHotTags(10);
        stats.put("hotTags", hotTags);
        
        // 标签使用统计
        Map<String, Long> tagUsage = countTagsUsage();
        stats.put("tagUsage", tagUsage);
        
        // 系统标签数量
        List<String> systemTags = getSystemTags();
        stats.put("systemTagCount", systemTags.size());
        
        // 自定义标签数量
        long customTagCount = totalTags - systemTags.size();
        stats.put("customTagCount", customTagCount);
        
        return stats;
    }
    
    @Override
    public Page<JobTagDetailDTO> getAllTagsWithPagination(JobTagQueryDTO queryDTO) {
        // 构建分页和排序
        Pageable pageable = buildPageable(queryDTO);
        
        // 调用Repository查询标签实体
        Page<JobTag> jobTagsPage = jobTagRepository.findByConditions(
                queryDTO.getTagName(),
                queryDTO.getTagType(),
                pageable
        );
        
        // 转换为JobTagDetailDTO
        return jobTagsPage.map(this::convertToDetailDTO);
    }
    
    @Override
    public Page<JobTagDetailDTO> getSystemTagsWithPagination(JobTagQueryDTO queryDTO) {
        // 获取系统标签名称
        List<String> systemTagNames = getSystemTags();
        
        // 应用筛选条件
        List<String> filteredTagNames = systemTagNames.stream()
                .filter(tag -> queryDTO.getTagName() == null || tag.contains(queryDTO.getTagName()))
                .collect(Collectors.toList());
        
        // 构建分页
        Pageable pageable = buildPageable(queryDTO);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredTagNames.size());
        
        // 转换为JobTagDetailDTO
        List<JobTagDetailDTO> detailDTOs = filteredTagNames.subList(start, end).stream()
                .map(this::createSystemTagDetailDTO)
                .collect(Collectors.toList());
        
        // 返回分页结果
        return new org.springframework.data.domain.PageImpl<>(
                detailDTOs,
                pageable,
                filteredTagNames.size()
        );
    }
    
    @Override
    public List<JobTagDetailDTO> getAllTagDetails(JobTagQueryDTO queryDTO) {
        // 查询所有符合条件的标签
        List<JobTag> jobTags = jobTagRepository.findAll();
        
        // 应用筛选条件
        List<JobTag> filteredTags = jobTags.stream()
                .filter(tag -> {
                    boolean match = true;
                    if (queryDTO.getTagName() != null) {
                        match = tag.getTagName().contains(queryDTO.getTagName());
                    }
                    if (match && queryDTO.getTagType() != null) {
                        match = tag.getTagType().equals(queryDTO.getTagType());
                    }
                    return match;
                })
                .collect(Collectors.toList());
        
        // 转换为JobTagDetailDTO
        return filteredTags.stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Long countTagsByConditions(JobTagQueryDTO queryDTO) {
        return jobTagRepository.countDistinctTagNamesByConditions(
                queryDTO.getTagName(),
                queryDTO.getTagType()
        );
    }
    
    /**
     * 构建分页和排序信息
     */
    private Pageable buildPageable(JobTagQueryDTO queryDTO) {
        // 使用传入的分页参数，如果没有则使用默认值
        int page = queryDTO.getPageable() != null ? queryDTO.getPageable().getPageNumber() : 0;
        int size = queryDTO.getPageable() != null ? queryDTO.getPageable().getPageSize() : 20;
        
        // 构建排序
        Sort sort = buildSort(queryDTO);
        
        return PageRequest.of(page, size, sort);
    }
    
    /**
     * 构建排序信息
     */
    private Sort buildSort(JobTagQueryDTO queryDTO) {
        String sortBy = queryDTO.getSortBy();
        Sort.Direction direction = "desc".equalsIgnoreCase(queryDTO.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // 根据排序字段构建排序
        switch (sortBy) {
            case "createTime":
                return Sort.by(direction, "createTime");
            case "usageCount":
                return Sort.by(direction, "useCount");
            case "tagName":
            default:
                return Sort.by(direction, "tagName");
        }
    }
    
    /**
     * 将JobTag转换为JobTagDetailDTO
     */
    private JobTagDetailDTO convertToDetailDTO(JobTag jobTag) {
        JobTagDetailDTO detailDTO = new JobTagDetailDTO();
        detailDTO.setId(jobTag.getId());
        detailDTO.setTagName(jobTag.getTagName());
        detailDTO.setTagColor(jobTag.getTagColor());
        detailDTO.setTagType(jobTag.getTagType());
        detailDTO.setDescription(jobTag.getDescription());
        detailDTO.setCreateTime(jobTag.getCreateTime());
        detailDTO.setUsageCount((long) jobTag.getUseCount());
        
        // 设置系统标签标识
        List<String> systemTags = getSystemTags();
        detailDTO.setSystemTag(systemTags.contains(jobTag.getTagName()));
        
        return detailDTO;
    }
    
    /**
     * 为系统标签创建JobTagDetailDTO
     */
    private JobTagDetailDTO createSystemTagDetailDTO(String tagName) {
        JobTagDetailDTO detailDTO = new JobTagDetailDTO();
        detailDTO.setTagName(tagName);
        detailDTO.setTagType(0); // 系统标签
        detailDTO.setSystemTag(true);
        
        // 设置默认颜色
        detailDTO.setTagColor("#4ecdc4"); // 默认系统标签颜色
        
        // 设置使用次数（从实际标签中获取）
        List<JobTag> tags = jobTagRepository.findByTagName(tagName);
        if (!tags.isEmpty()) {
            detailDTO.setUsageCount((long) tags.get(0).getUseCount());
        } else {
            detailDTO.setUsageCount(0L);
        }
        
        return detailDTO;
    }

    @Override
    @Transactional
    public JobTag createTagWithDetails(JobTagDTO jobTagDTO) {
        // 检查标签是否已存在
        List<JobTag> existingTags = jobTagRepository.findByTagName(jobTagDTO.getTagName());
        if (!existingTags.isEmpty()) {
            return existingTags.get(0);
        }
        
        JobTag jobTag = new JobTag();
        jobTag.setTagName(jobTagDTO.getTagName());
        jobTag.setTagType(jobTagDTO.getTagType() != null ? jobTagDTO.getTagType() : 1); // 默认为自定义标签
        jobTag.setTagColor(jobTagDTO.getTagColor());
        jobTag.setDescription(jobTagDTO.getDescription());
        
        return jobTagRepository.save(jobTag);
    }
    
    /**
     * 生成随机颜色
     */
    private String generateRandomColor() {
        String[] colors = {
            "#ff6b6b", "#ff9e2c", "#4ecdc4", "#45b7d1", "#96ceb4", 
            "#feca57", "#ff9ff3", "#54a0ff", "#5f27cd", "#00d2d3",
            "#ff9f43", "#10ac84", "#0abde3", "#ee5a24", "#a3cb38",
            "#1289a7", "#d980fa", "#f368e0", "#ff9f43", "#ee5a24"
        };
        return colors[(int) (Math.random() * colors.length)];
    }
}
