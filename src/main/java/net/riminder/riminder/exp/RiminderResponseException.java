package net.riminder.riminder.exp;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class RiminderResponseException extends RiminderException {

    private static final long serialVersionUID = 1L;
	public int statusCode;
    public String reason;
    public String apiMessage;
    public HttpResponse response;


    private static  String PrepareMessage(HttpResponse response)
    {
        Gson gson = new Gson();
        int statusCode = response.getStatusLine().getStatusCode();
        String reason = response.getStatusLine().getReasonPhrase();
        String apiMessage = "...";

        try {
            Map<String, Object> mapResponse = new HashMap<String, Object>();
            String strBody = EntityUtils.toString(response.getEntity()); 
            
            mapResponse = gson.fromJson(strBody, mapResponse.getClass());
            apiMessage = mapResponse.get("message").toString();
        }catch (Exception e)
        {
            apiMessage = "Cannot parse api message: " + e.toString();
        }

        return String.format("%d: %s -> (%s)", statusCode, reason, apiMessage);
    }

    public RiminderResponseException(HttpResponse resp) {

        super(PrepareMessage(resp));
        Gson gson = new Gson();
        response = resp;
        this.statusCode = response.getStatusLine().getStatusCode();
        this.reason = response.getStatusLine().getReasonPhrase();
        this.apiMessage = "...";

        try {
            Map<String, Object> mapResponse = new HashMap<String, Object>();
            String strBody = EntityUtils.toString(response.getEntity());

            mapResponse = gson.fromJson(strBody, mapResponse.getClass());
            this.apiMessage = mapResponse.get("message").toString();
        }catch (Exception e)
        {
            this.apiMessage = "Cannot parse api message: " + e.toString();
        }
    }
}
