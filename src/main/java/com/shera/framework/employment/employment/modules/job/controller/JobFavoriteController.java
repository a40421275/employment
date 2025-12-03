package com.shera.framework.employment.employment.modules.job.controller;

import com.shera.framework.employment.employment.modules.job.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobFavorite;
import com.shera.framework.employment.employment.modules.job.service.JobFavoriteService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import com.shera.framework.employment.employment.util.SecurityContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 岗位收藏控制器
 */
@RestController
@RequestMapping("/api/job-favorites")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "岗位收藏管理", description = "岗位收藏相关接口")
public class JobFavoriteController {
    
    private final JobFavoriteService jobFavoriteService;
    
    @PostMapping("/{jobId}")
    @Operation(summary = "收藏岗位", description = "用户收藏指定岗位")
    public ResponseEntity<?> favoriteJob(
            @Parameter(description = "岗位ID") @PathVariable Long jobId) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            JobFavorite favorite = jobFavoriteService.favoriteJob(userId, jobId);
            return ResponseUtil.success("收藏成功", favorite);
        } catch (Exception e) {
            log.error("收藏岗位失败 - 岗位ID: {}, 错误: {}", jobId, e.getMessage());
            return ResponseUtil.error("收藏失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{jobId}")
    @Operation(summary = "取消收藏", description = "用户取消收藏指定岗位")
    public ResponseEntity<?> unfavoriteJob(
            @Parameter(description = "岗位ID") @PathVariable Long jobId) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            jobFavoriteService.unfavoriteJob(userId, jobId);
            return ResponseUtil.success("取消收藏成功");
        } catch (Exception e) {
            log.error("取消收藏失败 - 岗位ID: {}, 错误: {}", jobId, e.getMessage());
            return ResponseUtil.error("取消收藏失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/check/{jobId}")
    @Operation(summary = "检查收藏状态", description = "检查用户是否收藏了指定岗位")
    public ResponseEntity<?> checkFavoriteStatus(
            @Parameter(description = "岗位ID") @PathVariable Long jobId) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            boolean isFavorited = jobFavoriteService.isJobFavorited(userId, jobId);
            return ResponseUtil.success("查询成功", Map.of("isFavorited", isFavorited));
        } catch (Exception e) {
            log.error("检查收藏状态失败 - 岗位ID: {}, 错误: {}", jobId, e.getMessage());
            return ResponseUtil.error("检查收藏状态失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-favorites")
    @Operation(summary = "获取我的收藏列表", description = "获取当前用户的岗位收藏列表")
    public ResponseEntity<?> getMyFavorites(
            @Parameter(description = "分页参数") @PageableDefault(size = 20) Pageable pageable) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            Page<JobFavorite> favorites = jobFavoriteService.getUserFavorites(userId, pageable);
            return ResponseUtil.success("获取成功", favorites);
        } catch (Exception e) {
            log.error("获取收藏列表失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取收藏列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-favorite-jobs")
    @Operation(summary = "获取我的收藏岗位详情", description = "获取当前用户收藏的岗位详情列表")
    public ResponseEntity<?> getMyFavoriteJobs(
            @Parameter(description = "分页参数") @PageableDefault(size = 20) Pageable pageable) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            Page<JobWithCompanyDTO> favoriteJobs = jobFavoriteService.getUserFavoriteJobs(userId, pageable);
            return ResponseUtil.success("获取成功", favoriteJobs);
        } catch (Exception e) {
            log.error("获取收藏岗位详情失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取收藏岗位详情失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-favorite-job-ids")
    @Operation(summary = "获取我的收藏岗位ID列表", description = "获取当前用户收藏的岗位ID列表")
    public ResponseEntity<?> getMyFavoriteJobIds() {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            List<Long> favoriteJobIds = jobFavoriteService.getUserFavoriteJobIds(userId);
            return ResponseUtil.success("获取成功", favoriteJobIds);
        } catch (Exception e) {
            log.error("获取收藏岗位ID列表失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取收藏岗位ID列表失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/batch-check")
    @Operation(summary = "批量检查收藏状态", description = "批量检查用户是否收藏了指定岗位列表")
    public ResponseEntity<?> batchCheckFavoriteStatus(
            @Parameter(description = "岗位ID列表") @RequestBody List<Long> jobIds) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            Map<Long, Boolean> favoriteStatus = jobFavoriteService.batchCheckFavoriteStatus(userId, jobIds);
            return ResponseUtil.success("批量检查成功", favoriteStatus);
        } catch (Exception e) {
            log.error("批量检查收藏状态失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("批量检查收藏状态失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-count")
    @Operation(summary = "获取我的收藏数量", description = "获取当前用户的收藏岗位数量")
    public ResponseEntity<?> getMyFavoriteCount() {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            Long count = jobFavoriteService.getUserFavoriteCount(userId);
            return ResponseUtil.success("获取成功", Map.of("count", count));
        } catch (Exception e) {
            log.error("获取收藏数量失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取收藏数量失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/job/{jobId}/count")
    @Operation(summary = "获取岗位收藏数量", description = "获取指定岗位的收藏数量")
    public ResponseEntity<?> getJobFavoriteCount(
            @Parameter(description = "岗位ID") @PathVariable Long jobId) {
        try {
            Long count = jobFavoriteService.getJobFavoriteCount(jobId);
            return ResponseUtil.success("获取成功", Map.of("count", count));
        } catch (Exception e) {
            log.error("获取岗位收藏数量失败 - 岗位ID: {}, 错误: {}", jobId, e.getMessage());
            return ResponseUtil.error("获取岗位收藏数量失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/batch-job-counts")
    @Operation(summary = "批量获取岗位收藏数量", description = "批量获取多个岗位的收藏数量")
    public ResponseEntity<?> batchGetJobFavoriteCounts(
            @Parameter(description = "岗位ID列表") @RequestBody List<Long> jobIds) {
        try {
            Map<Long, Long> counts = jobFavoriteService.batchGetJobFavoriteCounts(jobIds);
            return ResponseUtil.success("批量获取成功", counts);
        } catch (Exception e) {
            log.error("批量获取岗位收藏数量失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("批量获取岗位收藏数量失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/hot-favorites")
    @Operation(summary = "获取热门收藏岗位", description = "获取收藏数量最多的热门岗位")
    public ResponseEntity<?> getHotFavoriteJobs(
            @Parameter(description = "返回数量限制") @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Long> hotJobs = jobFavoriteService.getHotFavoriteJobs(limit);
            return ResponseUtil.success("获取成功", hotJobs);
        } catch (Exception e) {
            log.error("获取热门收藏岗位失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取热门收藏岗位失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-latest-favorites")
    @Operation(summary = "获取我的最近收藏", description = "获取当前用户最近收藏的岗位")
    public ResponseEntity<?> getMyLatestFavorites(
            @Parameter(description = "返回数量限制") @RequestParam(defaultValue = "10") int limit) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            List<JobFavorite> latestFavorites = jobFavoriteService.getUserLatestFavorites(userId, limit);
            return ResponseUtil.success("获取成功", latestFavorites);
        } catch (Exception e) {
            log.error("获取最近收藏失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取最近收藏失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/my-all")
    @Operation(summary = "清空我的收藏", description = "清空当前用户的所有收藏")
    public ResponseEntity<?> deleteAllMyFavorites() {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            jobFavoriteService.deleteAllUserFavorites(userId);
            return ResponseUtil.success("清空收藏成功");
        } catch (Exception e) {
            log.error("清空收藏失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("清空收藏失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除收藏", description = "批量删除当前用户的指定收藏")
    public ResponseEntity<?> batchDeleteMyFavorites(
            @Parameter(description = "岗位ID列表") @RequestBody List<Long> jobIds) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            jobFavoriteService.batchDeleteUserFavorites(userId, jobIds);
            return ResponseUtil.success("批量删除收藏成功");
        } catch (Exception e) {
            log.error("批量删除收藏失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("批量删除收藏失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-statistics")
    @Operation(summary = "获取我的收藏统计", description = "获取当前用户的收藏统计信息")
    public ResponseEntity<?> getMyFavoriteStatistics() {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            Map<String, Object> statistics = jobFavoriteService.getFavoriteStatistics(userId);
            return ResponseUtil.success("获取统计成功", statistics);
        } catch (Exception e) {
            log.error("获取收藏统计失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("获取收藏统计失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/toggle/{jobId}")
    @Operation(summary = "切换收藏状态", description = "切换指定岗位的收藏状态（收藏/取消收藏）")
    public ResponseEntity<?> toggleFavorite(
            @Parameter(description = "岗位ID") @PathVariable Long jobId) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            boolean isFavorited = jobFavoriteService.isJobFavorited(userId, jobId);
            
            if (isFavorited) {
                jobFavoriteService.unfavoriteJob(userId, jobId);
                return ResponseUtil.success("取消收藏成功", Map.of("action", "unfavorite"));
            } else {
                JobFavorite favorite = jobFavoriteService.favoriteJob(userId, jobId);
                return ResponseUtil.success("收藏成功", Map.of("action", "favorite", "favorite", favorite));
            }
        } catch (Exception e) {
            log.error("切换收藏状态失败 - 岗位ID: {}, 错误: {}", jobId, e.getMessage());
            return ResponseUtil.error("切换收藏状态失败: " + e.getMessage());
        }
    }
}
