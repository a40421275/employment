package com.shera.framework.employment.employment.modules.job.controller;

import com.shera.framework.employment.employment.modules.job.dto.JobApplyDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobApply;
import com.shera.framework.employment.employment.modules.job.service.JobApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 岗位申请控制器
 */
@RestController
@RequestMapping("/api/job-applies")
@RequiredArgsConstructor
public class JobApplyController {
    
    private final JobApplyService jobApplyService;
    
    /**
     * 创建岗位申请
     */
    @PostMapping
    public ResponseEntity<JobApply> createJobApply(@RequestBody JobApplyDTO jobApplyDTO) {
        JobApply jobApply = jobApplyService.createJobApply(jobApplyDTO);
        return ResponseEntity.ok(jobApply);
    }
    
    /**
     * 更新岗位申请
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobApply> updateJobApply(@PathVariable Long id, @RequestBody JobApplyDTO jobApplyDTO) {
        JobApply jobApply = jobApplyService.updateJobApply(id, jobApplyDTO);
        return ResponseEntity.ok(jobApply);
    }
    
    /**
     * 删除岗位申请
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApply(@PathVariable Long id) {
        jobApplyService.deleteJobApply(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取申请详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobApply> getJobApply(@PathVariable Long id) {
        JobApply jobApply = jobApplyService.getJobApplyDetail(id);
        return ResponseEntity.ok(jobApply);
    }
    
    /**
     * 分页查询申请列表
     */
    @GetMapping
    public ResponseEntity<Page<JobApply>> getJobApplies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<JobApply> jobApplies = jobApplyService.getJobApplies(pageable);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据用户ID查询申请列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApply>> getJobAppliesByUser(@PathVariable Long userId) {
        List<JobApply> jobApplies = jobApplyService.getJobAppliesByUserId(userId);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据用户ID分页查询申请列表
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<JobApply>> getJobAppliesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobApply> jobApplies = jobApplyService.getJobAppliesByUserId(userId, pageable);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据岗位ID查询申请列表
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApply>> getJobAppliesByJob(@PathVariable Long jobId) {
        List<JobApply> jobApplies = jobApplyService.getJobAppliesByJobId(jobId);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据岗位ID分页查询申请列表
     */
    @GetMapping("/job/{jobId}/page")
    public ResponseEntity<Page<JobApply>> getJobAppliesByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobApply> jobApplies = jobApplyService.getJobAppliesByJobId(jobId, pageable);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据简历ID查询申请列表
     */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<JobApply>> getJobAppliesByResume(@PathVariable Long resumeId) {
        List<JobApply> jobApplies = jobApplyService.getJobAppliesByResumeId(resumeId);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据状态查询申请列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApply>> getJobAppliesByStatus(@PathVariable Integer status) {
        List<JobApply> jobApplies = jobApplyService.getJobAppliesByStatus(status);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据用户ID和状态查询申请列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<JobApply>> getJobAppliesByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable Integer status) {
        List<JobApply> jobApplies = jobApplyService.getJobAppliesByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 根据岗位ID和状态查询申请列表
     */
    @GetMapping("/job/{jobId}/status/{status}")
    public ResponseEntity<List<JobApply>> getJobAppliesByJobAndStatus(
            @PathVariable Long jobId,
            @PathVariable Integer status) {
        List<JobApply> jobApplies = jobApplyService.getJobAppliesByJobIdAndStatus(jobId, status);
        return ResponseEntity.ok(jobApplies);
    }
    
    /**
     * 更新申请状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<JobApply> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        JobApply jobApply = jobApplyService.updateStatus(id, status);
        return ResponseEntity.ok(jobApply);
    }
    
    /**
     * 设置面试时间
     */
    @PutMapping("/{id}/interview")
    public ResponseEntity<JobApply> setInterviewTime(
            @PathVariable Long id,
            @RequestParam String interviewTime,
            @RequestParam String interviewLocation) {
        JobApply jobApply = jobApplyService.setInterviewTime(id, interviewTime, interviewLocation);
        return ResponseEntity.ok(jobApply);
    }
    
    /**
     * 添加申请反馈
     */
    @PutMapping("/{id}/feedback")
    public ResponseEntity<JobApply> addFeedback(
            @PathVariable Long id,
            @RequestParam String feedback) {
        JobApply jobApply = jobApplyService.addFeedback(id, feedback);
        return ResponseEntity.ok(jobApply);
    }
    
    /**
     * 计算匹配度
     */
    @GetMapping("/match-score")
    public ResponseEntity<BigDecimal> calculateMatchScore(
            @RequestParam Long jobId,
            @RequestParam Long resumeId) {
        BigDecimal matchScore = jobApplyService.calculateMatchScore(jobId, resumeId);
        return ResponseEntity.ok(matchScore);
    }
    
    /**
     * 检查用户是否已申请该岗位
     */
    @GetMapping("/check-apply")
    public ResponseEntity<Boolean> hasUserAppliedJob(
            @RequestParam Long userId,
            @RequestParam Long jobId) {
        boolean hasApplied = jobApplyService.hasUserAppliedJob(userId, jobId);
        return ResponseEntity.ok(hasApplied);
    }
    
    /**
     * 统计各状态的申请数量
     */
    @GetMapping("/stats/status")
    public ResponseEntity<Map<Integer, Long>> countJobAppliesByStatus() {
        Map<Integer, Long> stats = jobApplyService.countJobAppliesByStatus();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 统计用户申请数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countJobAppliesByUser(@PathVariable Long userId) {
        Long count = jobApplyService.countJobAppliesByUserId(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * 统计岗位申请数量
     */
    @GetMapping("/job/{jobId}/count")
    public ResponseEntity<Long> countJobAppliesByJob(@PathVariable Long jobId) {
        Long count = jobApplyService.countJobAppliesByJobId(jobId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * 获取热门申请岗位
     */
    @GetMapping("/hot-jobs")
    public ResponseEntity<List<Object[]>> getHotAppliedJobs(@RequestParam(defaultValue = "10") int limit) {
        List<Object[]> hotJobs = jobApplyService.getHotAppliedJobs(limit);
        return ResponseEntity.ok(hotJobs);
    }
    
    /**
     * 获取用户申请统计
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserApplyStats(@PathVariable Long userId) {
        Map<String, Object> stats = jobApplyService.getUserApplyStats(userId);
        return ResponseEntity.ok(stats);
    }
}
