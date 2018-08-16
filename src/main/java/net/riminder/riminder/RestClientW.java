package net.riminder.riminder;

import com.google.gson.Gson;
import com.sun.jersey.api.client.*;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

import java.util.HashMap;
import java.util.Map;

public class RestClientW {

    private Client client;
    private String base_url;
    private Map<String, Object> default_headers;
    private Gson gson;

    public RestClientW(Client client, String base_url, Map<String, Object> default_headers)
    {
        this.client = client;
        this.base_url = base_url;
        this.default_headers = default_headers;
        this.gson = new Gson();
    }

    private WebResource.Builder fill_headers(WebResource.Builder ress)
    {
        for (Map.Entry<String, Object> ent : default_headers.entrySet())
        {
            ress = ress.header(ent.getKey(), ent.getValue());
        }
        return ress;
    }

    private WebResource fill_query(WebResource ress, Map<String, String> query)
    {
        if (query == null)
            return ress;

        for (Map.Entry<String, String> ent : query.entrySet())
        {
            ress = ress.queryParam(ent.getKey(), ent.getValue());
        }
        return ress;
    }


    public Map<String,Token> get(String endpoint) throws RiminderTransferException, RiminderResponseException {
        return get(endpoint, null);
    }

    public Map<String,Token> get(String endpoint, Map<String, String> query) throws RiminderResponseException, RiminderTransferException {
        WebResource ress = client.resource(this.base_url + endpoint);
        ress = fill_query(ress, query);
        WebResource.Builder bress = fill_headers(ress.getRequestBuilder());
        ClientResponse response = null;

        try
        {
            response = bress.get(ClientResponse.class);
        }catch (UniformInterfaceException exp)
        {
            throw new RiminderResponseException(exp);
        }catch (ClientHandlerException exp)
        {
            throw new RiminderTransferException("Error while handling request.", exp);
        }



        Map<String, Object> mapResponse = new HashMap<String, Object>();
        mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());

        return Token.fromResponse(mapResponse);
    }
}
