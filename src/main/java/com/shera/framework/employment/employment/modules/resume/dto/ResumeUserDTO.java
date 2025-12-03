package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 简历相关的用户信息DTO
 * 包含简历展示所需的基本用户信息和用户资料信息，不包含敏感信息
 */
@Data
public class ResumeUserDTO {
    
    // 用户基本信息（来自User实体）
    private Long id;
    
    private String username;
    
    private String phone;
    
    private String email;
    
    // 用户资料信息（来自UserProfile实体）
    private String realName;
    
    private Integer gender;
    
    private LocalDate birthday;
    
    private String avatar;
    
    private String education;
    
    private Integer workYears;
    
    private BigDecimal currentSalary;
    
    private BigDecimal expectedSalary;
    
    private String city;
    
    private String selfIntro;
    
    // 不包含以下敏感信息：
    // - password
    // - wxOpenid
    // - wxUnionid
    // - companyId
    // - createTime
    // - updateTime
    // - idCardFront
    // - idCardBack
    // - 其他敏感字段
}
