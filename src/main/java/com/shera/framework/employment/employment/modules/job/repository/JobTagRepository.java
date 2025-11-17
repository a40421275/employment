package com.shera.framework.employment.employment.modules.job.repository;

import com.shera.framework.employment.employment.modules.job.entity.JobTag;
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
     * 根据标签名称查询标签
     */
    List<JobTag> findByTagName(String tagName);
    
    /**
     * 根据标签名称列表查询标签
     */
    List<JobTag> findByTagNameIn(List<String> tagNames);
    
    /**
     * 统计标签的使用次数
     */
    @Query("SELECT jt.tagName, jt.useCount FROM JobTag jt ORDER BY jt.useCount DESC")
    List<Object[]> countByTagName();
    
    /**
     * 查询热门标签
     */
    @Query("SELECT jt FROM JobTag jt ORDER BY jt.useCount DESC")
    List<JobTag> findHotTags(Pageable pageable);
    
    /**
     * 根据条件查询标签（分页）
     */
    @Query("SELECT jt FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType)")
    Page<JobTag> findByConditions(@Param("tagName") String tagName,
                                  @Param("tagType") Integer tagType,
                                  Pageable pageable);
    
    /**
     * 查询所有唯一的标签名称（分页）
     */
    @Query("SELECT DISTINCT jt.tagName FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType)")
    Page<String> findDistinctTagNamesByConditions(@Param("tagName") String tagName,
                                                  @Param("tagType") Integer tagType,
                                                  Pageable pageable);
    
    /**
     * 查询所有唯一的标签名称（不分页）
     */
    @Query("SELECT DISTINCT jt.tagName FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType)")
    List<String> findDistinctTagNamesByConditions(@Param("tagName") String tagName,
                                                  @Param("tagType") Integer tagType);
    
    /**
     * 统计符合条件的标签数量
     */
    @Query("SELECT COUNT(DISTINCT jt.tagName) FROM JobTag jt WHERE " +
           "(:tagName IS NULL OR jt.tagName LIKE %:tagName%) AND " +
           "(:tagType IS NULL OR jt.tagType = :tagType)")
    Long countDistinctTagNamesByConditions(@Param("tagName") String tagName,
                                           @Param("tagType") Integer tagType);
}
