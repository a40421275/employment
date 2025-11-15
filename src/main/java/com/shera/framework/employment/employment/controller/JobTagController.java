package com.shera.framework.employment.employment.controller;

import com.shera.framework.employment.employment.dto.JobTagDTO;
import com.shera.framework.employment.employment.dto.JobTagDetailDTO;
import com.shera.framework.employment.employment.dto.JobTagQueryDTO;
import com.shera.framework.employment.employment.entity.JobTag;
import com.shera.framework.employment.employment.service.JobTagService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 岗位标签控制器
 */
@RestController
@RequestMapping("/api/job-tags")
@RequiredArgsConstructor
public class JobTagController {
    
    private final JobTagService jobTagService;
    
    /**
     * 新建标签（岗位ID可选）
     */
    @PostMapping("/")
    public ResponseEntity<?> createTag(@RequestParam String tagName, @RequestParam(required = false) Long jobId) {
        JobTag jobTag = jobTagService.createTag(tagName, jobId);
        return ResponseUtil.success("新建成功", jobTag);
    }

    /**
     * 创建完整标签信息
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTagWithDetails(@RequestBody JobTagDTO jobTagDTO) {
        JobTag jobTag = jobTagService.createTagWithDetails(jobTagDTO);
        return ResponseUtil.success("创建成功", jobTag);
    }
    
    /**
     * 为岗位添加标签
     */
    @PostMapping("/job/{jobId}")
    public ResponseEntity<?> addTagToJob(@PathVariable Long jobId, @RequestParam String tagName) {
        JobTag jobTag = jobTagService.addTagToJob(jobId, tagName);
        return ResponseUtil.success("添加成功", jobTag);
    }
    
    /**
     * 为岗位批量添加标签
     */
    @PostMapping("/job/{jobId}/batch")
    public ResponseEntity<?> addTagsToJob(@PathVariable Long jobId, @RequestBody List<String> tagNames) {
        List<JobTag> jobTags = jobTagService.addTagsToJob(jobId, tagNames);
        return ResponseUtil.success("批量添加成功", jobTags);
    }
    
    /**
     * 从岗位移除标签
     */
    @DeleteMapping("/job/{jobId}")
    public ResponseEntity<?> removeTagFromJob(@PathVariable Long jobId, @RequestParam String tagName) {
        jobTagService.removeTagFromJob(jobId, tagName);
        return ResponseUtil.success("移除成功");
    }
    
    /**
     * 从岗位批量移除标签
     */
    @DeleteMapping("/job/{jobId}/batch")
    public ResponseEntity<?> removeTagsFromJob(@PathVariable Long jobId, @RequestBody List<String> tagNames) {
        jobTagService.removeTagsFromJob(jobId, tagNames);
        return ResponseUtil.success("批量移除成功");
    }
    
    /**
     * 获取岗位的所有标签
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getTagsByJobId(@PathVariable Long jobId) {
        List<JobTag> jobTags = jobTagService.getTagsByJobId(jobId);
        return ResponseUtil.success("获取成功", jobTags);
    }
    
    /**
     * 获取岗位的标签名称列表
     */
    @GetMapping("/job/{jobId}/names")
    public ResponseEntity<?> getTagNamesByJobId(@PathVariable Long jobId) {
        List<String> tagNames = jobTagService.getTagNamesByJobId(jobId);
        return ResponseUtil.success("获取成功", tagNames);
    }
    
    /**
     * 根据标签名称获取岗位列表
     */
    @GetMapping("/tag/{tagName}/jobs")
    public ResponseEntity<?> getJobIdsByTagName(@PathVariable String tagName) {
        List<Long> jobIds = jobTagService.getJobIdsByTagName(tagName);
        return ResponseUtil.success("获取成功", jobIds);
    }
    
    /**
     * 根据多个标签名称获取岗位列表
     */
    @GetMapping("/tags/jobs")
    public ResponseEntity<?> getJobIdsByTagNames(@RequestParam List<String> tagNames) {
        List<Long> jobIds = jobTagService.getJobIdsByTagNames(tagNames);
        return ResponseUtil.success("获取成功", jobIds);
    }
    
    /**
     * 根据标签名称搜索岗位
     */
    @GetMapping("/search/jobs")
    public ResponseEntity<?> searchJobIdsByTagName(@RequestParam String tagName) {
        List<Long> jobIds = jobTagService.searchJobIdsByTagName(tagName);
        return ResponseUtil.success("搜索成功", jobIds);
    }
    
    /**
     * 获取热门标签
     */
    @GetMapping("/hot")
    public ResponseEntity<?> getHotTags(@RequestParam(defaultValue = "10") int limit) {
        List<Object[]> hotTags = jobTagService.getHotTags(limit);
        return ResponseUtil.success("获取成功", hotTags);
    }
    
    /**
     * 统计标签使用次数
     */
    @GetMapping("/stats/usage")
    public ResponseEntity<?> countTagsUsage() {
        Map<String, Long> tagUsage = jobTagService.countTagsUsage();
        return ResponseUtil.success("统计成功", tagUsage);
    }
    
    /**
     * 获取所有标签
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllTags() {
        List<String> allTags = jobTagService.getAllTags();
        return ResponseUtil.success("获取成功", allTags);
    }
    
    /**
     * 获取系统常用标签
     */
    @GetMapping("/system")
    public ResponseEntity<?> getSystemTags() {
        List<String> systemTags = jobTagService.getSystemTags();
        return ResponseUtil.success("获取成功", systemTags);
    }
    
    /**
     * 检查标签是否已存在
     */
    @GetMapping("/check")
    public ResponseEntity<?> isTagExists(@RequestParam Long jobId, @RequestParam String tagName) {
        boolean exists = jobTagService.isTagExists(jobId, tagName);
        return ResponseUtil.success("检查成功", exists);
    }
    
    /**
     * 更新标签
     */
    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable Long tagId, @RequestBody JobTagDTO jobTagDTO) {
        JobTag jobTag = jobTagService.updateTag(tagId, jobTagDTO);
        return ResponseUtil.success("更新成功", jobTag);
    }
    
    /**
     * 删除标签
     */
    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId) {
        jobTagService.deleteTag(tagId);
        return ResponseUtil.success("删除成功");
    }
    
    /**
     * 批量删除标签
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDeleteTags(@RequestBody List<Long> tagIds) {
        jobTagService.batchDeleteTags(tagIds);
        return ResponseUtil.success("批量删除成功");
    }
    
    /**
     * 获取标签统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getTagStats() {
        Map<String, Object> stats = jobTagService.getTagStats();
        return ResponseUtil.success("统计成功", stats);
    }
    
    /**
     * 根据标签类型获取标签
     */
    @GetMapping("/type/{tagType}")
    public ResponseEntity<?> getTagsByType(@PathVariable Integer tagType) {
        List<JobTag> tags = jobTagService.getTagsByType(tagType);
        return ResponseUtil.success("查询成功", tags);
    }
    
    /**
     * 获取所有标签（支持分页和筛选）
     */
    @GetMapping("/all/paged")
    public ResponseEntity<?> getAllTagsWithPagination(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) Integer tagType,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Boolean independentOnly,
            @RequestParam(required = false) Boolean associatedOnly,
            @RequestParam(defaultValue = "tagName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // 构建查询DTO
        JobTagQueryDTO queryDTO = new JobTagQueryDTO();
        queryDTO.setTagName(tagName);
        queryDTO.setTagType(tagType);
        queryDTO.setJobId(jobId);
        queryDTO.setIndependentOnly(independentOnly);
        queryDTO.setAssociatedOnly(associatedOnly);
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortDirection(sortDirection);
        
        // 构建分页信息
        Sort sort = buildSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        queryDTO.setPageable(pageable);
        
        // 执行查询
        Page<JobTagDetailDTO> tags = jobTagService.getAllTagsWithPagination(queryDTO);
        
        // 构建响应
        Map<String, Object> response = Map.of(
                "content", tags.getContent(),
                "totalElements", tags.getTotalElements(),
                "totalPages", tags.getTotalPages(),
                "currentPage", tags.getNumber(),
                "pageSize", tags.getSize()
        );
        
        return ResponseUtil.success("获取成功", response);
    }
    
    /**
     * 获取系统常用标签（支持分页）
     */
    @GetMapping("/system/paged")
    public ResponseEntity<?> getSystemTagsWithPagination(
            @RequestParam(required = false) String tagName,
            @RequestParam(defaultValue = "tagName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // 构建查询DTO
        JobTagQueryDTO queryDTO = new JobTagQueryDTO();
        queryDTO.setTagName(tagName);
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortDirection(sortDirection);
        
        // 构建分页信息
        Sort sort = buildSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        queryDTO.setPageable(pageable);
        
        // 执行查询
        Page<JobTagDetailDTO> tags = jobTagService.getSystemTagsWithPagination(queryDTO);
        
        // 构建响应
        Map<String, Object> response = Map.of(
                "content", tags.getContent(),
                "totalElements", tags.getTotalElements(),
                "totalPages", tags.getTotalPages(),
                "currentPage", tags.getNumber(),
                "pageSize", tags.getSize()
        );
        
        return ResponseUtil.success("获取成功", response);
    }
    
    /**
     * 统计符合条件的标签数量
     */
    @GetMapping("/count")
    public ResponseEntity<?> countTagsByConditions(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) Integer tagType,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Boolean independentOnly,
            @RequestParam(required = false) Boolean associatedOnly) {
        
        // 构建查询DTO
        JobTagQueryDTO queryDTO = new JobTagQueryDTO();
        queryDTO.setTagName(tagName);
        queryDTO.setTagType(tagType);
        queryDTO.setJobId(jobId);
        queryDTO.setIndependentOnly(independentOnly);
        queryDTO.setAssociatedOnly(associatedOnly);
        
        // 执行统计
        Long count = jobTagService.countTagsByConditions(queryDTO);
        
        return ResponseUtil.success("统计成功", count);
    }
    
    /**
     * 构建排序信息
     */
    private Sort buildSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        switch (sortBy) {
            case "createTime":
                return Sort.by(direction, "createTime");
            case "usageCount":
                return Sort.by(direction, "tagName"); // 使用次数排序需要特殊处理
            case "tagName":
            default:
                return Sort.by(direction, "tagName");
        }
    }
}
