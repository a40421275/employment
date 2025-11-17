package com.shera.framework.employment.employment.modules.job.service.impl;

import com.shera.framework.employment.employment.modules.job.dto.JobCategoryDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobCategory;
import com.shera.framework.employment.employment.modules.job.repository.JobCategoryRepository;
import com.shera.framework.employment.employment.modules.job.service.JobCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位分类服务实现类
 */
@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {
    
    private final JobCategoryRepository jobCategoryRepository;
    
    @Override
    @Transactional
    public JobCategory createJobCategory(JobCategoryDTO jobCategoryDTO) {
        // 检查分类名称是否已存在
        if (jobCategoryRepository.existsByName(jobCategoryDTO.getName())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        JobCategory jobCategory = new JobCategory();
        jobCategory.setName(jobCategoryDTO.getName());
        jobCategory.setDescription(jobCategoryDTO.getDescription());
        jobCategory.setParentId(jobCategoryDTO.getParentId());
        jobCategory.setLevel(jobCategoryDTO.getLevel() != null ? jobCategoryDTO.getLevel() : 1);
        jobCategory.setSortOrder(jobCategoryDTO.getSortOrder() != null ? jobCategoryDTO.getSortOrder() : 0);
        jobCategory.setStatus(jobCategoryDTO.getStatus() != null ? jobCategoryDTO.getStatus() : 1);
        jobCategory.setIcon(jobCategoryDTO.getIcon());
        jobCategory.setColor(jobCategoryDTO.getColor());
        
        return jobCategoryRepository.save(jobCategory);
    }
    
    @Override
    @Transactional
    public JobCategory updateJobCategory(Long id, JobCategoryDTO jobCategoryDTO) {
        JobCategory jobCategory = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        // 检查分类名称是否已存在（排除当前分类）
        if (jobCategoryRepository.existsByNameAndIdNot(jobCategoryDTO.getName(), id)) {
            throw new RuntimeException("分类名称已存在");
        }
        
        jobCategory.setName(jobCategoryDTO.getName());
        jobCategory.setDescription(jobCategoryDTO.getDescription());
        jobCategory.setParentId(jobCategoryDTO.getParentId());
        jobCategory.setLevel(jobCategoryDTO.getLevel());
        jobCategory.setSortOrder(jobCategoryDTO.getSortOrder());
        jobCategory.setStatus(jobCategoryDTO.getStatus());
        jobCategory.setIcon(jobCategoryDTO.getIcon());
        jobCategory.setColor(jobCategoryDTO.getColor());
        
        return jobCategoryRepository.save(jobCategory);
    }
    
    @Override
    @Transactional
    public void deleteJobCategory(Long id) {
        JobCategory jobCategory = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        // 检查是否有子分类
        List<JobCategory> children = jobCategoryRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该分类下有子分类，无法删除");
        }
        
        jobCategoryRepository.delete(jobCategory);
    }
    
    @Override
    public JobCategory getJobCategoryDetail(Long id) {
        return jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
    }
    
    @Override
    public Page<JobCategory> getJobCategories(Pageable pageable) {
        return jobCategoryRepository.findAll(pageable);
    }

    @Override
    public List<JobCategory> getJobCategories(Sort sort) {
        return jobCategoryRepository.findAll(sort);
    }
    
    @Override
    public List<JobCategory> getAllJobCategories() {
        return jobCategoryRepository.findAll();
    }
    
    @Override
    public List<JobCategory> getJobCategoriesByParentId(Long parentId) {
        return jobCategoryRepository.findByParentId(parentId);
    }
    
    @Override
    public List<JobCategory> getTopLevelCategories() {
        return jobCategoryRepository.findByParentIdIsNull();
    }
    
    @Override
    public List<JobCategory> getJobCategoriesByStatus(Integer status) {
        return jobCategoryRepository.findByStatus(status);
    }
    
    @Override
    public List<JobCategory> searchJobCategoriesByName(String name) {
        return jobCategoryRepository.findByNameContaining(name);
    }
    
    @Override
    @Transactional
    public JobCategory updateStatus(Long id, Integer status) {
        JobCategory jobCategory = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        jobCategory.setStatus(status);
        
        return jobCategoryRepository.save(jobCategory);
    }
    
    @Override
    @Transactional
    public JobCategory updateSortOrder(Long id, Integer sortOrder) {
        JobCategory jobCategory = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        jobCategory.setSortOrder(sortOrder);
        
        return jobCategoryRepository.save(jobCategory);
    }
    
    @Override
    public List<JobCategory> getCategoryTree() {
        List<JobCategory> allCategories = jobCategoryRepository.findAll();
        return buildCategoryTree(allCategories, null);
    }
    
    private List<JobCategory> buildCategoryTree(List<JobCategory> categories, Long parentId) {
        return categories.stream()
                .filter(category -> Objects.equals(category.getParentId(), parentId))
                .peek(category -> {
                    List<JobCategory> children = buildCategoryTree(categories, category.getId());
                    // 这里可以设置子分类，但需要实体类支持
                })
                .sorted(Comparator.comparing(JobCategory::getSortOrder))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<JobCategory> getCategoryPath(Long id) {
        List<JobCategory> path = new ArrayList<>();
        JobCategory current = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        path.add(current);
        
        while (current.getParentId() != null) {
            current = jobCategoryRepository.findById(current.getParentId())
                    .orElseThrow(() -> new RuntimeException("父分类不存在"));
            path.add(0, current);
        }
        
        return path;
    }
    
    @Override
    public Map<Long, Long> countJobsByCategory() {
        List<Object[]> results = jobCategoryRepository.countJobsByCategory();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0],
                        result -> (Long) result[2]
                ));
    }
    
    @Override
    public List<Object[]> getHotCategories() {
        return jobCategoryRepository.findHotCategories();
    }
    
    @Override
    public boolean isNameExists(String name) {
        return jobCategoryRepository.existsByName(name);
    }
    
    @Override
    public boolean isNameExists(String name, Long excludeId) {
        return jobCategoryRepository.existsByNameAndIdNot(name, excludeId);
    }
    
    @Override
    public List<JobCategory> getEnabledCategories() {
        return jobCategoryRepository.findByStatusOrderBySortOrderAsc(1);
    }
    
    @Override
    public List<JobCategory> getJobCategoriesByLevel(Integer level) {
        return jobCategoryRepository.findByLevel(level);
    }
    
    @Override
    @Transactional
    public List<JobCategory> batchUpdateStatus(List<Long> ids, Integer status) {
        List<JobCategory> categories = jobCategoryRepository.findAllById(ids);
        categories.forEach(category -> {
            category.setStatus(status);
        });
        return jobCategoryRepository.saveAll(categories);
    }
    
    @Override
    public Map<String, Object> getCategoryStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalCategories = jobCategoryRepository.count();
        long enabledCategories = jobCategoryRepository.findByStatus(1).size();
        long disabledCategories = jobCategoryRepository.findByStatus(0).size();
        
        stats.put("totalCategories", totalCategories);
        stats.put("enabledCategories", enabledCategories);
        stats.put("disabledCategories", disabledCategories);
        stats.put("topLevelCategories", jobCategoryRepository.findByParentIdIsNull().size());
        
        return stats;
    }
}
