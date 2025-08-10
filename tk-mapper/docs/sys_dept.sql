drop TABLE IF EXISTS sys_dept;
CREATE TABLE `sys_dept` (
                            `dept_no` varchar(20) NOT NULL COMMENT '部门号',
                            `dept_name` varchar(40) DEFAULT NULL COMMENT '部门名称',
                            `state` varchar(10) DEFAULT NULL COMMENT '是否启用',
                            `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                            `parent_dept_no` varchar(20) DEFAULT NULL COMMENT '上级部门',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`dept_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统部门表';

INSERT INTO `sys_dept` (`dept_no`, `dept_name`, `state`, `remark`, `parent_dept_no`) VALUES
-- 顶级部门
('D001', '总部', '1', '公司总部', NULL),
('D002', '人力资源中心', '1', '负责招聘、培训等', NULL),
('D003', '财务中心', '1', '财务管理', NULL),
('D004', '技术研发中心', '1', '技术研发与创新', NULL),
('D005', '市场运营中心', '1', '市场推广与运营', NULL),

-- 一级子部门
('D006', '行政部', '1', '日常行政管理', 'D001'),
('D007', '法务部', '1', '法律事务', 'D001'),
('D008', '招聘部', '1', '人才招聘', 'D002'),
('D009', '培训部', '1', '员工培训', 'D002'),
('D010', '薪酬福利部', '1', '薪资与福利管理', 'D002'),
('D011', '会计部', '1', '会计核算', 'D003'),
('D012', '税务部', '1', '税务管理', 'D003'),
('D013', '资金部', '1', '资金运作', 'D003'),
('D014', '前端开发部', '1', '前端技术研发', 'D004'),
('D015', '后端开发部', '1', '后端技术研发', 'D004'),
('D016', '测试部', '1', '质量保障', 'D004'),
('D017', '运维部', '1', '系统运维', 'D004'),
('D018', '市场部', '1', '品牌推广', 'D005'),
('D019', '销售部', '1', '产品销售', 'D005'),
('D020', '客户服务部', '1', '客户支持', 'D005'),
