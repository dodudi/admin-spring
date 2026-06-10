package kr.it.rudy.admin.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class AuthServerClientConfig {

    private static final String M2M_REGISTRATION_ID = "m2m-client";

    @Value("${auth-server.base-url}")
    private String baseUrl;

    @Bean
    public OAuth2AuthorizedClientManager m2mAuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        manager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build()
        );
        return manager;
    }

    @Bean
    public RestClient authServerRestClient(OAuth2AuthorizedClientManager m2mAuthorizedClientManager) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor((request, body, execution) -> {
                    OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                            .withClientRegistrationId(M2M_REGISTRATION_ID)
                            .principal(M2M_REGISTRATION_ID)
                            .build();
                    OAuth2AuthorizedClient client = m2mAuthorizedClientManager.authorize(authorizeRequest);
                    if (client != null && client.getAccessToken() != null) {
                        request.getHeaders().setBearerAuth(client.getAccessToken().getTokenValue());
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
