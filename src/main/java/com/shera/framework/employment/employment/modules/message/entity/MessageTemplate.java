package com.shera.framework.employment.employment.modules.message.entity;

import com.shera.framework.employment.employment.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 消息模板实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message_template")
public class MessageTemplate extends BaseEntity {

    /**
     * 模板编码（唯一标识，用于自动选择模板）
     */
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    /**
     * 模板名称
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * 渠道类型：email-邮件，sms-短信，wechat-微信，push-推送
     */
    @Column(name = "channel_type", length = 20, nullable = false)
    private String channelType;

    /**
     * 业务类型（用于自动匹配模板，如：user_welcome, job_apply_notify等）
     */
    @Column(name = "business_type", length = 50)
    private String businessType;

    /**
     * 邮件主题（仅邮件模板使用）
     */
    @Column(name = "subject", length = 200)
    private String subject;

    /**
     * 模板内容
     */
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 模板变量（JSON格式）
     */
    @Column(name = "variables", columnDefinition = "TEXT")
    private String variables;

    /**
     * 模板状态：0-禁用，1-启用
     */
    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1")
    private Integer status = 1;

    /**
     * 模板描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 模板类型枚举
     */
    public enum Type {
        EMAIL("email", "邮件"),
        SMS("sms", "短信"),
        WECHAT("wechat", "微信"),
        PUSH("push", "推送");

        private final String code;
        private final String desc;

        Type(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }


        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static Type fromCode(String code) {
            for (Type type : Type.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return EMAIL;
        }
    }

    /**
     * 状态枚举
     */
    public enum Status {
        DISABLED(0, "禁用"),
        ENABLED(1, "启用");

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
            return ENABLED;
        }
    }
}
