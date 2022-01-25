create table router_spec
(
    router_id    bigint                     not null,
    spec_id      bigint                     not null,
    node_name    varchar(255),
    spec_special text,
    create_time  datetime default getdate() not null,
    primary key (router_id, spec_id)
)
go

exec sp_addextendedproperty 'MS_Description', 'Router中 spec 的指定关系定义', 'SCHEMA', 'dbo', 'TABLE', 'router_spec'
go

exec sp_addextendedproperty 'MS_Description', '业务路径ID', 'SCHEMA', 'dbo', 'TABLE', 'router_spec', 'COLUMN', 'router_id'
go

exec sp_addextendedproperty 'MS_Description', '转换SpecID', 'SCHEMA', 'dbo', 'TABLE', 'router_spec', 'COLUMN', 'spec_id'
go

exec sp_addextendedproperty 'MS_Description', '结点的名称标记', 'SCHEMA', 'dbo', 'TABLE', 'router_spec', 'COLUMN', 'node_name'
go

exec sp_addextendedproperty 'MS_Description', '对应Router的JOLT spec特别指定
推荐：使用时覆盖spec中的定义', 'SCHEMA', 'dbo', 'TABLE', 'router_spec', 'COLUMN', 'spec_special'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'router_spec', 'COLUMN', 'create_time'
go

