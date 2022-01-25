create table router_his
(
    id          bigint identity
        primary key,
    router_id   bigint                     not null,
    router_name varchar(255)               not null,
    router_flow xml                        not null,
    status      char     default N't'      not null,
    op_type     bit                        not null,
    op_code     varchar(50),
    create_time datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', '业务路径变更履历', 'SCHEMA', 'dbo', 'TABLE', 'router_his'
go

exec sp_addextendedproperty 'MS_Description', '业务路径履历ID', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', '业务路径ID', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'router_id'
go

exec sp_addextendedproperty 'MS_Description', '业务路径名称', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'router_name'
go

exec sp_addextendedproperty 'MS_Description', 'Camel的process 配置定义（XML想定）', 'SCHEMA', 'dbo', 'TABLE', 'router_his',
     'COLUMN', 'router_flow'
go

exec sp_addextendedproperty 'MS_Description', '状态：
t - tentative 暂定(Default)
c - close 关闭
s - start 开启', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'status'
go

exec sp_addextendedproperty 'MS_Description', '操作类型：
1 - 新规
2 - 变更
3 - 删除', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'op_type'
go

exec sp_addextendedproperty 'MS_Description', '操作人(工号)', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'op_code'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'router_his', 'COLUMN', 'create_time'
go

