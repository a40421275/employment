package com.shera.framework.employment.employment.modules.job.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * 岗位查询DTO
 */
@Data
public class JobQueryDTO {
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 岗位分类ID
     */
    private Long categoryId;
    
    /**
     * 工作城市
     */
    private String workCity;
    
    /**
     * 岗位类型：1-全职，2-兼职，3-实习
     */
    private Integer jobType;
    
    /**
     * 岗位状态：0-草稿，1-已发布，2-已下架，3-已归档
     */
    private Integer status;
    
    /**
     * 最低薪资
     */
    private Double minSalary;
    
    /**
     * 最高薪资
     */
    private Double maxSalary;
    
    /**
     * 页码，从0开始
     */
    private Integer page = 0;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    /**
     * 排序字段
     */
    private String sort = "createTime";
    
    /**
     * 排序方向
     */
    private Sort.Direction direction = Sort.Direction.DESC;
}
