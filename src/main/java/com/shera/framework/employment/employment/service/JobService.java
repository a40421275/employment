package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.JobDTO;
import com.shera.framework.employment.employment.dto.JobQueryDTO;
import com.shera.framework.employment.employment.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 岗位服务接口
 */
public interface JobService {
    
    /**
     * 创建岗位
     */
    Job createJob(JobDTO jobDTO);
    
    /**
     * 更新岗位
     */
    Job updateJob(Long id, JobDTO jobDTO);
    
    /**
     * 删除岗位
     */
    void deleteJob(Long id);
    
    /**
     * 根据ID获取岗位
     */
    Job getJobById(Long id);
    
    /**
     * 获取岗位详情（包含关联信息）
     */
    Job getJobDetail(Long id);
    
    /**
     * 分页查询岗位列表
     */
    Page<Job> getJobs(Pageable pageable);
    
    /**
     * 根据状态分页查询岗位列表
     */
    Page<Job> getJobsByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据分类ID查询岗位列表
     */
    List<Job> getJobsByCategoryId(Long categoryId);
    
    /**
     * 根据工作城市查询岗位列表
     */
    List<Job> getJobsByWorkCity(String workCity);
    
    /**
     * 根据岗位类型查询岗位列表
     */
    List<Job> getJobsByJobType(Integer jobType);
    
    /**
     * 根据关键词搜索岗位
     */
    Page<Job> searchJobs(String keyword, Pageable pageable);
    
    /**
     * 多条件筛选岗位
     */
    Page<Job> filterJobs(Long categoryId, String workCity, Integer jobType, 
                         Double minSalary, Double maxSalary, Pageable pageable);
    
    /**
     * 发布岗位
     */
    Job publishJob(Long id);
    
    /**
     * 下架岗位
     */
    Job offlineJob(Long id);
    
    /**
     * 归档岗位
     */
    Job archiveJob(Long id);
    
    /**
     * 增加岗位浏览量
     */
    void increaseViewCount(Long id);
    
    /**
     * 增加岗位申请量
     */
    void increaseApplyCount(Long id);
    
    /**
     * 获取活跃岗位（已发布且未过期）
     */
    List<Job> getActiveJobs();
    
    /**
     * 获取即将过期的岗位
     */
    List<Job> getExpiringJobs();
    
    /**
     * 统计各状态的岗位数量
     */
    Map<Integer, Long> countJobsByStatus();
    
    /**
     * 统计各城市的岗位数量
     */
    Map<String, Long> countJobsByCity();
    
    /**
     * 获取热门岗位（按浏览量排序）
     */
    List<Job> getHotJobs(int limit);
    
    /**
     * 获取最新岗位
     */
    List<Job> getLatestJobs(int limit);

    // ==================== 投影查询方法 ====================

    /**
     * 根据ID查询岗位与企业信息（投影查询）
     */
    Optional<JobWithCompanyDTO> getJobWithCompanyById(Long jobId);

    /**
     * 分页查询岗位列表与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> getJobsWithCompanyByStatus(Integer status, Pageable pageable);

    /**
     * 根据企业ID查询岗位列表与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> getJobsWithCompanyByCompanyIdAndStatus(Long companyId, Integer status, Pageable pageable);

    /**
     * 根据关键词搜索岗位与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> searchJobsWithCompanyByKeyword(String keyword, Integer status, Pageable pageable);

    /**
     * 多条件筛选岗位与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> filterJobsWithCompanyByMultipleConditions(Long categoryId, String workCity, Integer jobType, 
                                                                     Double minSalary, Double maxSalary, Integer status, Pageable pageable);

    /**
     * 根据分类ID查询岗位与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> getJobsWithCompanyByCategoryId(Long categoryId, Integer status, Pageable pageable);

    /**
     * 根据工作城市查询岗位与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> getJobsWithCompanyByWorkCity(String workCity, Integer status, Pageable pageable);

    /**
     * 根据岗位类型查询岗位与企业信息（投影查询）
     */
    Page<JobWithCompanyDTO> getJobsWithCompanyByJobType(Integer jobType, Integer status, Pageable pageable);

    // ==================== 统一查询方法 ====================

    /**
     * 统一查询岗位列表（支持多条件筛选、搜索、分页）
     */
    Page<JobWithCompanyDTO> queryJobs(JobQueryDTO queryDTO);
}
