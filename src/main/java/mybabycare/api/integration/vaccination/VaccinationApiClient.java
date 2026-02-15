package mybabycare.api.integration.vaccination;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class VaccinationApiClient {

    private final RestTemplate restTemplate;
    private final VaccinationApiProperties properties;

    public VaccinationApiClient(RestTemplate restTemplate, VaccinationApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public List<VaccinationPeriodResponse> getVaccinationPeriods(LocalDate birthday) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(properties.baseUrl() + "/api/v1/vaccinations/periods")
                .queryParam("serviceKey", properties.serviceKey())
                .queryParam("birthday", birthday)
                .build(true)
                .toUri();

        ResponseEntity<VaccinationApiItem[]> response = restTemplate.getForEntity(uri, VaccinationApiItem[].class);
        VaccinationApiItem[] body = response.getBody();

        if (body == null || body.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(body)
                .map(item -> new VaccinationPeriodResponse(
                        item.vaccineName(),
                        item.startDate(),
                        item.endDate(),
                        item.targetAge()
                ))
                .toList();
    }

    private record VaccinationApiItem(
            String vaccineName,
            LocalDate startDate,
            LocalDate endDate,
            String targetAge
    ) {
    }
}
