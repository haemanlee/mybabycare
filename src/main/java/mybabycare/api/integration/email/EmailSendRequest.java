package mybabycare.api.integration.email;

public record EmailSendRequest(String to, String subject, String body) {
}
