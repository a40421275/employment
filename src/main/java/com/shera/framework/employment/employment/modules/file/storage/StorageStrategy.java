package com.shera.framework.employment.employment.modules.file.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * 存储策略接口
 * 定义统一的文件存储操作，支持多种存储方式（本地、H3C对象存储等）
 */
public interface StorageStrategy {
    
    /**
     * 上传文件
     * @param file 文件
     * @param storagePath 存储路径
     * @param metadata 元数据
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String storagePath, Map<String, String> metadata);
    
    /**
     * 上传文件（通过输入流）
     * @param inputStream 文件输入流
     * @param storagePath 存储路径
     * @param fileName 文件名
     * @param contentType 文件类型
     * @param metadata 元数据
     * @return 文件访问URL
     */
    String uploadFile(InputStream inputStream, String storagePath, String fileName, 
                     String contentType, Map<String, String> metadata);
    
    /**
     * 下载文件
     * @param storagePath 存储路径
     * @return 文件字节数组
     */
    byte[] downloadFile(String storagePath);
    
    /**
     * 获取文件输入流
     * @param storagePath 存储路径
     * @return 文件输入流
     */
    InputStream getFileInputStream(String storagePath);
    
    /**
     * 删除文件
     * @param storagePath 存储路径
     * @return 是否删除成功
     */
    boolean deleteFile(String storagePath);
    
    /**
     * 获取文件URL
     * @param storagePath 存储路径
     * @return 文件访问URL
     */
    String getFileUrl(String storagePath);
    
    /**
     * 检查文件是否存在
     * @param storagePath 存储路径
     * @return 是否存在
     */
    boolean fileExists(String storagePath);
    
    /**
     * 获取文件信息
     * @param storagePath 存储路径
     * @return 文件信息
     */
    Map<String, Object> getFileInfo(String storagePath);
    
    /**
     * 获取存储类型
     * @return 存储类型标识
     */
    String getStorageType();
    
    /**
     * 测试存储连接
     * @return 连接测试结果
     */
    boolean testConnection();
    
    /**
     * 获取存储统计信息
     * @return 统计信息
     */
    Map<String, Object> getStorageStatistics();
    
    /**
     * 列出目录下的文件
     * @param directoryPath 目录路径
     * @return 文件列表
     */
    Map<String, Object> listDirectory(String directoryPath);
}
