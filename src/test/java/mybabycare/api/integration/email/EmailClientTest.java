package mybabycare.api.integration.email;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class EmailClientTest {

    @Test
    void sendEmail() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        EmailClient client = new EmailClient(restTemplate, new EmailProperties("https://mail.example.com", "api-key", "from@example.com"));

        server.expect(requestTo("https://mail.example.com/v1/messages"))
                .andExpect(method(POST))
                .andExpect(header("X-Api-Key", "api-key"))
                .andRespond(withSuccess("{\"success\":true,\"requestId\":\"req-1\",\"errorMessage\":null}", MediaType.APPLICATION_JSON));

        EmailSendResult result = client.send(new EmailSendRequest("to@example.com", "subject", "body"));

        assertThat(result.success()).isTrue();
        assertThat(result.requestId()).isEqualTo("req-1");
        server.verify();
    }
}
