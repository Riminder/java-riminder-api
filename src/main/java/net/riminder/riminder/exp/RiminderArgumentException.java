package net.riminder.riminder.exp;

public class RiminderArgumentException extends RiminderException {

    private static final long serialVersionUID = 1L;

	public RiminderArgumentException(String message) {
        super(message);
    }

    public RiminderArgumentException(String message, Exception before) {
        super(message, before);
    }
}
