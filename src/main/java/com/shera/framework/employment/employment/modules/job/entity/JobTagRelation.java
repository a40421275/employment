package com.shera.framework.employment.employment.modules.job.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.shera.framework.employment.employment.util.BaseEntity;

/**
 * 岗位标签关联实体类
 * 建立岗位和标签的多对多关系
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "job_tag_relation", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"job_id", "tag_id"})
})
public class JobTagRelation extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Column(name = "tag_id", nullable = false)
    private Long tagId;
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}
