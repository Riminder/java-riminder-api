package net.riminder.riminder.exp;

public class RiminderTransferException extends RiminderException {

    private static final long serialVersionUID = 1L;

	public RiminderTransferException(String message) {
        super(message);
    }

    public RiminderTransferException(String message, Exception before)
    {
        super(message, before);
    }
}
