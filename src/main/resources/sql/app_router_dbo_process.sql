create table process
(
    id           bigint identity
        primary key,
    process_name varchar(255)               not null,
    process_flow xml                        not null,
    status       char     default N't'      not null,
    create_time  datetime default getdate() not null
)
go

exec sp_addextendedproperty 'MS_Description', '业务流程定义', 'SCHEMA', 'dbo', 'TABLE', 'process'
go

exec sp_addextendedproperty 'MS_Description', '业务流程ID', 'SCHEMA', 'dbo', 'TABLE', 'process', 'COLUMN', 'id'
go

exec sp_addextendedproperty 'MS_Description', '业务流程名称', 'SCHEMA', 'dbo', 'TABLE', 'process', 'COLUMN', 'process_name'
go

exec sp_addextendedproperty 'MS_Description', 'Camel component 配置（XML想定）', 'SCHEMA', 'dbo', 'TABLE', 'process',
     'COLUMN', 'process_flow'
go

exec sp_addextendedproperty 'MS_Description', '状态：
t - tentative 暂定(Default)
c - close 关闭
s - start 开启', 'SCHEMA', 'dbo', 'TABLE', 'process', 'COLUMN', 'status'
go

exec sp_addextendedproperty 'MS_Description', '创建日时', 'SCHEMA', 'dbo', 'TABLE', 'process', 'COLUMN', 'create_time'
go

INSERT INTO app_router.dbo.process (id, process_name, process_flow, status, create_time) VALUES (100019, N'checkSign', N'<route id="test_timer_router"><from uri="timer://foo?fixedRate=true&amp;period=6000&amp;repeatCount=10"/><to uri="bean:testBean?method=testBean"/><to uri="direct:check-sign"/></route>', N't', N'2022-01-07 02:56:44.457');
INSERT INTO app_router.dbo.process (id, process_name, process_flow, status, create_time) VALUES (100020, N'checkSign', N'<route id="sp-check-sign"><from uri="direct:check-sign"/><choice><when><simple>${header.CamelHttpMethod} == ''GET''</simple><to uri="direct:cypher-check-sign-get"/></when><when><simple>${header.CamelHttpMethod} == ''POST''</simple><to uri="direct:cypher-check-sign-post"/></when></choice></route>', N't', N'2022-01-07 02:56:44.517');
INSERT INTO app_router.dbo.process (id, process_name, process_flow, status, create_time) VALUES (100023, N'cypherCheckSignGet', N'<route id="sp-cypher-check-sign-get"><from uri="direct:cypher-check-sign-get"/><to uri="bean:cipherRequestProcess?method=assemblySignRequestByGet"/><to uri="grpc:localhost:8088/com.ford.ivisl.rpc.service.SignService?method=sign&amp;synchronous=true"/><to uri="bean:cipherResponseProcess?method=checkSignResponse"/></route>', N't', N'2022-01-07 02:56:44.533');
INSERT INTO app_router.dbo.process (id, process_name, process_flow, status, create_time) VALUES (100024, N'cypherCheckSignPost', N'<route id="sp-cypher-check-sign-post"><from uri="direct:cypher-check-sign-post"/><to uri="bean:cipherRequestProcess?method=assemblySignRequestByPost"/><to uri="grpc:localhost:8088/com.ford.ivisl.rpc.service.SignService?method=sign&amp;synchronous=true"/><to uri="bean:cipherResponseProcess?method=checkSignResponse"/></route>', N't', N'2022-01-07 02:56:44.550');
INSERT INTO app_router.dbo.process (id, process_name, process_flow, status, create_time) VALUES (100025, N'updateBody', N'<route id="updateBody"><from uri="direct:updateBody"/><setBody><simple>Hello 222</simple></setBody><to uri="log:hello"/></route>', N's', N'2022-01-07 07:41:09.233');
INSERT INTO app_router.dbo.process (id, process_name, process_flow, status, create_time) VALUES (100026, N'errorhandle', N'<onException><!-- the exception is full qualified names as plain strings --><!-- there can be more just add a 2nd, 3rd exception element (unbounded) --><exception>java.lang.Exception</exception><!-- we can set the redelivery policy here as well --><redeliveryPolicy maximumRedeliveries="1"/><!-- mark this as handled --><handled><constant>true</constant></handled><!-- let our order service handle this exception, call the orderFailed method --><bean ref="camelExceptionHandler" method="handleCamelExecutionException"/><!--        &lt;!&ndash; and since this is a unit test we use mock for assertions &ndash;&gt;--><!--        <to uri="mock:error" />--></onException>', N's', N'2022-01-10 09:52:35.927');