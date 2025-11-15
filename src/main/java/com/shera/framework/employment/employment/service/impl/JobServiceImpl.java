package com.shera.framework.employment.employment.service.impl;

import com.shera.framework.employment.employment.dto.JobDTO;
import com.shera.framework.employment.employment.dto.JobQueryDTO;
import com.shera.framework.employment.employment.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.entity.Job;
import com.shera.framework.employment.employment.repository.JobRepository;
import com.shera.framework.employment.employment.service.JobService;
import com.shera.framework.employment.employment.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 岗位服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    
    private final JobRepository jobRepository;
    
    @Override
    @Transactional
    public Job createJob(JobDTO jobDTO) {
        Job job = new Job();
        copyJobProperties(job, jobDTO);
        job.setStatus(0); // 默认草稿状态
        return jobRepository.save(job);
    }
    
    @Override
    @Transactional
    public Job updateJob(Long id, JobDTO jobDTO) {
        Job job = getJobById(id);
        copyJobProperties(job, jobDTO);
        return jobRepository.save(job);
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
    public Job getJobDetail(Long id) {
        Job job = getJobById(id);
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
        Job job = getJobById(id);
        job.setViewCount(job.getViewCount() + 1);
        jobRepository.save(job);
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
}
