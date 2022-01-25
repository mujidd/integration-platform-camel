create table router_schema
(
    router_id         bigint                     not null,
    schema_id         bigint                     not null,
    node_name         varchar(255),
    query_path_method varchar(520),
    schema_special    text,
    create_time       datetime default getdate() not null,
    primary key (router_id, schema_id)
)
go

exec sp_addextendedproperty 'MS_Description', '业务路径和Schema关系的定义', 'SCHEMA', 'dbo', 'TABLE', 'router_schema'
go

exec sp_addextendedproperty 'MS_Description', '业务路径ID', 'SCHEMA', 'dbo', 'TABLE', 'router_schema', 'COLUMN', 'router_id'
go

exec sp_addextendedproperty 'MS_Description', 'js_SchemaID', 'SCHEMA', 'dbo', 'TABLE', 'router_schema', 'COLUMN',
     'schema_id'
go

exec sp_addextendedproperty 'MS_Description', '结点的名称标记', 'SCHEMA', 'dbo', 'TABLE', 'router_schema', 'COLUMN',
     'node_name'
go

exec sp_addextendedproperty 'MS_Description', '访问路径+访问方法', 'SCHEMA', 'dbo', 'TABLE', 'router_schema', 'COLUMN',
     'query_path_method'
go

exec sp_addextendedproperty 'MS_Description', '对应Router的JsonSchema特别指定
推荐：使用时覆盖schema中的定义', 'SCHEMA', 'dbo', 'TABLE', 'router_schema', 'COLUMN', 'schema_special'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'router_schema', 'COLUMN', 'create_time'
go

