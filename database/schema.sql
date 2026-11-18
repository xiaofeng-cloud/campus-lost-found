-- 校园失物招领信息管理系统数据库
CREATE DATABASE IF NOT EXISTS campus_lost_found
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;
USE campus_lost_found;

CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户编号',
  username VARCHAR(40) NOT NULL COMMENT '登录账号',
  nickname VARCHAR(80) NOT NULL COMMENT '用户昵称',
  password VARCHAR(255) NOT NULL COMMENT 'BCrypt密码摘要',
  phone VARCHAR(20) NULL COMMENT '手机号',
  role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：USER或ADMIN',
  enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_user_username (username)
) ENGINE=InnoDB COMMENT='系统用户';

CREATE TABLE IF NOT EXISTS item_post (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '信息编号',
  post_type VARCHAR(20) NOT NULL COMMENT '信息类型：LOST或FOUND',
  title VARCHAR(120) NOT NULL COMMENT '标题',
  description VARCHAR(2000) NOT NULL COMMENT '物品描述',
  category VARCHAR(30) NOT NULL COMMENT '物品分类',
  location VARCHAR(160) NOT NULL COMMENT '发生地点',
  occurred_at DATETIME NOT NULL COMMENT '发生时间',
  contact VARCHAR(100) NOT NULL COMMENT '联系方式',
  image_url VARCHAR(500) NULL COMMENT '图片地址',
  status VARCHAR(30) NOT NULL COMMENT '流程状态',
  review_comment VARCHAR(500) NULL COMMENT '审核意见',
  publisher_id BIGINT NOT NULL COMMENT '发布人编号',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT fk_item_publisher FOREIGN KEY (publisher_id) REFERENCES sys_user(id),
  KEY idx_item_status_created (status, created_at),
  KEY idx_item_type_category (post_type, category)
) ENGINE=InnoDB COMMENT='失物和招领信息';

CREATE TABLE IF NOT EXISTS claim_application (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请编号',
  item_id BIGINT NOT NULL COMMENT '物品信息编号',
  applicant_id BIGINT NOT NULL COMMENT '申请人编号',
  proof VARCHAR(1000) NOT NULL COMMENT '认领证明',
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '申请状态',
  handle_comment VARCHAR(500) NULL COMMENT '处理意见',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  handled_at DATETIME NULL COMMENT '处理时间',
  CONSTRAINT fk_claim_item FOREIGN KEY (item_id) REFERENCES item_post(id),
  CONSTRAINT fk_claim_applicant FOREIGN KEY (applicant_id) REFERENCES sys_user(id),
  KEY idx_claim_item_status (item_id, status)
) ENGINE=InnoDB COMMENT='认领申请';

CREATE TABLE IF NOT EXISTS notification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知编号',
  recipient_id BIGINT NOT NULL COMMENT '接收人编号',
  title VARCHAR(120) NOT NULL COMMENT '通知标题',
  content VARCHAR(1000) NOT NULL COMMENT '通知内容',
  read_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  CONSTRAINT fk_notification_recipient FOREIGN KEY (recipient_id) REFERENCES sys_user(id),
  KEY idx_notification_recipient (recipient_id, read_flag, created_at)
) ENGINE=InnoDB COMMENT='站内通知';

CREATE TABLE IF NOT EXISTS operation_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志编号',
  operator_id BIGINT NOT NULL COMMENT '操作人编号',
  action VARCHAR(60) NOT NULL COMMENT '操作类型',
  target_type VARCHAR(60) NOT NULL COMMENT '目标类型',
  target_id BIGINT NOT NULL COMMENT '目标编号',
  detail VARCHAR(500) NOT NULL COMMENT '操作说明',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  CONSTRAINT fk_log_operator FOREIGN KEY (operator_id) REFERENCES sys_user(id),
  KEY idx_log_created (created_at)
) ENGINE=InnoDB COMMENT='管理员操作日志';
