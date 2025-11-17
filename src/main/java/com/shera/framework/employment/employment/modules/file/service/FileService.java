package com.shera.framework.employment.employment.modules.file.service;

import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件服务接口（基于两步式上传绑定方案）
 */
public interface FileService {
    
    // ==================== 两步式上传绑定方案 ====================
    
    /**
     * 第一步：上传文件到文件池
     * 只保存物理文件信息，返回文件ID
     */
    Long uploadFileToPool(MultipartFile file, Long userId, String fileType);
    
    /**
     * 第二步：创建附件记录
     * 将文件ID与业务属性关联，返回附件ID
     */
    Long createAttachment(Long fileId, Long userId, String businessType, Long businessId,
                         String description, String tags, Boolean isPublic);
    
    /**
     * 两步式上传绑定的便捷方法
     * 一次性完成文件上传和附件创建
     */
    Long uploadAndCreateAttachment(MultipartFile file, Long userId, String fileType, String businessType, Long businessId,
                                  String description, String tags, Boolean isPublic);
    
    // ==================== 临时文件管理 ====================
    
    /**
     * 上传临时文件（7天过期）
     */
    Long uploadTemporaryFile(MultipartFile file, Long userId, String fileType);
    
    /**
     * 绑定临时文件到业务
     * 将临时文件绑定到具体业务，使其成为正式文件
     */
    boolean bindTemporaryFileToBusiness(Long attachmentId, String businessType, Long businessId, 
                                       String description, String tags, Boolean isPublic);
    
    // ==================== 核心文件操作 ====================
    
    /**
     * 下载文件
     */
    byte[] downloadFile(Long attachmentId);
    
    /**
     * 获取文件信息
     */
    Map<String, Object> getFileInfo(Long attachmentId);
    
    /**
     * 删除文件（删除附件记录，减少文件引用计数）
     */
    boolean deleteFile(Long attachmentId);
    
    /**
     * 获取用户文件列表
     */
    List<Map<String, Object>> getUserFiles(Long userId, String businessType);

    /**
     * 根据业务类型和业务ID获取附件列表
     */
    List<Map<String, Object>> getAttachmentsByBusiness(String businessType, Long businessId, String fileType);

    /**
     * 统一文件查询接口 - 支持附件属性筛选和分页
     */
    Page<AttachmentWithFileDTO> queryAttachments(FileQueryDTO queryDTO);
    
    /**
     * 检查文件是否存在
     */
    boolean fileExists(Long attachmentId);
    
    /**
     * 获取文件URL
     */
    String getFileUrl(Long attachmentId);

    /**
     * 生成签名文件URL（用于前端图片访问）
     * @param attachmentId 附件ID
     * @return 签名URL
     */
    String generateSignedFileUrl(Long attachmentId);

    /**
     * 验证签名URL并下载文件
     * @param attachmentId 附件ID
     * @param expires 过期时间戳
     * @param signature 签名
     * @return 文件内容
     */
    byte[] downloadFileWithSignature(Long attachmentId, Long expires, String signature);
    
    /**
     * 验证文件类型
     */
    boolean validateFileType(MultipartFile file, String allowedTypes);
    
    /**
     * 验证文件大小
     */
    boolean validateFileSize(MultipartFile file, long maxSize);
    
    // ==================== 文件池管理 ====================
    
    /**
     * 根据文件哈希查找文件
     */
    Long findFileByHash(String fileHash);
    
    /**
     * 获取文件存储统计
     */
    Map<String, Object> getStorageStatistics();
    
    /**
     * 清理过期文件
     */
    boolean cleanupExpiredFiles(int days);
    
    /**
     * 清理未引用的文件
     */
    boolean cleanupUnreferencedFiles(int days);
}
