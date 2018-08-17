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

    private  void check_response(ClientResponse response) throws RiminderResponseException {
        if (response.getStatusInfo().getStatusCode() >= 300)
            throw new RiminderResponseException(response);
    }


    public Map<String,Token> get(String endpoint) throws RiminderTransferException, RiminderResponseException {
        return get(endpoint, null);
    }

    private  interface ReqExecutor
    {
        ClientResponse execute();
    }

    private ClientResponse executeRequest(ReqExecutor executor) throws RiminderResponseException, RiminderTransferException {
        ClientResponse response;
        try
        {
            response = executor.execute();
        }catch (UniformInterfaceException exp)
        {
            throw new RiminderResponseException(exp);
        }catch (ClientHandlerException exp)
        {
            throw new RiminderTransferException("Error while handling request.", exp);
        }
        check_response(response);
        return response;
    }

    public Map<String,Token> get(String endpoint, Map<String, String> query) throws RiminderResponseException, RiminderTransferException {
        WebResource ress = client.resource(this.base_url + endpoint);
        ress = fill_query(ress, query);
        WebResource.Builder bress = fill_headers(ress.getRequestBuilder());

        ClientResponse response = executeRequest(() -> { return bress.get(ClientResponse.class); });

        Map<String, Object> mapResponse = new HashMap<String, Object>();
        mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());

        return Token.fromResponse(mapResponse);
    }

    public Map<String, Token> post(String endpoint, Map<String, Object> bodyparams) throws RiminderResponseException, RiminderTransferException {
        WebResource ress = client.resource(this.base_url + endpoint);
        WebResource.Builder bress = fill_headers(ress.getRequestBuilder());
        
        Gson gson = new Gson();
        String jsonbody = gson.toJson(bodyparams);

        ClientResponse response = executeRequest(() -> {
            return bress.post(ClientResponse.class, jsonbody);
        });

        Map<String, Object> mapResponse = new HashMap<String, Object>();
        mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());

        return Token.fromResponse(mapResponse);
    }

    // Todo : Finish it
    public Map<String, Token> postfile(String endpoint, Map<String, Object> bodyparams, String filepath)
            throws RiminderResponseException, RiminderTransferException {
        /*WebResource ress = client.resource(this.base_url + endpoint);
        WebResource.Builder bress = fill_headers(ress.getRequestBuilder());

        Gson gson = new Gson();
        String jsonbody = gson.toJson(bodyparams);

        ClientResponse response = executeRequest(() -> {
            return bress.post(ClientResponse.class, jsonbody);
        });

        Map<String, Object> mapResponse = new HashMap<String, Object>();
        mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());
        */
        return Token.fromResponse(null/*mapResponse*/);
    }


    public Map<String, Token> patch(String endpoint, Map<String, Object> bodyparams, String filepath)
            throws RiminderResponseException, RiminderTransferException {
        WebResource ress = client.resource(this.base_url + endpoint);
        WebResource.Builder bress = fill_headers(ress.getRequestBuilder());

        Gson gson = new Gson();
        String jsonbody = gson.toJson(bodyparams);

        ClientResponse response = executeRequest(() -> {
            return bress.method("PATCH", ClientResponse.class, jsonbody);
        });

        Map<String, Object> mapResponse = new HashMap<String, Object>();
        mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());

        return Token.fromResponse(mapResponse);
    }

    public static Map<String, String> add_with_default(Map<String, String> map, String key, String value, String def) {
        return add_with_default(map, key, value, def, true);
    }

    // Add a key value to a map
    // if the value is null a default value (def) is added instead
    // if default value is null and acceptnull is false, nothing will be added to
    // the map
    public static Map<String, String> add_with_default(Map<String, String> map, String key, String value, String def,
            Boolean acceptnull) {
        Map<String, Object> tmp = new HashMap<>();

        tmp = add_with_default(tmp, key, (Object)value, (Object)def, acceptnull);
        if (tmp.containsKey(key))
            map.put(key, (String)tmp.get(key));
        return map;
    }

    public static Map<String, Object> add_with_default(Map<String, Object> map, String key, Object value, Object def,
            Boolean acceptnull) {
        if (value != null)
            map.put(key, value);
        else if (def != null)
            map.put(key, def);
        else if (acceptnull)
            map.put(key, null);
        return map;
    }

}
