package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 简历基础信息
 */
@Data
public class BasicInfo {
    private String realName;
    private Integer gender;
    private LocalDate birthday;
    private String avatar;
    private String phone;
    private String email;
    private String city;
    private String education;
    private Integer workYears;
    private BigDecimal currentSalary;
    private BigDecimal expectedSalary;
    private String selfIntro;
    
    // 求职偏好信息
    private List<String> preferredCities; // 期望城市列表
    private List<String> jobTypes; // 工作类型列表
    private List<String> industries; // 行业偏好列表
    private String workMode; // 工作模式：全职/兼职/实习
    private String jobStatus; // 求职状态：积极求职/观望中/在职看机会
}
