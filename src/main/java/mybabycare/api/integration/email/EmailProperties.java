package mybabycare.api.integration.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.email")
public record EmailProperties(
        String baseUrl,
        String apiKey,
        String sender
) {
}
