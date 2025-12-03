package com.shera.framework.employment.employment.modules.user.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 用户资料详情数据传输对象
 * 包含用户基础信息和详细资料信息
 */
@Data
public class UserProfileDetailDTO {
    
    // 用户基础信息
    private Long id;
    private String username;
    private String phone;
    private String email;
    private Integer userType;
    private Integer status;
    private Integer authStatus;
    
    // 用户资料信息
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
    private List<String> skills; // 技能标签列表
    private String selfIntro;
    
    // 求职偏好
    private List<String> preferredCities; // 期望城市列表
    private List<String> jobTypes; // 工作类型列表
    private List<String> industries; // 行业偏好列表
    private String workMode; // 工作模式：全职/兼职/实习
    private String jobStatus; // 求职状态：积极求职/观望中/在职看机会
    
    // 统计信息
    private Integer totalResumes; // 简历总数
    private Integer jobApplyCount; // 投递次数
    private Integer interviewCount; // 面试次数
    private Integer offerCount; // 收到offer数
    
    // 时间信息
    private String createTime;
    private String updateTime;
    private String lastLoginTime;
}
