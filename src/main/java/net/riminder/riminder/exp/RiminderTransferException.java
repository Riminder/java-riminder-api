package net.riminder.riminder.exp;

public class RiminderTransferException extends RiminderException {

    public RiminderTransferException(String message) {
        super(message);
    }

    public RiminderTransferException(String message, Exception before)
    {
        super(message, before);
    }
}
