create table js_schema_his
(
    id           bigint identity
        primary key,
    schema_id    bigint                     not null,
    [schema]     text                       not null,
    schema_title varchar(255)               not null,
    hash_code    varchar(255)               not null,
    ep_in_dto    text,
    op_type      bit                        not null,
    op_code      varchar(50),
    create_time  datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', 'JsonSchema变更履历', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his'
go

exec sp_addextendedproperty 'MS_Description', 'Schema履历ID', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', 'SchemaID', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN',
     'schema_id'
go

exec sp_addextendedproperty 'MS_Description', 'JsonSchema的内容信息', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN',
     'schema'
go

exec sp_addextendedproperty 'MS_Description', 'Schema名称', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN',
     'schema_title'
go

exec sp_addextendedproperty 'MS_Description', '散列值，做唯一区分', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN',
     'hash_code'
go

exec sp_addextendedproperty 'MS_Description', 'Process InputDTO（Json形式想定，用于备份）', 'SCHEMA', 'dbo', 'TABLE',
     'js_schema_his', 'COLUMN', 'ep_in_dto'
go

exec sp_addextendedproperty 'MS_Description', '操作类型：
1 - 新规
2 - 变更
3 - 删除', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN', 'op_type'
go

exec sp_addextendedproperty 'MS_Description', '操作人(工号)', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN', 'op_code'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'js_schema_his', 'COLUMN', 'create_time'
go

