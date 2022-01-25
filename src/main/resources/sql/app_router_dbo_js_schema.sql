create table js_schema
(
    id             bigint identity
        primary key,
    schema_content text                                         not null,
    schema_title   varchar(255)                                 not null
        constraint UK_js_schem_1
            unique,
    schema_version varchar(50)
        constraint DF__js_schema__schem__17036CC0 default N'V7' not null,
    hash_code      varchar(255)                                 not null,
    ep_in_dto      text,
    create_time    datetime default getdate()                   not null
)
go

exec sp_addextendedproperty 'MS_Description', 'JsonSchema定义', 'SCHEMA', 'dbo', 'TABLE', 'js_schema'
go

exec sp_addextendedproperty 'MS_Description', 'SchemaID', 'SCHEMA', 'dbo', 'TABLE', 'js_schema', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', 'SCHEMA名称', 'SCHEMA', 'dbo', 'TABLE', 'js_schema', 'COLUMN',
     'schema_title'
go

exec sp_addextendedproperty 'MS_Description', '目前为止支持的版本及内部值：
V201909 - 8
V7 - 4 (default)
V6 - 2
V4 - 1', 'SCHEMA', 'dbo', 'TABLE', 'js_schema', 'COLUMN', 'schema_version'
go

exec sp_addextendedproperty 'MS_Description', '散列值，做唯一区分', 'SCHEMA', 'dbo', 'TABLE', 'js_schema', 'COLUMN', 'hash_code'
go

exec sp_addextendedproperty 'MS_Description', 'Process InputDTO（Json形式想定，用于备份）', 'SCHEMA', 'dbo', 'TABLE', 'js_schema',
     'COLUMN', 'ep_in_dto'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'js_schema', 'COLUMN', 'create_time'
go

INSERT INTO app_router.dbo.js_schema (id, schema_content, schema_title, schema_version, hash_code, ep_in_dto, create_time) VALUES (100002, N'{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "title": "The test schema",
  "description": "schema for test",
  "examples": [
    {
      "refreshToken": "ZmRzYWZkc2FmZHNhZg==11",
      "vin": "LVSHFCAC012341234"
    }
  ],
  "required": [
    "header"
  ],
  "properties": {
    "refreshToken": {
      "$id": "#/properties/refreshToken",
      "type": "string"
    },
    "vin": {
      "$id": "#/properties/vin",
      "type": "string",
      "pattern" : "([\\s\\S]{1,50})",
      "minLength": 1,
      "maxLength": 50
    }
  }
}', N'schema_test', N'V7', N'123', null, N'2021-12-28 08:01:02.490');