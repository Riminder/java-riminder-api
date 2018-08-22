package net.riminder.riminder.exp;

public class RiminderWebhookException extends RiminderException {

    private static final long serialVersionUID = 1L;

    public RiminderWebhookException(String message) {
        super(message);
    }

    public RiminderWebhookException(String message, Exception before) {
        super(message, before);
    }
}
