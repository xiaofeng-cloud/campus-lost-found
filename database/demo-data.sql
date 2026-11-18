-- 演示数据说明
-- 启动后端时会自动创建管理员账号 admin，密码由 ADMIN_INITIAL_PASSWORD 环境变量指定。
-- 请先在页面注册普通用户，再发布演示信息，以确保密码均通过 BCrypt 安全加密。

USE campus_lost_found;

-- 可以在首次演示后执行以下查询，检查各流程的数据数量。
SELECT status, COUNT(*) AS information_count FROM item_post GROUP BY status;
SELECT status, COUNT(*) AS claim_count FROM claim_application GROUP BY status;
