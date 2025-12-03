# 消息管理与通知管理定位梳理及通知模块完善方案

## 一、现状分析

### 1.1 当前系统架构

当前系统存在三个相关模块：
1. **消息管理模块** (`MessageController`) - 功能最全面
2. **通知管理模块** (`NotificationController`) - 功能相对简单
3. **消息推送系统** (`MessagePushController`) - 多通道推送

### 1.2 模块功能对比

| 模块 | 核心功能 | 数据模型 | 推送渠道 | 使用场景 |
|------|----------|----------|----------|----------|
| **消息管理** | 完整的消息生命周期管理，支持邮件、短信、微信、推送等多种渠道，支持模板化 | `Message` 实体，包含发送者、接收者、类型、优先级、状态、过期时间等 | 邮件、短信、微信、推送、站内消息 | 系统通知、用户间消息、业务通知、验证码发送 |
| **通知管理** | 简单的站内通知，主要用于业务状态变更通知 | `Notification` 实体，仅包含用户、标题、内容、类型、阅读状态 | 仅站内通知 | 申请状态变更、面试通知、系统消息 |
| **消息推送** | 多通道消息发送，专注于消息的发送和状态跟踪 | 与消息管理共享数据模型 | 邮件、短信、微信、推送 | 批量消息发送、模板消息发送 |

### 1.3 问题识别

1. **功能重叠**：消息管理和通知管理在站内消息功能上存在重叠
2. **职责不清**：消息管理模块过于庞大，承担了太多职责
3. **数据模型不一致**：`Message` 和 `Notification` 实体设计差异大
4. **推送能力分散**：消息推送功能分散在多个模块中
5. **用户体验不一致**：用户需要同时关注消息和通知两个入口

## 二、定位梳理

### 2.1 消息管理模块定位

**核心定位**：**全渠道消息通信平台**

**职责范围**：
1. **消息发送**：支持邮件、短信、微信、推送、站内消息等多种渠道
2. **消息管理**：完整的消息生命周期管理（创建、发送、状态跟踪、归档）
3. **模板管理**：消息模板的创建、管理和使用
4. **验证码服务**：邮件验证码的发送和验证
5. **消息统计**：消息发送效果统计分析

**适用场景**：
- 系统级通知（如系统维护、公告）
- 用户间通信（如企业HR与求职者沟通）
- 业务通知（如岗位推荐、申请状态变更）
- 验证码发送（注册、登录、密码重置）
- 营销消息（如活动通知、优惠信息）

### 2.2 通知管理模块定位

**核心定位**：**实时业务状态通知中心**

**职责范围**：
1. **实时通知**：业务状态变更的实时通知
2. **状态同步**：业务状态与用户感知的同步
3. **轻量级提醒**：无需复杂交互的简单提醒
4. **用户行为触发**：用户操作触发的即时反馈

**适用场景**：
- 申请状态变更（已查看、已处理、已拒绝、已通过）
- 面试安排通知（时间、地点、注意事项）
- 系统操作反馈（操作成功、失败提示）
- 实时提醒（新消息、新申请、新关注）

### 2.3 边界划分

| 维度 | 消息管理 | 通知管理 |
|------|----------|----------|
| **消息类型** | 正式消息、业务通知、验证码 | 实时提醒、状态变更、操作反馈 |
| **持久性** | 长期存储，支持历史查询 | 短期存储，可自动清理 |
| **交互性** | 支持复杂交互（附件、链接、模板） | 简单交互（查看、标记已读） |
| **时效性** | 可设置过期时间，支持定时发送 | 实时性要求高，即时触达 |
| **渠道** | 多渠道（邮件、短信、微信、推送） | 主要站内通知，可扩展推送 |
| **用户感知** | 需要用户主动查看和处理 | 主动推送给用户，强提醒 |

## 三、通知模块完善方案

### 3.1 架构设计

```
┌─────────────────────────────────────────────────────────────┐
│                   通知模块架构设计                           │
├─────────────────────────────────────────────────────────────┤
│ 应用层                                                      │
│  ├─ 通知控制器 (NotificationController)                     │
│  ├─ 通知服务 (NotificationService)                          │
│  └─ 事件监听器 (NotificationEventListener)                  │
├─────────────────────────────────────────────────────────────┤
│ 领域层                                                      │
│  ├─ 通知实体 (Notification)                                 │
│  ├─ 通知策略 (NotificationStrategy)                         │
│  ├─ 通知渠道 (NotificationChannel)                          │
│  └─ 通知模板 (NotificationTemplate)                         │
├─────────────────────────────────────────────────────────────┤
│ 基础设施层                                                  │
│  ├─ 消息队列 (Message Queue)                                │
│  ├─ WebSocket 服务                                          │
│  ├─ 推送服务集成                                            │
│  └─ 数据库存储                                              │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 数据模型优化

#### 3.2.1 通知实体 (`Notification`) 增强

```java
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    
    // 基础信息
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    // 通知类型
    @Column(name = "type", nullable = false)
    private Integer type;
    
    // 通知子类型（细化分类）
    @Column(name = "sub_type", length = 50)
    private String subType;
    
    // 优先级（1-低，2-中，3-高，4-紧急）
    @Column(name = "priority", nullable = false)
    private Integer priority = 2;
    
    // 状态（0-未发送，1-已发送，2-已送达，3-已读，4-已处理）
    @Column(name = "status", nullable = false)
    private Integer status = 0;
    
    // 关联业务信息
    @Column(name = "business_id")
    private Long businessId;
    
    @Column(name = "business_type", length = 50)
    private String businessType;
    
    @Column(name = "business_data", columnDefinition = "JSON")
    private String businessData;
    
    // 时间信息
    @Column(name = "send_time")
    private LocalDateTime sendTime;
    
    @Column(name = "deliver_time")
    private LocalDateTime deliverTime;
    
    @Column(name = "read_time")
    private LocalDateTime readTime;
    
    @Column(name = "action_time")
    private LocalDateTime actionTime;
    
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    // 交互信息
    @Column(name = "action_url", length = 500)
    private String actionUrl;
    
    @Column(name = "action_text", length = 50)
    private String actionText;
    
    @Column(name = "icon_url", length = 500)
    private String iconUrl;
    
    // 渠道信息
    @Column(name = "channels", length = 100)
    private String channels; // 逗号分隔：web,push,email,sms
    
    // 模板信息
    @Column(name = "template_code", length = 50)
    private String templateCode;
    
    @Column(name = "template_params", columnDefinition = "JSON")
    private String templateParams;
    
    // 统计信息
    @Column(name = "click_count")
    private Integer clickCount = 0;
    
    // 用户反馈
    @Column(name = "feedback", length = 500)
    private String feedback;
    
    @Column(name = "feedback_time")
    private LocalDateTime feedbackTime;
}
```

#### 3.2.2 通知类型枚举扩展

```java
public enum NotificationType {
    // 系统通知
    SYSTEM_ANNOUNCEMENT(1, "系统公告", "system"),
    SYSTEM_MAINTENANCE(2, "系统维护", "system"),
    SYSTEM_UPDATE(3, "系统更新", "system"),
    
    // 申请相关
    APPLICATION_SUBMITTED(101, "申请已提交", "application"),
    APPLICATION_VIEWED(102, "申请已被查看", "application"),
    APPLICATION_PROCESSING(103, "申请处理中", "application"),
    APPLICATION_ACCEPTED(104, "申请已通过", "application"),
    APPLICATION_REJECTED(105, "申请未通过", "application"),
    APPLICATION_CANCELLED(106, "申请已取消", "application"),
    
    // 面试相关
    INTERVIEW_SCHEDULED(201, "面试已安排", "interview"),
    INTERVIEW_UPDATED(202, "面试时间更新", "interview"),
    INTERVIEW_CANCELLED(203, "面试已取消", "interview"),
    INTERVIEW_REMINDER(204, "面试提醒", "interview"),
    INTERVIEW_FEEDBACK(205, "面试反馈", "interview"),
    
    // 岗位相关
    JOB_RECOMMENDATION(301, "岗位推荐", "job"),
    JOB_APPLICATION_DEADLINE(302, "岗位申请截止提醒", "job"),
    JOB_STATUS_CHANGED(303, "岗位状态变更", "job"),
    
    // 消息相关
    NEW_MESSAGE(401, "新消息", "message"),
    MESSAGE_REPLY(402, "消息回复", "message"),
    
    // 用户相关
    USER_WELCOME(501, "欢迎通知", "user"),
    USER_PROFILE_UPDATE(502, "资料更新提醒", "user"),
    USER_SECURITY(503, "安全提醒", "user"),
    
    // 企业相关
    COMPANY_VERIFIED(601, "企业认证通过", "company"),
    COMPANY_UPDATE(602, "企业信息更新", "company"),
    
    // 活动相关
    EVENT_REMINDER(701, "活动提醒", "event"),
    EVENT_UPDATE(702, "活动更新", "event"),
    
    // 营销相关
    PROMOTION_OFFER(801, "优惠活动", "promotion"),
    NEWSLETTER(802, "资讯推送", "promotion");
    
    private final int code;
    private final String name;
    private final String category;
    
    // 构造函数、getter方法等
}
```

### 3.3 核心功能设计

#### 3.3.1 通知发送服务

```java
public interface NotificationService {
    
    // 基础通知操作
    Notification sendNotification(NotificationDTO dto);
    List<Notification> sendBatchNotifications(List<NotificationDTO> dtos);
    Notification getNotification(Long id);
    Page<Notification> listNotifications(NotificationQuery query);
    
    // 状态管理
    Notification markAsRead(Long notificationId);
    List<Notification> markMultipleAsRead(List<Long> notificationIds);
    void markAllAsRead(Long userId);
    
    // 业务通知
    Notification sendApplicationStatusNotification(Long userId, Long applicationId, String status);
    Notification sendInterviewNotification(Long userId, Long interviewId, LocalDateTime time, String location);
    Notification sendSystemNotification(Long userId, String title, String content);
    
    // 统计查询
    NotificationStats getNotificationStats(Long userId);
    Map<String, Long> getUnreadCountByType(Long userId);
    
    // 模板通知
    Notification sendTemplateNotification(String templateCode, Long userId, Map<String, Object> params);
    
    // 渠道管理
    void enableChannel(Long userId, String channel);
    void disableChannel(Long userId, String channel);
    List<String> getUserChannels(Long userId);
}
```

#### 3.3.2 通知渠道管理

支持多种通知渠道：
1. **Web通知**：站内消息中心
2. **推送通知**：APP推送（iOS/Android）
3. **邮件通知**：重要通知的邮件备份
4. **短信通知**：紧急通知的短信提醒
5. **微信通知**：微信公众号模板消息

```java
public interface NotificationChannel {
    String getChannelType();
    boolean isEnabled(Long userId);
    NotificationResult send(Notification notification);
    ChannelConfig getConfig();
}

public class WebNotificationChannel implements NotificationChannel {
    // WebSocket 实时推送
}

public class PushNotificationChannel implements NotificationChannel {
    // 移动端推送
}

public class EmailNotificationChannel implements NotificationChannel {
    // 邮件发送
}

public class SmsNotificationChannel implements NotificationChannel {
    // 短信发送
}

public class WechatNotificationChannel implements NotificationChannel {
    // 微信模板消息
}
```

#### 3.3.3 通知策略引擎

```java
public interface NotificationStrategy {
    boolean match(Notification notification);
    List<String> getChannels(Notification notification);
    Integer getPriority(Notification notification);
    LocalDateTime getSendTime(Notification notification);
    String getTemplate(Notification notification);
}

public class UrgentNotificationStrategy implements NotificationStrategy {
    // 紧急通知策略：所有渠道，最高优先级，立即发送
}

public class BusinessNotificationStrategy implements NotificationStrategy {
    // 业务通知策略：Web+推送，中优先级，立即发送
}

public class MarketingNotificationStrategy implements NotificationStrategy {
    // 营销通知策略：Web+邮件，低优先级，定时发送
}

public class ReminderNotificationStrategy implements NotificationStrategy {
    // 提醒通知策略：Web+推送，中优先级，提前提醒
}
```

### 3.4 API接口设计

#### 3.4.1 通知管理接口

```java
@RestController
@RequestMapping("/api/v2/notifications")
public class NotificationControllerV2 {
    
    // 通知查询
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id);
    
    @GetMapping
    public ResponseEntity<Page<Notification>> listNotifications(NotificationQuery query);
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Notification>> listUserNotifications(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size);
    
    // 状态管理
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id);
    
    @PutMapping("/batch-read")
    public ResponseEntity<List<Notification>> markMultipleAsRead(@RequestBody List<Long> ids);
    
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId);
    
    // 统计查询
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<NotificationStats> getStats(@PathVariable Long userId);
    
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId);
    
    // 渠道管理
    @GetMapping("/user/{userId}/channels")
    public ResponseEntity<List<ChannelConfig>> getUserChannels(@PathVariable Long userId);
    
    @PutMapping("/user/{userId}/channels/{channel}")
    public ResponseEntity<Void> updateChannel(
        @PathVariable Long userId,
        @PathVariable String channel,
        @RequestParam boolean enabled);
    
    // 业务通知
    @PostMapping("/application/{applicationId}/status")
    public ResponseEntity<Notification> notifyApplicationStatus(
        @PathVariable Long applicationId,
        @RequestParam String status);
    
    @PostMapping("/interview/{interviewId}/schedule")
    public ResponseEntity<Notification> notifyInterviewSchedule(
        @PathVariable Long interviewId);
    
    @PostMapping("/interview/{interviewId}/reminder")
    public ResponseEntity<Notification> sendInterviewReminder(
        @PathVariable Long interviewId);
}
```

#### 3.4.2 WebSocket实时通知接口

```java
@Controller
public class NotificationWebSocketController {
    
    @MessageMapping("/notifications/subscribe")
    public void subscribe(@Payload SubscriptionRequest request);
    
    @MessageMapping("/notifications/unsubscribe")
    public void unsubscribe(@Payload SubscriptionRequest request);
    
    @MessageMapping("/notifications/ack")
    public void acknowledge(@Payload AckRequest request);
    
    // 服务端推送
    public void pushNotification(Long userId, Notification notification);
    public void pushUnreadCount(Long userId, Map<String, Long> counts);
}
```

### 3.5 事件驱动架构

#### 3.5.1 领域事件定义

```java
// 通知相关领域事件
public class NotificationCreatedEvent {
    private final Notification notification;
    private final LocalDateTime createdAt;
}

public class NotificationDeliveredEvent {
    private final Long notificationId;
    private final String channel;
    private final LocalDateTime deliveredAt;
}

public class NotificationReadEvent {
    private final Long notificationId;
    private final Long userId;
    private final LocalDateTime readAt;
}

public class NotificationActionEvent {
    private final Long notificationId;
    private final String action;
    private final LocalDateTime actionTime;
}

// 业务事件触发通知
public class ApplicationStatusChangedEvent {
    private final Long applicationId;
    private final String oldStatus;
    private final String newStatus;
    private final Long userId;
    private final LocalDateTime changedAt;
}

public class InterviewScheduledEvent {
    private final Long interviewId;
    private final Long userId;
    private final LocalDateTime interviewTime;
    private final String location;
}
```

#### 3.5.2 事件监听器

```java
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    
    private final NotificationService notificationService;
    
    @EventListener
    public void handleApplicationStatusChanged(ApplicationStatusChangedEvent event) {
        // 根据状态变更发送相应通知
        notificationService.sendApplicationStatusNotification(
            event.getUserId(),
            event.getApplicationId(),
            event.getNewStatus()
        );
    }
    
    @EventListener
    public void handleInterviewScheduled(InterviewScheduledEvent event) {
        // 发送面试安排通知
        notificationService.sendInterviewNotification(
            event.getUserId(),
            event.getInterviewId(),
            event.getInterviewTime(),
            event.getLocation()
        );
        
        // 安排面试提醒（提前1小时）
        notificationService.scheduleReminder(
            event.getUserId(),
            event.getInterviewId(),
            event.getInterviewTime().minusHours(1)
        );
    }
    
    @EventListener
    public void handleMessageReceived(MessageReceivedEvent event) {
        // 新消息通知
        notificationService.sendNewMessageNotification(
            event.getReceiverId(),
            event.getMessageId(),
            event.getSenderName()
        );
    }
    
    @EventListener
    public void handleJobRecommendation(JobRecommendationEvent event) {
        // 岗位推荐通知
        notificationService.sendJobRecommendationNotification(
            event.getUserId(),
            event.getJobId(),
            event.getRecommendationReason()
        );
    }
}
