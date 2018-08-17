package net.riminder.riminder.route;

import net.riminder.riminder.RestClientW;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Source {

    private RestClientW rclient;

    public Source(RestClientW rclient)
    {
        this.rclient = rclient;
    }

    public List<Token> list() throws RiminderTransferException, RiminderResponseException, RiminderResponseCastException {
        return rclient.get("sources").get("data").getAsList();
    }

    public Map<String, Token> get(String source_id) throws RiminderTransferException, RiminderResponseException, RiminderResponseCastException {

        Map<String, String> query = new HashMap<>();
        query.put("source_id", source_id);

        return rclient.get("source", query).get("data").getAsMap();
    }
}

