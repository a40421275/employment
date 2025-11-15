package com.shera.framework.employment.employment.service.impl;

import com.shera.framework.employment.employment.dto.ResumeDTO;
import com.shera.framework.employment.employment.entity.Resume;
import com.shera.framework.employment.employment.repository.ResumeRepository;
import com.shera.framework.employment.employment.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 简历服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    
    private final ResumeRepository resumeRepository;
    
    @Override
    @Transactional
    public Resume createResume(ResumeDTO resumeDTO) {
        Resume resume = new Resume();
        copyResumeProperties(resume, resumeDTO);
        
        // 如果是默认简历，取消其他简历的默认状态
        if (Boolean.TRUE.equals(resumeDTO.getIsDefault())) {
            clearDefaultResume(resumeDTO.getUserId());
        }
        
        return resumeRepository.save(resume);
    }
    
    @Override
    @Transactional
    public Resume updateResume(Long id, ResumeDTO resumeDTO) {
        Resume resume = getResumeById(id);
        copyResumeProperties(resume, resumeDTO);
        
        // 如果是默认简历，取消其他简历的默认状态
        if (Boolean.TRUE.equals(resumeDTO.getIsDefault())) {
            clearDefaultResume(resume.getUserId());
        }
        
        return resumeRepository.save(resume);
    }
    
    @Override
    @Transactional
    public void deleteResume(Long id) {
        Resume resume = getResumeById(id);
        resumeRepository.delete(resume);
    }
    
    @Override
    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("简历不存在: " + id));
    }
    
    @Override
    public Resume getResumeDetail(Long id) {
        Resume resume = getResumeById(id);
        // 这里可以加载关联的实体信息，如用户信息等
        return resume;
    }
    
    @Override
    public Page<Resume> getResumes(Pageable pageable) {
        return resumeRepository.findAll(pageable);
    }
    
    @Override
    public List<Resume> getResumesByUserId(Long userId) {
        return resumeRepository.findByUserId(userId);
    }
    
    @Override
    public Page<Resume> getResumesByUserId(Long userId, Pageable pageable) {
        return resumeRepository.findByUserId(userId, pageable);
    }
    
    @Override
    public Resume getDefaultResumeByUserId(Long userId) {
        return resumeRepository.findByUserIdAndIsDefault(userId, true)
                .orElseThrow(() -> new RuntimeException("用户没有默认简历: " + userId));
    }
    
    @Override
    @Transactional
    public Resume setDefaultResume(Long id) {
        Resume resume = getResumeById(id);
        
        // 取消其他简历的默认状态
        clearDefaultResume(resume.getUserId());
        
        // 设置当前简历为默认
        resume.setIsDefault(true);
        return resumeRepository.save(resume);
    }
    
    @Override
    public List<Resume> getResumesByPrivacyLevel(Integer privacyLevel) {
        return resumeRepository.findByPrivacyLevel(privacyLevel);
    }
    
    @Override
    public List<Resume> getResumesByFileType(String fileType) {
        return resumeRepository.findByFileType(fileType);
    }
    
    @Override
    public Page<Resume> searchResumes(String keyword, Pageable pageable) {
        return resumeRepository.searchByTitle(keyword, pageable);
    }
    
    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        Resume resume = getResumeById(id);
        resume.setViewCount(resume.getViewCount() + 1);
        resumeRepository.save(resume);
    }
    
    @Override
    @Transactional
    public Resume updatePrivacyLevel(Long id, Integer privacyLevel) {
        Resume resume = getResumeById(id);
        resume.setPrivacyLevel(privacyLevel);
        return resumeRepository.save(resume);
    }
    
    @Override
    public List<Resume> getPublicResumes() {
        return resumeRepository.findByPrivacyLevel(1); // 隐私级别为1表示公开
    }
    
    @Override
    public List<Resume> getHotResumes(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "viewCount"));
        return resumeRepository.findByPrivacyLevel(1, pageable).getContent(); // 只获取公开简历
    }
    
    @Override
    public Long countResumesByUserId(Long userId) {
        return resumeRepository.countByUserId(userId);
    }
    
    @Override
    public boolean isResumeBelongsToUser(Long resumeId, Long userId) {
        return resumeRepository.findByIdAndUserId(resumeId, userId).isPresent();
    }
    
    /**
     * 取消用户所有简历的默认状态
     */
    private void clearDefaultResume(Long userId) {
        List<Resume> resumes = resumeRepository.findByUserId(userId);
        for (Resume resume : resumes) {
            if (Boolean.TRUE.equals(resume.getIsDefault())) {
                resume.setIsDefault(false);
                resumeRepository.save(resume);
            }
        }
    }
    
    /**
     * 复制ResumeDTO属性到Resume实体
     */
    private void copyResumeProperties(Resume resume, ResumeDTO resumeDTO) {
        resume.setUserId(resumeDTO.getUserId());
        resume.setTitle(resumeDTO.getTitle());
        resume.setContent(resumeDTO.getContent());
        resume.setFileUrl(resumeDTO.getFileUrl());
        resume.setFileType(resumeDTO.getFileType());
        resume.setIsDefault(resumeDTO.getIsDefault());
        resume.setPrivacyLevel(resumeDTO.getPrivacyLevel());
    }
}
