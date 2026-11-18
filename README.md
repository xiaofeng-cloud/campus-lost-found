# 拾光校园——校园失物招领信息管理系统

基于 Spring Boot、Vue 3 和 MySQL 的前后端分离毕业设计项目。系统围绕“发布、审核、检索、认领、归还”建立完整业务闭环，并提供站内通知、数据统计和操作日志。

## 已实现功能

- 用户注册、登录和 JWT 身份认证
- 寻物启事、招领启事发布及管理员审核
- 关键字、类型和分类组合检索
- 认领申请、发布者处理和确认归还
- 我的发布、我的申请、收到的申请和站内通知
- 管理后台统计、待审核列表和操作日志
- 中文响应提示、参数校验及统一异常处理
- 响应式中文界面，支持电脑和手机访问

## 技术栈

- 后端：Java 17、Spring Boot 3.5、Spring Security、Spring Data JPA、JWT、Maven
- 前端：Vue 3、TypeScript、Vite、Pinia、Vue Router、Axios、Element Plus
- 数据库：MySQL 8.4；自动化测试使用 H2

## 目录结构

```text
├─ backend/                  Spring Boot 后端
├─ frontend/                 Vue 3 前端
├─ database/                 MySQL 建表及演示脚本
├─ docs/                     接口、设计和实施文档
├─ .env.example              环境变量示例
└─ README.md
```

## 本地运行

### 1. 准备数据库

安装 MySQL 8.x，并执行：

```sql
SOURCE database/schema.sql;
```

### 2. 启动后端

根据 `.env.example` 设置数据库密码、JWT 密钥和管理员初始密码。PowerShell 示例：

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="你的MySQL密码"
$env:JWT_SECRET="一段至少32字节且仅自己知道的随机字符串"
$env:ADMIN_INITIAL_PASSWORD="你的管理员初始密码"
cd backend
mvn spring-boot:run
```

后端地址为 `http://localhost:8080`。首次启动会创建管理员账号 `admin`，密码为 `ADMIN_INITIAL_PASSWORD` 的值。

### 3. 启动前端

```powershell
cd frontend
npm install
npm run dev
```

浏览器访问 `http://localhost:5173`。

## 测试与构建

```powershell
cd backend
mvn clean verify

cd ../frontend
npm run test
npm run build
```

## 演示建议

1. 普通用户发布一条寻物启事。
2. 管理员登录后台审核通过。
3. 第二名用户在信息广场检索并提交认领证明。
4. 发布者在个人中心通过申请并确认归还。
5. 展示双方站内通知、已找回状态、后台统计和操作日志。

## 安全提醒

- 不要把真实数据库密码或 JWT 密钥提交到仓库。
- 演示环境也应修改管理员初始密码。
- 发布证件信息时不要展示完整证件号码或其他敏感信息。

更详细的接口列表见 [docs/api.md](docs/api.md)，系统设计见 [docs/superpowers/specs/2026-11-18-campus-lost-found-design.md](docs/superpowers/specs/2026-11-18-campus-lost-found-design.md)。
