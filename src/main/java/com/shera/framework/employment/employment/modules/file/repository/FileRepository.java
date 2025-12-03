package com.shera.framework.employment.employment.modules.file.repository;

import com.shera.framework.employment.employment.modules.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文件数据访问接口 - 只管理物理文件信息
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
    /**
     * 根据文件哈希值查找文件
     */
    Optional<File> findByFileHash(String fileHash);
    
    /**
     * 根据创建者用户ID查找文件
     */
    List<File> findByCreatorUserId(Long creatorUserId);
    
    /**
     * 根据文件类型查找文件
     */
    List<File> findByFileType(String fileType);
    
    /**
     * 根据状态查找文件
     */
    List<File> findByStatus(Integer status);
    
    /**
     * 根据引用计数查找文件
     */
    List<File> findByReferenceCount(Integer referenceCount);
    
    /**
     * 查找引用计数为0的文件（可以被删除的文件）
     */
    List<File> findByReferenceCountLessThanEqual(Integer referenceCount);
    
    /**
     * 根据最后访问时间查找文件
     */
    List<File> findByLastAccessTimeBefore(LocalDateTime lastAccessTime);
    
    /**
     * 检查文件是否存在
     */
    boolean existsById(Long id);
    
    /**
     * 更新文件状态
     */
    @Modifying
    @Query("UPDATE File f SET f.status = :status WHERE f.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 增加引用计数
     */
    @Modifying
    @Query("UPDATE File f SET f.referenceCount = f.referenceCount + 1, f.lastAccessTime = :lastAccessTime WHERE f.id = :id")
    int incrementReferenceCount(@Param("id") Long id, @Param("lastAccessTime") LocalDateTime lastAccessTime);
    
    /**
     * 减少引用计数
     */
    @Modifying
    @Query("UPDATE File f SET f.referenceCount = f.referenceCount - 1, f.lastAccessTime = :lastAccessTime WHERE f.id = :id AND f.referenceCount > 0")
    int decrementReferenceCount(@Param("id") Long id, @Param("lastAccessTime") LocalDateTime lastAccessTime);
    
    /**
     * 批量更新文件状态
     */
    @Modifying
    @Query("UPDATE File f SET f.status = :status WHERE f.id IN :ids")
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
    
    /**
     * 获取文件统计信息
     */
    @Query("SELECT " +
           "COUNT(f) as totalFiles, " +
           "SUM(f.fileSize) as totalSize, " +
           "COUNT(DISTINCT f.creatorUserId) as totalUsers, " +
           "AVG(f.fileSize) as avgFileSize " +
           "FROM File f WHERE f.status = 1")
    Object[] getFileStatistics();
    
    /**
     * 获取用户文件统计信息
     */
    @Query("SELECT " +
           "COUNT(f) as fileCount, " +
           "SUM(f.fileSize) as totalSize, " +
           "MAX(f.createTime) as lastUploadTime " +
           "FROM File f WHERE f.creatorUserId = :creatorUserId AND f.status = 1")
    Object[] getUserFileStatistics(@Param("creatorUserId") Long creatorUserId);
    
    /**
     * 获取文件类型统计
     */
    @Query("SELECT f.fileType, COUNT(f) FROM File f WHERE f.status = 1 GROUP BY f.fileType")
    List<Object[]> getFileTypeStatistics();
    
    /**
     * 获取引用计数统计
     */
    @Query("SELECT f.referenceCount, COUNT(f) FROM File f WHERE f.status = 1 GROUP BY f.referenceCount")
    List<Object[]> getReferenceCountStatistics();
    
    /**
     * 查找长时间未访问的文件
     */
    @Query("SELECT f FROM File f WHERE f.lastAccessTime < :cutoffTime AND f.referenceCount = 0 AND f.status = 1")
    List<File> findUnusedFiles(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * 根据文件大小范围查找文件
     */
    @Query("SELECT f FROM File f WHERE f.fileSize BETWEEN :minSize AND :maxSize AND f.status = 1")
    List<File> findByFileSizeBetween(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    /**
     * 根据MIME类型查找文件
     */
    List<File> findByMimeType(String mimeType);
    
    /**
     * 根据文件扩展名查找文件
     */
    List<File> findByFileExtension(String fileExtension);

    /**
     * 更新最后访问时间
     */
    @Modifying
    @Query("UPDATE File f SET f.lastAccessTime = :lastAccessTime WHERE f.id = :id")
    int updateLastAccessTime(@Param("id") Long id, @Param("lastAccessTime") LocalDateTime lastAccessTime);
}
