package com.shera.framework.employment.employment.modules.message.entity;

import com.shera.framework.employment.employment.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 消息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    /**
     * 消息标题
     */
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    /**
     * 消息内容
     */
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 发送者ID
     */
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    /**
     * 接收者ID
     */
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    /**
     * 消息类型：1-系统消息，2-用户消息，3-岗位消息
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 优先级：1-低，2-中，3-高
     */
    @Column(name = "priority", columnDefinition = "TINYINT DEFAULT 1")
    private Integer priority = 1;

    /**
     * 消息状态：0-未读，1-已读，2-删除
     */
    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 0")
    private Integer status = 0;

    /**
     * 阅读时间
     */
    @Column(name = "read_time")
    private LocalDateTime readTime;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 关联的业务ID（如岗位ID、简历ID等）
     */
    @Column(name = "business_id")
    private Long businessId;

    /**
     * 业务类型
     */
    @Column(name = "business_type", length = 50)
    private String businessType;

    /**
     * 消息模板ID
     */
    @Column(name = "template_id")
    private Long templateId;

    /**
     * 模板编码（用于自动选择模板）
     */
    @Column(name = "template_code", length = 50)
    private String templateCode;

    /**
     * 发送渠道类型：多个渠道用逗号分隔，如：email,sms,wechat,push
     * 支持渠道：email-邮件，sms-短信，wechat-微信，push-推送，internal-站内消息
     */
    @Column(name = "channel_type", length = 100)
    private String channelType;

    /**
     * 发送结果（JSON格式，包含发送状态、错误信息等）
     */
    @Column(name = "send_result", columnDefinition = "TEXT")
    private String sendResult;

    /**
     * 附件信息（JSON格式）
     */
    @Column(name = "attachments", columnDefinition = "TEXT")
    private String attachments;

    /**
     * 消息类型枚举
     */
    public enum Type {
        SYSTEM(1, "系统消息"),
        USER(2, "用户消息"),
        JOB(3, "岗位消息");

        private final int code;
        private final String desc;

        Type(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static Type fromCode(int code) {
            for (Type type : Type.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return SYSTEM;
        }
    }

    /**
     * 优先级枚举
     */
    public enum Priority {
        LOW(1, "低"),
        MEDIUM(2, "中"),
        HIGH(3, "高");

        private final int code;
        private final String desc;

        Priority(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static Priority fromCode(int code) {
            for (Priority priority : Priority.values()) {
                if (priority.code == code) {
                    return priority;
                }
            }
            return LOW;
        }
    }

    /**
     * 状态枚举
     */
    public enum Status {
        UNREAD(0, "未读"),
        READ(1, "已读"),
        DELETED(2, "删除");

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

        public static Status fromCode(int code) {
            for (Status status : Status.values()) {
                if (status.code == code) {
                    return status;
                }
            }
            return UNREAD;
        }
    }
}
