package mybabycare.api.integration.slack;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackClient {

    private final RestTemplate restTemplate;
    private final SlackProperties properties;

    public SlackClient(RestTemplate restTemplate, SlackProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public SlackSendResult sendMessage(SlackMessageRequest request) {
        String channel = StringUtils.hasText(request.channel()) ? request.channel() : properties.defaultChannel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.botToken());

        SlackApiRequest payload = new SlackApiRequest(channel, request.text());
        HttpEntity<SlackApiRequest> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<SlackApiResponse> response;
        try {
            response = restTemplate.postForEntity(
                    properties.baseUrl() + "/api/chat.postMessage",
                    entity,
                    SlackApiResponse.class
            );
        } catch (RestClientException exception) {
            return new SlackSendResult(false, null, "request_failed:" + exception.getClass().getSimpleName());
        }

        SlackApiResponse body = response.getBody();
        if (body == null) {
            return new SlackSendResult(false, null, "empty_response");
        }

        return new SlackSendResult(body.ok(), body.ts(), body.error());
    }

    private record SlackApiRequest(String channel, String text) {
    }

    private record SlackApiResponse(boolean ok, String ts, String error) {
    }
}
