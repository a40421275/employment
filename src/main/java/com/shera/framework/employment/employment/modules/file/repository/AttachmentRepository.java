package com.shera.framework.employment.employment.modules.file.repository;

import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 附件数据访问接口
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, AttachmentRepositoryCustom {
    
    /**
     * 根据文件ID查找附件
     */
    List<Attachment> findByFileId(Long fileId);
    
    /**
     * 根据用户ID查找附件
     */
    List<Attachment> findByUserId(Long userId);
    
    /**
     * 根据用户ID和业务类型查找附件
     */
    List<Attachment> findByUserIdAndBusinessType(Long userId, String businessType);
    
    /**
     * 根据业务类型和业务ID查找附件
     */
    List<Attachment> findByBusinessTypeAndBusinessId(String businessType, Long businessId);
    
    /**
     * 根据用户ID和业务类型统计附件数量
     */
    long countByUserIdAndBusinessType(Long userId, String businessType);
    
    /**
     * 根据用户ID统计附件数量
     */
    long countByUserId(Long userId);
    
    /**
     * 检查附件是否存在
     */
    boolean existsById(Long id);
    
    /**
     * 更新附件状态
     */
    @Modifying
    @Query("UPDATE Attachment a SET a.status = :status WHERE a.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 增加下载次数
     */
    @Modifying
    @Query("UPDATE Attachment a SET a.downloadCount = a.downloadCount + 1 WHERE a.id = :id")
    int incrementDownloadCount(@Param("id") Long id);
    
    /**
     * 查找过期的附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.expireTime < :cutoffTime AND a.isTemporary = true")
    List<Attachment> findByExpireTimeBefore(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * 根据业务类型、业务ID和文件ID查找附件
     */
    Optional<Attachment> findByBusinessTypeAndBusinessIdAndFileId(String businessType, Long businessId, Long fileId);
    
    /**
     * 根据用户ID和文件ID查找附件
     */
    Optional<Attachment> findByUserIdAndFileId(Long userId, Long fileId);
    
    /**
     * 根据状态统计附件数量
     */
    long countByStatus(Integer status);
    
    /**
     * 根据是否为临时文件统计附件数量
     */
    long countByIsTemporary(Boolean isTemporary);
    
    /**
     * 根据业务类型前缀查找附件
     * 支持模糊搜索，如传"resume_"就查询以这个开头的所有附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.businessType LIKE :businessTypePrefix%")
    List<Attachment> findByBusinessTypeStartingWith(@Param("businessTypePrefix") String businessTypePrefix);
    
    /**
     * 根据业务类型前缀和业务ID查找附件
     * 支持模糊搜索，如传"resume_"就查询以这个开头的所有附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.businessType LIKE :businessTypePrefix% AND a.businessId = :businessId")
    List<Attachment> findByBusinessTypeStartingWithAndBusinessId(@Param("businessTypePrefix") String businessTypePrefix, @Param("businessId") Long businessId);
}
