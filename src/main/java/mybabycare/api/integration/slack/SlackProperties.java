package mybabycare.api.integration.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.slack")
public record SlackProperties(
        String baseUrl,
        String botToken,
        String defaultChannel
) {
}
