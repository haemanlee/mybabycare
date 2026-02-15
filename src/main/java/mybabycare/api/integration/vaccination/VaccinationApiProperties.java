package mybabycare.api.integration.vaccination;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.vaccination")
public record VaccinationApiProperties(
        String baseUrl,
        String endpoint,
        String serviceKey,
        int defaultPageSize
) {
}
