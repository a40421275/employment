package com.shera.framework.employment.employment.modules.job.repository;

import com.shera.framework.employment.employment.modules.job.entity.JobApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 岗位申请Repository
 */
@Repository
public interface JobApplyRepository extends JpaRepository<JobApply, Long> {
    
    /**
     * 根据用户ID查找申请记录
     */
    List<JobApply> findByUserId(Long userId);
    
    /**
     * 根据用户ID分页查找申请记录
     */
    Page<JobApply> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据岗位ID查找申请记录
     */
    List<JobApply> findByJobId(Long jobId);
    
    /**
     * 根据岗位ID分页查找申请记录
     */
    Page<JobApply> findByJobId(Long jobId, Pageable pageable);
    
    /**
     * 根据用户ID和岗位ID查找申请记录
     */
    Optional<JobApply> findByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 根据状态查找申请记录
     */
    List<JobApply> findByStatus(Integer status);
    
    /**
     * 根据用户ID和状态查找申请记录
     */
    List<JobApply> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据岗位ID和状态查找申请记录
     */
    List<JobApply> findByJobIdAndStatus(Long jobId, Integer status);
    
    /**
     * 根据用户ID和状态分页查找申请记录
     */
    Page<JobApply> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);
    
    /**
     * 检查用户是否已申请该岗位
     */
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    
    /**
     * 统计用户的申请数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计岗位的申请数量
     */
    long countByJobId(Long jobId);
    
    /**
     * 统计各状态的申请数量
     */
    @Query("SELECT ja.status, COUNT(ja) FROM JobApply ja GROUP BY ja.status")
    List<Object[]> countByStatus();
    
    /**
     * 根据匹配分数范围查找申请记录
     */
    @Query("SELECT ja FROM JobApply ja WHERE ja.matchScore BETWEEN :minScore AND :maxScore")
    List<JobApply> findByMatchScoreBetween(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * 查找高匹配度的申请记录
     */
    @Query("SELECT ja FROM JobApply ja WHERE ja.matchScore >= :threshold")
    List<JobApply> findHighMatchApplications(@Param("threshold") Double threshold);
    
    /**
     * 根据简历ID查找申请记录
     */
    List<JobApply> findByResumeId(Long resumeId);
    
    /**
     * 根据用户ID和状态统计申请数量
     */
    @Query("SELECT ja.status, COUNT(ja) FROM JobApply ja WHERE ja.userId = :userId GROUP BY ja.status")
    List<Object[]> countByUserIdAndStatus(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和状态统计申请数量
     */
    Long countByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 获取热门申请岗位（按申请量排序）
     */
    @Query("SELECT j.id, j.title, COUNT(ja) as applyCount FROM Job j LEFT JOIN JobApply ja ON j.id = ja.jobId GROUP BY j.id, j.title ORDER BY applyCount DESC")
    List<Object[]> findHotAppliedJobs(Pageable pageable);
    
    /**
     * 根据多个条件筛选申请记录
     */
    @Query("SELECT ja FROM JobApply ja WHERE " +
           "(:userId IS NULL OR ja.userId = :userId) AND " +
           "(:jobId IS NULL OR ja.jobId = :jobId) AND " +
           "(:status IS NULL OR ja.status = :status) AND " +
           "(:minScore IS NULL OR ja.matchScore >= :minScore) AND " +
           "(:maxScore IS NULL OR ja.matchScore <= :maxScore)")
    Page<JobApply> findByMultipleConditions(@Param("userId") Long userId,
                                           @Param("jobId") Long jobId,
                                           @Param("status") Integer status,
                                           @Param("minScore") Double minScore,
                                           @Param("maxScore") Double maxScore,
                                           Pageable pageable);
}
