package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 岗位分类仓库接口
 */
@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    
    /**
     * 根据分类名称查询
     */
    Optional<JobCategory> findByName(String name);
    
    /**
     * 根据父分类ID查询子分类
     */
    List<JobCategory> findByParentId(Long parentId);
    
    /**
     * 查询顶级分类（parentId为null）
     */
    List<JobCategory> findByParentIdIsNull();
    
    /**
     * 根据状态查询分类
     */
    List<JobCategory> findByStatus(Integer status);
    
    /**
     * 根据分类名称模糊查询
     */
    List<JobCategory> findByNameContaining(String name);
    
    /**
     * 统计分类下的岗位数量
     */
    @Query("SELECT c.id, c.name, COUNT(j) FROM JobCategory c LEFT JOIN Job j ON j.categoryId = c.id GROUP BY c.id, c.name")
    List<Object[]> countJobsByCategory();
    
    /**
     * 查询热门分类（岗位数量最多的分类）
     */
    @Query("SELECT c.id, c.name, COUNT(j) as jobCount FROM JobCategory c LEFT JOIN Job j ON j.categoryId = c.id GROUP BY c.id, c.name ORDER BY jobCount DESC")
    List<Object[]> findHotCategories();
    
    /**
     * 检查分类名称是否已存在（排除当前分类）
     */
    @Query("SELECT COUNT(c) > 0 FROM JobCategory c WHERE c.name = :name AND c.id != :id")
    boolean existsByNameAndIdNot(String name, Long id);
    
    /**
     * 检查分类名称是否已存在
     */
    boolean existsByName(String name);
    
    /**
     * 根据分类层级查询
     */
    List<JobCategory> findByLevel(Integer level);
    
    /**
     * 查询所有启用的分类
     */
    List<JobCategory> findByStatusOrderBySortOrderAsc(Integer status);
}
