package com.demo.integrationplatform.eventhub.client;

import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class EventHubClientConfig {
    final String eventhub = "loginevents";
    final String authority = "https://login.chinacloudapi.cn/3953396c-814c-4dbe-b543-ae4999978206";
    final String clientId = "14f2bfb2-9a19-4c9a-9e19-4bc1319a119a"; // not needed to run with Managed Identity
    final String clientSecret = "15-2jni.F1_.B4GqN-OVtHXY13Z3uYK8-f"; // not needed to run with Managed Identity

    @Bean("eventHubClient")
    public EventHubClient createEventHubClient() throws IOException, InterruptedException, ExecutionException, URISyntaxException {
        java.net.URI namespace = new java.net.URI("https://ivislevents.servicebus.chinacloudapi.cn");
        final AuthCallback callback = new AuthCallback(clientId, clientSecret);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

        final EventHubClient ehClient = EventHubClient.createWithAzureActiveDirectory(namespace, eventhub, callback, authority, executorService, null).get();
        return ehClient;
    }
}
