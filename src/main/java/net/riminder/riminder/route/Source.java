package net.riminder.riminder.route;

import net.riminder.riminder.RestClientW;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

import java.util.Map;

public class Source {

    private RestClientW rclient;

    public Source(RestClientW rclient)
    {
        this.rclient = rclient;
    }

    public Map<String, Token> list() throws RiminderTransferException, RiminderResponseException {
        return rclient.get("sources");
    }
}

