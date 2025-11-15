package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.JobFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 岗位收藏数据访问接口
 */
@Repository
public interface JobFavoriteRepository extends JpaRepository<JobFavorite, Long> {
    
    /**
     * 根据用户ID查询收藏记录
     */
    List<JobFavorite> findByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询收藏记录
     */
    Page<JobFavorite> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据岗位ID查询收藏记录
     */
    List<JobFavorite> findByJobId(Long jobId);
    
    /**
     * 根据用户ID和岗位ID查询收藏记录
     */
    JobFavorite findByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 检查用户是否收藏了岗位
     */
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 根据用户ID列表查询收藏记录
     */
    List<JobFavorite> findByUserIdIn(List<Long> userIds);
    
    /**
     * 根据岗位ID列表查询收藏记录
     */
    List<JobFavorite> findByJobIdIn(List<Long> jobIds);
    
    /**
     * 删除用户的收藏记录
     */
    void deleteByUserId(Long userId);
    
    /**
     * 删除岗位的收藏记录
     */
    void deleteByJobId(Long jobId);
    
    /**
     * 删除用户的特定岗位收藏记录
     */
    void deleteByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 根据用户ID列表删除收藏记录
     */
    void deleteByUserIdIn(List<Long> userIds);
    
    /**
     * 根据岗位ID列表删除收藏记录
     */
    void deleteByJobIdIn(List<Long> jobIds);
    
    /**
     * 统计用户的收藏数量
     */
    Long countByUserId(Long userId);
    
    /**
     * 统计岗位的收藏数量
     */
    Long countByJobId(Long jobId);
    
    /**
     * 统计多个岗位的收藏数量
     */
    @Query("SELECT jf.jobId, COUNT(jf) FROM JobFavorite jf WHERE jf.jobId IN :jobIds GROUP BY jf.jobId")
    List<Object[]> countByJobIds(@Param("jobIds") List<Long> jobIds);
    
    /**
     * 查询用户收藏的岗位ID列表
     */
    @Query("SELECT jf.jobId FROM JobFavorite jf WHERE jf.userId = :userId")
    List<Long> findJobIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 查询收藏岗位的用户ID列表
     */
    @Query("SELECT jf.userId FROM JobFavorite jf WHERE jf.jobId = :jobId")
    List<Long> findUserIdsByJobId(@Param("jobId") Long jobId);
    
    /**
     * 查询热门收藏岗位
     */
    @Query("SELECT jf.jobId, COUNT(jf) as count FROM JobFavorite jf GROUP BY jf.jobId ORDER BY count DESC")
    List<Object[]> findHotFavoriteJobs(@Param("limit") int limit);
    
    /**
     * 根据创建时间范围查询收藏记录
     */
    List<JobFavorite> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据用户ID和创建时间范围查询收藏记录
     */
    List<JobFavorite> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据岗位ID和创建时间范围查询收藏记录
     */
    List<JobFavorite> findByJobIdAndCreateTimeBetween(Long jobId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 查询用户最近收藏的岗位
     */
    @Query("SELECT jf FROM JobFavorite jf WHERE jf.userId = :userId ORDER BY jf.createTime DESC")
    List<JobFavorite> findLatestByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * 批量检查用户是否收藏了岗位
     */
    @Query("SELECT jf.jobId FROM JobFavorite jf WHERE jf.userId = :userId AND jf.jobId IN :jobIds")
    List<Long> findFavoriteJobIdsByUserIdAndJobIds(@Param("userId") Long userId, @Param("jobIds") List<Long> jobIds);
}
