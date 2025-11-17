package com.shera.framework.employment.employment.modules.file.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * 文件查询数据传输对象
 */
@Data
public class FileQueryDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 业务ID
     */
    private Long businessId;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 文件扩展名
     */
    private String fileExtension;
    
    /**
     * 搜索关键词（文件名、描述、标签）
     */
    private String keyword;
    
    /**
     * 状态：1-正常，2-已删除，3-已过期
     */
    private Integer status;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    private Boolean isPublic;
    
    /**
     * 是否临时文件：0-否，1-是
     */
    private Boolean isTemporary;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 文件大小最小值（字节）
     */
    private Long minFileSize;
    
    /**
     * 文件大小最大值（字节）
     */
    private Long maxFileSize;
    
    /**
     * 排序字段
     */
    private String sortField = "createTime";
    
    /**
     * 排序方向：ASC/DESC
     */
    private String sortDirection = "DESC";
    
    /**
     * 页码
     */
    private Integer page = 0;
    
    /**
     * 每页大小
     */
    private Integer size = 20;
    
    /**
     * 获取分页信息
     */
    public Pageable getPageable() {
        return org.springframework.data.domain.PageRequest.of(
            page, size, 
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.fromString(sortDirection), 
                sortField
            )
        );
    }
    
    /**
     * 检查是否需要分页
     */
    public boolean needPagination() {
        return page != null && size != null && size > 0;
    }
    
    /**
     * 获取排序字段
     */
    public String getSortField() {
        // 防止SQL注入，只允许特定的字段排序
        switch (sortField) {
            case "fileName":
            case "fileSize":
            case "downloadCount":
            case "viewCount":
            case "createTime":
            case "updateTime":
                return sortField;
            default:
                return "createTime";
        }
    }
    
    /**
     * 获取排序方向
     */
    public String getSortDirection() {
        return "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";
    }
}
