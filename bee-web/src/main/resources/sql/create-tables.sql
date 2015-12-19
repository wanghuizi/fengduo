-- ------------------------------------------------------------------------------
--
--	蜂朵网数据库创建脚本
--	
--  1)创建表(Table)
--  2)创建主键约束(PK)
-- 	3)创建唯一性约束(Unique)
--	4)创建序列(Sequence)
--  5)创建索引(Index)
--
-- ------------------------------------------------------------------------------
-- ------------------------------------------------------------------------------
--
-- --命名规范：
-- --表名          	表名
-- --主键名        	pk_表名
-- --外键名        	fk_当前表名_参照表名_列名
-- --非唯一索引名  	idx_表名_列名
-- --唯一索引名    	unique_表名_列名
--
-- ------------------------------------------------------------------------------
-- 
-- --create database fengduo;
-- --grant all on fengduo.* to dev@'%' identified by 'dev1234' with grant option;

-- ------------------------------------------------------------------创建表--BEGIN-----------------------------------------------------------------


drop table if exists user;
drop table if exists identity_info;

-- 会员基础信息
create table user (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,

	nick 				varchar(64)  					COMMENT '用户名',
	email 				varchar(64) 					COMMENT '邮箱',
	password 			varchar(255) not null 			COMMENT '密码',
	phone 				varchar(64)						COMMENT '用户注册手机号',
	avatar				varchar(255)					COMMENT '头像',
	attentions 			varchar(255)					COMMENT '我的关注领域',
	province 			varchar(64)						COMMENT '所在省份',
	city 				varchar(64)						COMMENT '所在城市',
	wechat 				varchar(64)						COMMENT '微信',
	weibo 				varchar(64)						COMMENT '微博',
	memo 				varchar(64)						COMMENT '简介',
	verify_status 	    int(2) 							COMMENT '状态: 0=未审核,1=正常,2=停止',
	user_type 	    	int(2) 							COMMENT '类型',
	del_flag 			int(2)							COMMENT '逻辑删除:0=正常;1=删除',
	primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;
alter table user add index phone_index(`phone`);

-- 会员认证信息表
create table identity_info (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,
	
	user_id 			bigint not null				 COMMENT '会员ID',
	real_name 			varchar(64) not null		 COMMENT '会员真实姓名',
	id_card 			varchar(64) not null		 COMMENT '会员身份证ID',
	front_side 			varchar(255)          		 COMMENT '身份证前面',
	back_side 			varchar(255)           		 COMMENT '身份证背面',
	bank_card 			varchar(255) 			 	 COMMENT '银行卡',
	bank_phone 			varchar(64)			 		 COMMENT '银行预留手机号',
	bank_node 			varchar(256)           		 COMMENT '开户行网点',
	bank_address 		varchar(256)        		 COMMENT '开户行地址',
	business_card        varchar(256)                COMMENT '名片',
	investor_case        int(2)                      COMMENT '投资条件',
	investor_company    varchar(256)                 COMMENT '投资人公司',
	investor_title      varchar(256)                 COMMENT '头衔',
	investor_introduce  varchar(2000)                COMMENT '自我描述',
	primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;
alter table identity_info add bank_node 	varchar(256) comment '开户行网点';
alter table identity_info add bank_address  varchar(256) comment '开户行地址';

alter table identity_info add index userid_index(`user_id`);

-- 会员与产品关联表(我关注的,我发起的,我推荐的)
create table user_item (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,

	item_id				bigint	not null			COMMENT '众筹产品项目id',
	user_id				bigint	not null			COMMENT '用户id',
	handle_type			int(2) 	not null			COMMENT '操作类型:1=关注,2=发起,3=推荐',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;

-- 产品信息表
create table item (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,

	user_id				bigint	not null			COMMENT '用户id',
	tags				varchar(255)				COMMENT '产品标签',
	name				varchar(255)    			COMMENT '名称',
	introduce			varchar(255)				COMMENT '项目简介',
	stage				int(2)						COMMENT '所属阶段',
	team_count			int(2)						COMMENT '团队人数',
	province			varchar(255)				COMMENT '省',
	city				varchar(255)				COMMENT '市',
	video_url			varchar(255)				COMMENT '视频地址',
	content				text						COMMENT '项目信息',
	img_cf				varchar(255)				COMMENT '首屏图片',
	img_zf				varchar(255)				COMMENT '列表图片',
	img					varchar(255)				COMMENT '项目图片',
	progress			int(2)	default 0			COMMENT '众筹进展(预热,预售,付款,交接,成功,失败)',
	is_company			int(2)						COMMENT '是否是公司运营:0=是公司;1=不是公司',	
	company_name		varchar(512)    			COMMENT '公司名称',
	register_capital  	float  						COMMENT '注册资本(单位万元)',
	employee			int(5)						COMMENT '正式员工数',
	verify_status 	    int(2) 						COMMENT '状态: 0=未审核,1=正常,2=停止',
	verify_date 		datetime 					COMMENT '审核成功通过时间',
	item_type 	    	int(2) 						COMMENT '类型',
	del_flag 			int(2)						COMMENT '逻辑删除:0=正常;1=删除',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;
alter table item AUTO_INCREMENT=10000;

alter table item  add `progress` 			int(2);
alter table item  add `is_company` 			int(2);
alter table item  add `company_name` 		varchar(512);
alter table item  add `register_capital` 	float;
alter table item  add `employee` 			int(5);
alter table item  add `verify_date` 		datetime;

alter table item add index progress_tags_index(`progress`,`tags`);
alter table item add index userid_index(`user_id`);

-- 产品融资信息表
create table item_finance (	
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,
	
	item_id				bigint	not null		COMMENT '众筹产品项目id',
	amount 				int(6)	default 0		COMMENT '融资金额(单位是万元)',
	percent  			float  	default 0		COMMENT '出让股份百分比(单位%)',
	stock 				int(6)	default 0		COMMENT '出让多少股份数,投资人个数限制',
	capital_uses 		varchar(2048)			COMMENT '资金用途',
	pdf_url 			varchar(512)			COMMENT '融资计划书url',
	ex_finance  		text					COMMENT '融资资料',
	per_stock 			float  	default 0		COMMENT '每份多少钱(单位是万元)',
	per_percent 		float  	default 0   	COMMENT '每份占股多少百分比(单位%)',
	real_sub 			float  	default 0   	COMMENT '投资人实际已认购金额(已经支付过保证金)',
	real_pay 			float  	default 0   	COMMENT '投资人实际已支付额(全额支付)',
	suber_num           int     default 0       COMMENT '认购的人数(已交保证金的用户)',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;
alter table item_finance  add real_sub 		float;
alter table item_finance  add real_pay 		float;
alter table item_finance  add suber_num     int;

alter table item_finance add index itemid_index(`item_id`);

-- 产品团队信息表
create table item_member (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,

	item_id				bigint	not null		COMMENT '众筹产品项目id',
	name				varchar(255)			COMMENT '姓名',
	avatar				varchar(255)			COMMENT '头像',
	title				varchar(255)			COMMENT '职位',
	memo				varchar(255)			COMMENT '简介',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;

-- 产品评论信息表
create table item_comment (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,
	
	item_id				bigint	not null		COMMENT '众筹产品项目id',
	user_id				bigint	not null		COMMENT '用户id,评论者id',
	user_name			varchar(255)			COMMENT '评论者名称',
	avatar				varchar(255)			COMMENT '评论者头像',
	context				varchar(2048)			COMMENT '评论的具体内容',
	parent_id 			bigint	 				COMMENT '父评论id',
	parent_context  	varchar(2048)	 		COMMENT '父评论内容',
	comment_type 		int(2)					COMMENT '评论类型:0=评论;1=回复',
	del_flag 			int(2)					COMMENT '逻辑删除:0=正常;1=删除',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;
alter table item_comment add index userid_index(`user_id`);
alter table item_comment add index itemid_index(`item_id`);

-- 投资人认购信息表
create table user_sub (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,
	
	item_id				bigint	not null		COMMENT '众筹产品项目id',
	user_id				bigint	not null		COMMENT '用户id',
	user_type 			int(2) 					COMMENT '投资者类型(类型区分基石投资人和跟投人)',
	real_name			varchar(255)			COMMENT '投资人真实姓名',
	sub_amount			float					COMMENT '认购金额(单位是万元)',
	advances			float					COMMENT '保证金金额(单位是万元)(已认购金额=保证金+剩余待全额支付金额)',
	percent			    float					COMMENT '股份占有百分比(单位%)',
	sub_date 			datetime 				COMMENT '认购成功时间,保证金支付成功',
	pay_start 			datetime 				COMMENT '全额款支付开始时间',
	memo				varchar(255)			COMMENT '备注',
	handle_status     	int(2) 					COMMENT '投资状态:0=保证金尚未支付,1=保证金已支付,3=全额款尚未支付,4=全额款已支付,5=关闭',
	del_flag 			int(2)					COMMENT '逻辑删除:0=正常;1=删除',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;
alter table user_sub  add advances 		float;
alter table user_sub  add pay_start 	datetime;
alter table user_sub  add percent 	float;

alter table user_sub add index userid_index(`user_id`);
alter table user_sub add index itemid_index(`item_id`);

-- 支付订单表
create table pay_order (
	`id` 							bigint 	  not null auto_increment,
	`create_date` 					datetime not null default 0,
	`update_date` 					datetime not null default 0,
	
	`order_no` 						varchar(64) not null 			COMMENT '订单编号',
	`item_id`						bigint	not null				COMMENT '众筹产品项目id',
	`user_id` 						bigint						 	COMMENT '支付者',
	`sub_id`                        bigint                          COMMENT  '认购表id',
	`sub_user_type`                	int                             COMMENT  '认购人类型',
	`type` 							int 							COMMENT '类型：1:保证金，2:全额',
	`amount` 						float 							COMMENT '交易金额',
	`handling_cost` 				float 							COMMENT '投资人手续费',
	`platform_cost`                 float                           COMMENT '平台手续费',
	`bank_type` 					int 							COMMENT '银行类型',
	`status` 						int 							COMMENT '支付类型：0：未支付，1：支付成功，2：支付失败，3：取消支付，4：退款成功，5：退款失败',
	`deal_date` 					datetime 						COMMENT '交易完成时间',
	`item_name` 					varchar(64) 					COMMENT '项目名称',
	`item_logo` 					varchar(256) 					COMMENT '项目缩略图',
	`pay_type` 						int 							COMMENT '支付类型：1：线上支付，2：线下支付',
	`operator_id` 					bigint							COMMENT '操作者id',
	`operate_date` 					datetime default 0 				COMMENT '操作时间',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;
alter table pay_order add platform_cost float;
alter table pay_order add sub_id bigint;

alter table pay_order add index orderno_index(`order_no`);
alter table pay_order add index userid_index(`user_id`);

-- 发起人做股权转让交接记录信息表
create table equity_change (
	id 					bigint    not null auto_increment,
	create_date 		datetime not null default 0,
	update_date 		datetime not null default 0,

	item_id				bigint	not null	COMMENT '众筹产品项目id',
	user_id				bigint	not null	COMMENT '用户id,发起人id',
	amount				float				COMMENT '已到达平台托管账户的融资金额(单位是万元)',
	success_date 		datetime 			COMMENT '融资成功时间',
	deadline 			datetime 			COMMENT '股权变更提交截止日期',
	equity_url			varchar(1024)		COMMENT '上传的电子版股权变更书url路径',
	is_receive    		int(2) 				COMMENT '平台是否接收到发起人邮寄的纸质版股权转让通知书: 0=尚未接收到,1=已接收到',
	operator_id 		bigint  			COMMENT '平台操作者id',
	operator_date  		datetime 			COMMENT '平台操作时间',
	del_flag 			int(2)				COMMENT '逻辑删除:0=正常;1=删除',
	primary key (id)
)engine=InnoDB DEFAULT CHARSET=utf8;

-- 融资方提现表
create table withdraw (
  `id` 					bigint    not null auto_increment,
  `create_date` 		datetime not null default 0,
  `update_date` 		datetime not null default 0,
  
  `item_id`  			bigint 				COMMENT '产品id',
  `payee_id` 			bigint   			COMMENT '提现者id',
  `payee_company` 		varchar(64)  		COMMENT '公司名称',
  `operator_id` 		bigint  			COMMENT '平台操作者id',
  `operate_date` 		datetime 			COMMENT '操作时间',
  `operator_name` 		varchar(64) 		COMMENT '操作者名称',
  `status` 				int  				COMMENT '提现状态：0:成功；1：失败',
  `amount` 				int 				COMMENT '提现金额，单位万元',
  `withdraw_type` 		int 				COMMENT '提现方式，线上，线下',
  `bank_card` 			varchar(64) 		COMMENT '银行卡',
  `memo` 				varchar(256) 		COMMENT '备注',
   primary key (`id`)
)engine=InnoDB DEFAULT CHARSET=utf8;
alter table withdraw add `item_id` bigint;

-- 邮件提醒表
create table email_notify (
  `id` 					bigint    not null auto_increment,
  `create_date` 		datetime not null default 0,
  `update_date` 		datetime not null default 0,

  `status`              int(2)             	COMMENT '0:未发送、发送失败，1：发送成功',
  `item_id` 			bigint   			COMMENT '产品id',
  `receiver_id`  		bigint 				COMMENT '接收者id',
  `operator_id`			bigint  			COMMENT '平台操作者id',
  `handle_type` 		int 				COMMENT '用途类型',
  `title` 				varchar(128) 		COMMENT '标题',
  `content` 			text  				COMMENT '邮件内容', 
  `memo` 				varchar(256) 		COMMENT '备注',
   primary key (`id`)
)engine=InnoDB DEFAULT CHARSET=utf8;


-- 收货人地址表
create table consignee_addr(
   id 					bigint   not null auto_increment,
   create_date 			datetime not null default 0,
   update_date 			datetime not null default 0,

   del_flag 			int(2)				COMMENT '逻辑删除:0=正常;1=删除',
   user_id 				bigint 				COMMENT '网站会员用户',
   consignee_name 		varchar(16) 		COMMENT '收货人姓名',
   province 			varchar(16) 		COMMENT '省',
   city 				varchar(16) 		COMMENT '市',
   detail_addr 			varchar(128) 		COMMENT '收货人详细地址',
   phone 				varchar(32) 		COMMENT '收货人手机号',
   post_code 			varchar(32) 		COMMENT '邮政编号',
   default_flag 		int(2)				COMMENT '逻辑删除:0=默认;1=不默认',
   primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;


-- -----------------------------------------------------------------创建表---END------------------------------------------------------------------