package net.riminder.riminder.exp;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.util.HashMap;
import java.util.Map;

public class RiminderResponseException extends RiminderException {

    private static final long serialVersionUID = 1L;
	public int statusCode;
    public String reason;
    public String apiMessage;
    public ClientResponse response;


    private static  String PrepareMessage(ClientResponse response)
    {
        Gson gson = new Gson();
        int statusCode = response.getStatusInfo().getStatusCode();
        String reason = response.getStatusInfo().getReasonPhrase();
        String apiMessage = "...";

        try {
            Map<String, Object> mapResponse = new HashMap<String, Object>();
            mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());
            apiMessage = mapResponse.get("message").toString();
        }catch (Exception e)
        {
            apiMessage = "Cannot parse api message: " + e.toString();
        }

        return String.format("%d: %s -> (%s)", statusCode, reason, apiMessage);
    }

    public RiminderResponseException(UniformInterfaceException exp) {

        super(PrepareMessage(exp.getResponse()), exp);
        Gson gson = new Gson();
        response = exp.getResponse();
        this.statusCode = response.getStatusInfo().getStatusCode();
        this.reason = response.getStatusInfo().getReasonPhrase();
        this.apiMessage = "...";

        try {
            Map<String, Object> mapResponse = new HashMap<String, Object>();
            mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());
            this.apiMessage = mapResponse.get("message").toString();
        }catch (Exception e)
        {
            this.apiMessage = "Cannot parse api message: " + e.toString();
        }
    }

    public RiminderResponseException(ClientResponse resp) {

        super(PrepareMessage(resp));
        Gson gson = new Gson();
        response = resp;
        this.statusCode = response.getStatusInfo().getStatusCode();
        this.reason = response.getStatusInfo().getReasonPhrase();
        this.apiMessage = "...";

        try {
            Map<String, Object> mapResponse = new HashMap<String, Object>();
            mapResponse = gson.fromJson(response.getEntity(String.class), mapResponse.getClass());
            this.apiMessage = mapResponse.get("message").toString();
        }catch (Exception e)
        {
            this.apiMessage = "Cannot parse api message: " + e.toString();
        }
    }
}
