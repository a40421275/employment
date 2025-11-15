package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.ResumeDTO;
import com.shera.framework.employment.employment.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 简历服务接口
 */
public interface ResumeService {
    
    /**
     * 创建简历
     */
    Resume createResume(ResumeDTO resumeDTO);
    
    /**
     * 更新简历
     */
    Resume updateResume(Long id, ResumeDTO resumeDTO);
    
    /**
     * 删除简历
     */
    void deleteResume(Long id);
    
    /**
     * 根据ID获取简历
     */
    Resume getResumeById(Long id);
    
    /**
     * 获取简历详情（包含关联信息）
     */
    Resume getResumeDetail(Long id);
    
    /**
     * 分页查询简历列表
     */
    Page<Resume> getResumes(Pageable pageable);
    
    /**
     * 根据用户ID查询简历列表
     */
    List<Resume> getResumesByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询简历列表
     */
    Page<Resume> getResumesByUserId(Long userId, Pageable pageable);
    
    /**
     * 获取用户的默认简历
     */
    Resume getDefaultResumeByUserId(Long userId);
    
    /**
     * 设置默认简历
     */
    Resume setDefaultResume(Long id);
    
    /**
     * 根据隐私级别查询简历列表
     */
    List<Resume> getResumesByPrivacyLevel(Integer privacyLevel);
    
    /**
     * 根据文件类型查询简历列表
     */
    List<Resume> getResumesByFileType(String fileType);
    
    /**
     * 搜索简历（根据标题）
     */
    Page<Resume> searchResumes(String keyword, Pageable pageable);
    
    /**
     * 增加简历浏览量
     */
    void increaseViewCount(Long id);
    
    /**
     * 更新简历隐私级别
     */
    Resume updatePrivacyLevel(Long id, Integer privacyLevel);
    
    /**
     * 获取公开简历列表
     */
    List<Resume> getPublicResumes();
    
    /**
     * 获取热门简历（按浏览量排序）
     */
    List<Resume> getHotResumes(int limit);
    
    /**
     * 统计用户简历数量
     */
    Long countResumesByUserId(Long userId);
    
    /**
     * 验证简历是否属于用户
     */
    boolean isResumeBelongsToUser(Long resumeId, Long userId);
}
