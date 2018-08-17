package net.riminder.riminder;

import com.sun.jersey.api.client.Client;

import net.riminder.riminder.route.Filter;
import net.riminder.riminder.route.Profile;
import net.riminder.riminder.route.Source;
import net.riminder.riminder.route.Webhooks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Riminder {

    private static String DEFAULT_HOST = "https://www.riminder.net/sf/public/api/";
    private static String DEFAULT_HOST_BASE = "v1.0/";

    private Map<String, Object> headers;

    private String base_uri;
    private String webhook_secret;
    private String api_secret;
    private Client client;
    private RestClientW rclient;

    private Source source;
    private Filter filter;
    private Profile profile;
    private Webhooks webhooks;



    public Riminder(String apiSecret)
    {
        this(apiSecret, null, null);
    }

    public Riminder(String apiSecret, String webhookSecret)
    {
        this(apiSecret, webhookSecret, null);
    }


    private String putvalueWithDefault(String value, String defaultvalue)
    {
        if (value != null)
            return value;
        return defaultvalue;
    }

    public Riminder(String apiSecret, String webhookSecret, String url)
    {
        this.base_uri = putvalueWithDefault(url, DEFAULT_HOST + DEFAULT_HOST_BASE);
        this.webhook_secret = webhookSecret;
        this.api_secret = apiSecret;

        this.headers = new HashMap<>();
        this.headers.put("X-API-KEY", this.api_secret);

        this.client = new Client();
        this.rclient = new RestClientW(this.client, this.base_uri, this.headers);

        this.source = new Source(this.rclient);
        this.filter = new Filter(this.rclient);
        this.profile = new Profile(this.rclient);
        this.webhooks = new Webhooks(this.rclient, this.webhook_secret);
    }

    public Source Source()
    {
        return this.source;
    }

    public Filter Filter()
    {
        return this.filter;
    }

    public Profile Profile()
    {
        return this.profile;
    }

    public Webhooks Webhooks()
    {
        return this.webhooks;
    }
}
