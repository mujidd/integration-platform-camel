create table js_spec
(
    id          bigint identity
        primary key,
    jolt_spec   text                       not null,
    spec_title  varchar(255)
        constraint UK_js_spec_1
            unique,
    ep_in_dto   text,
    ep_out_dto  text,
    create_time datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', 'JOLT形式的spec定义', 'SCHEMA', 'dbo', 'TABLE', 'js_spec'
go

exec sp_addextendedproperty 'MS_Description', 'js_SpecID', 'SCHEMA', 'dbo', 'TABLE', 'js_spec', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', 'Spec的内容信息', 'SCHEMA', 'dbo', 'TABLE', 'js_spec', 'COLUMN', 'jolt_spec'
go

exec sp_addextendedproperty 'MS_Description', 'Spec名称', 'SCHEMA', 'dbo', 'TABLE', 'js_spec', 'COLUMN', 'spec_title'
go

exec sp_addextendedproperty 'MS_Description', 'SubProcess InputDTO ( From )（Json形式想定，用于备份）', 'SCHEMA', 'dbo', 'TABLE',
     'js_spec', 'COLUMN', 'ep_in_dto'
go

exec sp_addextendedproperty 'MS_Description', 'SubProcess OutputDTO( To )（Json形式想定，用于备份）', 'SCHEMA', 'dbo', 'TABLE',
     'js_spec', 'COLUMN', 'ep_out_dto'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'js_spec', 'COLUMN', 'create_time'
go

INSERT INTO app_router.dbo.js_spec (id, jolt_spec, spec_title, ep_in_dto, ep_out_dto, create_time) VALUES (100001, N'[
  {
    "operation": "shift",
    "spec": {
      "body": {
        "@": "&0"
      },
      "header": {
        "guid": "body.guid",
        "openid": "body.openid"
      }
    }
  }
]', N'spec_test', null, null, N'2021-12-24 08:43:46.090');
INSERT INTO app_router.dbo.js_spec (id, jolt_spec, spec_title, ep_in_dto, ep_out_dto, create_time) VALUES (100002, N'[
  {
    "operation": "shift",
    "spec": {
      "body": {
        "@": "&0"
      },
      "header": {
        "guid": "body.guid",
        "test": "body.test"
      }
    }
  }
]', N'spec_test1', null, null, N'2021-12-29 08:57:41.300');