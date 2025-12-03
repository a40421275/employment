package com.shera.framework.employment.employment.modules.job.service.impl;

import com.shera.framework.employment.employment.modules.job.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobFavorite;
import com.shera.framework.employment.employment.modules.job.repository.JobFavoriteRepository;
import com.shera.framework.employment.employment.modules.job.service.JobFavoriteService;
import com.shera.framework.employment.employment.modules.job.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位收藏服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobFavoriteServiceImpl implements JobFavoriteService {
    
    private final JobFavoriteRepository jobFavoriteRepository;
    private final JobService jobService;
    
    @Override
    @Transactional
    public JobFavorite favoriteJob(Long userId, Long jobId) {
        try {
            // 检查是否已经收藏
            if (jobFavoriteRepository.existsByUserIdAndJobId(userId, jobId)) {
                log.info("用户已收藏该岗位 - 用户ID: {}, 岗位ID: {}", userId, jobId);
                return jobFavoriteRepository.findByUserIdAndJobId(userId, jobId);
            }
            
            // 创建收藏记录
            JobFavorite favorite = new JobFavorite();
            favorite.setUserId(userId);
            favorite.setJobId(jobId);
            favorite.setCreateTime(LocalDateTime.now());
            
            JobFavorite savedFavorite = jobFavoriteRepository.save(favorite);
            
            log.info("岗位收藏成功 - 用户ID: {}, 岗位ID: {}, 收藏ID: {}", userId, jobId, savedFavorite.getId());
            return savedFavorite;
            
        } catch (Exception e) {
            log.error("岗位收藏失败 - 用户ID: {}, 岗位ID: {}, 错误: {}", userId, jobId, e.getMessage());
            throw new RuntimeException("岗位收藏失败", e);
        }
    }
    
    @Override
    @Transactional
    public void unfavoriteJob(Long userId, Long jobId) {
        try {
            // 检查是否存在收藏记录
            JobFavorite favorite = jobFavoriteRepository.findByUserIdAndJobId(userId, jobId);
            if (favorite != null) {
                jobFavoriteRepository.delete(favorite);
                log.info("取消收藏成功 - 用户ID: {}, 岗位ID: {}", userId, jobId);
            } else {
                log.warn("收藏记录不存在 - 用户ID: {}, 岗位ID: {}", userId, jobId);
            }
            
        } catch (Exception e) {
            log.error("取消收藏失败 - 用户ID: {}, 岗位ID: {}, 错误: {}", userId, jobId, e.getMessage());
            throw new RuntimeException("取消收藏失败", e);
        }
    }
    
    @Override
    public boolean isJobFavorited(Long userId, Long jobId) {
        return jobFavoriteRepository.existsByUserIdAndJobId(userId, jobId);
    }
    
    @Override
    public List<JobFavorite> getUserFavorites(Long userId) {
        return jobFavoriteRepository.findByUserId(userId);
    }
    
    @Override
    public Page<JobFavorite> getUserFavorites(Long userId, Pageable pageable) {
        return jobFavoriteRepository.findByUserId(userId, pageable);
    }
    
    @Override
    public Page<JobWithCompanyDTO> getUserFavoriteJobs(Long userId, Pageable pageable) {
        try {
            // 获取用户的收藏记录分页
            Page<JobFavorite> favoritesPage = jobFavoriteRepository.findByUserId(userId, pageable);
            
            // 提取岗位ID列表
            List<Long> jobIds = favoritesPage.getContent().stream()
                    .map(JobFavorite::getJobId)
                    .collect(Collectors.toList());
            
            // 获取岗位详情
            List<JobWithCompanyDTO> jobDetails = new ArrayList<>();
            for (Long jobId : jobIds) {
                jobService.getJobWithCompanyById(jobId).ifPresent(jobDetails::add);
            }
            
            // 返回分页结果
            return new PageImpl<>(jobDetails, pageable, favoritesPage.getTotalElements());
            
        } catch (Exception e) {
            log.error("获取用户收藏岗位详情失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("获取收藏岗位详情失败", e);
        }
    }
    
    @Override
    public List<Long> getUserFavoriteJobIds(Long userId) {
        return jobFavoriteRepository.findJobIdsByUserId(userId);
    }
    
    @Override
    public Map<Long, Boolean> batchCheckFavoriteStatus(Long userId, List<Long> jobIds) {
        try {
            // 获取用户已收藏的岗位ID列表
            List<Long> favoritedJobIds = jobFavoriteRepository.findFavoriteJobIdsByUserIdAndJobIds(userId, jobIds);
            
            // 构建结果映射
            Map<Long, Boolean> result = new HashMap<>();
            for (Long jobId : jobIds) {
                result.put(jobId, favoritedJobIds.contains(jobId));
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("批量检查收藏状态失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("批量检查收藏状态失败", e);
        }
    }
    
    @Override
    public Long getUserFavoriteCount(Long userId) {
        return jobFavoriteRepository.countByUserId(userId);
    }
    
    @Override
    public Long getJobFavoriteCount(Long jobId) {
        return jobFavoriteRepository.countByJobId(jobId);
    }
    
    @Override
    public Map<Long, Long> batchGetJobFavoriteCounts(List<Long> jobIds) {
        try {
            List<Object[]> countResults = jobFavoriteRepository.countByJobIds(jobIds);
            
            Map<Long, Long> result = new HashMap<>();
            for (Object[] countResult : countResults) {
                Long jobId = (Long) countResult[0];
                Long count = (Long) countResult[1];
                result.put(jobId, count);
            }
            
            // 为没有收藏记录的岗位设置0
            for (Long jobId : jobIds) {
                result.putIfAbsent(jobId, 0L);
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("批量获取岗位收藏数量失败 - 错误: {}", e.getMessage());
            throw new RuntimeException("批量获取岗位收藏数量失败", e);
        }
    }
    
    @Override
    public List<Long> getHotFavoriteJobs(int limit) {
        try {
            List<Object[]> hotJobs = jobFavoriteRepository.findHotFavoriteJobs(limit);
            return hotJobs.stream()
                    .map(result -> (Long) result[0])
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("获取热门收藏岗位失败 - 错误: {}", e.getMessage());
            throw new RuntimeException("获取热门收藏岗位失败", e);
        }
    }
    
    @Override
    public List<JobFavorite> getUserLatestFavorites(Long userId, int limit) {
        try {
            return jobFavoriteRepository.findLatestByUserId(userId, 
                    org.springframework.data.domain.PageRequest.of(0, limit));
            
        } catch (Exception e) {
            log.error("获取用户最近收藏失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("获取最近收藏失败", e);
        }
    }
    
    @Override
    @Transactional
    public void deleteAllUserFavorites(Long userId) {
        try {
            jobFavoriteRepository.deleteByUserId(userId);
            log.info("删除用户所有收藏成功 - 用户ID: {}", userId);
            
        } catch (Exception e) {
            log.error("删除用户所有收藏失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("删除用户所有收藏失败", e);
        }
    }
    
    @Override
    @Transactional
    public void deleteAllJobFavorites(Long jobId) {
        try {
            jobFavoriteRepository.deleteByJobId(jobId);
            log.info("删除岗位所有收藏成功 - 岗位ID: {}", jobId);
            
        } catch (Exception e) {
            log.error("删除岗位所有收藏失败 - 岗位ID: {}, 错误: {}", jobId, e.getMessage());
            throw new RuntimeException("删除岗位所有收藏失败", e);
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteUserFavorites(Long userId, List<Long> jobIds) {
        try {
            for (Long jobId : jobIds) {
                jobFavoriteRepository.deleteByUserIdAndJobId(userId, jobId);
            }
            log.info("批量删除用户收藏成功 - 用户ID: {}, 删除数量: {}", userId, jobIds.size());
            
        } catch (Exception e) {
            log.error("批量删除用户收藏失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("批量删除用户收藏失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getFavoriteStatistics(Long userId) {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 用户收藏总数
            Long totalFavorites = getUserFavoriteCount(userId);
            statistics.put("totalFavorites", totalFavorites);
            
            // 最近7天收藏数量
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            List<JobFavorite> recentFavorites = jobFavoriteRepository.findByUserIdAndCreateTimeBetween(
                    userId, weekAgo, LocalDateTime.now());
            statistics.put("recentWeekFavorites", recentFavorites.size());
            
            // 热门收藏岗位
            List<Long> hotFavoriteJobs = getHotFavoriteJobs(5);
            statistics.put("hotFavoriteJobs", hotFavoriteJobs);
            
            // 最近收藏的岗位
            List<JobFavorite> latestFavorites = getUserLatestFavorites(userId, 5);
            statistics.put("latestFavorites", latestFavorites);
            
            // 收藏最多的岗位类型统计（需要额外实现）
            statistics.put("favoriteJobTypes", getFavoriteJobTypes(userId));
            
            statistics.put("lastUpdated", LocalDateTime.now().toString());
            
            return statistics;
            
        } catch (Exception e) {
            log.error("获取收藏统计信息失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("获取收藏统计信息失败", e);
        }
    }
    
    /**
     * 获取用户收藏的岗位类型统计（辅助方法）
     */
    private Map<String, Long> getFavoriteJobTypes(Long userId) {
        // 这里需要实现具体的岗位类型统计逻辑
        // 暂时返回空映射，后续可以根据业务需求实现
        return new HashMap<>();
    }
}