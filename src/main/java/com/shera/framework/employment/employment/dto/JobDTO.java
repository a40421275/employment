package com.shera.framework.employment.employment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位数据传输对象
 */
@Data
public class JobDTO {
    
    private Long id;
    
    private String title;
    
    private Long categoryId;
    
    private Long companyId;
    
    private String companyName;
    
    private String department;
    
    private Integer jobType = 1; // 1-全职，2-兼职，3-实习
    
    private BigDecimal salaryMin;
    
    private BigDecimal salaryMax;
    
    private String salaryUnit = "月";
    
    private String workCity;
    
    private String workAddress;
    
    private String description;
    
    private String requirements;
    
    private String benefits;
    
    private String contactInfo;
    
    private String educationRequirement; // 学历要求
    
    private String experienceRequirement; // 经验要求
    
    private Integer recruitNumber = 1; // 招聘人数
    
    private Integer urgentLevel = 0; // 紧急程度：0-普通，1-紧急，2-非常紧急
    
    private Integer priorityLevel = 0; // 优先级：0-普通，1-重要，2-非常重要
    
    private Boolean isRecommended = false; // 是否推荐
    
    private String recommendReason; // 推荐理由
    
    private String keywords; // 关键词，用于搜索优化
    
    private Integer status = 0; // 0-草稿，1-已发布，2-已下架，3-已归档
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
}
