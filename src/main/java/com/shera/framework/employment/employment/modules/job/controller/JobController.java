package com.shera.framework.employment.employment.modules.job.controller;

import com.shera.framework.employment.employment.modules.job.dto.JobDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobQueryDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.modules.job.entity.Job;
import com.shera.framework.employment.employment.modules.job.service.JobService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 岗位控制器
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    
    private final JobService jobService;
    
    /**
     * 创建岗位
     */
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobDTO jobDTO) {
        Job job = jobService.createJob(jobDTO);
        return ResponseUtil.success("创建岗位成功", job);
    }
    
    /**
     * 更新岗位
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobDTO jobDTO) {
        Job job = jobService.updateJob(id, jobDTO);
        return ResponseUtil.success("更新岗位成功", job);
    }
    
    /**
     * 删除岗位
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseUtil.success("删除岗位成功");
    }
    
    /**
     * 获取岗位详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getJob(@PathVariable Long id) {
        // 使用投影查询获取岗位与企业信息
        JobWithCompanyDTO job = jobService.getJobWithCompanyById(id)
                .orElseThrow(() -> new RuntimeException("岗位不存在: " + id));
        jobService.increaseViewCount(id); // 增加浏览量
        
        return ResponseUtil.success("获取岗位详情成功", job);
    }
    
    /**
     * 统一查询岗位列表（支持多条件筛选、搜索、分页）
     */
    @PostMapping("/query")
    public ResponseEntity<?> queryJobs(@RequestBody JobQueryDTO queryDTO) {
        Page<JobWithCompanyDTO> jobs = jobService.queryJobs(queryDTO);
        return ResponseUtil.successPage(jobs, "查询成功");
    }
    
    /**
     * 发布岗位
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publishJob(@PathVariable Long id) {
        Job job = jobService.publishJob(id);
        return ResponseUtil.success("发布岗位成功", job);
    }
    
    /**
     * 下架岗位
     */
    @PostMapping("/{id}/offline")
    public ResponseEntity<?> offlineJob(@PathVariable Long id) {
        Job job = jobService.offlineJob(id);
        return ResponseUtil.success("下架岗位成功", job);
    }
    
    /**
     * 归档岗位
     */
    @PostMapping("/{id}/archive")
    public ResponseEntity<?> archiveJob(@PathVariable Long id) {
        Job job = jobService.archiveJob(id);
        return ResponseUtil.success("归档岗位成功", job);
    }
    
    /**
     * 获取活跃岗位（已发布且未过期）
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveJobs() {
        List<Job> jobs = jobService.getActiveJobs();
        
        // 手动构建响应数据，避免返回整个企业对象
        java.util.List<Map<String, Object>> jobList = jobs.stream().map(job -> {
            Map<String, Object> jobData = new java.util.HashMap<>();
            jobData.put("id", job.getId());
            jobData.put("title", job.getTitle());
            jobData.put("categoryId", job.getCategoryId());
            jobData.put("companyId", job.getCompanyId());
            jobData.put("companyName", job.getCompany() != null ? job.getCompany().getCompanyName() : null);
            jobData.put("jobType", job.getJobType());
            jobData.put("salaryMin", job.getSalaryMin());
            jobData.put("salaryMax", job.getSalaryMax());
            jobData.put("workCity", job.getWorkCity());
            jobData.put("status", job.getStatus());
            jobData.put("viewCount", job.getViewCount());
            jobData.put("applyCount", job.getApplyCount());
            jobData.put("createTime", job.getCreateTime());
            jobData.put("updateTime", job.getUpdateTime());
            return jobData;
        }).collect(java.util.stream.Collectors.toList());
        
        return ResponseUtil.successList(jobList, "查询成功");
    }
    
    /**
     * 获取即将过期的岗位
     */
    @GetMapping("/expiring")
    public ResponseEntity<?> getExpiringJobs() {
        List<Job> jobs = jobService.getExpiringJobs();
        
        // 手动构建响应数据，避免返回整个企业对象
        java.util.List<Map<String, Object>> jobList = jobs.stream().map(job -> {
            Map<String, Object> jobData = new java.util.HashMap<>();
            jobData.put("id", job.getId());
            jobData.put("title", job.getTitle());
            jobData.put("categoryId", job.getCategoryId());
            jobData.put("companyId", job.getCompanyId());
            jobData.put("companyName", job.getCompany() != null ? job.getCompany().getCompanyName() : null);
            jobData.put("jobType", job.getJobType());
            jobData.put("salaryMin", job.getSalaryMin());
            jobData.put("salaryMax", job.getSalaryMax());
            jobData.put("workCity", job.getWorkCity());
            jobData.put("status", job.getStatus());
            jobData.put("viewCount", job.getViewCount());
            jobData.put("applyCount", job.getApplyCount());
            jobData.put("expireTime", job.getExpireTime());
            jobData.put("createTime", job.getCreateTime());
            jobData.put("updateTime", job.getUpdateTime());
            return jobData;
        }).collect(java.util.stream.Collectors.toList());
        
        return ResponseUtil.successList(jobList, "查询成功");
    }
    
    /**
     * 统计各状态的岗位数量
     */
    @GetMapping("/stats/status")
    public ResponseEntity<?> countJobsByStatus() {
        Map<Integer, Long> stats = jobService.countJobsByStatus();
        return ResponseUtil.success("统计成功", stats);
    }
    
    /**
     * 统计各城市的岗位数量
     */
    @GetMapping("/stats/city")
    public ResponseEntity<?> countJobsByCity() {
        Map<String, Long> stats = jobService.countJobsByCity();
        return ResponseUtil.success("统计成功", stats);
    }
    
    /**
     * 获取热门岗位
     */
    @GetMapping("/hot")
    public ResponseEntity<?> getHotJobs(@RequestParam(defaultValue = "10") int limit) {
        List<Job> jobs = jobService.getHotJobs(limit);
        
        // 手动构建响应数据，避免返回整个企业对象
        java.util.List<Map<String, Object>> jobList = jobs.stream().map(job -> {
            Map<String, Object> jobData = new java.util.HashMap<>();
            jobData.put("id", job.getId());
            jobData.put("title", job.getTitle());
            jobData.put("categoryId", job.getCategoryId());
            jobData.put("companyId", job.getCompanyId());
            jobData.put("companyName", job.getCompany() != null ? job.getCompany().getCompanyName() : null);
            jobData.put("jobType", job.getJobType());
            jobData.put("salaryMin", job.getSalaryMin());
            jobData.put("salaryMax", job.getSalaryMax());
            jobData.put("workCity", job.getWorkCity());
            jobData.put("viewCount", job.getViewCount());
            jobData.put("applyCount", job.getApplyCount());
            jobData.put("createTime", job.getCreateTime());
            jobData.put("updateTime", job.getUpdateTime());
            return jobData;
        }).collect(java.util.stream.Collectors.toList());
        
        return ResponseUtil.successList(jobList, "获取成功");
    }
    
    /**
     * 获取最新岗位
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestJobs(@RequestParam(defaultValue = "10") int limit) {
        List<Job> jobs = jobService.getLatestJobs(limit);
        
        // 手动构建响应数据，避免返回整个企业对象
        java.util.List<Map<String, Object>> jobList = jobs.stream().map(job -> {
            Map<String, Object> jobData = new java.util.HashMap<>();
            jobData.put("id", job.getId());
            jobData.put("title", job.getTitle());
            jobData.put("categoryId", job.getCategoryId());
            jobData.put("companyId", job.getCompanyId());
            jobData.put("companyName", job.getCompany() != null ? job.getCompany().getCompanyName() : null);
            jobData.put("jobType", job.getJobType());
            jobData.put("salaryMin", job.getSalaryMin());
            jobData.put("salaryMax", job.getSalaryMax());
            jobData.put("workCity", job.getWorkCity());
            jobData.put("createTime", job.getCreateTime());
            jobData.put("updateTime", job.getUpdateTime());
            return jobData;
        }).collect(java.util.stream.Collectors.toList());
        
        return ResponseUtil.successList(jobList, "获取成功");
    }
}
