# 简历模块重构设计方案

## 1. 现状分析

### 1.1 当前问题识别

通过分析现有代码和文档，发现以下主要问题：

#### 数据冗余与重叠
- **用户资料表 (user_profile)** 包含：真实姓名、性别、生日、头像、学历、工作年限、当前薪资、期望薪资、所在城市、技能、自我介绍
- **结构化简历 (resume.structured_data)** 包含：基本信息（与用户资料重复）、教育经历、工作经历、专业技能、项目经验、证书荣誉
- **重复字段**：真实姓名、性别、生日、头像、学历、工作年限、当前薪资、期望薪资、所在城市、技能、自我介绍

#### 接口设计问题
- 普通简历接口和结构化简历接口分离，缺乏统一管理
- 用户资料和简历数据同步机制不明确
- 附件简历和结构化简历缺乏关联关系

#### 功能需求不满足
- 需要支持基本信息设置
- 需要支持附件简历+结构化简历模式
- 需要支持个人资料信息展示

### 1.2 现有表结构分析

#### user_profile 表字段
- 基本信息：real_name, gender, birthday, avatar, education, work_years, current_salary, expected_salary, city, skills, self_intro
- 求职偏好：preferred_cities, job_types, industries, work_mode, job_status
- 统计信息：job_apply_count, interview_count, offer_count

#### resume 表字段
- 基础信息：title, content, file_url, file_type, is_default, privacy_level
- 统计信息：view_count, download_count, share_count
- 结构化数据：structured_data (JSON格式)
- 显示设置：template_style, color_scheme, font_family, show_photo, show_salary

## 2. 重构方案设计

### 2.1 核心设计原则

1. **单一数据源**：用户基本信息统一存储在 user_profile 表中
2. **数据同步机制**：简历从用户资料自动同步基础信息
3. **灵活扩展**：支持两种简历类型（附件简历、结构化简历）
4. **权限控制**：简历隐私级别与用户资料权限分离

### 2.2 数据模型重构

#### 2.2.1 用户资料表 (user_profile) - 保持不变
作为用户基础信息的唯一来源，包含：
- 个人基本信息
- 求职偏好
- 统计信息

#### 2.2.2 简历表 (resume) - 优化设计
```sql
-- 优化后的简历表结构
ALTER TABLE `resume` ADD COLUMN `resume_type` TINYINT NOT NULL DEFAULT 1 COMMENT '简历类型：1-附件简历，2-结构化简历';
ALTER TABLE `resume` ADD COLUMN `sync_profile_data` TINYINT NOT NULL DEFAULT 1 COMMENT '是否同步用户资料：0-否，1-是';
ALTER TABLE `resume` ADD COLUMN `last_sync_time` DATETIME COMMENT '最后同步时间';
ALTER TABLE `resume` MODIFY `structured_data` TEXT COMMENT '结构化简历数据（JSON格式，包含教育经历、工作经历等）';
```

#### 2.2.3 简历显示设置优化
保留简历表中的显示设置字段，用于控制简历的展示样式：
- `template_style` - 模板样式
- `color_scheme` - 配色方案  
- `font_family` - 字体
- `show_photo` - 是否显示照片
- `show_salary` - 是否显示薪资

### 2.3 接口设计重构

#### 2.3.1 统一简历管理接口

**基础路径**: `/api/resumes`

| 方法 | 路径 | 功能 | 说明 |
|------|------|------|------|
| GET | `/` | 分页查询简历列表 | 支持按类型、隐私级别筛选 |
| POST | `/` | 创建简历 | 支持附件简历、结构化简历 |
| GET | `/{id}` | 获取简历详情 | 根据类型返回不同格式 |
| PUT | `/{id}` | 更新简历 | 支持部分更新 |
| DELETE | `/{id}` | 删除简历 | 软删除或硬删除 |
| POST | `/{id}/set-default` | 设置默认简历 | 每个用户只能有一个默认简历 |

#### 2.3.2 简历类型支持

**1. 附件简历 (Attachment Resume)**
- **主要用途**：上传已有的简历文件（PDF、Word等）
- **适用场景**：
  - 用户已有现成的简历文件
  - 需要快速上传简历开始求职
  - 简历内容格式复杂，不适合在线编辑
- **数据存储**：主要存储文件信息，可关联结构化数据作为补充
- **展示方式**：下载文件 + 基础信息展示

**2. 结构化简历 (Structured Resume)**
- **主要用途**：在线创建和编辑简历
- **适用场景**：
  - 用户需要在线创建新简历
  - 需要灵活编辑和更新简历内容
  - 希望简历内容可以被系统分析和推荐
- **数据存储**：完整的结构化数据存储
- **展示方式**：完整的在线简历展示，支持模板样式

**用户选择环境**
- **简历创建页面**：提供明确的类型选择界面
- **引导说明**：
  - "上传附件简历"：适合已有简历文件的用户
  - "创建在线简历"：适合需要在线编辑和管理的用户
- **默认推荐**：根据用户场景推荐合适的类型

#### 2.3.3 数据初始化机制

**初始化填充**
- 创建简历时，自动从用户资料同步基础信息作为初始值
- 用户可以在简历中独立编辑这些基础信息
- 简历创建后，基础信息与用户资料独立，互不影响

**个性化定制**
- 每份简历的基础信息可以独立编辑
- 用户可以为不同简历设置不同的基础信息（如不同岗位使用不同的自我介绍）
- 简历中的基础信息存储在 `structured_data.basicInfo` 中，与用户资料分离

**设计原则**
- **简历独立性**：每份简历的基础信息独立存储，不受用户资料变更影响
- **初始化便利**：创建简历时自动填充，避免重复输入
- **个性化支持**：支持为不同简历定制不同的基础信息

### 2.4 核心业务流程

#### 2.4.1 创建简历流程
```
1. 用户选择简历类型
2. 系统自动同步用户基础信息
3. 用户补充简历特定信息
4. 保存简历数据
5. 返回简历详情
```

#### 2.4.2 数据同步流程
```
1. 检查简历同步设置
2. 从user_profile获取最新数据
3. 更新resume.structured_data中的基础信息
4. 记录同步时间
5. 返回同步结果
```

#### 2.4.3 简历展示流程
```
1. 根据简历类型组装数据
2. 附件简历：返回文件信息 + 基础信息
3. 结构化简历：返回完整结构化数据
4. 应用显示设置
```

## 3. 详细设计

### 3.1 数据结构设计

#### 3.1.1 简历实体扩展
```java
@Entity
@Table(name = "resume")
public class Resume extends BaseEntity {
    // 现有字段...
    
    @Column(name = "resume_type", nullable = false)
    private Integer resumeType = 1; // 1-附件简历，2-结构化简历
    
    @Column(name = "sync_profile_data", nullable = false)
    private Boolean syncProfileData = true;
    
    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime;
}
```

### 3.2 接口详细设计

#### 3.2.1 创建简历请求
```json
{
  "userId": 1,
  "title": "Java开发工程师简历",
  "resumeType": 2, // 1-附件简历，2-结构化简历
  "fileUrl": "/files/resume.pdf", // 附件简历使用
  "fileType": "pdf", // 附件简历使用
  "structuredData": {
    "basicInfo": {}, // 从用户资料同步，可覆盖
    "educations": [...],
    "workExperiences": [...],
    "skills": [...],
    "projects": [...],
    "certificates": [...]
  },
  "settings": {
    "isDefault": false,
    "privacyLevel": 1,
    "syncProfileData": true,
    "templateStyle": "modern",
    "showPhoto": true,
    "showSalary": false
  }
}
```

#### 3.2.2 简历详情响应
```json
{
  "id": 1,
  "userId": 1,
  "title": "Java开发工程师简历",
  "resumeType": 2,
  "fileUrl": null,
  "fileType": null,
  "isDefault": true,
  "privacyLevel": 1,
  "syncProfileData": true,
  "lastSyncTime": "2025-11-26T10:00:00",
  "user": {
    "id": 1,
    "username": "zhangsan",
    "realName": "张三",
    "avatar": "/avatars/user1.jpg"
  },
  "basicInfo": {
    "realName": "张三",
    "gender": 1,
    "birthday": "1995-12-10",
    "phone": "138****8000",
    "email": "zh****@example.com",
    "avatar": "/avatars/user1.jpg",
    "city": "北京",
    "education": "本科",
    "workYears": 3,
    "currentSalary": 15000.0,
    "expectedSalary": 20000.0,
    "selfIntro": "5年Java开发经验..."
  },
  "educations": [...],
  "workExperiences": [...],
  "skills": [...],
  "projects": [...],
  "certificates": [...],
  "settings": {
    "templateStyle": "modern",
    "colorScheme": "blue",
    "fontFamily": "Arial",
    "showPhoto": true,
    "showSalary": false
  },
  "stats": {
    "viewCount": 45,
    "downloadCount": 12,
    "shareCount": 3
  }
}
```

### 3.3 服务层设计

#### 3.3.1 简历服务接口
```java
public interface ResumeService {
    // 创建简历
    Resume createResume(ResumeCreateDTO createDTO);
    
    // 更新简历
    Resume updateResume(Long resumeId, ResumeUpdateDTO updateDTO);
    
    // 获取简历详情
    ResumeDetailDTO getResumeDetail(Long resumeId);
    
    // 设置默认简历
    Resume setDefaultResume(Long resumeId);
    
    // 分页查询
    Page<ResumeListDTO> listResumes(ResumeQueryDTO queryDTO, Pageable pageable);
}
```

#### 3.3.2 数据初始化服务
```java
@Service
public class ResumeInitService {
    
    /**
     * 初始化简历基础信息
     */
    public StructuredResumeData initBasicInfoFromProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在"));
        
        return buildBasicInfoFromProfile(userProfile);
    }
    
    /**
     * 构建基础信息
     */
    private BasicInfo buildBasicInfoFromProfile(UserProfile profile) {
        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setRealName(profile.getRealName());
        basicInfo.setGender(profile.getGender());
        basicInfo.setBirthday(profile.getBirthday());
        basicInfo.setAvatar(profile.getAvatar());
        basicInfo.setEducation(profile.getEducation());
        basicInfo.setWorkYears(profile.getWorkYears());
        basicInfo.setCurrentSalary(profile.getCurrentSalary());
        basicInfo.setExpectedSalary(profile.getExpectedSalary());
        basicInfo.setCity(profile.getCity());
        basicInfo.setSelfIntro(profile.getSelfIntro());
        return basicInfo;
    }
}
```

## 4. 实施计划

### 4.1 第一阶段：数据模型迁移
1. 修改resume表结构，添加新字段
2. 数据迁移脚本：为现有简历设置resume_type

### 4.2 第二阶段：服务层重构
1. 实现统一简历服务
2. 实现数据初始化服务

### 4.3 第三阶段：接口重构
1. 统一简历管理接口

### 4.4 第四阶段：前端适配
1. 简历创建页面支持两种类型
2. 简历编辑页面支持个性化定制
3. 简历展示页面支持显示设置

## 5. 优势与收益

### 5.1 技术优势
- **数据一致性**：消除用户资料和简历数据的冗余
- **扩展性**：支持两种简历类型，易于维护
- **维护性**：统一接口设计，降低维护成本

### 5.2 业务优势
- **用户体验**：支持灵活的简历管理方式
- **初始化便利**：创建简历时自动填充基础信息
- **个性化定制**：支持为不同简历设置不同的基础信息
- **显示设置**：提供个性化的简历展示

### 5.3 性能优化
- **数据分离**：基础信息与详细经历分离存储
- **缓存策略**：热门简历数据缓存
- **懒加载**：大字段数据按需加载

## 6. 风险评估与应对

### 6.1 技术风险
- **数据迁移风险**：现有简历数据迁移可能失败
  - 应对：分批次迁移，提供回滚方案
- **接口兼容性**：前端需要适配新接口
  - 应对：提供版本兼容，逐步迁移

### 6.2 业务风险
- **用户接受度**：新的简历类型可能不被接受
  - 应对：保留原有功能，提供引导说明

## 7. 总结

本重构方案通过统一数据源、优化接口设计、支持两种简历类型，解决了当前简历模块的数据冗余和功能不足问题。方案实施后将提供更灵活、更一致的简历管理体验，同时为未来的功能扩展奠定良好基础。

方案核心亮点：
1. **数据一致性**：用户资料作为唯一数据源
2. **类型灵活性**：支持附件、结构化两种简历类型
3. **初始化便利**：创建简历时自动填充基础信息
4. **个性化定制**：支持为不同简历设置不同的基础信息
5. **显示设置**：可定制的简历展示样式
