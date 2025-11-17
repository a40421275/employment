package com.shera.framework.employment.employment.modules.resume.controller;

import com.shera.framework.employment.employment.modules.resume.dto.ResumeDTO;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import com.shera.framework.employment.employment.modules.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历控制器
 */
@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {
    
    private final ResumeService resumeService;
    
    /**
     * 创建简历
     */
    @PostMapping
    public ResponseEntity<Resume> createResume(@RequestBody ResumeDTO resumeDTO) {
        Resume resume = resumeService.createResume(resumeDTO);
        return ResponseEntity.ok(resume);
    }
    
    /**
     * 更新简历
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Long id, @RequestBody ResumeDTO resumeDTO) {
        Resume resume = resumeService.updateResume(id, resumeDTO);
        return ResponseEntity.ok(resume);
    }
    
    /**
     * 删除简历
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取简历详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        Resume resume = resumeService.getResumeDetail(id);
        resumeService.increaseViewCount(id); // 增加浏览量
        return ResponseEntity.ok(resume);
    }
    
    /**
     * 分页查询简历列表
     */
    @GetMapping
    public ResponseEntity<?> getResumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Resume> resumes = resumeService.getResumes(pageable);
        
        // 构建标准响应格式
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("content", resumes.getContent());
        data.put("totalElements", resumes.getTotalElements());
        data.put("totalPages", resumes.getTotalPages());
        data.put("size", resumes.getSize());
        data.put("number", resumes.getNumber());
        data.put("sort", resumes.getSort());
        data.put("first", resumes.isFirst());
        data.put("last", resumes.isLast());
        data.put("numberOfElements", resumes.getNumberOfElements());
        data.put("pageable", resumes.getPageable());
        data.put("empty", resumes.isEmpty());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据用户ID查询简历列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resume>> getResumesByUser(@PathVariable Long userId) {
        List<Resume> resumes = resumeService.getResumesByUserId(userId);
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 根据用户ID分页查询简历列表
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<Resume>> getResumesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Resume> resumes = resumeService.getResumesByUserId(userId, pageable);
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 获取用户的默认简历
     */
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<Resume> getDefaultResume(@PathVariable Long userId) {
        Resume resume = resumeService.getDefaultResumeByUserId(userId);
        return ResponseEntity.ok(resume);
    }
    
    /**
     * 设置默认简历
     */
    @PostMapping("/{id}/set-default")
    public ResponseEntity<Resume> setDefaultResume(@PathVariable Long id) {
        Resume resume = resumeService.setDefaultResume(id);
        return ResponseEntity.ok(resume);
    }
    
    /**
     * 根据隐私级别查询简历列表
     */
    @GetMapping("/privacy/{privacyLevel}")
    public ResponseEntity<List<Resume>> getResumesByPrivacy(@PathVariable Integer privacyLevel) {
        List<Resume> resumes = resumeService.getResumesByPrivacyLevel(privacyLevel);
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 根据文件类型查询简历列表
     */
    @GetMapping("/file-type/{fileType}")
    public ResponseEntity<List<Resume>> getResumesByFileType(@PathVariable String fileType) {
        List<Resume> resumes = resumeService.getResumesByFileType(fileType);
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 搜索简历
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Resume>> searchResumes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Resume> resumes = resumeService.searchResumes(keyword, pageable);
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 更新简历隐私级别
     */
    @PutMapping("/{id}/privacy")
    public ResponseEntity<Resume> updatePrivacyLevel(
            @PathVariable Long id,
            @RequestParam Integer privacyLevel) {
        Resume resume = resumeService.updatePrivacyLevel(id, privacyLevel);
        return ResponseEntity.ok(resume);
    }
    
    /**
     * 获取公开简历列表
     */
    @GetMapping("/public")
    public ResponseEntity<List<Resume>> getPublicResumes() {
        List<Resume> resumes = resumeService.getPublicResumes();
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 获取热门简历
     */
    @GetMapping("/hot")
    public ResponseEntity<List<Resume>> getHotResumes(@RequestParam(defaultValue = "10") int limit) {
        List<Resume> resumes = resumeService.getHotResumes(limit);
        return ResponseEntity.ok(resumes);
    }
    
    /**
     * 统计用户简历数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countResumesByUser(@PathVariable Long userId) {
        Long count = resumeService.countResumesByUserId(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * 验证简历是否属于用户
     */
    @GetMapping("/{id}/belongs-to/{userId}")
    public ResponseEntity<Boolean> isResumeBelongsToUser(
            @PathVariable Long id,
            @PathVariable Long userId) {
        boolean belongs = resumeService.isResumeBelongsToUser(id, userId);
        return ResponseEntity.ok(belongs);
    }
}
