package com.shera.framework.employment.employment.modules.resume.repository;

import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 简历Repository
 */
@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
    
    /**
     * 根据用户ID查找简历列表
     */
    List<Resume> findByUserId(Long userId);
    
    /**
     * 根据用户ID分页查找简历列表
     */
    Page<Resume> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和是否默认查找简历
     */
    Optional<Resume> findByUserIdAndIsDefault(Long userId, Boolean isDefault);
    
    /**
     * 根据隐私级别查找简历列表
     */
    List<Resume> findByPrivacyLevel(Integer privacyLevel);
    
    /**
     * 根据隐私级别分页查找简历列表
     */
    Page<Resume> findByPrivacyLevel(Integer privacyLevel, Pageable pageable);
    
    /**
     * 根据文件类型查找简历列表
     */
    List<Resume> findByFileType(String fileType);
    
    /**
     * 根据标题搜索简历
     */
    @Query("SELECT r FROM Resume r WHERE r.title LIKE %:keyword%")
    Page<Resume> searchByTitle(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 根据用户ID和隐私级别查找简历列表
     */
    List<Resume> findByUserIdAndPrivacyLevel(Long userId, Integer privacyLevel);
    
    /**
     * 统计用户简历数量
     */
    Long countByUserId(Long userId);
    
    /**
     * 根据用户ID和文件类型查找简历列表
     */
    List<Resume> findByUserIdAndFileType(Long userId, String fileType);
    
    /**
     * 查找公开简历（隐私级别为1）
     */
    List<Resume> findByPrivacyLevelOrderByViewCountDesc(Integer privacyLevel);
    
    /**
     * 根据用户ID删除简历
     */
    void deleteByUserId(Long userId);
    
    /**
     * 根据用户ID和简历ID查找简历
     */
    Optional<Resume> findByIdAndUserId(Long id, Long userId);
}
