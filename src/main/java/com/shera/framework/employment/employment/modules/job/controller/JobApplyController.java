package com.shera.framework.employment.employment.modules.job.controller;

import com.shera.framework.employment.employment.modules.job.dto.JobApplyDTO;
import com.shera.framework.employment.employment.modules.job.service.JobApplyService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
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
    public ResponseEntity<?> createJobApply(@RequestBody JobApplyDTO jobApplyDTO) {
        try {
            JobApplyDTO result = jobApplyService.createJobApply(jobApplyDTO);
            return ResponseUtil.success("申请岗位成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 更新岗位申请
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJobApply(@PathVariable Long id, @RequestBody JobApplyDTO jobApplyDTO) {
        try {
            JobApplyDTO result = jobApplyService.updateJobApply(id, jobApplyDTO);
            return ResponseUtil.success("更新岗位申请成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 删除岗位申请
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJobApply(@PathVariable Long id) {
        try {
            jobApplyService.deleteJobApply(id);
            return ResponseUtil.success("删除岗位申请成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 获取申请详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobApply(@PathVariable Long id) {
        try {
            JobApplyDTO result = jobApplyService.getJobApplyDetail(id);
            return ResponseUtil.success("获取申请详情成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 分页查询申请列表
     */
    @GetMapping
    public ResponseEntity<?> getJobApplies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            Page<JobApplyDTO> jobApplies = jobApplyService.getJobApplies(pageable);
            return ResponseUtil.successPage(jobApplies, "查询申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据用户ID查询申请列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getJobAppliesByUser(@PathVariable Long userId) {
        try {
            List<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByUserId(userId);
            return ResponseUtil.successList(jobApplies, "查询用户申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据用户ID分页查询申请列表
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<?> getJobAppliesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByUserId(userId, pageable);
            return ResponseUtil.successPage(jobApplies, "查询用户申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据岗位ID查询申请列表
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getJobAppliesByJob(@PathVariable Long jobId) {
        try {
            List<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByJobId(jobId);
            return ResponseUtil.successList(jobApplies, "查询岗位申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据岗位ID分页查询申请列表
     */
    @GetMapping("/job/{jobId}/page")
    public ResponseEntity<?> getJobAppliesByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByJobId(jobId, pageable);
            return ResponseUtil.successPage(jobApplies, "查询岗位申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据简历ID查询申请列表
     */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<?> getJobAppliesByResume(@PathVariable Long resumeId) {
        try {
            List<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByResumeId(resumeId);
            return ResponseUtil.successList(jobApplies, "查询简历申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据状态查询申请列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getJobAppliesByStatus(@PathVariable Integer status) {
        try {
            List<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByStatus(status);
            return ResponseUtil.successList(jobApplies, "查询状态申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据用户ID和状态查询申请列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getJobAppliesByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable Integer status) {
        try {
            List<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByUserIdAndStatus(userId, status);
            return ResponseUtil.successList(jobApplies, "查询用户状态申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据岗位ID和状态查询申请列表
     */
    @GetMapping("/job/{jobId}/status/{status}")
    public ResponseEntity<?> getJobAppliesByJobAndStatus(
            @PathVariable Long jobId,
            @PathVariable Integer status) {
        try {
            List<JobApplyDTO> jobApplies = jobApplyService.getJobAppliesByJobIdAndStatus(jobId, status);
            return ResponseUtil.successList(jobApplies, "查询岗位状态申请列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 更新申请状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            JobApplyDTO result = jobApplyService.updateStatus(id, status);
            return ResponseUtil.success("更新申请状态成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 设置面试时间
     */
    @PutMapping("/{id}/interview")
    public ResponseEntity<?> setInterviewTime(
            @PathVariable Long id,
            @RequestParam String interviewTime,
            @RequestParam String interviewLocation) {
        try {
            JobApplyDTO result = jobApplyService.setInterviewTime(id, interviewTime, interviewLocation);
            return ResponseUtil.success("设置面试时间成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 添加申请反馈
     */
    @PutMapping("/{id}/feedback")
    public ResponseEntity<?> addFeedback(
            @PathVariable Long id,
            @RequestParam String feedback) {
        try {
            JobApplyDTO result = jobApplyService.addFeedback(id, feedback);
            return ResponseUtil.success("添加申请反馈成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 面试通过
     */
    @PostMapping("/{id}/pass-interview")
    public ResponseEntity<?> passInterview(@PathVariable Long id, @RequestParam(required = false) String feedback) {
        try {
            JobApplyDTO result = jobApplyService.passInterview(id, feedback);
            return ResponseUtil.success("面试通过操作成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 拒绝申请
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectApplication(@PathVariable Long id, @RequestParam String reason) {
        try {
            JobApplyDTO result = jobApplyService.rejectApplication(id, reason);
            return ResponseUtil.success("拒绝申请操作成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 取消申请
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelApplication(@PathVariable Long id) {
        try {
            JobApplyDTO result = jobApplyService.cancelApplication(id);
            return ResponseUtil.success("取消申请操作成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 计算匹配度
     */
    @GetMapping("/match-score")
    public ResponseEntity<?> calculateMatchScore(
            @RequestParam Long jobId,
            @RequestParam Long resumeId) {
        try {
            BigDecimal matchScore = jobApplyService.calculateMatchScore(jobId, resumeId);
            return ResponseUtil.success("计算匹配度成功", matchScore);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 检查用户是否已申请该岗位
     */
    @GetMapping("/check-apply")
    public ResponseEntity<?> hasUserAppliedJob(
            @RequestParam Long userId,
            @RequestParam Long jobId) {
        try {
            boolean hasApplied = jobApplyService.hasUserAppliedJob(userId, jobId);
            return ResponseUtil.success("检查申请状态成功", hasApplied);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 检查是否已申请
     */
    @GetMapping("/check-applied")
    public ResponseEntity<?> checkAppliedStatus(@RequestParam Long userId, @RequestParam Long jobId) {
        try {
            Map<String, Object> result = jobApplyService.checkAppliedStatus(userId, jobId);
            return ResponseUtil.success("检查申请状态成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 统计各状态的申请数量
     */
    @GetMapping("/stats/status")
    public ResponseEntity<?> countJobAppliesByStatus() {
        try {
            Map<Integer, Long> stats = jobApplyService.countJobAppliesByStatus();
            // 转换为Map<String, Object>以匹配ResponseUtil的期望类型
            Map<String, Object> result = new HashMap<>();
            stats.forEach((key, value) -> result.put(key.toString(), value));
            return ResponseUtil.successStats(result, "统计申请状态成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 统计用户申请数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> countJobAppliesByUser(@PathVariable Long userId) {
        try {
            Long count = jobApplyService.countJobAppliesByUserId(userId);
            return ResponseUtil.success("统计用户申请数量成功", count);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 统计岗位申请数量
     */
    @GetMapping("/job/{jobId}/count")
    public ResponseEntity<?> countJobAppliesByJob(@PathVariable Long jobId) {
        try {
            Long count = jobApplyService.countJobAppliesByJobId(jobId);
            return ResponseUtil.success("统计岗位申请数量成功", count);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 获取热门申请岗位
     */
    @GetMapping("/hot-jobs")
    public ResponseEntity<?> getHotAppliedJobs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Object[]> hotJobs = jobApplyService.getHotAppliedJobs(limit);
            return ResponseUtil.success("获取热门申请岗位成功", hotJobs);
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 获取最新申请
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestApplications(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<JobApplyDTO> applications = jobApplyService.getLatestApplications(limit);
            return ResponseUtil.successList(applications, "获取最新申请成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
    
    /**
     * 获取用户申请统计
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<?> getUserApplyStats(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = jobApplyService.getUserApplyStats(userId);
            return ResponseUtil.successStats(stats, "获取用户申请统计成功");
        } catch (Exception e) {
            return ResponseUtil.error(400, e.getMessage());
        }
    }
}
