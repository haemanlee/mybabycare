package mybabycare.api.integration.slack;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.POST;

class SlackClientTest {

    @Test
    void sendMessage() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        SlackClient client = new SlackClient(restTemplate, new SlackProperties("https://slack.com", "token", "#default"));

        server.expect(requestTo("https://slack.com/api/chat.postMessage"))
                .andExpect(method(POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andRespond(withSuccess("{\"ok\":true,\"ts\":\"123.45\",\"error\":null}", MediaType.APPLICATION_JSON));

        SlackSendResult result = client.sendMessage(new SlackMessageRequest(null, "hello"));

        assertThat(result.success()).isTrue();
        assertThat(result.messageTs()).isEqualTo("123.45");
        server.verify();
    }
}
