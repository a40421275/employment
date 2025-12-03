package com.shera.framework.employment.employment.modules.resume.controller;

import com.shera.framework.employment.employment.modules.resume.dto.ResumeCreateDTO;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeDetailDTO;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeListDTO;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeUpdateDTO;
import com.shera.framework.employment.employment.modules.resume.service.ResumeService;
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
 * 统一简历控制器
 * 根据新方案重构，支持附件简历和结构化简历两种类型
 */
@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {
    
    private final ResumeService resumeService;
    
    /**
     * 创建简历
     * 支持附件简历和结构化简历两种类型
     */
    @PostMapping
    public ResponseEntity<?> createResume(@RequestBody ResumeCreateDTO createDTO) {
        try {
            ResumeDetailDTO resume = resumeService.createResume(createDTO);
            return ResponseUtil.success("创建简历成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("创建简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新简历
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateResume(@PathVariable Long id, @RequestBody ResumeUpdateDTO updateDTO) {
        try {
            ResumeDetailDTO resume = resumeService.updateResume(id, updateDTO);
            return ResponseUtil.success("更新简历成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("更新简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除简历
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable Long id) {
        try {
            resumeService.deleteResume(id);
            return ResponseUtil.success("删除简历成功");
        } catch (Exception e) {
            return ResponseUtil.error("删除简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取简历详情
     * 根据简历类型返回不同格式的数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(@PathVariable Long id) {
        try {
            ResumeDetailDTO resume = resumeService.getResumeDetail(id);
            resumeService.increaseViewCount(id); // 增加浏览量
            return ResponseUtil.success("获取简历详情成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("获取简历详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询简历列表
     * 支持按类型、隐私级别筛选
     */
    @GetMapping
    public ResponseEntity<?> getResumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Integer resumeType,
            @RequestParam(required = false) Integer privacyLevel,
            @RequestParam(required = false) Long userId) {
        try {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            Page<ResumeListDTO> resumes = resumeService.listResumes(resumeType, privacyLevel, userId, pageable);
            return ResponseUtil.successPage(resumes, "查询简历列表成功");
        } catch (Exception e) {
            return ResponseUtil.error("查询简历列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户的简历列表
     */
    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUserResumes() {
        try {
            List<ResumeListDTO> resumes = resumeService.getCurrentUserResumes();
            return ResponseUtil.success("获取当前用户简历列表成功", resumes);
        } catch (Exception e) {
            return ResponseUtil.error("获取当前用户简历列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 设置默认简历
     * 每个用户只能有一个默认简历
     */
    @PostMapping("/{id}/set-default")
    public ResponseEntity<?> setDefaultResume(@PathVariable Long id) {
        try {
            ResumeDetailDTO resume = resumeService.setDefaultResume(id);
            return ResponseUtil.success("设置默认简历成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("设置默认简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户的默认简历
     */
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<?> getDefaultResume(@PathVariable Long userId) {
        try {
            ResumeDetailDTO resume = resumeService.getDefaultResumeByUserId(userId);
            return ResponseUtil.success("获取默认简历成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("获取默认简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 同步用户资料数据到简历
     */
    @PostMapping("/{id}/sync-profile")
    public ResponseEntity<?> syncProfileData(@PathVariable Long id) {
        try {
            ResumeDetailDTO resume = resumeService.syncProfileData(id);
            return ResponseUtil.success("同步用户资料成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("同步用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新简历隐私级别
     */
    @PutMapping("/{id}/privacy")
    public ResponseEntity<?> updatePrivacyLevel(
            @PathVariable Long id,
            @RequestParam Integer privacyLevel) {
        try {
            ResumeDetailDTO resume = resumeService.updatePrivacyLevel(id, privacyLevel);
            return ResponseUtil.success("更新隐私级别成功", resume);
        } catch (Exception e) {
            return ResponseUtil.error("更新隐私级别失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索简历
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchResumes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ResumeListDTO> resumes = resumeService.searchResumes(keyword, pageable);
            return ResponseUtil.successPage(resumes, "搜索简历成功", keyword);
        } catch (Exception e) {
            return ResponseUtil.error("搜索简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取热门简历
     */
    @GetMapping("/hot")
    public ResponseEntity<?> getHotResumes(@RequestParam(defaultValue = "10") int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "viewCount"));
            Page<ResumeListDTO> resumes = resumeService.listResumes(null, null, null, pageable);
            return ResponseUtil.success("获取热门简历成功", resumes.getContent());
        } catch (Exception e) {
            return ResponseUtil.error("获取热门简历失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计用户简历数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> countResumesByUser(@PathVariable Long userId) {
        try {
            Long count = resumeService.countResumesByUserId(userId);
            return ResponseUtil.success("统计简历数量成功", Map.of("count", count));
        } catch (Exception e) {
            return ResponseUtil.error("统计简历数量失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证简历是否属于用户
     */
    @GetMapping("/{id}/belongs-to/{userId}")
    public ResponseEntity<?> isResumeBelongsToUser(
            @PathVariable Long id,
            @PathVariable Long userId) {
        try {
            boolean belongs = resumeService.isResumeBelongsToUser(id, userId);
            return ResponseUtil.success("验证简历归属成功", Map.of("belongs", belongs));
        } catch (Exception e) {
            return ResponseUtil.error("验证简历归属失败: " + e.getMessage());
        }
    }
}
