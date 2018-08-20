package net.riminder.riminder.exp;

public class RiminderRequestException extends RiminderException {

    private static final long serialVersionUID = 1L;

    public RiminderRequestException(String message) {
        super(message);
    }

    public RiminderRequestException(String message, Exception before) {
        super(message, before);
    }
}
