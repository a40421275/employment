package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.JobCategoryDTO;
import com.shera.framework.employment.employment.entity.JobCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * 岗位分类服务接口
 */
public interface JobCategoryService {
    
    /**
     * 创建岗位分类
     */
    JobCategory createJobCategory(JobCategoryDTO jobCategoryDTO);
    
    /**
     * 更新岗位分类
     */
    JobCategory updateJobCategory(Long id, JobCategoryDTO jobCategoryDTO);
    
    /**
     * 删除岗位分类
     */
    void deleteJobCategory(Long id);
    
    /**
     * 获取岗位分类详情
     */
    JobCategory getJobCategoryDetail(Long id);
    
    /**
     * 分页查询岗位分类列表
     */
    Page<JobCategory> getJobCategories(Pageable pageable);

    /**
     * 查询岗位分类列表（支持排序）
     */
    List<JobCategory> getJobCategories(Sort sort);
    
    /**
     * 获取所有岗位分类列表
     */
    List<JobCategory> getAllJobCategories();
    
    /**
     * 根据父分类ID查询子分类
     */
    List<JobCategory> getJobCategoriesByParentId(Long parentId);
    
    /**
     * 获取顶级分类列表
     */
    List<JobCategory> getTopLevelCategories();
    
    /**
     * 根据状态查询分类列表
     */
    List<JobCategory> getJobCategoriesByStatus(Integer status);
    
    /**
     * 根据分类名称搜索
     */
    List<JobCategory> searchJobCategoriesByName(String name);
    
    /**
     * 更新分类状态
     */
    JobCategory updateStatus(Long id, Integer status);
    
    /**
     * 更新排序序号
     */
    JobCategory updateSortOrder(Long id, Integer sortOrder);
    
    /**
     * 获取分类树形结构
     */
    List<JobCategory> getCategoryTree();
    
    /**
     * 获取分类路径（从根分类到当前分类）
     */
    List<JobCategory> getCategoryPath(Long id);
    
    /**
     * 统计分类下的岗位数量
     */
    Map<Long, Long> countJobsByCategory();
    
    /**
     * 获取热门分类
     */
    List<Object[]> getHotCategories();
    
    /**
     * 检查分类名称是否已存在
     */
    boolean isNameExists(String name);
    
    /**
     * 检查分类名称是否已存在（排除当前分类）
     */
    boolean isNameExists(String name, Long excludeId);
    
    /**
     * 获取启用的分类列表
     */
    List<JobCategory> getEnabledCategories();
    
    /**
     * 根据层级查询分类
     */
    List<JobCategory> getJobCategoriesByLevel(Integer level);
    
    /**
     * 批量更新分类状态
     */
    List<JobCategory> batchUpdateStatus(List<Long> ids, Integer status);
    
    /**
     * 获取分类统计信息
     */
    Map<String, Object> getCategoryStats();
}
