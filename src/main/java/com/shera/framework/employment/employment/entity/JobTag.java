package com.shera.framework.employment.employment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 岗位标签实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "job_tag")
public class JobTag extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_id")
    private Long jobId;
    
    @Column(name = "tag_name", nullable = false, length = 50)
    private String tagName;
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @Column(name = "tag_type", nullable = false)
    private Integer tagType = 0; // 0-系统标签，1-自定义标签
    
    @Column(name = "tag_color", length = 20)
    private String tagColor; // 标签颜色
    
    @Column(name = "description", length = 200)
    private String description; // 标签描述
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
    
    // 常用标签枚举
    public enum CommonTags {
        URGENT("急招"),
        HIGH_SALARY("高薪"),
        FLEXIBLE("弹性工作"),
        REMOTE("远程办公"),
        INTERNSHIP("实习"),
        PART_TIME("兼职"),
        FULL_TIME("全职"),
        STARTUP("创业公司"),
        BIG_COMPANY("大厂"),
        TECH("技术驱动"),
        GROWTH("成长空间大"),
        BENEFITS("福利待遇好"),
        TRAINING("培训体系完善"),
        TEAM("团队氛围好"),
        LEADER("领导好"),
        OVERTIME("加班少"),
        BONUS("年终奖"),
        STOCK("股票期权"),
        INSURANCE("五险一金"),
        MEAL_SUBSIDY("餐补"),
        TRANSPORT_SUBSIDY("交通补贴"),
        HOUSING_SUBSIDY("住房补贴");
        
        private final String name;
        
        CommonTags(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
}
