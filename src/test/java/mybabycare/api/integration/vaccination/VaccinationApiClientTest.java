package mybabycare.api.integration.vaccination;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        VaccinationApiClient client = new VaccinationApiClient(
                restTemplate,
                new VaccinationApiProperties("https://apis.data.go.kr", "/1471000/NIPTimetableService/getTimetableList", "service-key", 100),
                new ObjectMapper()
        );

        server.expect(requestTo("https://apis.data.go.kr/1471000/NIPTimetableService/getTimetableList?serviceKey=service-key&pageNo=1&numOfRows=100&_type=json"))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          \"response\": {
                            \"body\": {
                              \"items\": {
                                \"item\": [
                                  {
                                    \"vcnNm\": \"B형간염\",
                                    \"inocCnt\": \"1차\",
                                    \"inocTerm\": \"출생 시\"
                                  }
                                ]
                              }
                            }
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        List<VaccinationPeriodResponse> periods = client.getVaccinationPeriods(LocalDate.now().minusMonths(1));

        assertThat(periods).hasSize(1);
        assertThat(periods.get(0).vaccineName()).isEqualTo("B형간염");
        assertThat(periods.get(0).inoculationOrder()).isEqualTo("1차");
        assertThat(periods.get(0).recommendedPeriod()).isEqualTo("출생 시");
        server.verify();
    }
}
