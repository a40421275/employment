package com.shera.framework.employment.employment.modules.resume.service;

import com.shera.framework.employment.employment.modules.resume.dto.ResumeCreateDTO;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeDetailDTO;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeListDTO;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 统一简历服务接口
 * 根据新方案重构，支持附件简历和结构化简历两种类型
 */
public interface ResumeService {
    
    /**
     * 创建简历
     * 支持附件简历和结构化简历两种类型
     */
    ResumeDetailDTO createResume(ResumeCreateDTO createDTO);
    
    /**
     * 更新简历
     */
    ResumeDetailDTO updateResume(Long id, ResumeUpdateDTO updateDTO);
    
    /**
     * 删除简历
     */
    void deleteResume(Long id);
    
    /**
     * 获取简历详情
     * 根据简历类型返回不同格式的数据
     */
    ResumeDetailDTO getResumeDetail(Long id);
    
    /**
     * 分页查询简历列表
     * 支持按类型、隐私级别、用户ID筛选
     */
    Page<ResumeListDTO> listResumes(Integer resumeType, Integer privacyLevel, Long userId, Pageable pageable);
    
    /**
     * 获取当前用户的简历列表
     */
    List<ResumeListDTO> getCurrentUserResumes();
    
    /**
     * 获取用户的默认简历
     */
    ResumeDetailDTO getDefaultResumeByUserId(Long userId);
    
    /**
     * 设置默认简历
     */
    ResumeDetailDTO setDefaultResume(Long id);
    
    /**
     * 同步用户资料数据到简历
     */
    ResumeDetailDTO syncProfileData(Long id);
    
    /**
     * 更新简历隐私级别
     */
    ResumeDetailDTO updatePrivacyLevel(Long id, Integer privacyLevel);
    
    /**
     * 搜索简历
     */
    Page<ResumeListDTO> searchResumes(String keyword, Pageable pageable);
    
    /**
     * 增加简历浏览量
     */
    void increaseViewCount(Long id);
    
    /**
     * 统计用户简历数量
     */
    Long countResumesByUserId(Long userId);
    
    /**
     * 验证简历是否属于用户
     */
    boolean isResumeBelongsToUser(Long resumeId, Long userId);
}
