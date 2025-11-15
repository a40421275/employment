# 智能推荐模块 (RecommendationController)

**基础路径**: `/api/recommendations`

## 接口列表

### 获取岗位推荐
- **URL**: `GET /jobs`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
  | algorithm | 查询 | String | 否 | 推荐算法：content/collaborative/hybrid，默认hybrid |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "hybrid",
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.85,
          "reason": "基于您的技能和浏览历史推荐"
        }
      ]
    }
  }
  ```
- **功能**: 获取岗位推荐

### 获取简历推荐
- **URL**: `GET /resumes`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 查询 | Long | 是 | 岗位ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
  | algorithm | 查询 | String | 否 | 推荐算法：content/collaborative/hybrid，默认hybrid |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "jobId": 1,
      "algorithm": "hybrid",
      "recommendations": [
        {
          "resumeId": 1,
          "userId": 2,
          "userName": "张三",
          "title": "Java开发工程师简历",
          "education": "本科",
          "workExperience": 3,
          "score": 0.92,
          "reason": "技能匹配度高，经验丰富"
        }
      ]
    }
  }
  ```
- **功能**: 获取简历推荐

### 获取热门岗位推荐
- **URL**: `GET /jobs/hot`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
  | city | 查询 | String | 否 | 城市筛选 |
  | categoryId | 查询 | Long | 否 | 分类筛选 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "viewCount": 500,
          "applyCount": 50,
          "score": 0.95,
          "reason": "热门岗位，关注度高"
        }
      ]
    }
  }
  ```
- **功能**: 获取热门岗位推荐

### 获取相似岗位推荐
- **URL**: `GET /jobs/similar`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 查询 | Long | 是 | 岗位ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "sourceJobId": 1,
      "recommendations": [
        {
          "jobId": 2,
          "title": "Java高级开发工程师",
          "companyName": "科技公司",
          "salaryMin": 20000,
          "salaryMax": 30000,
          "workCity": "北京",
          "similarity": 0.88,
          "reason": "技能要求相似，薪资范围相近"
        }
      ]
    }
  }
  ```
- **功能**: 获取相似岗位推荐

### 获取个性化推荐
- **URL**: `GET /jobs/personalized`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.85,
          "reason": "基于您的浏览历史和技能匹配"
        }
      ]
    }
  }
  ```
- **功能**: 获取个性化岗位推荐

### 获取基于内容的推荐
- **URL**: `GET /jobs/content-based`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "content",
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.82,
          "reason": "技能匹配度高，岗位要求符合您的背景"
        }
      ]
    }
  }
  ```
- **功能**: 获取基于内容的岗位推荐

### 获取协同过滤推荐
- **URL**: `GET /jobs/collaborative`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "collaborative",
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.78,
          "reason": "与您相似的用户也喜欢这个岗位"
        }
      ]
    }
  }
  ```
- **功能**: 获取协同过滤岗位推荐

### 获取混合推荐
- **URL**: `GET /jobs/hybrid`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "hybrid",
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.85,
          "reason": "综合内容匹配和用户行为推荐"
        }
      ]
    }
  }
  ```
- **功能**: 获取混合算法岗位推荐

### 获取实时推荐
- **URL**: `GET /jobs/realtime`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "realtime",
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.83,
          "reason": "基于您最近的浏览行为推荐"
        }
      ]
    }
  }
  ```
- **功能**: 获取实时岗位推荐

### 获取冷启动推荐
- **URL**: `GET /jobs/cold-start`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 推荐数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "cold-start",
      "recommendations": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "salaryMin": 15000,
          "salaryMax": 25000,
          "workCity": "北京",
          "score": 0.75,
          "reason": "基于热门岗位和基础信息推荐"
        }
      ]
    }
  }
  ```
- **功能**: 获取冷启动岗位推荐

### 记录用户行为
- **URL**: `POST /behavior`
- **请求参数**:
  ```json
  {
    "userId": 1,
    "itemId": 1,
    "itemType": "job", // job/resume
    "behaviorType": "view", // view/apply/favorite/share
    "score": 1.0,
    "timestamp": "2025-11-08T12:00:00"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "记录成功",
    "data": {
      "userId": 1,
      "itemId": 1,
      "itemType": "job",
      "behaviorType": "view",
      "recordId": 1001
    }
  }
  ```
- **功能**: 记录用户行为数据

### 获取推荐解释
- **URL**: `GET /explain`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | jobId | 查询 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "jobId": 1,
      "explanations": [
        {
          "type": "skill_match",
          "description": "您的Java技能与岗位要求高度匹配",
          "score": 0.9
        },
        {
          "type": "experience_match",
          "description": "您的工作经验符合岗位要求",
          "score": 0.8
        },
        {
          "type": "similar_users",
          "description": "与您相似的用户也申请了这个岗位",
          "score": 0.7
        }
      ],
      "overallScore": 0.85
    }
  }
  ```
- **功能**: 获取推荐解释

### 获取推荐质量评估
- **URL**: `GET /quality`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 否 | 用户ID |
  | algorithm | 查询 | String | 否 | 推荐算法 |
  | days | 查询 | Integer | 否 | 评估天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "algorithm": "hybrid",
      "days": 30,
      "metrics": {
        "precision": 0.75,
        "recall": 0.68,
        "coverage": 0.82,
        "diversity": 0.65,
        "novelty": 0.58
      },
      "recommendationCount": 150,
      "clickCount": 45,
      "applyCount": 20
    }
  }
  ```
- **功能**: 获取推荐质量评估

### 训练推荐模型
- **URL**: `POST /train`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | algorithm | 请求体 | String | 否 | 训练算法，默认all |
  | force | 请求体 | Boolean | 否 | 强制重新训练，默认false |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "训练成功",
    "data": {
      "algorithm": "hybrid",
      "trainingTime": "2025-11-08T12:00:00",
      "status": "completed",
      "metrics": {
        "rmse": 0.12,
        "mae": 0.08,
        "trainingTimeSeconds": 120
      }
    }
  }
  ```
- **功能**: 训练推荐模型

### 获取模型状态
- **URL**: `GET /model/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | algorithm | 查询 | String | 否 | 算法名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "algorithm": "hybrid",
      "status": "trained",
      "lastTrainingTime": "2025-11-08T12:00:00",
      "modelVersion": "v1.2.3",
      "performance": {
        "precision": 0.75,
        "recall": 0.68,
        "coverage": 0.82
      }
    }
  }
  ```
- **功能**: 获取模型训练状态

### 获取推荐统计
- **URL**: `GET /stats`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 7,
      "totalRecommendations": 1000,
      "totalClicks": 250,
      "totalApplies": 80,
      "clickRate": 0.25,
      "applyRate": 0.08,
      "algorithms": {
        "hybrid": {
          "count": 600,
          "clickRate": 0.28,
          "applyRate": 0.09
        },
        "content": {
          "count": 300,
          "clickRate": 0.22,
          "applyRate": 0.07
        },
        "collaborative": {
          "count": 100,
          "clickRate": 0.20,
          "applyRate": 0.06
        }
      }
    }
  }
  ```
- **功能**: 获取推荐统计

### 获取用户画像
- **URL**: `GET /user/{userId}/profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "skills": ["Java", "Spring", "MySQL"],
      "interests": ["后端开发", "分布式系统"],
      "preferences": {
        "salaryMin": 15000,
        "salaryMax": 25000,
        "workCity": "北京",
        "companySize": "中型"
      },
      "behaviorStats": {
        "totalViews": 50,
        "totalApplies": 10,
        "totalFavorites": 5
      },
      "lastUpdateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取用户画像

### 更新用户画像
- **URL**: `PUT /user/{userId}/profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | profileData | 请求体 | Object | 是 | 画像数据 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "userId": 1,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新用户画像

### 获取物品画像
- **URL**: `GET /item/{itemId}/profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | itemId | 路径 | Long | 是 | 物品ID |
  | itemType | 查询 | String | 是 | 物品类型：job/resume |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "itemId": 1,
      "itemType": "job",
      "title": "Java开发工程师",
      "skills": ["Java", "Spring", "MySQL"],
      "requirements": ["本科", "3年经验"],
      "features": {
        "salaryLevel": "中高",
        "companyType": "科技",
        "workLocation": "北京"
      },
      "popularity": {
        "viewCount": 500,
        "applyCount": 50,
        "favoriteCount": 25
      },
      "lastUpdateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取物品画像

### 更新物品画像
- **URL**: `PUT /item/{itemId}/profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | itemId | 路径 | Long | 是 | 物品ID |
  | itemType | 查询 | String | 是 | 物品类型：job/resume |
  | profileData | 请求体 | Object | 是 | 画像数据 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "itemId": 1,
      "itemType": "job",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新物品画像

### 获取推荐配置
- **URL**: `GET /config`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "algorithms": {
        "hybrid": {
          "enabled": true,
          "weight": 0.6
        },
        "content": {
          "enabled": true,
          "weight": 0.3
        },
        "collaborative": {
          "enabled": true,
          "weight": 0.1
        }
      },
      "limits": {
        "maxRecommendations": 20,
        "minScore": 0.3
      },
      "training": {
        "interval": "24h",
        "batchSize": 1000
      }
    }
  }
  ```
- **功能**: 获取推荐系统配置

### 更新推荐配置
- **URL**: `PUT /config`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configData | 请求体 | Object | 是 | 配置数据 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新推荐系统配置

### 重置推荐模型
- **URL**: `POST /model/reset`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | algorithm | 请求体 | String | 否 | 算法名称，默认all |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "重置成功",
    "data": {
      "algorithm": "all",
      "resetTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 重置推荐模型

### 获取推荐日志
- **URL**: `GET /logs`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | userId | 查询 | Long | 否 | 用户ID |
  | algorithm | 查询 | String | 否 | 算法名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "algorithm": "hybrid",
          "recommendations": [1, 2, 3],
          "timestamp": "2025-11-08T12:00:00",
          "clickedItems": [1],
          "appliedItems": []
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 获取推荐日志

## 使用示例

### 获取岗位推荐示例
```bash
curl -X GET "http://localhost:8080/api/recommendations/jobs?userId=1&limit=10&algorithm=hybrid" \
  -H "Authorization: Bearer {token}"
```

### 记录用户行为示例
```bash
curl -X POST "http://localhost:8080/api/recommendations/behavior" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "itemId": 1,
    "itemType": "job",
    "behaviorType": "view",
    "score": 1.0,
    "timestamp": "2025-11-08T12:00:00"
  }'
```

### 训练推荐模型示例
```bash
curl -X POST "http://localhost:8080/api/recommendations/train" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "algorithm": "hybrid",
    "force": true
  }'
```

### 获取推荐解释示例
```bash
curl -X GET "http://localhost:8080/api/recommendations/explain?userId=1&jobId=1" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有推荐系统接口都需要有效的JWT Token进行认证
- 推荐算法支持：content（基于内容）、collaborative（协同过滤）、hybrid（混合算法）
- 用户行为类型：view（浏览）、apply（申请）、favorite（收藏）、share（分享）
- 推荐质量评估包括准确率、召回率、覆盖率等指标
- 推荐模型支持在线训练和离线训练
- 用户画像和物品画像用于提高推荐准确性
- 推荐系统支持冷启动场景
- 推荐结果包含推荐分数和推荐原因

[返回主文档](../docs/README.md)
