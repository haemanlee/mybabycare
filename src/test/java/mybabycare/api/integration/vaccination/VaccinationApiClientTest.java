package mybabycare.api.integration.vaccination;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class VaccinationApiClientTest {

    @Test
    void getVaccinationPeriods() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        VaccinationApiClient client = new VaccinationApiClient(restTemplate,
                new VaccinationApiProperties("https://public.example.com", "service-key"));

        server.expect(requestTo("https://public.example.com/api/v1/vaccinations/periods?serviceKey=service-key&birthday=2024-01-15"))
                .andExpect(method(GET))
                .andRespond(withSuccess("[{\"vaccineName\":\"B형간염\",\"startDate\":\"2024-01-15\",\"endDate\":\"2024-02-15\",\"targetAge\":\"0개월\"}]",
                        MediaType.APPLICATION_JSON));

        List<VaccinationPeriodResponse> periods = client.getVaccinationPeriods(LocalDate.of(2024, 1, 15));

        assertThat(periods).hasSize(1);
        assertThat(periods.get(0).vaccineName()).isEqualTo("B형간염");
        server.verify();
    }
}
