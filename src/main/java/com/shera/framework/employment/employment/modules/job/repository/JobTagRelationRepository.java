package com.shera.framework.employment.employment.modules.job.repository;

import com.shera.framework.employment.employment.modules.job.entity.JobTagRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 岗位标签关联数据访问接口
 */
@Repository
public interface JobTagRelationRepository extends JpaRepository<JobTagRelation, Long> {
    
    /**
     * 根据岗位ID查询关联关系
     */
    List<JobTagRelation> findByJobId(Long jobId);
    
    /**
     * 根据标签ID查询关联关系
     */
    List<JobTagRelation> findByTagId(Long tagId);
    
    /**
     * 根据岗位ID和标签ID查询关联关系
     */
    JobTagRelation findByJobIdAndTagId(Long jobId, Long tagId);
    
    /**
     * 检查岗位和标签是否已关联
     */
    boolean existsByJobIdAndTagId(Long jobId, Long tagId);
    
    /**
     * 根据岗位ID删除所有关联关系
     */
    void deleteByJobId(Long jobId);
    
    /**
     * 根据标签ID删除所有关联关系
     */
    void deleteByTagId(Long tagId);
    
    /**
     * 删除特定岗位和标签的关联关系
     */
    void deleteByJobIdAndTagId(Long jobId, Long tagId);
    
    /**
     * 根据岗位ID列表删除关联关系
     */
    void deleteByJobIdIn(List<Long> jobIds);
    
    /**
     * 根据标签ID列表删除关联关系
     */
    void deleteByTagIdIn(List<Long> tagIds);
    
    /**
     * 统计岗位的标签数量
     */
    Long countByJobId(Long jobId);
    
    /**
     * 统计标签的使用次数
     */
    Long countByTagId(Long tagId);
    
    /**
     * 根据岗位ID列表查询关联关系
     */
    List<JobTagRelation> findByJobIdIn(List<Long> jobIds);
    
    /**
     * 根据标签ID列表查询关联关系
     */
    List<JobTagRelation> findByTagIdIn(List<Long> tagIds);
    
    /**
     * 批量插入关联关系
     */
    @Modifying
    @Query(value = "INSERT IGNORE INTO job_tag_relation (job_id, tag_id, create_time) VALUES (:jobId, :tagId, NOW())", nativeQuery = true)
    void insertRelation(@Param("jobId") Long jobId, @Param("tagId") Long tagId);
}
