create table router_process
(
    router_id   bigint                     not null,
    process_id  bigint                     not null,
    create_time datetime default getdate() not null,
    primary key (router_id, process_id)
)
go

exec sp_addextendedproperty 'MS_Description', '业务路径和流程关系定义', 'SCHEMA', 'dbo', 'TABLE', 'router_process'
go

exec sp_addextendedproperty 'MS_Description', '业务路径ID', 'SCHEMA', 'dbo', 'TABLE', 'router_process', 'COLUMN',
     'router_id'
go

exec sp_addextendedproperty 'MS_Description', '业务流程ID', 'SCHEMA', 'dbo', 'TABLE', 'router_process', 'COLUMN',
     'process_id'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'router_process', 'COLUMN',
     'create_time'
go

