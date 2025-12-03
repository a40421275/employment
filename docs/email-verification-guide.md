# 邮件验证码功能使用指南

## 功能概述

根据message-management.md文档，我们实现了完整的邮件验证码功能，用于找回密码流程。该功能包括：

1. 发送邮件验证码
2. 验证邮件验证码
3. 重置密码

## 接口说明

### 发送邮件验证码

**接口**: `POST /api/messages/email-verification`

**请求参数**:
```json
{
  "email": "user@example.com",
  "codeType": "reset_password",
  "businessScene": "找回密码",
  "expireMinutes": 30,
  "codeLength": 6
}
```

**响应**:
```json
{
  "code": 200,
  "message": "邮件验证码发送成功",
  "data": {
    "success": true,
    "message": "邮件验证码发送成功",
    "verificationCode": "123456",
    "taskId": "email_task_123456",
    "expireMinutes": 30,
    "sendTime": "2025-11-22T16:51:00"
  }
}
```

### 验证邮件验证码

**接口**: `POST /api/messages/email-verification/verify`

**请求参数**:
```json
{
  "email": "user@example.com",
  "code": "123456",
  "codeType": "reset_password"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "验证码验证成功",
  "data": null
}
```

### 重置密码

**接口**: `POST /api/users/reset-password`

**请求参数**:
```json
{
  "email": "user@example.com",
  "verificationCode": "123456",
  "newPassword": "NewPassword123"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "密码重置成功",
  "data": null
}
```

## 前端实现

### 忘记密码页面流程

1. **步骤1：验证邮箱**
   - 用户输入邮箱地址
   - 点击"发送验证码"按钮
   - 输入收到的6位验证码
   - 点击"下一步"验证验证码

2. **步骤2：重置密码**
   - 用户输入新密码（需满足密码规则）
   - 确认新密码
   - 点击"重置密码"按钮

### 密码规则

- 至少8位字符
- 包含字母
- 包含数字

### 前端代码结构

#### API接口 (utils/api.js)
```javascript
// 消息管理接口
message: {
  // 发送邮件验证码
  sendEmailVerificationCode: (data) => request({ url: '/messages/email-verification', method: 'POST', data }),
  // 验证邮件验证码
  verifyEmailVerificationCode: (data) => request({ url: '/messages/email-verification/verify', method: 'POST', data }),
  // 检查验证码是否有效
  checkEmailVerificationCode: (data) => request({ url: '/messages/email-verification/valid', method: 'GET', data }),
  // 获取验证码剩余时间
  getEmailVerificationRemainingTime: (data) => request({ url: '/messages/email-verification/remaining-time', method: 'GET', data }),
  // 发送邮件
  sendEmail: (data) => request({ url: '/messages/email', method: 'POST', data })
}
```

#### Vuex Store (store/modules/user.js)
```javascript
// 发送邮箱验证码（忘记密码）
async sendEmailVerificationCode({ commit }, email) {
  try {
    const response = await api.message.sendEmailVerificationCode({
      email: email,
      codeType: 'reset_password',
      businessScene: '找回密码',
      expireMinutes: 30,
      codeLength: 6
    })
    // ... 处理响应
  }
}

// 重置密码
async resetPassword({ commit }, { email, verificationCode, newPassword }) {
  try {
    const response = await api.user.resetPassword({ email, verificationCode, newPassword })
    // ... 处理响应
  }
}
```

#### 页面组件 (pages/forgot/forgot.vue)
- 双步骤流程设计
- 邮箱格式验证
- 验证码倒计时功能
- 密码规则实时检查
- 错误处理和用户反馈

## 使用示例

### 完整流程

1. 用户访问忘记密码页面 (`/pages/forgot/forgot`)
2. 输入邮箱地址并发送验证码
3. 收到邮件验证码后输入验证码
4. 验证成功后进入重置密码步骤
5. 设置新密码并确认
6. 密码重置成功后跳转到登录页

### 错误处理

- 邮箱格式不正确时无法发送验证码
- 验证码错误或过期时显示相应提示
- 密码不符合规则时无法提交
- 网络错误时显示友好提示

## 注意事项

1. **验证码有效期**: 30分钟
2. **验证码长度**: 6位数字
3. **发送频率限制**: 防止恶意发送
4. **验证码一次性使用**: 验证成功后自动失效
5. **密码安全性**: 强制要求复杂密码

## 测试建议

1. 测试正常流程：发送验证码 → 验证 → 重置密码
2. 测试错误情况：无效邮箱、错误验证码、弱密码
3. 测试网络异常情况
4. 验证邮件模板和内容

## 相关文件

- `docs/message-management.md` - 消息管理模块详细文档
- `pages/forgot/forgot.vue` - 忘记密码页面
- `store/modules/user.js` - 用户管理Vuex模块
