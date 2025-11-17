package com.shera.framework.employment.employment.modules.job.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.shera.framework.employment.employment.util.BaseEntity;

/**
 * 岗位收藏实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "job_favorite", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_id"})
})
public class JobFavorite extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
    
    // 收藏状态枚举
    public enum FavoriteStatus {
        FAVORITED(1, "已收藏"),
        UNFAVORITED(0, "未收藏");
        
        private final int code;
        private final String desc;
        
        FavoriteStatus(int code, String desc) {
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
