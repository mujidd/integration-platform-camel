package com.demo.integrationplatform.eventhub.client;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.azure.eventhubs.AzureActiveDirectoryTokenProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

class AuthCallback implements AzureActiveDirectoryTokenProvider.AuthenticationCallback {
    final private String clientId;
    final private String clientSecret;

    public AuthCallback(final String clientId, final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public CompletableFuture<String> acquireToken(String audience, String authority, Object state) {
        try {
            ConfidentialClientApplication app = ConfidentialClientApplication.builder(this.clientId, ClientCredentialFactory.createFromSecret(this.clientSecret))
                    .authority(authority)
                    .build();
            ClientCredentialParameters parameters = ClientCredentialParameters.builder(Collections.singleton(audience + ".default")).build();
            return app.acquireToken(parameters).thenApply((authResult) -> {
                return authResult.accessToken();
            });
        } catch (Exception e) {
            CompletableFuture<String> failed = new CompletableFuture<String>();
            failed.completeExceptionally(e);
            return failed;
        }
    }
}
