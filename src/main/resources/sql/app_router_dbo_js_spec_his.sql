create table js_spec_his
(
    id          bigint identity
        primary key,
    spec_id     bigint                     not null,
    jolt_spec   text                       not null,
    spec_title  varchar(255),
    ep_in_dto   text,
    ep_out_dto  text,
    op_type     bit                        not null,
    op_code     varchar(50),
    create_time datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', 'Spec变更履历', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his'
go

exec sp_addextendedproperty 'MS_Description', 'js_Spec履历ID', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', 'js_SpecID', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN', 'spec_id'
go

exec sp_addextendedproperty 'MS_Description', 'Spec的内容信息', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN',
     'jolt_spec'
go

exec sp_addextendedproperty 'MS_Description', 'Spec名称', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN', 'spec_title'
go

exec sp_addextendedproperty 'MS_Description', 'SubProcess InputDTO ( From )（Json形式想定，用于备份）', 'SCHEMA', 'dbo', 'TABLE',
     'js_spec_his', 'COLUMN', 'ep_in_dto'
go

exec sp_addextendedproperty 'MS_Description', 'SubProcess OutputDTO( To )（Json形式想定，用于备份）', 'SCHEMA', 'dbo', 'TABLE',
     'js_spec_his', 'COLUMN', 'ep_out_dto'
go

exec sp_addextendedproperty 'MS_Description', '操作类型：
1 - 新规
2 - 变更
3 - 删除', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN', 'op_type'
go

exec sp_addextendedproperty 'MS_Description', '操作人(工号)', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN', 'op_code'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'js_spec_his', 'COLUMN', 'create_time'
go

