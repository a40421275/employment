package com.shera.framework.employment.employment.modules.job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位申请数据传输对象
 */
@Data
public class JobApplyDTO {
    /**
     * 申请ID
     */
    private Long id;

    private Long userId;

    private Long jobId;

    private Long resumeId;

    private Integer status = 1; // 1-已投递，2-已查看，3-已通知面试，4-面试通过，5-已拒绝

    private BigDecimal matchScore;
    
    private String applyNotes;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime interviewTime;
    
    private String interviewLocation;
    
    private String feedback;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    // 基本信息字段，不包含关联对象
    private String jobTitle; // 岗位名称
    
    private String companyName; // 公司名称
    
    private String resumeName; // 简历名称
    
    private String userName; // 用户名称
    
    private String userEmail; // 用户邮箱
}
