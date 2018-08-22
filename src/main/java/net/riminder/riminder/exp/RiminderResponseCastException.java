package net.riminder.riminder.exp;

public class RiminderResponseCastException extends RiminderException {

    private static final long serialVersionUID = 1L;

	public RiminderResponseCastException(String message)
    {
        super(message);
    }

    public RiminderResponseCastException(String message, Exception e)
    {
        super(message, e);
    }
}
