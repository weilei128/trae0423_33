-- 珠宝定制门店系统数据库初始化脚本

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS db3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE db3;

-- 客户表
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱',
    gender TINYINT COMMENT '性别：0-女，1-男',
    birthday DATE COMMENT '生日',
    address VARCHAR(500) COMMENT '地址',
    level INT DEFAULT 1 COMMENT '客户等级：1-普通，2-银卡，3-金卡，4-钻石卡',
    total_consumption DECIMAL(12,2) DEFAULT 0.00 COMMENT '累计消费金额',
    remark TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_phone (phone),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';

-- 客户偏好表
DROP TABLE IF EXISTS customer_preference;
CREATE TABLE customer_preference (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    preference_type VARCHAR(50) COMMENT '偏好类型（如材质、款式、颜色等）',
    preference_value VARCHAR(200) COMMENT '偏好值',
    description TEXT COMMENT '详细描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户偏好表';

-- 重要纪念日表
DROP TABLE IF EXISTS important_date;
CREATE TABLE important_date (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    date_type VARCHAR(50) COMMENT '日期类型（生日、纪念日等）',
    date_date DATE COMMENT '日期',
    reminder_days INT DEFAULT 7 COMMENT '提前提醒天数',
    description TEXT COMMENT '描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer_id (customer_id),
    INDEX idx_date_date (date_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重要纪念日表';

-- 商品表
DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    category VARCHAR(100) COMMENT '商品分类',
    style VARCHAR(100) COMMENT '款式',
    material VARCHAR(100) COMMENT '材质',
    weight DECIMAL(10,2) COMMENT '重量（克）',
    price DECIMAL(12,2) COMMENT '价格',
    image_urls TEXT COMMENT '图片URL列表，多个用逗号分隔',
    description TEXT COMMENT '商品描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    stock_id BIGINT COMMENT '关联库存ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_name (name),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 库存表
DROP TABLE IF EXISTS inventory;
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    item_type TINYINT NOT NULL COMMENT '库存类型：1-成品，2-原材料',
    item_name VARCHAR(200) NOT NULL COMMENT '物品名称',
    quantity INT DEFAULT 0 COMMENT '数量',
    unit VARCHAR(50) COMMENT '单位',
    specification VARCHAR(200) COMMENT '规格',
    location VARCHAR(200) COMMENT '存放位置',
    min_stock INT DEFAULT 0 COMMENT '最低库存预警值',
    remark TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_item_type (item_type),
    INDEX idx_item_name (item_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 入出库记录表
DROP TABLE IF EXISTS inventory_record;
CREATE TABLE inventory_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    inventory_id BIGINT NOT NULL COMMENT '库存ID',
    record_type TINYINT NOT NULL COMMENT '记录类型：1-入库，2-出库',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(12,2) COMMENT '单价',
    operator VARCHAR(100) COMMENT '操作人',
    reason TEXT COMMENT '出入库原因',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_inventory_id (inventory_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入出库记录表';

-- 定制需求表
DROP TABLE IF EXISTS customization_request;
CREATE TABLE customization_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    request_no VARCHAR(50) NOT NULL COMMENT '需求单号',
    style_desc TEXT COMMENT '款式描述',
    material VARCHAR(100) COMMENT '材质',
    size VARCHAR(100) COMMENT '尺寸',
    budget DECIMAL(12,2) COMMENT '预算金额',
    description TEXT COMMENT '详细描述',
    reference_images TEXT COMMENT '参考图片URL列表',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待接单，1-设计中，2-待确认，3-生产中，4-已完成，5-已取消',
    designer_id BIGINT COMMENT '设计师ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_request_no (request_no),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定制需求表';

-- 设计师表
DROP TABLE IF EXISTS designer;
CREATE TABLE designer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '设计师姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    specialty VARCHAR(200) COMMENT '专长',
    status TINYINT DEFAULT 1 COMMENT '状态：0-休息，1-可用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设计师表';

-- 设计打样表
DROP TABLE IF EXISTS design_sample;
CREATE TABLE design_sample (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    request_id BIGINT NOT NULL COMMENT '定制需求ID',
    version INT DEFAULT 1 COMMENT '设计版本',
    design_image TEXT COMMENT '设计图URL',
    design_desc TEXT COMMENT '设计说明',
    sample_fee DECIMAL(12,2) DEFAULT 0.00 COMMENT '打样费用',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待确认，1-已确认，2-已修改，3-已拒绝',
    confirm_time DATETIME COMMENT '确认时间',
    confirm_remark TEXT COMMENT '确认备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_request_id (request_id),
    INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设计打样表';

-- 生产跟踪表
DROP TABLE IF EXISTS production_tracking;
CREATE TABLE production_tracking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    request_id BIGINT NOT NULL COMMENT '定制需求ID',
    process_name VARCHAR(100) COMMENT '当前工序',
    progress INT DEFAULT 0 COMMENT '进度百分比',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待生产，1-生产中，2-已完成',
    estimated_complete_date DATE COMMENT '预计完成日期',
    actual_complete_date DATE COMMENT '实际完成日期',
    delay_warning TINYINT DEFAULT 0 COMMENT '延期预警：0-正常，1-预警',
    remark TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_request_id (request_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产跟踪表';

-- 生产进度节点表
DROP TABLE IF EXISTS production_process;
CREATE TABLE production_process (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tracking_id BIGINT NOT NULL COMMENT '生产跟踪ID',
    process_name VARCHAR(100) COMMENT '工序名称',
    process_order INT COMMENT '工序顺序',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    status TINYINT DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已完成',
    operator VARCHAR(100) COMMENT '操作人',
    remark TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_tracking_id (tracking_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产进度节点表';

-- 销售订单表
DROP TABLE IF EXISTS sales_order;
CREATE TABLE sales_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    product_id BIGINT COMMENT '商品ID（成品销售）',
    request_id BIGINT COMMENT '定制需求ID（定制销售）',
    order_type TINYINT COMMENT '订单类型：1-成品销售，2-定制销售',
    total_amount DECIMAL(12,2) COMMENT '总金额',
    deposit_amount DECIMAL(12,2) COMMENT '首付金额',
    final_amount DECIMAL(12,2) COMMENT '尾款金额',
    deposit_paid TINYINT DEFAULT 0 COMMENT '首付是否已付：0-未付，1-已付',
    final_paid TINYINT DEFAULT 0 COMMENT '尾款是否已付：0-未付，1-已付',
    invoice_status TINYINT DEFAULT 0 COMMENT '发票状态：0-未开，1-已开',
    invoice_no VARCHAR(100) COMMENT '发票号',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-待付款，1-已付款，2-已发货，3-已完成，4-已取消',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_customer_id (customer_id),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- 售后记录表
DROP TABLE IF EXISTS after_sales;
CREATE TABLE after_sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    type VARCHAR(100) COMMENT '售后类型（维修、保养、退换等）',
    description TEXT COMMENT '问题描述',
    solution TEXT COMMENT '解决方案',
    cost DECIMAL(12,2) DEFAULT 0.00 COMMENT '费用',
    status TINYINT DEFAULT 0 COMMENT '状态：0-处理中，1-已完成',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='售后记录表';

-- 消费历史表（视图或统计表，也可以是实体表记录消费记录）

-- 系统用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码',
    name VARCHAR(100) COMMENT '姓名',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(50) COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 插入初始数据
INSERT INTO sys_user (username, password, name, phone, role, status) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '管理员', '13800138000', 'admin', 1);

INSERT INTO designer (name, phone, specialty, status) 
VALUES ('张设计师', '13900139000', '珠宝设计、戒指设计', 1);

INSERT INTO designer (name, phone, specialty, status) 
VALUES ('李设计师', '13900139001', '项链设计、耳环设计', 1);
