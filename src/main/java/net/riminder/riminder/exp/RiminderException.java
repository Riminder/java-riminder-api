package net.riminder.riminder.exp;

public class RiminderException extends Exception {

    private static final long serialVersionUID = 1L;

	public RiminderException(String message)
    {
        super(message);
    }

    public RiminderException(String message, Exception before)
    {
        super(message, before);
    }
}
