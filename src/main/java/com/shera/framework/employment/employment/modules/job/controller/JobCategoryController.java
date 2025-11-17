package com.shera.framework.employment.employment.modules.job.controller;

import com.shera.framework.employment.employment.modules.job.dto.JobCategoryDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobCategory;
import com.shera.framework.employment.employment.modules.job.service.JobCategoryService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位分类控制器
 */
@RestController
@RequestMapping("/api/job-categories")
@RequiredArgsConstructor
public class JobCategoryController {
    
    private final JobCategoryService jobCategoryService;
    
    /**
     * 创建岗位分类
     */
    @PostMapping
    public ResponseEntity<?> createJobCategory(@RequestBody JobCategoryDTO jobCategoryDTO) {
        JobCategory jobCategory = jobCategoryService.createJobCategory(jobCategoryDTO);
        return ResponseUtil.success("创建成功", jobCategory);
    }
    
    /**
     * 更新岗位分类
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJobCategory(@PathVariable Long id, @RequestBody JobCategoryDTO jobCategoryDTO) {
        JobCategory jobCategory = jobCategoryService.updateJobCategory(id, jobCategoryDTO);
        return ResponseUtil.success("更新成功", jobCategory);
    }
    
    /**
     * 删除岗位分类
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJobCategory(@PathVariable Long id) {
        jobCategoryService.deleteJobCategory(id);
        return ResponseUtil.successOperation("删除成功");
    }
    
    /**
     * 获取岗位分类详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobCategory(@PathVariable Long id) {
        JobCategory jobCategory = jobCategoryService.getJobCategoryDetail(id);
        return ResponseUtil.success("获取成功", jobCategory);
    }
    
    /**
     * 查询岗位分类列表
     */
    @GetMapping
    public ResponseEntity<?> getJobCategories(
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(sortDirection, sort);
        List<JobCategory> jobCategories = jobCategoryService.getJobCategories(sortObj);
        return ResponseUtil.successList(jobCategories, "查询成功");
    }
    
    /**
     * 获取所有岗位分类列表
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllJobCategories() {
        List<JobCategory> jobCategories = jobCategoryService.getAllJobCategories();
        return ResponseUtil.successList(jobCategories, "获取成功");
    }
    
    /**
     * 根据父分类ID查询子分类
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<?> getJobCategoriesByParent(@PathVariable Long parentId) {
        List<JobCategory> jobCategories = jobCategoryService.getJobCategoriesByParentId(parentId);
        return ResponseUtil.successList(jobCategories, "查询成功");
    }
    
    /**
     * 获取顶级分类列表
     */
    @GetMapping("/top-level")
    public ResponseEntity<?> getTopLevelCategories() {
        List<JobCategory> jobCategories = jobCategoryService.getTopLevelCategories();
        return ResponseUtil.successList(jobCategories, "获取成功");
    }
    
    /**
     * 根据状态查询分类列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getJobCategoriesByStatus(@PathVariable Integer status) {
        List<JobCategory> jobCategories = jobCategoryService.getJobCategoriesByStatus(status);
        return ResponseUtil.successList(jobCategories, "查询成功");
    }
    
    /**
     * 根据分类名称搜索
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchJobCategories(@RequestParam String name) {
        List<JobCategory> jobCategories = jobCategoryService.searchJobCategoriesByName(name);
        return ResponseUtil.successList(jobCategories, "搜索成功");
    }
    
    /**
     * 更新分类状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        JobCategory jobCategory = jobCategoryService.updateStatus(id, status);
        return ResponseUtil.success("更新状态成功", jobCategory);
    }
    
    /**
     * 更新排序序号
     */
    @PutMapping("/{id}/sort-order")
    public ResponseEntity<?> updateSortOrder(@PathVariable Long id, @RequestParam Integer sortOrder) {
        JobCategory jobCategory = jobCategoryService.updateSortOrder(id, sortOrder);
        return ResponseUtil.success("更新排序成功", jobCategory);
    }
    
    /**
     * 获取分类树形结构
     */
    @GetMapping("/tree")
    public ResponseEntity<?> getCategoryTree() {
        List<JobCategory> categoryTree = jobCategoryService.getCategoryTree();
        return ResponseUtil.successList(categoryTree, "获取成功");
    }
    
    /**
     * 获取分类路径
     */
    @GetMapping("/{id}/path")
    public ResponseEntity<?> getCategoryPath(@PathVariable Long id) {
        List<JobCategory> categoryPath = jobCategoryService.getCategoryPath(id);
        return ResponseUtil.successList(categoryPath, "获取成功");
    }
    
    /**
     * 统计分类下的岗位数量
     */
    @GetMapping("/stats/job-count")
    public ResponseEntity<?> countJobsByCategory() {
        Map<Long, Long> jobCounts = jobCategoryService.countJobsByCategory();
        return ResponseUtil.success("统计成功", jobCounts);
    }
    
    /**
     * 获取热门分类
     */
    @GetMapping("/hot")
    public ResponseEntity<?> getHotCategories() {
        List<Object[]> hotCategories = jobCategoryService.getHotCategories();
        return ResponseUtil.success("获取成功", hotCategories);
    }
    
    /**
     * 检查分类名称是否已存在
     */
    @GetMapping("/check-name")
    public ResponseEntity<?> checkNameExists(@RequestParam String name, @RequestParam(required = false) Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = jobCategoryService.isNameExists(name, excludeId);
        } else {
            exists = jobCategoryService.isNameExists(name);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("valid", !exists);
        result.put("message", exists ? "分类名称已存在" : "分类名称可用");
        return ResponseUtil.success("验证成功", result);
    }
    
    /**
     * 获取启用的分类列表
     */
    @GetMapping("/enabled")
    public ResponseEntity<?> getEnabledCategories() {
        List<JobCategory> enabledCategories = jobCategoryService.getEnabledCategories();
        return ResponseUtil.successList(enabledCategories, "获取成功");
    }
    
    /**
     * 根据层级查询分类
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<?> getJobCategoriesByLevel(@PathVariable Integer level) {
        List<JobCategory> jobCategories = jobCategoryService.getJobCategoriesByLevel(level);
        return ResponseUtil.successList(jobCategories, "查询成功");
    }
    
    /**
     * 批量更新分类状态
     */
    @PutMapping("/batch-status")
    public ResponseEntity<?> batchUpdateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        List<JobCategory> categories = jobCategoryService.batchUpdateStatus(ids, status);
        return ResponseUtil.success("批量更新状态成功", categories);
    }
    
    /**
     * 获取分类统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getCategoryStats() {
        Map<String, Object> stats = jobCategoryService.getCategoryStats();
        return ResponseUtil.successStats(stats, "统计成功");
    }
}
