package mybabycare.api.integration.vaccination;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class VaccinationApiClient {

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
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", properties.defaultPageSize())
                .queryParam("_type", "json")
                .build(true)
                .toUri();

        String payload = restTemplate.getForObject(uri, String.class);
        if (payload == null || payload.isBlank()) {
            return Collections.emptyList();
        }

        return extractItems(payload, birthday);
    }

    private List<VaccinationPeriodResponse> extractItems(String payload, LocalDate birthday) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode itemsNode = root.path("response").path("body").path("items").path("item");
            if (itemsNode.isMissingNode() || itemsNode.isNull()) {
                return Collections.emptyList();
            }

            if (itemsNode.isArray()) {
                List<VaccinationPeriodResponse> results = new ArrayList<>();
                for (JsonNode item : itemsNode) {
                    results.add(toResponse(item, birthday));
                }
                return results;
            }

            return List.of(toResponse(itemsNode, birthday));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse vaccination API response", e);
        }
    }

    private VaccinationPeriodResponse toResponse(JsonNode item, LocalDate birthday) {
        String vaccineName = readFirst(item, "vcnNm", "vaccineNm", "inocName");
        String inoculationOrder = readFirst(item, "inocCnt", "inoculationOrder", "doseNo");
        String recommendedPeriod = readFirst(item, "inocTerm", "inocPeriod", "recommendedPeriod");
        String targetAge = readFirst(item, "stdAge", "targetAge", "ageInfo");
        if (targetAge.isBlank()) {
            targetAge = calculateTargetAgeLabel(birthday);
        }

        return new VaccinationPeriodResponse(vaccineName, inoculationOrder, recommendedPeriod, targetAge);
    }

    private String calculateTargetAgeLabel(LocalDate birthday) {
        int months = Math.max(0, (int) Period.between(birthday, LocalDate.now()).toTotalMonths());
        return "생후 " + months + "개월";
    }

    private String readFirst(JsonNode node, String... fields) {
        for (String field : fields) {
            JsonNode value = node.get(field);
            if (value != null && !value.isNull() && !value.asText().isBlank()) {
                return value.asText();
            }
        }
        return "";
    }
}
