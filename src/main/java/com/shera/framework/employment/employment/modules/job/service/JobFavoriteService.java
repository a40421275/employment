package com.shera.framework.employment.employment.modules.job.service;

import com.shera.framework.employment.employment.modules.job.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 岗位收藏服务接口
 */
public interface JobFavoriteService {
    
    /**
     * 收藏岗位
     */
    JobFavorite favoriteJob(Long userId, Long jobId);
    
    /**
     * 取消收藏岗位
     */
    void unfavoriteJob(Long userId, Long jobId);
    
    /**
     * 检查用户是否收藏了岗位
     */
    boolean isJobFavorited(Long userId, Long jobId);
    
    /**
     * 获取用户的收藏列表
     */
    List<JobFavorite> getUserFavorites(Long userId);
    
    /**
     * 分页获取用户的收藏列表
     */
    Page<JobFavorite> getUserFavorites(Long userId, Pageable pageable);
    
    /**
     * 获取用户收藏的岗位详情列表
     */
    Page<JobWithCompanyDTO> getUserFavoriteJobs(Long userId, Pageable pageable);
    
    /**
     * 获取用户收藏的岗位ID列表
     */
    List<Long> getUserFavoriteJobIds(Long userId);
    
    /**
     * 批量检查用户是否收藏了岗位
     */
    Map<Long, Boolean> batchCheckFavoriteStatus(Long userId, List<Long> jobIds);
    
    /**
     * 统计用户的收藏数量
     */
    Long getUserFavoriteCount(Long userId);
    
    /**
     * 统计岗位的收藏数量
     */
    Long getJobFavoriteCount(Long jobId);
    
    /**
     * 批量统计岗位的收藏数量
     */
    Map<Long, Long> batchGetJobFavoriteCounts(List<Long> jobIds);
    
    /**
     * 获取热门收藏岗位（按收藏数量排序）
     */
    List<Long> getHotFavoriteJobs(int limit);
    
    /**
     * 获取用户最近收藏的岗位
     */
    List<JobFavorite> getUserLatestFavorites(Long userId, int limit);
    
    /**
     * 删除用户的所有收藏
     */
    void deleteAllUserFavorites(Long userId);
    
    /**
     * 删除岗位的所有收藏
     */
    void deleteAllJobFavorites(Long jobId);
    
    /**
     * 批量删除用户的收藏
     */
    void batchDeleteUserFavorites(Long userId, List<Long> jobIds);
    
    /**
     * 获取收藏统计信息
     */
    Map<String, Object> getFavoriteStatistics(Long userId);
}
