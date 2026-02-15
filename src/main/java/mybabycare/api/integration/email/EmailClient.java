package mybabycare.api.integration.email;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailClient {

    private final RestTemplate restTemplate;
    private final EmailProperties properties;

    public EmailClient(RestTemplate restTemplate, EmailProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public EmailSendResult send(EmailSendRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", properties.apiKey());

        EmailApiRequest payload = new EmailApiRequest(properties.sender(), request.to(), request.subject(), request.body());
        HttpEntity<EmailApiRequest> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<EmailApiResponse> response;
        try {
            response = restTemplate.postForEntity(
                    properties.baseUrl() + "/v1/messages",
                    entity,
                    EmailApiResponse.class
            );
        } catch (RestClientException exception) {
            return new EmailSendResult(false, null, "request_failed:" + exception.getClass().getSimpleName());
        }

        EmailApiResponse body = response.getBody();
        if (body == null) {
            return new EmailSendResult(false, null, "empty_response");
        }

        return new EmailSendResult(body.success(), body.requestId(), body.errorMessage());
    }

    private record EmailApiRequest(String from, String to, String subject, String body) {
    }

    private record EmailApiResponse(boolean success, String requestId, String errorMessage) {
    }
}
