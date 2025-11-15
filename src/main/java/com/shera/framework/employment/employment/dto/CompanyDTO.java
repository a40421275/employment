package com.shera.framework.employment.employment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业数据传输对象
 */
@Data
public class CompanyDTO {
    
    private Long id;
    
    private String companyName;
    
    private String companyShortName;
    
    private String companyLogo;
    
    private Integer companyType;
    
    private String companyTypeDesc;
    
    private String industry;
    
    private Integer scale;
    
    private String scaleDesc;
    
    private String legalPerson;
    
    private BigDecimal registeredCapital;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishDate;
    
    private String businessLicenseNo;
    
    private String businessLicenseImage;
    
    private String businessScope;
    
    private String province;
    
    private String city;
    
    private String district;
    
    private String address;
    
    private Double latitude;
    
    private Double longitude;
    
    private String contactPerson;
    
    private String contactPhone;
    
    private String contactEmail;
    
    private String website;
    
    private String introduction;
    
    private String culture;
    
    private String welfare;
    
    private String development;
    
    private String honors;
    
    private Integer status;
    
    private String statusDesc;
    
    private Integer authStatus;
    
    private String authStatusDesc;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime authTime;
    
    private Long authOperatorId;
    
    private String authFailReason;
    
    private Integer jobCount;
    
    private Integer viewCount;
    
    private Integer favoriteCount;
    
    private Integer applyCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
