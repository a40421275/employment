package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.JobTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 岗位标签数据访问接口
 */
@Repository
public interface JobTagRepository extends JpaRepository<JobTag, Long> {
    
    /**
     * 根据岗位ID查询标签
     */
    List<JobTag> findByJobId(Long jobId);
    
    /**
     * 根据标签名称查询标签
     */
    List<JobTag> findByTagName(String tagName);
    
    /**
     * 根据岗位ID和标签名称查询标签
     */
    JobTag findByJobIdAndTagName(Long jobId, String tagName);
    
    /**
     * 检查岗位是否包含特定标签
     */
    boolean existsByJobIdAndTagName(Long jobId, String tagName);
    
    /**
     * 根据岗位ID列表查询标签
     */
    List<JobTag> findByJobIdIn(List<Long> jobIds);
    
    /**
     * 根据标签名称列表查询标签
     */
    List<JobTag> findByTagNameIn(List<String> tagNames);
    
    /**
     * 删除岗位的所有标签
     */
    void deleteByJobId(Long jobId);
    
    /**
     * 删除特定岗位的特定标签
     */
    void deleteByJobIdAndTagName(Long jobId, String tagName);
    
    /**
     * 根据岗位ID列表删除标签
     */
    void deleteByJobIdIn(List<Long> jobIds);
    
    /**
     * 统计岗位的标签数量
     */
    Long countByJobId(Long jobId);
    
    /**
     * 统计标签的使用次数
     */
    @Query("SELECT jt.tagName, COUNT(jt) FROM JobTag jt GROUP BY jt.tagName ORDER BY COUNT(jt) DESC")
    List<Object[]> countByTagName();
    
    /**
     * 查询热门标签
     */
    @Query("SELECT jt.tagName, COUNT(jt) as count FROM JobTag jt GROUP BY jt.tagName ORDER BY count DESC")
    List<Object[]> findHotTags(@Param("limit") int limit);
    
    /**
     * 根据标签名称搜索岗位ID
     */
    @Query("SELECT DISTINCT jt.jobId FROM JobTag jt WHERE jt.tagName LIKE %:tagName%")
    List<Long> findJobIdsByTagNameContaining(@Param("tagName") String tagName);
    
    /**
     * 根据多个标签名称查询岗位ID
     */
    @Query("SELECT DISTINCT jt.jobId FROM JobTag jt WHERE jt.tagName IN :tagNames")
    List<Long> findJobIdsByTagNames(@Param("tagNames") List<String> tagNames);
    
    /**
     * 查询包含所有指定标签的岗位ID
     */
    @Query("SELECT jt.jobId FROM JobTag jt WHERE jt.tagName IN :tagNames GROUP BY jt.jobId HAVING COUNT(DISTINCT jt.tagName) = :tagCount")
    List<Long> findJobIdsByAllTagNames(@Param("tagNames") List<String> tagNames, @Param("tagCount") long tagCount);
    
    /**
     * 查询岗位的标签名称列表
     */
    @Query("SELECT jt.tagName FROM JobTag jt WHERE jt.jobId = :jobId")
    List<String> findTagNamesByJobId(@Param("jobId") Long jobId);
    
    /**
     * 批量插入标签
     */
    @Query(value = "INSERT IGNORE INTO job_tag (job_id, tag_name, create_time) VALUES (:jobId, :tagName, NOW())", nativeQuery = true)
    void insertTag(@Param("jobId") Long jobId, @Param("tagName") String tagName);
    
    /**
     * 根据条件查询标签（分页）
     */
    @Query("SELECT jt FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType) AND " +
           "(:jobId IS NULL OR jt.jobId = :jobId)")
    Page<JobTag> findByConditions(@Param("tagName") String tagName,
                                  @Param("tagType") Integer tagType,
                                  @Param("jobId") Long jobId,
                                  Pageable pageable);
    
    /**
     * 查询所有唯一的标签名称（分页）
     */
    @Query("SELECT DISTINCT jt.tagName FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType) AND " +
           "(:jobId IS NULL OR jt.jobId = :jobId) AND " +
           "(:independentOnly = false OR jt.jobId IS NULL) AND " +
           "(:associatedOnly = false OR jt.jobId IS NOT NULL)")
    Page<String> findDistinctTagNamesByConditions(@Param("tagName") String tagName,
                                                  @Param("tagType") Integer tagType,
                                                  @Param("jobId") Long jobId,
                                                  @Param("independentOnly") Boolean independentOnly,
                                                  @Param("associatedOnly") Boolean associatedOnly,
                                                  Pageable pageable);
    
    /**
     * 查询所有唯一的标签名称（不分页）
     */
    @Query("SELECT DISTINCT jt.tagName FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType) AND " +
           "(:jobId IS NULL OR jt.jobId = :jobId) AND " +
           "(:independentOnly = false OR jt.jobId IS NULL) AND " +
           "(:associatedOnly = false OR jt.jobId IS NOT NULL)")
    List<String> findDistinctTagNamesByConditions(@Param("tagName") String tagName,
                                                  @Param("tagType") Integer tagType,
                                                  @Param("jobId") Long jobId,
                                                  @Param("independentOnly") Boolean independentOnly,
                                                  @Param("associatedOnly") Boolean associatedOnly);
    
    /**
     * 统计符合条件的标签数量
     */
    @Query("SELECT COUNT(DISTINCT jt.tagName) FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType) AND " +
           "(:jobId IS NULL OR jt.jobId = :jobId) AND " +
           "(:independentOnly = false OR jt.jobId IS NULL) AND " +
           "(:associatedOnly = false OR jt.jobId IS NOT NULL)")
    Long countDistinctTagNamesByConditions(@Param("tagName") String tagName,
                                           @Param("tagType") Integer tagType,
                                           @Param("jobId") Long jobId,
                                           @Param("independentOnly") Boolean independentOnly,
                                           @Param("associatedOnly") Boolean associatedOnly);
}
