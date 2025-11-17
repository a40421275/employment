package com.shera.framework.employment.employment.modules.company.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shera.framework.employment.employment.util.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "company")
public class Company extends BaseEntity {
    
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;
    
    @Column(name = "company_short_name", length = 100)
    private String companyShortName;
    
    @Column(name = "company_logo", length = 500)
    private String companyLogo;
    
    @Column(name = "company_type", nullable = false)
    private Integer companyType = 1; // 1-有限责任公司，2-股份有限公司，3-个人独资企业，4-合伙企业，5-外资企业，6-其他
    
    @Column(name = "industry", nullable = false, length = 100)
    private String industry; // 所属行业
    
    @Column(name = "scale", nullable = false)
    private Integer scale = 1; // 1-1-50人，2-51-100人，3-101-500人，4-501-1000人，5-1000人以上
    
    @Column(name = "legal_person", nullable = false, length = 50)
    private String legalPerson; // 法人代表
    
    @Column(name = "registered_capital", precision = 15, scale = 2)
    private BigDecimal registeredCapital; // 注册资本（万元）
    
    @Column(name = "establish_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishDate; // 成立日期
    
    @Column(name = "business_license_no", length = 50)
    private String businessLicenseNo; // 营业执照号
    
    @Column(name = "business_license_image", length = 500)
    private String businessLicenseImage; // 营业执照图片
    
    @Column(name = "business_scope", columnDefinition = "TEXT")
    private String businessScope; // 经营范围
    
    @Column(name = "province", nullable = false, length = 50)
    private String province; // 省份
    
    @Column(name = "city", nullable = false, length = 50)
    private String city; // 城市
    
    @Column(name = "district", length = 50)
    private String district; // 区县
    
    @Column(name = "address", nullable = false, length = 500)
    private String address; // 详细地址
    
    @Column(name = "latitude")
    private Double latitude; // 纬度
    
    @Column(name = "longitude")
    private Double longitude; // 经度
    
    @Column(name = "contact_person", nullable = false, length = 50)
    private String contactPerson; // 联系人
    
    @Column(name = "contact_phone", nullable = false, length = 20)
    private String contactPhone; // 联系电话
    
    @Column(name = "contact_email", length = 100)
    private String contactEmail; // 联系邮箱
    
    @Column(name = "website", length = 200)
    private String website; // 公司网站
    
    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction; // 公司介绍
    
    @Column(name = "culture", columnDefinition = "TEXT")
    private String culture; // 企业文化
    
    @Column(name = "welfare", columnDefinition = "TEXT")
    private String welfare; // 公司福利
    
    @Column(name = "development", columnDefinition = "TEXT")
    private String development; // 发展历程
    
    @Column(name = "honors", columnDefinition = "TEXT")
    private String honors; // 荣誉资质
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用，1-正常，2-审核中，3-审核失败
    
    @Column(name = "auth_status", nullable = false)
    private Integer authStatus = 0; // 0-未认证，1-审核中，2-已认证
    
    @Column(name = "auth_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime authTime; // 认证时间
    
    @Column(name = "auth_operator_id")
    private Long authOperatorId; // 认证操作员ID
    
    @Column(name = "auth_fail_reason", length = 500)
    private String authFailReason; // 认证失败原因
    
    @Column(name = "job_count", nullable = false)
    private Integer jobCount = 0; // 发布岗位数量
    
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0; // 被查看次数
    
    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount = 0; // 被收藏次数
    
    @Column(name = "apply_count", nullable = false)
    private Integer applyCount = 0; // 收到申请数量
    
    // 企业类型枚举
    public enum CompanyType {
        LIMITED_LIABILITY(1, "有限责任公司"),
        JOINT_STOCK(2, "股份有限公司"),
        SOLE_PROPRIETORSHIP(3, "个人独资企业"),
        PARTNERSHIP(4, "合伙企业"),
        FOREIGN_INVESTED(5, "外资企业"),
        OTHER(6, "其他");
        
        private final int code;
        private final String desc;
        
        CompanyType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
    
    // 企业规模枚举
    public enum Scale {
        SMALL_1(1, "1-50人"),
        SMALL_2(2, "51-100人"),
        MEDIUM(3, "101-500人"),
        LARGE_1(4, "501-1000人"),
        LARGE_2(5, "1000人以上");
        
        private final int code;
        private final String desc;
        
        Scale(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
    
    // 企业状态枚举
    public enum Status {
        DISABLED(0, "禁用"),
        NORMAL(1, "正常"),
        PENDING(2, "审核中"),
        REJECTED(3, "审核失败");
        
        private final int code;
        private final String desc;
        
        Status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
    
    // 认证状态枚举
    public enum AuthStatus {
        UNAUTHENTICATED(0, "未认证"),
        PENDING(1, "审核中"),
        AUTHENTICATED(2, "已认证");
        
        private final int code;
        private final String desc;
        
        AuthStatus(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
