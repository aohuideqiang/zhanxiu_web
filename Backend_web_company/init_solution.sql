-- =============================================
-- 数据库迁移脚本：为 solution 表添加新字段
-- 在 MySQL 客户端或 Navicat 中执行以下 SQL
-- =============================================

-- 1. 检查并添加 icon 列（图标名称，如 Van、HomeFilled）
ALTER TABLE solution ADD COLUMN icon VARCHAR(100) NULL AFTER tags;

-- 2. 检查并添加 image 列（图片链接/路径/base64）
ALTER TABLE solution ADD COLUMN image TEXT NULL AFTER icon;

-- =============================================
-- 2. 初始化 5 条解决方案数据
-- =============================================
INSERT INTO solution (solution_key, title, description, tags, icon, image) VALUES
('automotive', '智能驾驶', '面向自动驾驶、车道线、3D点云和DMS场景，提供采集、标注、质检一体化方案。', '车道线标注,3D点云,语义分割', 'Van', 'https://picsum.photos/seed/auto/600/400'),
('smart-home', '智能家居', '围绕家庭场景的目标检测、交互识别与设备联动数据，支持定制化采集。', '目标检测,行为识别,设备联动', 'HomeFilled', 'https://picsum.photos/seed/home/600/400'),
('security', '智能安防', '为安防巡检、异常识别、身份核验等场景提供高质量训练数据。', '异常检测,人脸属性,事件分析', 'Warning', 'https://picsum.photos/seed/security/600/400'),
('finance', '智能金融', '面向风控、反欺诈、客户画像等金融场景，提供可信的数据服务支持。', '风控建模,反欺诈,客户画像', 'Money', 'https://picsum.photos/seed/finance/600/400'),
('retail', '新零售', '针对线上线下零售场景，提供商品识别、客流分析等核心数据解决方案。', '商品识别,客流分析,货架检测', 'ShoppingCart', 'https://picsum.photos/seed/retail/600/400');
