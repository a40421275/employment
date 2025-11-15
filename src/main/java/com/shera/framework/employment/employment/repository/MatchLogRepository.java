package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.MatchLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 匹配记录数据访问接口
 */
@Repository
public interface MatchLogRepository extends JpaRepository<MatchLog, Long> {
    
    /**
     * 根据用户ID查询匹配记录
     */
    List<MatchLog> findByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询匹配记录
     */
    Page<MatchLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据岗位ID查询匹配记录
     */
    List<MatchLog> findByJobId(Long jobId);
    
    /**
     * 根据用户ID和岗位ID查询匹配记录
     */
    MatchLog findByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 根据匹配分数范围查询匹配记录
     */
    List<MatchLog> findByMatchScoreBetween(Double minScore, Double maxScore);
    
    /**
     * 根据是否已查看查询匹配记录
     */
    List<MatchLog> findByIsViewed(Boolean isViewed);
    
    /**
     * 根据用户ID和是否已查看查询匹配记录
     */
    List<MatchLog> findByUserIdAndIsViewed(Long userId, Boolean isViewed);
    
    /**
     * 根据岗位ID和是否已查看查询匹配记录
     */
    List<MatchLog> findByJobIdAndIsViewed(Long jobId, Boolean isViewed);
    
    /**
     * 根据用户ID和匹配分数范围查询匹配记录
     */
    List<MatchLog> findByUserIdAndMatchScoreBetween(Long userId, Double minScore, Double maxScore);
    
    /**
     * 根据岗位ID和匹配分数范围查询匹配记录
     */
    List<MatchLog> findByJobIdAndMatchScoreBetween(Long jobId, Double minScore, Double maxScore);
    
    /**
     * 根据创建时间范围查询匹配记录
     */
    List<MatchLog> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据用户ID和创建时间范围查询匹配记录
     */
    List<MatchLog> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据岗位ID和创建时间范围查询匹配记录
     */
    List<MatchLog> findByJobIdAndCreateTimeBetween(Long jobId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 检查用户和岗位是否存在匹配记录
     */
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 统计用户的匹配记录数量
     */
    Long countByUserId(Long userId);
    
    /**
     * 统计岗位的匹配记录数量
     */
    Long countByJobId(Long jobId);
    
    /**
     * 统计用户未查看的匹配记录数量
     */
    Long countByUserIdAndIsViewed(Long userId, Boolean isViewed);
    
    /**
     * 统计岗位未查看的匹配记录数量
     */
    Long countByJobIdAndIsViewed(Long jobId, Boolean isViewed);
    
    /**
     * 查询高匹配度的记录
     */
    @Query("SELECT ml FROM MatchLog ml WHERE ml.matchScore >= :threshold ORDER BY ml.matchScore DESC")
    List<MatchLog> findHighMatchApplications(@Param("threshold") Double threshold);
    
    /**
     * 查询用户的高匹配度记录
     */
    @Query("SELECT ml FROM MatchLog ml WHERE ml.userId = :userId AND ml.matchScore >= :threshold ORDER BY ml.matchScore DESC")
    List<MatchLog> findHighMatchByUserId(@Param("userId") Long userId, @Param("threshold") Double threshold);
    
    /**
     * 查询岗位的高匹配度记录
     */
    @Query("SELECT ml FROM MatchLog ml WHERE ml.jobId = :jobId AND ml.matchScore >= :threshold ORDER BY ml.matchScore DESC")
    List<MatchLog> findHighMatchByJobId(@Param("jobId") Long jobId, @Param("threshold") Double threshold);
    
    /**
     * 查询用户的最新匹配记录
     */
    @Query("SELECT ml FROM MatchLog ml WHERE ml.userId = :userId ORDER BY ml.createTime DESC")
    List<MatchLog> findLatestByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * 查询岗位的最新匹配记录
     */
    @Query("SELECT ml FROM MatchLog ml WHERE ml.jobId = :jobId ORDER BY ml.createTime DESC")
    List<MatchLog> findLatestByJobId(@Param("jobId") Long jobId, Pageable pageable);
    
    /**
     * 统计匹配分数分布
     */
    @Query("SELECT FLOOR(ml.matchScore/10)*10 as scoreRange, COUNT(ml) FROM MatchLog ml GROUP BY FLOOR(ml.matchScore/10)*10 ORDER BY scoreRange")
    List<Object[]> countByScoreRange();
    
    /**
     * 统计用户的匹配分数分布
     */
    @Query("SELECT FLOOR(ml.matchScore/10)*10 as scoreRange, COUNT(ml) FROM MatchLog ml WHERE ml.userId = :userId GROUP BY FLOOR(ml.matchScore/10)*10 ORDER BY scoreRange")
    List<Object[]> countByUserIdAndScoreRange(@Param("userId") Long userId);
    
    /**
     * 统计岗位的匹配分数分布
     */
    @Query("SELECT FLOOR(ml.matchScore/10)*10 as scoreRange, COUNT(ml) FROM MatchLog ml WHERE ml.jobId = :jobId GROUP BY FLOOR(ml.matchScore/10)*10 ORDER BY scoreRange")
    List<Object[]> countByJobIdAndScoreRange(@Param("jobId") Long jobId);
    
    /**
     * 批量标记为已查看
     */
    @Query("UPDATE MatchLog ml SET ml.isViewed = true WHERE ml.id IN :ids")
    int markMultipleAsViewed(@Param("ids") List<Long> ids);
    
    /**
     * 标记用户的所有匹配记录为已查看
     */
    @Query("UPDATE MatchLog ml SET ml.isViewed = true WHERE ml.userId = :userId")
    int markAllAsViewedByUserId(@Param("userId") Long userId);
    
    /**
     * 标记岗位的所有匹配记录为已查看
     */
    @Query("UPDATE MatchLog ml SET ml.isViewed = true WHERE ml.jobId = :jobId")
    int markAllAsViewedByJobId(@Param("jobId") Long jobId);
    
    /**
     * 删除用户的匹配记录
     */
    void deleteByUserId(Long userId);
    
    /**
     * 删除岗位的匹配记录
     */
    void deleteByJobId(Long jobId);
    
    /**
     * 根据用户ID列表删除匹配记录
     */
    void deleteByUserIdIn(List<Long> userIds);
    
    /**
     * 根据岗位ID列表删除匹配记录
     */
    void deleteByJobIdIn(List<Long> jobIds);
    
    /**
     * 查询平均匹配分数
     */
    @Query("SELECT AVG(ml.matchScore) FROM MatchLog ml")
    Double findAverageMatchScore();
    
    /**
     * 查询用户的平均匹配分数
     */
    @Query("SELECT AVG(ml.matchScore) FROM MatchLog ml WHERE ml.userId = :userId")
    Double findAverageMatchScoreByUserId(@Param("userId") Long userId);
    
    /**
     * 查询岗位的平均匹配分数
     */
    @Query("SELECT AVG(ml.matchScore) FROM MatchLog ml WHERE ml.jobId = :jobId")
    Double findAverageMatchScoreByJobId(@Param("jobId") Long jobId);
}
