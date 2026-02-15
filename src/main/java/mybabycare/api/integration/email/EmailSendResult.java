package mybabycare.api.integration.email;

public record EmailSendResult(boolean success, String requestId, String errorMessage) {
}
