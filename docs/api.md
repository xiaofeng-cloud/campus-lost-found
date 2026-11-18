# 接口说明

## 通用约定

- 基础地址：`http://localhost:8080/api`
- 请求与响应编码：UTF-8
- 请求体格式：`application/json`
- 除注册、登录和公共信息查询外，请在请求头携带 `Authorization: Bearer <令牌>`。

成功响应：

```json
{
  "success": true,
  "message": "操作成功",
  "data": {}
}
```

失败响应：

```json
{
  "success": false,
  "message": "错误原因",
  "data": null
}
```

## 认证接口

### 注册普通用户

`POST /auth/register`

```json
{
  "username": "student_2026",
  "nickname": "校园用户",
  "password": "Student@2026",
  "phone": "13800000000"
}
```

约束：账号最长 40 个字符，昵称最长 80 个字符，密码长度为 8 至 64 位；手机号可为空，填写时必须为 11 位中国大陆手机号格式。

### 登录

`POST /auth/login`

```json
{
  "username": "student_2026",
  "password": "Student@2026"
}
```

响应的 `data.token` 用于后续鉴权，`data.user.role` 为 `USER` 或 `ADMIN`。

## 失物招领信息接口

### 公共分页检索

`GET /items/public`

| 参数 | 必填 | 说明 |
|---|---|---|
| `keyword` | 否 | 在标题、描述和地点中检索 |
| `type` | 否 | `LOST` 寻物启事，`FOUND` 招领启事 |
| `category` | 否 | `CARD`、`DIGITAL`、`BOOK`、`CLOTHING`、`KEY`、`SPORTS`、`OTHER` |
| `page` | 否 | 从 0 开始，默认 0 |
| `size` | 否 | 每页条数，默认 12 |

示例：

```text
GET /api/items/public?keyword=校园卡&type=LOST&category=CARD&page=0&size=12
```

公共列表只返回审核通过且仍处于业务流程中的信息。

### 查看公开详情

`GET /items/public/{id}`

### 发布信息

`POST /items`

```json
{
  "postType": "LOST",
  "title": "图书馆遗失蓝色校园卡",
  "description": "卡套为深蓝色，背面有银色星星贴纸。",
  "category": "CARD",
  "location": "图书馆二楼自习区",
  "occurredAt": "2026-11-18T17:00:00",
  "contact": "站内联系发布者",
  "imageUrl": ""
}
```

发生时间不能晚于当前时间。发布成功后状态为 `PENDING_REVIEW`。

### 修改本人信息

`PUT /items/{id}`

仅可修改本人处于待审核或已驳回状态的信息，修改后重新进入审核流程。

### 查看本人发布

`GET /items/mine`

## 认领与通知接口

### 提交认领申请

`POST /claims/item/{itemId}`

```json
{
  "proof": "认领留言：卡号末四位为2026，卡套背面有银色星星贴纸，可现场核验。"
}
```

用户不能认领自己发布的信息，也不能对同一信息重复提交有效申请。

### 查看本人提交的申请

`GET /claims/mine`

### 查看本人收到的申请

`GET /claims/received`

### 发布者处理申请

`POST /claims/{id}/process`

```json
{
  "approved": true,
  "comment": "描述一致，请到服务台核验领取"
}
```

同意后申请状态变为 `APPROVED`，物品信息状态变为 `CLAIMING`；拒绝后申请状态变为 `REJECTED`。

### 发布者确认归还

`POST /claims/{id}/confirm-returned`

确认后申请状态变为 `COMPLETED`，物品信息状态变为 `FOUND`。

### 查看站内通知

`GET /notifications`

### 标记通知已读

`POST /notifications/{id}/read`

## 管理端接口

以下接口仅允许 `ADMIN` 角色访问。

### 获取待审核信息

`GET /admin/pending-items`

### 审核信息

`POST /admin/items/{id}/review`

```json
{
  "approved": true,
  "comment": "信息完整，审核通过"
}
```

审核通过后状态为 `SEARCHING`；驳回后状态为 `REJECTED`。

### 获取统计数据

`GET /admin/statistics`

返回用户总数、待审核、寻找中、认领中和已找回数量。

### 获取操作日志

`GET /admin/logs`

返回最近 100 条管理员操作记录。

## 状态流转

```text
信息：PENDING_REVIEW → SEARCHING → CLAIMING → FOUND
                    ↘ REJECTED

申请：PENDING → APPROVED → COMPLETED
             ↘ REJECTED
```

## 常见 HTTP 状态

| 状态码 | 说明 |
|---|---|
| 200 | 请求成功 |
| 400 | 参数错误或业务规则不允许 |
| 401 | 未登录或令牌无效 |
| 403 | 当前角色无权访问 |
| 404 | 目标记录不存在 |
