package mybabycare.api.integration.vaccination;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VaccinationApiClient {

    private static final Logger log = LoggerFactory.getLogger(VaccinationApiClient.class);

    private final RestTemplate restTemplate;
    private final VaccinationApiProperties properties;
    private final ObjectMapper objectMapper;

    public VaccinationApiClient(RestTemplate restTemplate, VaccinationApiProperties properties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public List<VaccinationPeriodResponse> getVaccinationPeriods(LocalDate birthday) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(properties.baseUrl() + properties.endpoint())
                .queryParam("serviceKey", properties.serviceKey())
                .queryParam("birth", birthday)
                .queryParam("numOfRows", properties.defaultRows())
                .queryParam("type", "json")
                .build(true)
                .toUri();

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(uri, String.class);
        } catch (RestClientException exception) {
            throw new IllegalStateException("Failed to call vaccination api", exception);
        }

        if (!response.hasBody() || response.getBody() == null || response.getBody().isBlank()) {
            return List.of();
        }

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode itemNodes = root.path("response").path("body").path("items").path("item");

            if (itemNodes.isMissingNode() || itemNodes.isNull()) {
                return List.of();
            }

            List<VaccinationPeriodResponse> periods = new ArrayList<>();
            if (itemNodes.isArray()) {
                for (JsonNode itemNode : itemNodes) {
                    addPeriodIfValid(periods, itemNode);
                }
            } else {
                addPeriodIfValid(periods, itemNodes);
            }

            return periods;
        } catch (RuntimeException exception) {
            throw new IllegalStateException("Failed to parse vaccination api response", exception);
        }
    }

    private void addPeriodIfValid(List<VaccinationPeriodResponse> periods, JsonNode itemNode) {
        try {
            periods.add(toPeriod(itemNode));
        } catch (RuntimeException exception) {
            log.warn("Skip malformed vaccination item: {}", itemNode, exception);
        }
    }

    private VaccinationPeriodResponse toPeriod(JsonNode itemNode) {
        return new VaccinationPeriodResponse(
                itemNode.path("vcnNm").asText(),
                parseDate(itemNode.path("inoculationSttDt").asText()),
                parseDate(itemNode.path("inoculationEndDt").asText()),
                itemNode.path("inoculationWk").asText()
        );
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid date format: " + value, exception);
        }
    }
}
