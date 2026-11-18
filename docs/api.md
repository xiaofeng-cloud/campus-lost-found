# 接口说明

所有接口以 `/api` 开头，统一返回：

```json
{ "success": true, "message": "操作成功", "data": {} }
```

除注册、登录和公共信息查询外，请在请求头携带 `Authorization: Bearer <令牌>`。

## 认证

| 方法 | 地址 | 说明 |
|---|---|---|
| POST | `/auth/register` | 注册普通用户 |
| POST | `/auth/login` | 登录并获取 JWT |

## 失物招领信息

| 方法 | 地址 | 说明 |
|---|---|---|
| GET | `/items/public` | 分页检索已审核信息 |
| GET | `/items/public/{id}` | 查看公开详情 |
| POST | `/items` | 发布信息 |
| PUT | `/items/{id}` | 修改本人待审核或已驳回信息 |
| GET | `/items/mine` | 查看本人发布 |

公共检索支持 `keyword`、`type`、`category`、`page` 和 `size` 参数。

## 认领和通知

| 方法 | 地址 | 说明 |
|---|---|---|
| POST | `/claims/item/{itemId}` | 提交认领申请 |
| GET | `/claims/mine` | 查看本人提交的申请 |
| GET | `/claims/received` | 查看本人收到的申请 |
| POST | `/claims/{id}/process` | 发布者通过或拒绝申请 |
| POST | `/claims/{id}/confirm-returned` | 发布者确认物品归还 |
| GET | `/notifications` | 查看站内通知 |
| POST | `/notifications/{id}/read` | 标记通知已读 |

## 管理端

| 方法 | 地址 | 说明 |
|---|---|---|
| GET | `/admin/pending-items` | 获取待审核信息 |
| POST | `/admin/items/{id}/review` | 审核通过或驳回 |
| GET | `/admin/statistics` | 获取系统统计 |
| GET | `/admin/logs` | 获取最近100条操作日志 |
