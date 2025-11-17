package com.shera.framework.employment.employment.modules.user.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户资料数据传输对象
 */
@Data
public class UserProfileDTO {
    
    private Long id;
    
    private Long userId;
    
    private String realName;
    
    private Integer gender; // 0-未知，1-男，2-女
    
    private LocalDate birthday;
    
    private String avatar;
    
    private String idCardFront;
    
    private String idCardBack;
    
    private String education;
    
    private Integer workYears;
    
    private BigDecimal currentSalary;
    
    private BigDecimal expectedSalary;
    
    private String city;
    
    private String skills; // JSON格式的技能标签
    
    private String selfIntro;
}
