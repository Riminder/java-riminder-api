package net.riminder.riminder.route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.riminder.riminder.Ident;
import net.riminder.riminder.RestClientW;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

public class Filter {

    private RestClientW rclient;

    public Filter(RestClientW rclient)
    {
        this.rclient = rclient;
    }

    public List<Token> list() throws RiminderResponseCastException, RiminderTransferException, RiminderResponseException
    {
        return rclient.get("filters").get("data").getAsList();
    }

    public Map<String, Token> get(Ident filter_ident) throws RiminderResponseCastException, RiminderResponseException, RiminderTransferException
    {
        Map<String, String> query = new HashMap<>();
        query.put(filter_ident.getName(), filter_ident.getValue());

        return rclient.get("filter", query).get("data").getAsMap();
    }

}