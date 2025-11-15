# 数据分析模块 (AnalyticsController)

**基础路径**: `/api/analytics`

## 接口列表

### 获取平台概览数据
- **URL**: `GET /overview`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "totalUsers": 1000,
      "totalJobs": 500,
      "totalResumes": 800,
      "totalApplies": 2000,
      "activeUsers": 300,
      "newUsersToday": 50,
      "newJobsToday": 20,
      "newAppliesToday": 100
    }
  }
  ```
- **功能**: 获取平台概览数据

### 获取用户增长趋势
- **URL**: `GET /users/growth`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
  | granularity | 查询 | String | 否 | 粒度：day/week/month，默认day |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "granularity": "day",
      "growthData": [
        {
          "date": "2025-11-01",
          "newUsers": 50,
          "totalUsers": 1000,
          "growthRate": 0.05
        }
      ],
      "summary": {
        "totalGrowth": 500,
        "averageDailyGrowth": 16.7,
        "peakGrowth": 80
      }
    }
  }
  ```
- **功能**: 获取用户增长趋势

### 获取岗位增长趋势
- **URL**: `GET /jobs/growth`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
  | granularity | 查询 | String | 否 | 粒度：day/week/month，默认day |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "granularity": "day",
      "growthData": [
        {
          "date": "2025-11-01",
          "newJobs": 20,
          "totalJobs": 500,
          "growthRate": 0.04
        }
      ],
      "summary": {
        "totalGrowth": 150,
        "averageDailyGrowth": 5,
        "peakGrowth": 35
      }
    }
  }
  ```
- **功能**: 获取岗位增长趋势

### 获取申请增长趋势
- **URL**: `GET /applies/growth`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
  | granularity | 查询 | String | 否 | 粒度：day/week/month，默认day |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "granularity": "day",
      "growthData": [
        {
          "date": "2025-11-01",
          "newApplies": 100,
          "totalApplies": 2000,
          "growthRate": 0.05
        }
      ],
      "summary": {
        "totalGrowth": 600,
        "averageDailyGrowth": 20,
        "peakGrowth": 150
      }
    }
  }
  ```
- **功能**: 获取申请增长趋势

### 获取用户分布统计
- **URL**: `GET /users/distribution`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | dimension | 查询 | String | 否 | 维度：userType/city/education，默认userType |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "dimension": "userType",
      "distribution": [
        {
          "category": "求职者",
          "count": 800,
          "percentage": 0.8
        },
        {
          "category": "招聘者",
          "count": 200,
          "percentage": 0.2
        }
      ],
      "totalCount": 1000
    }
  }
  ```
- **功能**: 获取用户分布统计

### 获取岗位分布统计
- **URL**: `GET /jobs/distribution`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | dimension | 查询 | String | 否 | 维度：category/city/jobType，默认category |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "dimension": "category",
      "distribution": [
        {
          "category": "技术开发",
          "count": 200,
          "percentage": 0.4
        },
        {
          "category": "产品经理",
          "count": 100,
          "percentage": 0.2
        },
        {
          "category": "运营",
          "count": 80,
          "percentage": 0.16
        }
      ],
      "totalCount": 500
    }
  }
  ```
- **功能**: 获取岗位分布统计

### 获取申请状态统计
- **URL**: `GET /applies/status`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "statusDistribution": [
        {
          "status": "已申请",
          "count": 800,
          "percentage": 0.4
        },
        {
          "status": "已查看",
          "count": 600,
          "percentage": 0.3
        },
        {
          "status": "已面试",
          "count": 300,
          "percentage": 0.15
        },
        {
          "status": "已录用",
          "count": 200,
          "percentage": 0.1
        },
        {
          "status": "已拒绝",
          "count": 100,
          "percentage": 0.05
        }
      ],
      "totalCount": 2000
    }
  }
  ```
- **功能**: 获取申请状态统计

### 获取热门岗位统计
- **URL**: `GET /jobs/popular`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 限制数量，默认10 |
  | days | 查询 | Integer | 否 | 统计天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 7,
      "popularJobs": [
        {
          "jobId": 1,
          "title": "Java开发工程师",
          "companyName": "科技公司",
          "viewCount": 500,
          "applyCount": 50,
          "favoriteCount": 25,
          "popularityScore": 0.95
        }
      ],
      "totalCount": 10
    }
  }
  ```
- **功能**: 获取热门岗位统计

### 获取热门分类统计
- **URL**: `GET /categories/popular`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 限制数量，默认10 |
  | days | 查询 | Integer | 否 | 统计天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 7,
      "popularCategories": [
        {
          "categoryId": 1,
          "categoryName": "技术开发",
          "jobCount": 200,
          "applyCount": 800,
          "popularityScore": 0.92
        }
      ],
      "totalCount": 10
    }
  }
  ```
- **功能**: 获取热门分类统计

### 获取热门城市统计
- **URL**: `GET /cities/popular`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 限制数量，默认10 |
  | days | 查询 | Integer | 否 | 统计天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 7,
      "popularCities": [
        {
          "city": "北京",
          "jobCount": 150,
          "userCount": 300,
          "applyCount": 600,
          "popularityScore": 0.88
        }
      ],
      "totalCount": 10
    }
  }
  ```
- **功能**: 获取热门城市统计

### 获取薪资分布统计
- **URL**: `GET /salaries/distribution`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | categoryId | 查询 | Long | 否 | 分类ID |
  | city | 查询 | String | 否 | 城市 |
  | jobType | 查询 | Integer | 否 | 岗位类型 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "categoryId": 1,
      "city": "北京",
      "salaryDistribution": [
        {
          "range": "10k-15k",
          "count": 50,
          "percentage": 0.25
        },
        {
          "range": "15k-20k",
          "count": 80,
          "percentage": 0.4
        },
        {
          "range": "20k-25k",
          "count": 40,
          "percentage": 0.2
        },
        {
          "range": "25k+",
          "count": 30,
          "percentage": 0.15
        }
      ],
      "averageSalary": 18000,
      "medianSalary": 17500,
      "totalCount": 200
    }
  }
  ```
- **功能**: 获取薪资分布统计

### 获取用户活跃度统计
- **URL**: `GET /users/activity`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认7 |
  | granularity | 查询 | String | 否 | 粒度：day/hour，默认day |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 7,
      "granularity": "day",
      "activityData": [
        {
          "date": "2025-11-01",
          "activeUsers": 300,
          "loginCount": 450,
          "pageViews": 1500,
          "avgSessionDuration": 8.5
        }
      ],
      "summary": {
        "avgDailyActiveUsers": 280,
        "avgDailyLoginCount": 420,
        "avgDailyPageViews": 1400,
        "avgSessionDuration": 8.2
      }
    }
  }
  ```
- **功能**: 获取用户活跃度统计

### 获取转化率统计
- **URL**: `GET /conversion`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "conversionRates": {
        "viewToApply": 0.05,
        "applyToInterview": 0.15,
        "interviewToOffer": 0.25,
        "offerToAccept": 0.8
      },
      "funnelData": {
        "views": 10000,
        "applies": 500,
        "interviews": 75,
        "offers": 19,
        "acceptances": 15
      },
      "overallConversionRate": 0.0015
    }
  }
  ```
- **功能**: 获取转化率统计

### 获取留存率统计
- **URL**: `GET /retention`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "retentionRates": {
        "day1": 0.65,
        "day7": 0.45,
        "day14": 0.35,
        "day30": 0.25
      },
      "cohortData": [
        {
          "cohort": "2025-11-01",
          "day1": 0.68,
          "day7": 0.48,
          "day14": 0.38,
          "day30": 0.28
        }
      ],
      "avgRetentionRate": 0.42
    }
  }
  ```
- **功能**: 获取留存率统计

### 获取漏斗分析
- **URL**: `GET /funnel`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "funnelStages": [
        {
          "stage": "访问",
          "count": 10000,
          "percentage": 1.0
        },
        {
          "stage": "浏览岗位",
          "count": 5000,
          "percentage": 0.5
        },
        {
          "stage": "查看详情",
          "count": 2000,
          "percentage": 0.2
        },
        {
          "stage": "提交申请",
          "count": 500,
          "percentage": 0.05
        },
        {
          "stage": "收到面试",
          "count": 75,
          "percentage": 0.0075
        },
        {
          "stage": "录用成功",
          "count": 15,
          "percentage": 0.0015
        }
      ],
      "dropOffRates": {
        "visitToBrowse": 0.5,
        "browseToDetail": 0.6,
        "detailToApply": 0.75,
        "applyToInterview": 0.85,
        "interviewToOffer": 0.8
      }
    }
  }
  ```
- **功能**: 获取漏斗分析

### 获取用户行为分析
- **URL**: `GET /users/behavior`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 否 | 用户ID |
  | days | 查询 | Integer | 否 | 统计天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "days": 7,
      "behaviorStats": {
        "totalLogins": 15,
        "totalPageViews": 80,
        "totalJobViews": 25,
        "totalApplies": 5,
        "totalFavorites": 8
      },
      "dailyActivity": [
        {
          "date": "2025-11-01",
          "logins": 2,
          "pageViews": 12,
          "jobViews": 4,
          "applies": 1,
          "favorites": 2
        }
      ],
      "preferences": {
        "preferredCategories": ["技术开发", "后端开发"],
        "preferredCities": ["北京", "上海"],
        "preferredSalaryRange": "15k-25k"
      }
    }
  }
  ```
- **功能**: 获取用户行为分析

### 获取岗位效果分析
- **URL**: `GET /jobs/performance`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 查询 | Long | 否 | 岗位ID |
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "jobId": 1,
      "days": 30,
      "performanceMetrics": {
        "totalViews": 500,
        "totalApplies": 50,
        "totalFavorites": 25,
        "applyRate": 0.1,
        "favoriteRate": 0.05,
        "avgViewDuration": 45.5
      },
      "dailyPerformance": [
        {
          "date": "2025-11-01",
          "views": 20,
          "applies": 2,
          "favorites": 1,
          "applyRate": 0.1
        }
      ],
      "comparison": {
        "categoryAvgApplyRate": 0.08,
        "cityAvgApplyRate": 0.09,
        "performanceScore": 0.85
      }
    }
  }
  ```
- **功能**: 获取岗位效果分析

### 获取推荐效果分析
- **URL**: `GET /recommendations/performance`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 统计天数，默认30 |
  | algorithm | 查询 | String | 否 | 推荐算法 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 30,
      "algorithm": "hybrid",
      "performanceMetrics": {
        "totalRecommendations": 1000,
        "totalClicks": 250,
        "totalApplies": 80,
        "clickRate": 0.25,
        "applyRate": 0.08,
        "precision": 0.75,
        "recall": 0.68,
        "coverage": 0.82
      },
      "algorithmComparison": {
        "hybrid": {
          "clickRate": 0.25,
          "applyRate": 0.08,
          "precision": 0.75
        },
        "content": {
          "clickRate": 0.22,
          "applyRate": 0.07,
          "precision": 0.72
        },
        "collaborative": {
          "clickRate": 0.20,
          "applyRate": 0.06,
          "precision": 0.70
        }
      }
    }
  }
  ```
- **功能**: 获取推荐效果分析

### 获取实时数据
- **URL**: `GET /realtime`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "currentTime": "2025-11-08T22:00:00",
      "activeUsers": 150,
      "newUsersToday": 50,
      "newJobsToday": 20,
      "newAppliesToday": 100,
      "onlineUsers": 80,
      "apiRequests": 1200,
      "systemLoad": 0.65
    }
  }
  ```
- **功能**: 获取实时数据

### 获取数据报表
- **URL**: `GET /reports`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 查询 | String | 否 | 报表类型：daily/weekly/monthly，默认daily |
  | date | 查询 | String | 否 | 日期，格式：yyyy-MM-dd |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "reportType": "daily",
      "date": "2025-11-08",
      "metrics": {
        "newUsers": 50,
        "newJobs": 20,
        "newApplies": 100,
        "activeUsers": 300,
        "pageViews": 1500,
        "conversionRate": 0.05
      },
      "trends": {
        "userGrowth": 0.05,
        "jobGrowth": 0.04,
        "applyGrowth": 0.06
      },
      "insights": [
        "用户活跃度较昨日增长5%",
        "技术开发类岗位申请量最高",
        "北京地区用户转化率最高"
      ]
    }
  }
  ```
- **功能**: 获取数据报表

### 生成自定义报表
- **URL**: `POST /reports/custom`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | name | 请求体 | String | 是 | 报表名称 |
  | metrics | 请求体 | List<String> | 是 | 指标列表 |
  | dimensions | 请求体 | List<String> | 否 | 维度列表 |
  | filters | 请求体 | Object | 否 | 筛选条件 |
  | dateRange | 请求体 | Object | 否 | 日期范围 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "生成成功",
    "data": {
      "reportId": 1001,
      "name": "用户活跃度分析",
      "metrics": ["activeUsers", "newUsers", "userRetention"],
      "dimensions": ["userType", "city"],
      "dateRange": {
        "startDate": "2025-11-01",
        "endDate": "2025-11-08"
      },
      "reportData": {
        "summary": {
          "totalActiveUsers": 2100,
          "totalNewUsers": 350,
          "avgRetentionRate": 0.42
        },
        "breakdown": [
          {
            "userType": "求职者",
            "city": "北京",
            "activeUsers": 600,
            "newUsers": 100,
            "retentionRate": 0.45
          }
        ]
      },
      "generatedAt": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 生成自定义报表

### 导出数据
- **URL**: `GET /export`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 查询 | String | 是 | 数据类型：users/jobs/applies |
  | format | 查询 | String | 否 | 格式：csv/excel/json，默认csv |
  | startDate | 查询 | String | 否 | 开始日期 |
  | endDate | 查询 | String | 否 | 结束日期 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "导出成功",
    "data": {
      "type": "users",
      "format": "csv",
      "startDate": "2025-11-01",
      "endDate": "2025-11-08",
      "fileUrl": "/downloads/users_20251101_20251108.csv",
      "fileSize": "2.5MB",
      "recordCount": 1000,
      "exportedAt": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 导出数据

### 获取数据质量报告
- **URL**: `GET /quality`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "overallScore": 0.88,
      "dimensions": {
        "completeness": 0.92,
        "accuracy": 0.85,
        "consistency": 0.90,
        "timeliness": 0.95
      },
      "issues": [
        {
          "type": "missing_data",
          "description": "用户教育背景信息缺失率15%",
          "severity": "medium",
          "affectedRecords": 150
        },
        {
          "type": "inconsistent_data",
          "description": "岗位薪资范围格式不一致",
          "severity": "low",
          "affectedRecords": 50
        }
      ],
      "recommendations": [
        "完善用户信息收集流程",
        "统一数据录入标准",
        "建立数据质量监控机制"
      ]
    }
  }
  ```
- **功能**: 获取数据质量报告

### 获取数据异常检测
- **URL**: `GET /anomalies`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 检测天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "days": 7,
      "anomalies": [
        {
          "metric": "newUsers",
          "date": "2025-11-05",
          "expectedValue": 45,
          "actualValue": 120,
          "deviation": 2.67,
          "severity": "high",
          "description": "新用户注册量异常激增"
        },
        {
          "metric": "applyRate",
          "date": "2025-11-06",
          "expectedValue": 0.05,
          "actualValue": 0.01,
          "deviation": -0.8,
          "severity": "medium",
          "description": "申请转化率异常下降"
        }
      ],
      "summary": {
        "totalAnomalies": 5,
        "highSeverity": 1,
        "mediumSeverity": 3,
        "lowSeverity": 1
      }
    }
  }
  ```
- **功能**: 获取数据异常检测

### 获取数据预测
- **URL**: `GET /predictions`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | metric | 查询 | String | 是 | 预测指标：users/jobs/applies |
  | days | 查询 | Integer | 否 | 预测天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "metric": "users",
      "days": 30,
      "predictions": [
        {
          "date": "2025-11-09",
          "predictedValue": 52,
          "confidenceInterval": [45, 59],
          "trend": "up"
        }
      ],
      "modelInfo": {
        "modelType": "ARIMA",
        "accuracy": 0.85,
        "lastTrainingDate": "2025-11-08"
      },
      "insights": [
        "预计用户增长将保持稳定上升趋势",
        "周末用户注册量通常较低",
        "促销活动可能带来额外增长"
      ]
    }
  }
  ```
- **功能**: 获取数据预测

### 获取数据洞察
- **URL**: `GET /insights`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 查询 | String | 否 | 洞察类型：trends/patterns/anomalies，默认trends |
  | days | 查询 | Integer | 否 | 分析天数，默认30 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "type": "trends",
      "days": 30,
      "insights": [
        {
          "title": "用户活跃度上升趋势",
          "description": "过去30天用户活跃度增长15%，主要增长来自移动端",
          "impact": "high",
          "confidence": 0.92,
          "recommendation": "加强移动端功能优化"
        },
        {
          "title": "技术开发岗位需求激增",
          "description": "技术开发类岗位申请量增长25%，Java和Python岗位最受欢迎",
          "impact": "medium",
          "confidence": 0.88,
          "recommendation": "增加技术开发类岗位推广"
        }
      ],
      "summary": {
        "totalInsights": 8,
        "highImpact": 3,
        "mediumImpact": 4,
        "lowImpact": 1
      }
    }
  }
  ```
- **功能**: 获取数据洞察

### 获取数据对比
- **URL**: `GET /comparison`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | metric | 查询 | String | 是 | 对比指标 |
  | period1 | 查询 | String | 是 | 期间1，格式：yyyy-MM-dd |
  | period2 | 查询 | String | 是 | 期间2，格式：yyyy-MM-dd |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "metric": "activeUsers",
      "period1": "2025-11-01",
      "period2": "2025-11-08",
      "comparison": {
        "period1Value": 280,
        "period2Value": 300,
        "change": 20,
        "changePercentage": 0.071,
        "trend": "up"
      },
      "dailyComparison": [
        {
          "date": "2025-11-01",
          "period1Value": 280,
          "period2Value": 300,
          "change": 20
        }
      ],
      "insights": [
        "用户活跃度整体呈上升趋势",
        "周末活跃度增长更为明显",
        "移动端用户增长贡献较大"
      ]
    }
  }
  ```
- **功能**: 获取数据对比

### 获取数据仪表板
- **URL**: `GET /dashboard`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 查询 | String | 否 | 仪表板类型：overview/users/jobs，默认overview |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "type": "overview",
      "widgets": [
        {
          "type": "metric",
          "title": "总用户数",
          "value": 1000,
          "trend": "up",
          "change": 50
        },
        {
          "type": "metric",
          "title": "总岗位数",
          "value": 500,
          "trend": "up",
          "change": 20
        },
        {
          "type": "chart",
          "title": "用户增长趋势",
          "data": [
            {
              "date": "2025-11-01",
              "value": 950
            }
          ]
        }
      ],
      "lastUpdated": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 获取数据仪表板

### 获取数据API使用统计
- **URL**: `GET /api-usage`
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
      "usageStats": {
        "totalRequests": 8400,
        "avgDailyRequests": 1200,
        "peakRequests": 1800,
        "successRate": 0.98
      },
      "endpointUsage": [
        {
          "endpoint": "/api/analytics/overview",
          "requestCount": 2100,
          "avgResponseTime": 120
        },
        {
          "endpoint": "/api/analytics/users/growth",
          "requestCount": 1500,
          "avgResponseTime": 180
        }
      ],
      "userDistribution": [
        {
          "userType": "admin",
          "requestCount": 5000,
          "percentage": 0.6
        },
        {
          "userType": "recruiter",
          "requestCount": 2500,
          "percentage": 0.3
        }
      ]
    }
  }
  ```
- **功能**: 获取数据API使用统计

### 获取数据缓存状态
- **URL**: `GET /cache/status`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "cacheStats": {
        "totalEntries": 150,
        "hitRate": 0.85,
        "missRate": 0.15,
        "avgResponseTime": 45
      },
      "cacheTypes": [
        {
          "type": "overview",
          "entries": 10,
          "hitRate": 0.92,
          "size": "2MB"
        },
        {
          "type": "growth",
          "entries": 50,
          "hitRate": 0.88,
          "size": "5MB"
        }
      ],
      "memoryUsage": {
        "used": "15MB",
        "max": "50MB",
        "percentage": 0.3
      }
    }
  }
  ```
- **功能**: 获取数据缓存状态

### 清除数据缓存
- **URL**: `POST /cache/clear`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 请求体 | String | 否 | 缓存类型，默认all |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "清除成功",
    "data": {
      "clearedTypes": ["overview", "growth", "distribution"],
      "clearedEntries": 150,
      "clearedSize": "15MB",
      "clearedAt": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 清除数据缓存

## 使用示例

### 获取平台概览示例
```bash
curl -X GET "http://localhost:8080/api/analytics/overview?days=30" \
  -H "Authorization: Bearer {token}"
```

### 获取用户增长趋势示例
```bash
curl -X GET "http://localhost:8080/api/analytics/users/growth?days=30&granularity=day" \
  -H "Authorization: Bearer {token}"
```

### 获取热门岗位统计示例
```bash
curl -X GET "http://localhost:8080/api/analytics/jobs/popular?limit=10&days=7" \
  -H "Authorization: Bearer {token}"
```

### 导出数据示例
```bash
curl -X GET "http://localhost:8080/api/analytics/export?type=users&format=csv&startDate=2025-11-01&endDate=2025-11-08" \
  -H "Authorization: Bearer {token}" \
  --output users.csv
```

### 生成自定义报表示例
```bash
curl -X POST "http://localhost:8080/api/analytics/reports/custom" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "用户活跃度分析",
    "metrics": ["activeUsers", "newUsers", "userRetention"],
    "dimensions": ["userType", "city"],
    "dateRange": {
      "startDate": "2025-11-01",
      "endDate": "2025-11-08"
    }
  }'
```

## 注意事项

- 所有数据分析接口都需要有效的JWT Token进行认证
- 数据统计支持多种时间粒度：day/week/month
- 支持多维度数据分析：用户类型、城市、分类等
- 数据支持实时查询和历史查询
- 支持数据导出和自定义报表
- 数据分析包含趋势分析、分布分析、对比分析等
- 数据预测基于历史数据进行机器学习预测
- 数据洞察提供智能分析和建议
- 数据缓存提高查询性能，支持手动清除

[返回主文档](../docs/README.md)
