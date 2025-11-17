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
}
