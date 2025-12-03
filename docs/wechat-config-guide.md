# 微信小程序配置指南

## 域名校验问题解决方案

### 问题描述
微信小程序在开发工具中运行时，会校验请求的域名是否在微信后台配置的合法域名列表中。如果未配置，会出现以下错误：
- "工具未校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书"

### 解决方案

#### 方案1：在微信小程序后台配置合法域名
1. 登录[微信公众平台](https://mp.weixin.qq.com/)
2. 进入"开发" -> "开发管理" -> "开发设置"
3. 在"服务器域名"中配置以下域名：
   - **request合法域名**：`http://localhost:8080` (开发环境)
   - **uploadFile合法域名**：`http://localhost:8080` (开发环境)
   - **downloadFile合法域名**：`http://localhost:8080` (开发环境)

#### 方案2：开发环境临时解决方案
在微信开发者工具中：
1. 点击右上角"详情"
2. 勾选"不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书"
3. 重新编译项目

#### 方案3：修改API配置
修改 `utils/api.js` 中的 `baseURL`：

```javascript
// 开发环境配置
const baseURL = 'http://localhost:8080/api'

// 生产环境配置（需要HTTPS）
// const baseURL = 'https://your-domain.com/api'
```

## 微信登录配置

### 1. 微信小程序后台配置
1. 在微信公众平台 -> "开发" -> "开发设置" -> "接口权限"中：
   - 确保"微信登录"已开启
   - 配置"网页授权域名"（如果需要）

### 2. 开发环境模拟登录
由于开发环境可能无法正常调用微信登录API，我们提供了模拟登录功能：

```javascript
// 在登录页面添加模拟登录按钮（仅开发环境使用）
<button v-if="isDev" @click="handleMockWxLogin">模拟微信登录</button>
```

### 3. 模拟登录实现
```javascript
// 模拟微信登录（仅开发环境使用）
async handleMockWxLogin() {
  try {
    await this.wxLogin({
      openid: 'mock_openid_' + Date.now(),
      unionid: 'mock_unionid_' + Date.now()
    })
  } catch (error) {
    console.error('模拟微信登录失败:', error)
  }
}
```

## 生产环境部署要求

### 1. 域名要求
- 必须使用HTTPS协议
- 域名必须在微信后台配置
- 服务器必须支持TLS 1.2及以上版本

### 2. 服务器配置
```nginx
# Nginx配置示例
server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    
    location /api {
        proxy_pass http://backend:8080;
    }
}
```

### 3. API接口要求
- 所有接口必须返回标准JSON格式
- 微信登录接口路径：`/users/login/wx`
- 请求方法：POST
- 请求参数：`{ openid, unionid }`
- 响应格式：`{ code: 200, data: { token, user } }`

## 常见问题排查

### 1. 微信登录失败
- 检查微信小程序AppID是否正确
- 检查微信后台"微信登录"权限是否开启
- 检查网络连接是否正常

### 2. 域名校验失败
- 检查微信后台域名配置
- 检查服务器HTTPS证书是否有效
- 检查TLS版本是否支持

### 3. API请求失败
- 检查baseURL配置
- 检查网络连接
- 检查后端服务是否正常运行

## 开发调试技巧

### 1. 使用Charles等代理工具
- 可以查看网络请求详情
- 可以模拟不同的网络环境

### 2. 微信开发者工具调试
- 使用"Network"面板查看请求
- 使用"Console"面板查看错误信息
- 使用"Storage"面板查看本地存储

### 3. 日志记录
```javascript
// 在关键位置添加日志
console.log('微信登录开始:', { openid, unionid })
try {
  const response = await api.user.wxLogin({ openid, unionid })
  console.log('微信登录响应:', response)
} catch (error) {
  console.error('微信登录错误:', error)
}
```

