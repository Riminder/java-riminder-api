package net.riminder.riminder.exp;

import net.riminder.riminder.Riminder;

public class RiminderException extends Exception {

    public RiminderException(String message)
    {
        super(message);
    }

    public RiminderException(String message, Exception before)
    {
        super(message, before);
    }
}
