create table process_his
(
    id           bigint identity
        constraint PK__process___3214EC27CAFF2BD2
            primary key,
    process_id   bigint                     not null,
    process_name varchar(255)               not null,
    process_flow xml                        not null,
    op_type      bit                        not null,
    op_code      varchar(50),
    create_time  datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', '业务流程变更履历', 'SCHEMA', 'dbo', 'TABLE', 'process_his'
go

exec sp_addextendedproperty 'MS_Description', '流程履历ID', 'SCHEMA', 'dbo', 'TABLE', 'process_his', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', '流程ID', 'SCHEMA', 'dbo', 'TABLE', 'process_his', 'COLUMN', 'process_id'
go

exec sp_addextendedproperty 'MS_Description', '业务流程名称', 'SCHEMA', 'dbo', 'TABLE', 'process_his', 'COLUMN',
     'process_name'
go

exec sp_addextendedproperty 'MS_Description', 'Camel component 配置（XML想定）', 'SCHEMA', 'dbo', 'TABLE', 'process_his',
     'COLUMN', 'process_flow'
go

exec sp_addextendedproperty 'MS_Description', '操作类型
1 - 新规
2 - 变更
3 - 删除', 'SCHEMA', 'dbo', 'TABLE', 'process_his', 'COLUMN', 'op_type'
go

exec sp_addextendedproperty 'MS_Description', '操作人(工号)', 'SCHEMA', 'dbo', 'TABLE', 'process_his', 'COLUMN', 'op_code'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'process_his', 'COLUMN', 'create_time'
go

