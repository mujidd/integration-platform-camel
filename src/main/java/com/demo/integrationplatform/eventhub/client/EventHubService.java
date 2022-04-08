package com.demo.integrationplatform.eventhub.client;


import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.apache.camel.Body;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service("eventHubService")
public class EventHubService{
    @Autowired
    public EventHubClient eventHubClient;

    public void send(@Body String body) throws EventHubException {
        if(StringUtils.isNotBlank(body)){
            byte[] payloadBytes = body.getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);
            eventHubClient.sendSync(sendEvent);
        }
    }

}
