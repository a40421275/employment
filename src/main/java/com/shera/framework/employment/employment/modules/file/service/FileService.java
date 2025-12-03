package com.shera.framework.employment.employment.modules.file.service;

import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentUpdateDTO;
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
     * 第一步：上传文件到文件池（支持自定义文件名）
     * 只保存物理文件信息，返回文件ID
     */
    Long uploadFileToPool(MultipartFile file, Long userId, String fileType, String fileName);
    
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
     * 下载文件（通过附件ID）
     */
    byte[] downloadFile(Long attachmentId);

    /**
     * 通过文件ID下载文件
     */
    byte[] downloadFileByFileId(Long fileId);

    /**
     * 通过文件ID获取文件信息
     */
    Map<String, Object> getFileInfoByFileId(Long fileId);
    
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
     * 通过文件ID生成签名文件URL（用于前端图片访问）
     * @param fileId 文件ID
     * @return 签名URL
     */
    String generateSignedFileUrlByFileId(Long fileId);

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
    
    // ==================== 附件编辑功能 ====================
    
    /**
     * 更新附件信息
     * 支持更新描述、标签、公开状态等元数据
     */
    boolean updateAttachment(AttachmentUpdateDTO updateDTO);
    
    /**
     * 更换附件关联的文件
     * 支持后期更改文件关联，自动处理引用计数
     */
    boolean changeAttachmentFile(Long attachmentId, Long userId, Long newFileId);
    
    /**
     * 更新附件业务信息
     * 支持更改业务类型和业务ID
     */
    boolean updateAttachmentBusinessInfo(Long attachmentId, Long userId, String businessType, Long businessId);
    
    /**
     * 批量更新附件状态
     * 支持批量更新附件状态（正常、删除、过期等）
     */
    boolean batchUpdateAttachmentStatus(List<Long> attachmentIds, Long userId, Integer status);
    
    /**
     * 获取附件编辑历史
     * 记录附件的编辑操作历史
     */
    List<Map<String, Object>> getAttachmentEditHistory(Long attachmentId);
    
    // ==================== 附件删除功能 ====================
    
    /**
     * 只删除附件记录，不删除物理文件
     * 适用于需要保留文件但删除业务关联的场景
     */
    boolean deleteAttachmentOnly(Long attachmentId);
    
    /**
     * 批量只删除附件记录，不删除物理文件
     */
    boolean batchDeleteAttachmentsOnly(List<Long> attachmentIds);
    
    // ==================== 业务类型前缀搜索功能 ====================
    
    /**
     * 根据业务类型前缀获取附件列表
     * 支持模糊搜索，如传"resume_"就查询以这个开头的所有附件
     */
    List<Map<String, Object>> getAttachmentsByBusinessTypePrefix(String businessTypePrefix);
    
    /**
     * 根据业务类型前缀和业务ID获取附件列表
     * 支持模糊搜索，如传"resume_"就查询以这个开头的所有附件
     */
    List<Map<String, Object>> getAttachmentsByBusinessTypePrefixAndBusinessId(String businessTypePrefix, Long businessId);
    
    /**
     * 根据业务类型前缀删除附件
     * 支持清理某个业务模块的所有附件，如传"resume_"就删除所有以这个开头的附件
     */
    boolean deleteAttachmentsByBusinessTypePrefix(String businessTypePrefix);
    
    /**
     * 根据业务类型前缀和业务ID删除附件
     * 支持清理某个业务模块的特定业务ID的所有附件
     */
    boolean deleteAttachmentsByBusinessTypePrefixAndBusinessId(String businessTypePrefix, Long businessId);
}
