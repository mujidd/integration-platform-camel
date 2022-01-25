create table router
(
    id          bigint identity
        primary key,
    router_name varchar(255)               not null,
    router_flow xml                        not null,
    status      char     default N't'      not null,
    create_time datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', '业务路径定义', 'SCHEMA', 'dbo', 'TABLE', 'router'
go

exec sp_addextendedproperty 'MS_Description', '路径ID', 'SCHEMA', 'dbo', 'TABLE', 'router', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', '业务路径名称', 'SCHEMA', 'dbo', 'TABLE', 'router', 'COLUMN', 'router_name'
go

exec sp_addextendedproperty 'MS_Description', 'Camel的process 配置定义（XML想定）', 'SCHEMA', 'dbo', 'TABLE', 'router', 'COLUMN',
     'router_flow'
go

exec sp_addextendedproperty 'MS_Description', '状态：
t - tentative 暂定(Default)
c - close 关闭
s - start 开启', 'SCHEMA', 'dbo', 'TABLE', 'router', 'COLUMN', 'status'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'router', 'COLUMN', 'create_time'
go

INSERT INTO app_router.dbo.router (id, router_name, router_flow, status, create_time) VALUES (100001, N'e-jolt-test', N'<rest path="/rest"><post uri="/jolt-test" consumes="application/json" produces="application/json" id="e-jolt-test"><to uri="direct:call-cfs-service"/></post></rest>', N's', N'2021-12-24 04:02:37.303');
INSERT INTO app_router.dbo.router (id, router_name, router_flow, status, create_time) VALUES (100003, N'r-feedback-content', N'<route id="call-cfs-service"><from uri="direct:call-cfs-service"/><to uri="bean:dynaVerify?method=process(''schema_test'')"/><to uri="bean:dynaConf?method=process(''spec_test'')"/><log message="${body}"/><convertBodyTo type="java.lang.String"/><log message="${body}"/></route>', N's', N'2021-12-30 10:20:43.233');
INSERT INTO app_router.dbo.router (id, router_name, router_flow, status, create_time) VALUES (100004, N'e-feedback-content', N'<rest path="/rest" id="e-feedback-content" customId="true"><post uri="/feedback-content" consumes="application/json" produces="application/json" id="e-feedback-content"><to uri="direct:r-feedback-content"/></post></rest>', N't', N'2021-12-30 10:43:19.320');
INSERT INTO app_router.dbo.router (id, router_name, router_flow, status, create_time) VALUES (100005, N'r-enrich-demo', N'<route id="r-enrich-demo "><from uri="direct:r-enrich-demo"/><enrich><constant>direct:updateBody</constant></enrich><log message="Got ${body}"/></route>', N's', N'2022-01-07 07:59:38.760');
INSERT INTO app_router.dbo.router (id, router_name, router_flow, status, create_time) VALUES (100006, N'e-enrich-test', N'<rest path="/rest"><post uri="/enrich-test" consumes="application/json" produces="application/json" id="e-enrich-test"><to uri="direct:r-enrich-demo"/></post></rest>', N's', N'2022-01-07 07:59:38.813');