package mybabycare.api.integration.vaccination;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class VaccinationApiClientTest {

    @Test
    void getVaccinationPeriods() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        VaccinationApiClient client = new VaccinationApiClient(
                restTemplate,
                new VaccinationApiProperties("https://public.example.com", "service-key", "/openapi/service/rest/ChildVaccsService/getChildVaccsList", 50),
                new ObjectMapper()
        );

        server.expect(requestTo("https://public.example.com/openapi/service/rest/ChildVaccsService/getChildVaccsList?serviceKey=service-key&birth=2024-01-15&numOfRows=50&type=json"))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "response": {
                            "body": {
                              "items": {
                                "item": [
                                  {
                                    "vcnNm": "B형간염",
                                    "inoculationSttDt": "2024-01-15",
                                    "inoculationEndDt": "2024-02-15",
                                    "inoculationWk": "0개월"
                                  }
                                ]
                              }
                            }
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        List<VaccinationPeriodResponse> periods = client.getVaccinationPeriods(LocalDate.of(2024, 1, 15));

        assertThat(periods).hasSize(1);
        assertThat(periods.get(0).vaccineName()).isEqualTo("B형간염");
        assertThat(periods.get(0).targetAge()).isEqualTo("0개월");
        server.verify();
    }

    @Test
    void getVaccinationPeriods_throwsWhenRequestFails() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        VaccinationApiClient client = new VaccinationApiClient(
                restTemplate,
                new VaccinationApiProperties("https://public.example.com", "service-key", "/openapi/service/rest/ChildVaccsService/getChildVaccsList", 50),
                new ObjectMapper()
        );

        server.expect(requestTo("https://public.example.com/openapi/service/rest/ChildVaccsService/getChildVaccsList?serviceKey=service-key&birth=2024-01-15&numOfRows=50&type=json"))
                .andExpect(method(GET))
                .andRespond(withServerError());

        assertThatThrownBy(() -> client.getVaccinationPeriods(LocalDate.of(2024, 1, 15)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to call vaccination api");

        server.verify();
    }


    @Test
    void getVaccinationPeriods_throwsWhenResponseJsonIsMalformed() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        VaccinationApiClient client = new VaccinationApiClient(
                restTemplate,
                new VaccinationApiProperties("https://public.example.com", "service-key", "/openapi/service/rest/ChildVaccsService/getChildVaccsList", 50),
                new ObjectMapper()
        );

        server.expect(requestTo("https://public.example.com/openapi/service/rest/ChildVaccsService/getChildVaccsList?serviceKey=service-key&birth=2024-01-15&numOfRows=50&type=json"))
                .andExpect(method(GET))
                .andRespond(withSuccess("{", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> client.getVaccinationPeriods(LocalDate.of(2024, 1, 15)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to parse vaccination api response");

        server.verify();
    }

    @Test
    void getVaccinationPeriods_skipsMalformedItemAndReturnsValidItems() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        VaccinationApiClient client = new VaccinationApiClient(
                restTemplate,
                new VaccinationApiProperties("https://public.example.com", "service-key", "/openapi/service/rest/ChildVaccsService/getChildVaccsList", 50),
                new ObjectMapper()
        );

        server.expect(requestTo("https://public.example.com/openapi/service/rest/ChildVaccsService/getChildVaccsList?serviceKey=service-key&birth=2024-01-15&numOfRows=50&type=json"))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "response": {
                            "body": {
                              "items": {
                                "item": [
                                  {
                                    "vcnNm": "B형간염",
                                    "inoculationSttDt": "2024-01-15",
                                    "inoculationEndDt": "2024-02-15",
                                    "inoculationWk": "0개월"
                                  },
                                  {
                                    "vcnNm": "DTaP",
                                    "inoculationSttDt": "2024/03/20",
                                    "inoculationEndDt": "2024-04-20",
                                    "inoculationWk": "2개월"
                                  }
                                ]
                              }
                            }
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        List<VaccinationPeriodResponse> periods = client.getVaccinationPeriods(LocalDate.of(2024, 1, 15));

        assertThat(periods).hasSize(1);
        assertThat(periods.get(0).vaccineName()).isEqualTo("B형간염");
        server.verify();
    }
}
