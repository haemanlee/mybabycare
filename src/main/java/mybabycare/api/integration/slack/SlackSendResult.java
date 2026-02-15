package mybabycare.api.integration.slack;

public record SlackSendResult(boolean success, String messageTs, String error) {
}
