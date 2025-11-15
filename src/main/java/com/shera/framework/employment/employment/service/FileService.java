package com.shera.framework.employment.employment.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件服务接口
 */
public interface FileService {
    
    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file, Long userId, String fileType);
    
    /**
     * 批量上传文件
     */
    List<String> uploadFiles(List<MultipartFile> files, Long userId, String fileType);
    
    /**
     * 下载文件
     */
    byte[] downloadFile(String fileId);
    
    /**
     * 获取文件信息
     */
    Map<String, Object> getFileInfo(String fileId);
    
    /**
     * 删除文件
     */
    boolean deleteFile(String fileId);
    
    /**
     * 批量删除文件
     */
    boolean deleteFiles(List<String> fileIds);
    
    /**
     * 获取用户文件列表
     */
    List<Map<String, Object>> getUserFiles(Long userId, String fileType);
    
    /**
     * 获取用户文件数量
     */
    long getUserFileCount(Long userId, String fileType);
    
    /**
     * 检查文件是否存在
     */
    boolean fileExists(String fileId);
    
    /**
     * 获取文件URL
     */
    String getFileUrl(String fileId);
    
    /**
     * 获取文件预览URL
     */
    String getFilePreviewUrl(String fileId);
    
    /**
     * 验证文件类型
     */
    boolean validateFileType(MultipartFile file, String allowedTypes);
    
    /**
     * 验证文件大小
     */
    boolean validateFileSize(MultipartFile file, long maxSize);
    
    /**
     * 获取文件存储统计
     */
    Map<String, Object> getStorageStatistics();
    
    /**
     * 清理过期文件
     */
    boolean cleanupExpiredFiles(int days);
    
    /**
     * 压缩文件
     */
    String compressFile(String fileId, String format);
    
    /**
     * 转换文件格式
     */
    String convertFileFormat(String fileId, String targetFormat);
    
    /**
     * 提取文件元数据
     */
    Map<String, Object> extractFileMetadata(String fileId);
    
    /**
     * 生成文件缩略图
     */
    String generateThumbnail(String fileId, int width, int height);
    
    /**
     * 文件重命名
     */
    boolean renameFile(String fileId, String newName);
    
    /**
     * 移动文件
     */
    boolean moveFile(String fileId, String newPath);
    
    /**
     * 复制文件
     */
    String copyFile(String fileId, String newPath);
    
    /**
     * 搜索文件
     */
    List<Map<String, Object>> searchFiles(Long userId, String keyword, String fileType);
    
    /**
     * 获取文件版本历史
     */
    List<Map<String, Object>> getFileVersionHistory(String fileId);
    
    /**
     * 恢复文件版本
     */
    boolean restoreFileVersion(String fileId, String versionId);
    
    /**
     * 设置文件权限
     */
    boolean setFilePermissions(String fileId, Map<String, Boolean> permissions);
    
    /**
     * 获取文件权限
     */
    Map<String, Boolean> getFilePermissions(String fileId);
    
    /**
     * 分享文件
     */
    String shareFile(String fileId, Long targetUserId, String permission);
    
    /**
     * 取消文件分享
     */
    boolean unshareFile(String fileId, Long targetUserId);
    
    /**
     * 获取分享的文件列表
     */
    List<Map<String, Object>> getSharedFiles(Long userId);
    
    /**
     * 获取文件分享列表
     */
    List<Map<String, Object>> getFileShares(String fileId);
    
    /**
     * 验证文件完整性
     */
    boolean verifyFileIntegrity(String fileId);
    
    /**
     * 修复损坏的文件
     */
    boolean repairFile(String fileId);
    
    /**
     * 批量处理文件
     */
    List<String> batchProcessFiles(List<String> fileIds, String operation);
    
    /**
     * 获取文件处理进度
     */
    Map<String, Object> getFileProcessProgress(String processId);
    
    /**
     * 取消文件处理
     */
    boolean cancelFileProcess(String processId);
    
    /**
     * 获取文件操作日志
     */
    List<Map<String, Object>> getFileOperationLogs(String fileId, int limit);
    
    /**
     * 清理文件操作日志
     */
    boolean cleanupFileOperationLogs(int days);
}
