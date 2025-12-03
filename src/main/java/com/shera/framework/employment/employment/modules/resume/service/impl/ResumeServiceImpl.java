package com.shera.framework.employment.employment.modules.resume.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.resume.dto.*;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import com.shera.framework.employment.employment.modules.resume.repository.ResumeRepository;
import com.shera.framework.employment.employment.modules.resume.service.ResumeService;
import com.shera.framework.employment.employment.modules.user.dto.UserDTO;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import com.shera.framework.employment.employment.modules.user.repository.UserProfileRepository;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import com.shera.framework.employment.employment.modules.file.service.FileService;
import com.shera.framework.employment.employment.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一简历服务实现类
 * 根据新方案重构，支持附件简历和结构化简历两种类型
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FileService fileService;
    private final ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public ResumeDetailDTO createResume(ResumeCreateDTO createDTO) {
        Resume resume = new Resume();
        copyResumeProperties(resume, createDTO);
        
        // 如果是默认简历，取消其他简历的默认状态
        if (createDTO.getSettings() != null && Boolean.TRUE.equals(createDTO.getSettings().getIsDefault())) {
            clearDefaultResume(createDTO.getUserId());
        }
        
        Resume savedResume = resumeRepository.save(resume);
        return ResumeDetailDTO.fromEntity(savedResume);
    }
    
    @Override
    @Transactional
    public ResumeDetailDTO updateResume(Long id, ResumeUpdateDTO updateDTO) {
        Resume resume = getResumeById(id);
        copyResumeProperties(resume, updateDTO);
        
        // 如果是默认简历，取消其他简历的默认状态
        if (updateDTO.getSettings() != null && Boolean.TRUE.equals(updateDTO.getSettings().getIsDefault())) {
            clearDefaultResume(resume.getUserId());
        }
        
        Resume updatedResume = resumeRepository.save(resume);
        return ResumeDetailDTO.fromEntity(updatedResume);
    }
    
    @Override
    @Transactional
    public void deleteResume(Long id) {
        Resume resume = getResumeById(id);
        
        // 清理简历关联的附件信息
        cleanupResumeAttachments(id);
        
        resumeRepository.delete(resume);
    }
    
    @Override
    public ResumeDetailDTO getResumeDetail(Long id) {
        Resume resume = getResumeById(id);
        ResumeDetailDTO detailDTO = ResumeDetailDTO.fromEntity(resume);
        
        // 设置基础信息
        setBasicInfo(detailDTO, resume);
        
        // 设置结构化数据
        setStructuredData(detailDTO, resume);
        
        // 设置统计信息
        setStats(detailDTO, resume);
        
        return detailDTO;
    }
    
    @Override
    public Page<ResumeListDTO> listResumes(Integer resumeType, Integer privacyLevel, Long userId, Pageable pageable) {
        Specification<Resume> spec = buildResumeSpecification(resumeType, privacyLevel, userId);
        Page<Resume> resumePage = resumeRepository.findAll(spec, pageable);
        
        List<ResumeListDTO> dtoList = resumePage.getContent().stream()
                .map(ResumeListDTO::fromEntity)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, resumePage.getTotalElements());
    }
    
    @Override
    public List<ResumeListDTO> getCurrentUserResumes() {
        // 从安全上下文中获取当前用户ID
        Long currentUserId = SecurityContextUtil.getCurrentUserIdRequired();
        List<Resume> resumes = resumeRepository.findByUserId(currentUserId);
        
        return resumes.stream()
                .map(ResumeListDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public ResumeDetailDTO getDefaultResumeByUserId(Long userId) {
        Resume resume = resumeRepository.findByUserIdAndIsDefault(userId, true)
                .orElseThrow(() -> new RuntimeException("用户没有默认简历: " + userId));
        return ResumeDetailDTO.fromEntity(resume);
    }
    
    @Override
    @Transactional
    public ResumeDetailDTO setDefaultResume(Long id) {
        Resume resume = getResumeById(id);
        
        // 取消其他简历的默认状态
        clearDefaultResume(resume.getUserId());
        
        // 设置当前简历为默认
        resume.setIsDefault(true);
        Resume updatedResume = resumeRepository.save(resume);
        return ResumeDetailDTO.fromEntity(updatedResume);
    }
    
    @Override
    @Transactional
    public ResumeDetailDTO syncProfileData(Long id) {
        Resume resume = getResumeById(id);
        
        // 这里实现从用户资料同步数据的逻辑
        // 暂时先更新同步时间
        resume.setLastSyncTime(LocalDateTime.now());
        Resume updatedResume = resumeRepository.save(resume);
        
        return ResumeDetailDTO.fromEntity(updatedResume);
    }
    
    @Override
    @Transactional
    public ResumeDetailDTO updatePrivacyLevel(Long id, Integer privacyLevel) {
        Resume resume = getResumeById(id);
        resume.setPrivacyLevel(privacyLevel);
        Resume updatedResume = resumeRepository.save(resume);
        return ResumeDetailDTO.fromEntity(updatedResume);
    }
    
    @Override
    public Page<ResumeListDTO> searchResumes(String keyword, Pageable pageable) {
        Page<Resume> resumePage = resumeRepository.searchByTitle(keyword, pageable);
        
        List<ResumeListDTO> dtoList = resumePage.getContent().stream()
                .map(ResumeListDTO::fromEntity)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, resumePage.getTotalElements());
    }
    
    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        Resume resume = getResumeById(id);
        resume.setViewCount(resume.getViewCount() + 1);
        resume.setLastViewTime(LocalDateTime.now());
        resumeRepository.save(resume);
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
     * 根据ID获取简历实体
     */
    private Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("简历不存在: " + id));
    }
    
    /**
     * 构建简历查询条件
     */
    private Specification<Resume> buildResumeSpecification(Integer resumeType, Integer privacyLevel, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (resumeType != null) {
                predicates.add(criteriaBuilder.equal(root.get("resumeType"), resumeType));
            }
            
            if (privacyLevel != null) {
                predicates.add(criteriaBuilder.equal(root.get("privacyLevel"), privacyLevel));
            }
            
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
     * 复制ResumeCreateDTO属性到Resume实体
     */
    private void copyResumeProperties(Resume resume, ResumeCreateDTO createDTO) {
        resume.setUserId(createDTO.getUserId());
        resume.setTitle(createDTO.getTitle());
        resume.setResumeType(createDTO.getResumeType());
        resume.setFileUrl(createDTO.getFileUrl());
        resume.setFileType(createDTO.getFileType());
        
        // 设置显示设置
        if (createDTO.getSettings() != null) {
            resume.setIsDefault(createDTO.getSettings().getIsDefault());
            resume.setPrivacyLevel(createDTO.getSettings().getPrivacyLevel());
            resume.setTemplateStyle(createDTO.getSettings().getTemplateStyle());
            resume.setShowPhoto(createDTO.getSettings().getShowPhoto());
            resume.setShowSalary(createDTO.getSettings().getShowSalary());
        }
        
        // 设置结构化数据
        if (createDTO.getStructuredData() != null) {
            try {
                String structuredDataJson = objectMapper.writeValueAsString(createDTO.getStructuredData());
                resume.setStructuredData(structuredDataJson);
            } catch (JsonProcessingException e) {
                log.warn("序列化结构化数据失败: {}", e.getMessage());
            }
        }
        
        // 设置基础信息
        if (createDTO.getBasicInfo() != null) {
            try {
                String basicInfoJson = objectMapper.writeValueAsString(createDTO.getBasicInfo());
                resume.setBasicInfo(basicInfoJson);
            } catch (JsonProcessingException e) {
                log.warn("序列化基础信息失败: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 复制ResumeUpdateDTO属性到Resume实体
     */
    private void copyResumeProperties(Resume resume, ResumeUpdateDTO updateDTO) {
        if (updateDTO.getTitle() != null) {
            resume.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getFileUrl() != null) {
            resume.setFileUrl(updateDTO.getFileUrl());
        }
        if (updateDTO.getFileType() != null) {
            resume.setFileType(updateDTO.getFileType());
        }
        
        // 设置显示设置
        if (updateDTO.getSettings() != null) {
            if (updateDTO.getSettings().getIsDefault() != null) {
                resume.setIsDefault(updateDTO.getSettings().getIsDefault());
            }
            if (updateDTO.getSettings().getPrivacyLevel() != null) {
                resume.setPrivacyLevel(updateDTO.getSettings().getPrivacyLevel());
            }
            if (updateDTO.getSettings().getTemplateStyle() != null) {
                resume.setTemplateStyle(updateDTO.getSettings().getTemplateStyle());
            }
            if (updateDTO.getSettings().getShowPhoto() != null) {
                resume.setShowPhoto(updateDTO.getSettings().getShowPhoto());
            }
            if (updateDTO.getSettings().getShowSalary() != null) {
                resume.setShowSalary(updateDTO.getSettings().getShowSalary());
            }
        }
        
        // 设置结构化数据
        if (updateDTO.getStructuredData() != null) {
            try {
                String structuredDataJson = objectMapper.writeValueAsString(updateDTO.getStructuredData());
                resume.setStructuredData(structuredDataJson);
            } catch (JsonProcessingException e) {
                log.warn("序列化结构化数据失败: {}", e.getMessage());
            }
        }
        
        // 设置基础信息
        if (updateDTO.getBasicInfo() != null) {
            try {
                String basicInfoJson = objectMapper.writeValueAsString(updateDTO.getBasicInfo());
                resume.setBasicInfo(basicInfoJson);
            } catch (JsonProcessingException e) {
                log.warn("序列化基础信息失败: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 设置基础信息
     */
    private void setBasicInfo(ResumeDetailDTO detailDTO, Resume resume) {
        try {
            // 优先从简历存储的BasicInfo中获取
            if (resume.getBasicInfo() != null && !resume.getBasicInfo().isEmpty()) {
                BasicInfo basicInfo = objectMapper.readValue(resume.getBasicInfo(), BasicInfo.class);
                detailDTO.setBasicInfo(basicInfo);
            } else if (Boolean.TRUE.equals(resume.getSyncProfileData())) {
                // 如果简历设置了同步用户资料，则从用户资料获取基础信息
                User user = userRepository.findById(resume.getUserId())
                        .orElseThrow(() -> new RuntimeException("用户不存在: " + resume.getUserId()));
                UserProfile userProfile = userProfileRepository.findByUserId(resume.getUserId())
                        .orElseThrow(() -> new RuntimeException("用户资料不存在: " + resume.getUserId()));
                
                BasicInfo basicInfo = new BasicInfo();
                basicInfo.setRealName(userProfile.getRealName());
                basicInfo.setGender(userProfile.getGender());
                basicInfo.setBirthday(userProfile.getBirthday());
                basicInfo.setAvatar(userProfile.getAvatar());
                basicInfo.setPhone(user.getPhone()); // 从User实体获取
                basicInfo.setEmail(user.getEmail()); // 从User实体获取
                basicInfo.setCity(userProfile.getCity());
                basicInfo.setEducation(userProfile.getEducation());
                basicInfo.setWorkYears(userProfile.getWorkYears());
                basicInfo.setSelfIntro(userProfile.getSelfIntro());
                
                // 设置求职偏好信息
                try {
                    if (userProfile.getPreferredCities() != null && !userProfile.getPreferredCities().isEmpty()) {
                        // 检查是否是JSON格式，如果不是则直接创建列表
                        String preferredCities = userProfile.getPreferredCities();
                        if (preferredCities.startsWith("[") && preferredCities.endsWith("]")) {
                            basicInfo.setPreferredCities(objectMapper.readValue(preferredCities, List.class));
                        } else {
                            // 如果是普通字符串，直接创建单元素列表
                            basicInfo.setPreferredCities(List.of(preferredCities));
                        }
                    }
                    if (userProfile.getJobTypes() != null && !userProfile.getJobTypes().isEmpty()) {
                        // 检查是否是JSON格式，如果不是则直接创建列表
                        String jobTypes = userProfile.getJobTypes();
                        if (jobTypes.startsWith("[") && jobTypes.endsWith("]")) {
                            basicInfo.setJobTypes(objectMapper.readValue(jobTypes, List.class));
                        } else {
                            // 如果是普通字符串，直接创建单元素列表
                            basicInfo.setJobTypes(List.of(jobTypes));
                        }
                    }
                    if (userProfile.getIndustries() != null && !userProfile.getIndustries().isEmpty()) {
                        // 检查是否是JSON格式，如果不是则直接创建列表
                        String industries = userProfile.getIndustries();
                        if (industries.startsWith("[") && industries.endsWith("]")) {
                            basicInfo.setIndustries(objectMapper.readValue(industries, List.class));
                        } else {
                            // 如果是普通字符串，直接创建单元素列表
                            basicInfo.setIndustries(List.of(industries));
                        }
                    }
                    basicInfo.setWorkMode(userProfile.getWorkMode());
                    basicInfo.setJobStatus(userProfile.getJobStatus());
                } catch (JsonProcessingException e) {
                    log.warn("解析求职偏好信息失败: {}", e.getMessage());
                }
                
                detailDTO.setBasicInfo(basicInfo);
            }
        } catch (JsonProcessingException e) {
            log.warn("解析基础信息失败: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("设置基础信息失败: {}", e.getMessage());
        }
    }
    
    /**
     * 设置结构化数据
     */
    private void setStructuredData(ResumeDetailDTO detailDTO, Resume resume) {
        try {
            // 如果是结构化简历类型且有结构化数据
            if (resume.getResumeType() == 2 && resume.getStructuredData() != null) {
                StructuredResumeData structuredData = objectMapper.readValue(
                        resume.getStructuredData(), StructuredResumeData.class);
                detailDTO.setStructuredData(structuredData);
            }
        } catch (JsonProcessingException e) {
            log.warn("解析结构化数据失败: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("设置结构化数据失败: {}", e.getMessage());
        }
    }
    
    /**
     * 设置统计信息
     */
    private void setStats(ResumeDetailDTO detailDTO, Resume resume) {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("viewCount", resume.getViewCount());
            stats.put("downloadCount", resume.getDownloadCount());
            stats.put("shareCount", resume.getShareCount());
            stats.put("isDefault", resume.getIsDefault());
            stats.put("privacyLevel", resume.getPrivacyLevel());
            
            // 计算简历完整度
            int completeness = calculateResumeCompleteness(detailDTO);
            stats.put("completeness", completeness);
            
            detailDTO.setStats(stats);
        } catch (Exception e) {
            log.warn("设置统计信息失败: {}", e.getMessage());
        }
    }
    
    /**
     * 计算简历完整度
     */
    private int calculateResumeCompleteness(ResumeDetailDTO detailDTO) {
        int score = 0;
        int total = 10; // 总分
        
        // 基础信息完整度 (5分)
        if (detailDTO.getBasicInfo() != null) {
            BasicInfo basicInfo = detailDTO.getBasicInfo();
            if (basicInfo.getRealName() != null && !basicInfo.getRealName().isEmpty()) score++;
            if (basicInfo.getPhone() != null && !basicInfo.getPhone().isEmpty()) score++;
            if (basicInfo.getEmail() != null && !basicInfo.getEmail().isEmpty()) score++;
            if (basicInfo.getSelfIntro() != null && !basicInfo.getSelfIntro().isEmpty()) score++;
            if (basicInfo.getExpectedSalary() != null) score++;
        }
        
        // 结构化数据完整度 (5分)
        if (detailDTO.getStructuredData() != null) {
            StructuredResumeData structuredData = detailDTO.getStructuredData();
            if (structuredData.getEducations() != null && !structuredData.getEducations().isEmpty()) score++;
            if (structuredData.getWorkExperiences() != null && !structuredData.getWorkExperiences().isEmpty()) score++;
            if (structuredData.getSkills() != null && !structuredData.getSkills().isEmpty()) score++;
            if (structuredData.getProjects() != null && !structuredData.getProjects().isEmpty()) score++;
            if (structuredData.getCertificates() != null && !structuredData.getCertificates().isEmpty()) score++;
        }
        
        return (score * 100) / total;
    }
    
    /**
     * 清理简历关联的附件信息
     */
    private void cleanupResumeAttachments(Long resumeId) {
        try {
            // 简历相关的业务类型前缀
            String businessTypePrefix = "resume_";
            
            // 获取简历关联的所有附件（使用前缀搜索，支持resume_avatar、resume_attachment等）
            List<Map<String, Object>> attachments = fileService.getAttachmentsByBusinessTypePrefixAndBusinessId(businessTypePrefix, resumeId);
            
            if (attachments != null && !attachments.isEmpty()) {
                log.info("开始清理简历关联的附件 - 简历ID: {}, 业务类型前缀: {}, 附件数量: {}", resumeId, businessTypePrefix, attachments.size());
                
                for (Map<String, Object> attachmentInfo : attachments) {
                    Long attachmentId = (Long) attachmentInfo.get("attachmentId");
                    String businessType = (String) attachmentInfo.get("businessType");
                    if (attachmentId != null) {
                        try {
                            // 删除附件（包括物理文件）
                            boolean deleted = fileService.deleteFile(attachmentId);
                            if (deleted) {
                                log.info("成功删除简历附件 - 简历ID: {}, 业务类型: {}, 附件ID: {}", resumeId, businessType, attachmentId);
                            } else {
                                log.warn("删除简历附件失败 - 简历ID: {}, 业务类型: {}, 附件ID: {}", resumeId, businessType, attachmentId);
                            }
                        } catch (Exception e) {
                            log.error("删除简历附件时发生异常 - 简历ID: {}, 业务类型: {}, 附件ID: {}, 错误: {}", 
                                    resumeId, businessType, attachmentId, e.getMessage());
                        }
                    }
                }
                
                log.info("简历附件清理完成 - 简历ID: {}, 业务类型前缀: {}, 已处理附件数量: {}", resumeId, businessTypePrefix, attachments.size());
            } else {
                log.debug("简历没有关联的附件 - 简历ID: {}, 业务类型前缀: {}", resumeId, businessTypePrefix);
            }
        } catch (Exception e) {
            log.error("清理简历附件时发生异常 - 简历ID: {}, 错误: {}", resumeId, e.getMessage());
            // 这里不抛出异常，避免影响简历删除主流程
        }
    }
}
