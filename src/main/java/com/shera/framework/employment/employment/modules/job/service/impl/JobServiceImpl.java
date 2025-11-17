package com.shera.framework.employment.employment.modules.job.service.impl;

import com.shera.framework.employment.employment.modules.job.dto.JobDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobQueryDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.modules.job.entity.Job;
import com.shera.framework.employment.employment.modules.job.entity.JobTag;
import com.shera.framework.employment.employment.modules.job.entity.JobTagRelation;
import com.shera.framework.employment.employment.modules.job.repository.JobRepository;
import com.shera.framework.employment.employment.modules.job.repository.JobTagRepository;
import com.shera.framework.employment.employment.modules.job.repository.JobTagRelationRepository;
import com.shera.framework.employment.employment.modules.job.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 岗位服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    
    private final JobRepository jobRepository;
    private final JobTagRepository jobTagRepository;
    private final JobTagRelationRepository jobTagRelationRepository;
    
    @Override
    @Transactional
    public Job createJob(JobDTO jobDTO) {
        Job job = new Job();
        copyJobProperties(job, jobDTO);
        // 如果DTO中没有设置status，则默认为草稿状态(0)
        if (jobDTO.getStatus() == null) {
            job.setStatus(0); // 默认草稿状态
        }
        
        // 保存岗位
        Job savedJob = jobRepository.save(job);
        
        // 处理标签逻辑
        processJobTags(savedJob, jobDTO.getKeywords());
        
        return savedJob;
    }
    
    @Override
    @Transactional
    public Job updateJob(Long id, JobDTO jobDTO) {
        Job job = getJobById(id);
        copyJobProperties(job, jobDTO);
        
        // 保存岗位基本信息
        Job savedJob = jobRepository.save(job);
        
        // 处理标签逻辑 - 通过关键字字段先判断是否已存在关联，如果没有再建立关联
        processJobTagsWithAssociationCheck(savedJob, jobDTO.getKeywords());
        
        return savedJob;
    }
    
    @Override
    @Transactional
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        jobRepository.delete(job);
    }
    
    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位不存在: " + id));
    }
    
    @Override
    @Transactional
    public Job getJobDetail(Long id) {
        Job job = getJobById(id);
        // 每次查看详情时增加浏览次数
        increaseViewCount(id);
        // 这里可以加载关联的实体信息，如分类信息等
        return job;
    }
    
    @Override
    public Page<Job> getJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }
    
    @Override
    public Page<Job> getJobsByStatus(Integer status, Pageable pageable) {
        return jobRepository.findByStatus(status, pageable);
    }
    
    @Override
    public List<Job> getJobsByCategoryId(Long categoryId) {
        return jobRepository.findByCategoryId(categoryId);
    }
    
    @Override
    public List<Job> getJobsByWorkCity(String workCity) {
        return jobRepository.findByWorkCity(workCity);
    }
    
    @Override
    public List<Job> getJobsByJobType(Integer jobType) {
        return jobRepository.findByJobType(jobType);
    }
    
    @Override
    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchByKeyword(keyword, 1, pageable); // 只搜索已发布的岗位
    }
    
    @Override
    public Page<Job> filterJobs(Long categoryId, String workCity, Integer jobType, 
                               Double minSalary, Double maxSalary, Pageable pageable) {
        return jobRepository.findByMultipleConditions(categoryId, workCity, jobType, 
                                                     minSalary, maxSalary, 1, pageable); // 只筛选已发布的岗位
    }
    
    @Override
    @Transactional
    public Job publishJob(Long id) {
        Job job = getJobById(id);
        job.setStatus(1); // 已发布
        job.setPublishTime(LocalDateTime.now());
        return jobRepository.save(job);
    }
    
    @Override
    @Transactional
    public Job offlineJob(Long id) {
        Job job = getJobById(id);
        job.setStatus(2); // 已下架
        return jobRepository.save(job);
    }
    
    @Override
    @Transactional
    public Job archiveJob(Long id) {
        Job job = getJobById(id);
        job.setStatus(3); // 已归档
        return jobRepository.save(job);
    }
    
    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        // 使用原生SQL直接更新，避免锁表竞争
        jobRepository.incrementViewCount(id);
    }
    
    @Override
    @Transactional
    public void increaseApplyCount(Long id) {
        Job job = getJobById(id);
        job.setApplyCount(job.getApplyCount() + 1);
        jobRepository.save(job);
    }
    
    @Override
    public List<Job> getActiveJobs() {
        return jobRepository.findActiveJobs(LocalDateTime.now());
    }
    
    @Override
    public List<Job> getExpiringJobs() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(7); // 7天内即将过期的岗位
        return jobRepository.findExpiringJobs(now, end);
    }
    
    @Override
    public Map<Integer, Long> countJobsByStatus() {
        List<Object[]> results = jobRepository.countByStatus();
        Map<Integer, Long> countMap = new HashMap<>();
        for (Object[] result : results) {
            countMap.put((Integer) result[0], (Long) result[1]);
        }
        return countMap;
    }
    
    @Override
    public Map<String, Long> countJobsByCity() {
        List<Object[]> results = jobRepository.countByCity();
        Map<String, Long> countMap = new HashMap<>();
        for (Object[] result : results) {
            countMap.put((String) result[0], (Long) result[1]);
        }
        return countMap;
    }
    
    @Override
    public List<Job> getHotJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "viewCount"));
        return jobRepository.findByStatus(1, pageable).getContent(); // 只获取已发布的岗位
    }
    
    @Override
    public List<Job> getLatestJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishTime"));
        return jobRepository.findByStatus(1, pageable).getContent(); // 只获取已发布的岗位
    }
    
    /**
     * 复制JobDTO属性到Job实体
     */
    private void copyJobProperties(Job job, JobDTO jobDTO) {
        job.setTitle(jobDTO.getTitle());
        job.setCategoryId(jobDTO.getCategoryId());
        job.setCompanyId(jobDTO.getCompanyId());
        job.setDepartment(jobDTO.getDepartment());
        job.setJobType(jobDTO.getJobType());
        job.setSalaryMin(jobDTO.getSalaryMin());
        job.setSalaryMax(jobDTO.getSalaryMax());
        job.setSalaryUnit(jobDTO.getSalaryUnit());
        job.setWorkCity(jobDTO.getWorkCity());
        job.setWorkAddress(jobDTO.getWorkAddress());
        job.setDescription(jobDTO.getDescription());
        job.setRequirements(jobDTO.getRequirements());
        job.setBenefits(jobDTO.getBenefits());
        job.setContactInfo(jobDTO.getContactInfo());
        job.setExpireTime(jobDTO.getExpireTime());
        // 复制status字段
        if (jobDTO.getStatus() != null) {
            job.setStatus(jobDTO.getStatus());
        }
    }

    // ==================== 投影查询方法实现 ====================

    @Override
    public Optional<JobWithCompanyDTO> getJobWithCompanyById(Long jobId) {
        return jobRepository.findJobWithCompanyById(jobId);
    }

    @Override
    public Page<JobWithCompanyDTO> getJobsWithCompanyByStatus(Integer status, Pageable pageable) {
        return jobRepository.findJobsWithCompanyByStatus(status, pageable);
    }

    @Override
    public Page<JobWithCompanyDTO> getJobsWithCompanyByCompanyIdAndStatus(Long companyId, Integer status, Pageable pageable) {
        return jobRepository.findJobsWithCompanyByCompanyIdAndStatus(companyId, status, pageable);
    }

    @Override
    public Page<JobWithCompanyDTO> searchJobsWithCompanyByKeyword(String keyword, Integer status, Pageable pageable) {
        return jobRepository.searchJobsWithCompanyByKeyword(keyword, status, pageable);
    }

    @Override
    public Page<JobWithCompanyDTO> filterJobsWithCompanyByMultipleConditions(Long categoryId, String workCity, Integer jobType, 
                                                                             Double minSalary, Double maxSalary, Integer status, Pageable pageable) {
        return jobRepository.findJobsWithCompanyByMultipleConditions(categoryId, workCity, jobType, 
                                                                    minSalary, maxSalary, status, pageable);
    }

    @Override
    public Page<JobWithCompanyDTO> getJobsWithCompanyByCategoryId(Long categoryId, Integer status, Pageable pageable) {
        return jobRepository.findJobsWithCompanyByCategoryId(categoryId, status, pageable);
    }

    @Override
    public Page<JobWithCompanyDTO> getJobsWithCompanyByWorkCity(String workCity, Integer status, Pageable pageable) {
        return jobRepository.findJobsWithCompanyByWorkCity(workCity, status, pageable);
    }

    @Override
    public Page<JobWithCompanyDTO> getJobsWithCompanyByJobType(Integer jobType, Integer status, Pageable pageable) {
        return jobRepository.findJobsWithCompanyByJobType(jobType, status, pageable);
    }

    // ==================== 统一查询方法实现 ====================

    @Override
    public Page<JobWithCompanyDTO> queryJobs(JobQueryDTO queryDTO) {
        // 构建分页和排序
        Pageable pageable = PageRequest.of(
            queryDTO.getPage(), 
            queryDTO.getSize(), 
            Sort.by(queryDTO.getDirection(), queryDTO.getSort())
        );

        // 使用自定义的动态查询方法
        return jobRepository.findJobsWithCompanyByDynamicQuery(queryDTO, pageable);
    }

    // ==================== 标签处理方法 ====================

    /**
     * 处理岗位标签
     */
    private void processJobTags(Job job, String keywords) {
        if (keywords == null || keywords.trim().isEmpty()) {
            return;
        }

        // 按空格分割关键词
        String[] keywordArray = keywords.split("\\s+");
        
        for (String keyword : keywordArray) {
            if (keyword.trim().isEmpty()) {
                continue;
            }
            
            String tagName = keyword.trim();
            
            // 检查标签是否已存在（独立标签）
            List<JobTag> existingTags = jobTagRepository.findByTagName(tagName);
            JobTag tag;
            
            if (existingTags.isEmpty()) {
                // 创建新标签
                tag = new JobTag();
                tag.setTagName(tagName);
                tag.setTagType(1); // 自定义标签
                tag.setTagColor(generateRandomColor()); // 随机颜色
                tag.setDescription("自定义标签: " + tagName);
                tag = jobTagRepository.save(tag);
            } else {
                // 使用现有标签
                tag = existingTags.get(0);
            }
            
            // 创建岗位与标签的关联关系
            JobTagRelation relation = new JobTagRelation();
            relation.setJobId(job.getId());
            relation.setTagId(tag.getId());
            jobTagRelationRepository.save(relation);
            
            // 更新标签使用次数
            tag.setUseCount(tag.getUseCount() + 1);
            jobTagRepository.save(tag);
        }
    }

    /**
     * 处理岗位标签 - 通过关键字字段先判断是否已存在关联，如果没有再建立关联
     */
    private void processJobTagsWithAssociationCheck(Job job, String keywords) {
        if (keywords == null || keywords.trim().isEmpty()) {
            return;
        }

        // 按空格分割关键词
        String[] keywordArray = keywords.split("\\s+");
        
        for (String keyword : keywordArray) {
            if (keyword.trim().isEmpty()) {
                continue;
            }
            
            String tagName = keyword.trim();
            
            // 检查标签是否已存在（独立标签）
            List<JobTag> existingTags = jobTagRepository.findByTagName(tagName);
            JobTag tag;
            
            if (existingTags.isEmpty()) {
                // 创建新标签
                tag = new JobTag();
                tag.setTagName(tagName);
                tag.setTagType(1); // 自定义标签
                tag.setTagColor(generateRandomColor()); // 随机颜色
                tag.setDescription("自定义标签: " + tagName);
                tag = jobTagRepository.save(tag);
            } else {
                // 使用现有标签
                tag = existingTags.get(0);
            }
            
            // 关键优化：先判断是否已存在关联关系，如果没有再建立关联
            boolean associationExists = jobTagRelationRepository.existsByJobIdAndTagId(job.getId(), tag.getId());
            
            if (!associationExists) {
                // 创建岗位与标签的关联关系
                JobTagRelation relation = new JobTagRelation();
                relation.setJobId(job.getId());
                relation.setTagId(tag.getId());
                jobTagRelationRepository.save(relation);
                
                // 更新标签使用次数
                tag.setUseCount(tag.getUseCount() + 1);
                jobTagRepository.save(tag);
            }
        }
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
