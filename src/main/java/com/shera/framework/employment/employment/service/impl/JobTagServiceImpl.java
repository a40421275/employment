package com.shera.framework.employment.employment.service.impl;

import com.shera.framework.employment.employment.dto.JobTagDTO;
import com.shera.framework.employment.employment.dto.JobTagDetailDTO;
import com.shera.framework.employment.employment.dto.JobTagQueryDTO;
import com.shera.framework.employment.employment.entity.JobTag;
import com.shera.framework.employment.employment.repository.JobTagRepository;
import com.shera.framework.employment.employment.service.JobTagService;
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
 * 岗位标签服务实现类
 */
@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {
    
    private final JobTagRepository jobTagRepository;
    
    @Override
    @Transactional
    public JobTag createTag(String tagName, Long jobId) {
        // 如果提供了jobId，检查标签是否已存在
        if (jobId != null) {
            JobTag existingTag = jobTagRepository.findByJobIdAndTagName(jobId, tagName);
            if (existingTag != null) {
                return existingTag;
            }
        }
        
        JobTag jobTag = new JobTag();
        jobTag.setJobId(jobId); // jobId可以为null
        jobTag.setTagName(tagName);
        return jobTagRepository.save(jobTag);
    }
    
    @Override
    @Transactional
    public JobTag addTagToJob(Long jobId, String tagName) {
        // 检查标签是否已存在
        JobTag existingTag = jobTagRepository.findByJobIdAndTagName(jobId, tagName);
        if (existingTag != null) {
            return existingTag;
        }
        
        JobTag jobTag = new JobTag();
        jobTag.setJobId(jobId);
        jobTag.setTagName(tagName);
        return jobTagRepository.save(jobTag);
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
        JobTag jobTag = jobTagRepository.findByJobIdAndTagName(jobId, tagName);
        if (jobTag != null) {
            jobTagRepository.delete(jobTag);
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
        return jobTagRepository.findByJobId(jobId);
    }
    
    @Override
    public List<String> getTagNamesByJobId(Long jobId) {
        return jobTagRepository.findTagNamesByJobId(jobId);
    }
    
    @Override
    public List<Long> getJobIdsByTagName(String tagName) {
        return jobTagRepository.findJobIdsByTagNameContaining(tagName);
    }
    
    @Override
    public List<Long> getJobIdsByTagNames(List<String> tagNames) {
        return jobTagRepository.findJobIdsByTagNames(tagNames);
    }
    
    @Override
    public List<Long> searchJobIdsByTagName(String tagName) {
        return jobTagRepository.findJobIdsByTagNameContaining(tagName);
    }
    
    @Override
    public List<Object[]> getHotTags(int limit) {
        return jobTagRepository.findHotTags(limit);
    }
    
    @Override
    public Map<String, Long> countTagsUsage() {
        List<Object[]> tagCounts = jobTagRepository.countByTagName();
        Map<String, Long> result = new HashMap<>();
        for (Object[] tagCount : tagCounts) {
            result.put((String) tagCount[0], (Long) tagCount[1]);
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
        return jobTagRepository.findByJobIdAndTagName(jobId, tagName) != null;
    }
    
    @Override
    @Transactional
    public JobTag updateTag(Long tagId, JobTagDTO jobTagDTO) {
        Optional<JobTag> optionalJobTag = jobTagRepository.findById(tagId);
        if (optionalJobTag.isPresent()) {
            JobTag jobTag = optionalJobTag.get();
            jobTag.setTagName(jobTagDTO.getTagName());
            
            // 更新岗位ID（可以为null）
            jobTag.setJobId(jobTagDTO.getJobId());
            
            return jobTagRepository.save(jobTag);
        }
        throw new RuntimeException("标签不存在: " + tagId);
    }
    
    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        jobTagRepository.deleteById(tagId);
    }
    
    @Override
    @Transactional
    public void batchDeleteTags(List<Long> tagIds) {
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
                queryDTO.getJobId(),
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
                    if (match && queryDTO.getJobId() != null) {
                        match = queryDTO.getJobId().equals(tag.getJobId());
                    }
                    if (match && Boolean.TRUE.equals(queryDTO.getIndependentOnly())) {
                        match = tag.getJobId() == null;
                    }
                    if (match && Boolean.TRUE.equals(queryDTO.getAssociatedOnly())) {
                        match = tag.getJobId() != null;
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
                queryDTO.getTagType(),
                queryDTO.getJobId(),
                queryDTO.getIndependentOnly(),
                queryDTO.getAssociatedOnly()
        );
    }
    
    /**
     * 构建分页和排序信息
     */
    private Pageable buildPageable(JobTagQueryDTO queryDTO) {
        // 如果提供了pageable，直接使用
        if (queryDTO.getPageable() != null) {
            return queryDTO.getPageable();
        }
        
        // 构建默认分页（第一页，每页20条）
        int page = 0;
        int size = 20;
        
        // 构建排序
        Sort sort = buildSort(queryDTO);
        
        return PageRequest.of(page, size, sort);
    }
    
    /**
     * 构建排序信息
     */
    private Sort buildSort(JobTagQueryDTO queryDTO) {
        String sortField = queryDTO.getSortField();
        Sort.Direction direction = queryDTO.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // 根据排序字段构建排序
        switch (sortField) {
            case "createTime":
                return Sort.by(direction, "createTime");
            case "usageCount":
                // 使用次数排序需要特殊处理，这里先按标签名称排序
                return Sort.by(direction, "tagName");
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
        detailDTO.setJobId(jobTag.getJobId());
        detailDTO.setIndependent(jobTag.getJobId() == null);
        
        // 设置使用次数（需要从统计中获取）
        Map<String, Long> tagUsage = countTagsUsage();
        detailDTO.setUsageCount(tagUsage.getOrDefault(jobTag.getTagName(), 0L));
        
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
        detailDTO.setIndependent(true);
        
        // 设置默认颜色
        detailDTO.setTagColor("#4ecdc4"); // 默认系统标签颜色
        
        // 设置使用次数
        Map<String, Long> tagUsage = countTagsUsage();
        detailDTO.setUsageCount(tagUsage.getOrDefault(tagName, 0L));
        
        return detailDTO;
    }

    @Override
    @Transactional
    public JobTag createTagWithDetails(JobTagDTO jobTagDTO) {
        // 如果提供了jobId，检查标签是否已存在
        if (jobTagDTO.getJobId() != null) {
            JobTag existingTag = jobTagRepository.findByJobIdAndTagName(jobTagDTO.getJobId(), jobTagDTO.getTagName());
            if (existingTag != null) {
                return existingTag;
            }
        }
        
        JobTag jobTag = new JobTag();
        jobTag.setJobId(jobTagDTO.getJobId()); // jobId可以为null
        jobTag.setTagName(jobTagDTO.getTagName());
        jobTag.setTagType(jobTagDTO.getTagType() != null ? jobTagDTO.getTagType() : 1); // 默认为自定义标签
        jobTag.setTagColor(jobTagDTO.getTagColor());
        jobTag.setDescription(jobTagDTO.getDescription());
        
        return jobTagRepository.save(jobTag);
    }
}
