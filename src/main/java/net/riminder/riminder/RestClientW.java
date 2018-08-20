package net.riminder.riminder;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import net.riminder.riminder.exp.RiminderArgumentException;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.exp.RiminderRequestException;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClientW {

    private HttpClient client;
    private String base_url;
    private Map<String, Object> default_headers;
    private Gson gson;

    private static List<Header> prepare_headers(Map<String, Object> defaultHeaders) {
        List<Header> res = new ArrayList<>();

        for (Map.Entry<String, Object> ent : defaultHeaders.entrySet()) {
            Header hder = new BasicHeader(ent.getKey(), ent.getValue().toString());
            res.add(hder);
        }
        return res;
    }

    private static HttpClient prepare_client(String base_url, Map<String, Object> default_headers)
    {
        HttpClientBuilder bclient = HttpClients.custom();
        bclient.setDefaultHeaders(prepare_headers(default_headers));

        return bclient.build();
    }

    public RestClientW(String base_url, Map<String, Object> default_headers)
    {
    
        this.client = prepare_client(base_url, default_headers);
        this.base_url = base_url;
        this.default_headers = default_headers;
        this.gson = new Gson();
    }

    private URI build_uri_query(String url, Map<String, String> query) throws URISyntaxException
    {
        URIBuilder res = new URIBuilder(url);
        if (query == null)
            return res.build();

        for (Map.Entry<String, String> ent : query.entrySet())
        {
            res.addParameter(ent.getKey(), ent.getValue());
        }
        return res.build();
    }

    private  void check_response(HttpResponse response) throws RiminderResponseException {
        if (response.getStatusLine().getStatusCode() >= 300)
            throw new RiminderResponseException(response);
    }


    public Map<String,Token> get(String endpoint) throws RiminderException {
        return get(endpoint, null);
    }

    private  interface ReqExecutor
    {
        HttpResponse execute() throws IOException;
    }

    private HttpResponse executeRequest(ReqExecutor executor) throws RiminderException {
        HttpResponse response;
        try
        {
            response = executor.execute();
        }catch (IOException exp)
        {
            throw new RiminderTransferException("Error during request handling!", exp);
        }
        check_response(response);
        return response;
    }


    private String gen_url_final(String endpoint)
    {
        return this.base_url + endpoint;
    }

    public Map<String, Object> response_to_map(HttpResponse response) throws RiminderException
    {
        Map<String, Object> mapResponse = new HashMap<String, Object>();
        String strBody;
		try {
			strBody = EntityUtils.toString(response.getEntity());
		} catch (ParseException | IOException e) {
			throw new RiminderRequestException("Cannot create request: ", e);
		}

        mapResponse = gson.fromJson(strBody, mapResponse.getClass());
        return mapResponse;
    }

    public Map<String,Token> get(String endpoint, Map<String, String> query) throws RiminderException {

        String final_url = gen_url_final(endpoint);
        URI uri;
        try {
            uri = build_uri_query(final_url, query);
        } catch (URISyntaxException e) {
            throw new RiminderArgumentException(String.format("URI: %s is invalid", final_url), e);
        }

        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = executeRequest(() -> { return this.client.execute(httpget); });

        Map<String, Object> mapResponse = response_to_map(response);
        return Token.fromResponse(mapResponse);
    }

    public Map<String, Token> post(String endpoint, Map<String, Object> bodyparams) throws RiminderException {
        String final_url = gen_url_final(endpoint);
        URI uri;
        try {
            uri = new URIBuilder(final_url).build();
        } catch (URISyntaxException e) {
            throw new RiminderArgumentException(String.format("URI: %s is invalid", final_url), e);
        }
        
        String jsonbody = this.gson.toJson(bodyparams);

        HttpPost httppost = new HttpPost(uri);
        try {
            httppost.setEntity(new StringEntity(jsonbody));
        } catch (UnsupportedEncodingException e) {
            throw new RiminderRequestException("Cannot create request!", e);
        }      
        httppost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpResponse response = executeRequest(() -> {
            return this.client.execute(httppost);
        });

        Map<String, Object> mapResponse = response_to_map(response);
        return Token.fromResponse(mapResponse);
    }

    MultipartEntityBuilder fill_multiparts(MultipartEntityBuilder multbuilder, Map<String, Object> bodyparams)
    {
        for (Map.Entry<String, Object> ent : bodyparams.entrySet())
        {
            String str_value;
            str_value = this.gson.toJson(ent.getValue());

            multbuilder.addTextBody(ent.getKey(), str_value);
        }
        return multbuilder;
    }

    public Map<String, Token> postfile(String endpoint, Map<String, Object> bodyparams, String filepath) throws RiminderException {
        String final_url = gen_url_final(endpoint);
        URI uri;
        try {
            uri = new URIBuilder(final_url).build();
        } catch (URISyntaxException e) {
            throw new RiminderArgumentException(String.format("URI: %s is invalid", final_url), e);
        }

        File file = new File(filepath);
        FileEntity filebody = new FileEntity(file);

        HttpPost httppost = new HttpPost(uri);
        MultipartEntityBuilder multbuilder = MultipartEntityBuilder.create();
        multbuilder.addBinaryBody("file", file);
        multbuilder = fill_multiparts(multbuilder, bodyparams);
        httppost.setEntity(multbuilder.build());

        HttpResponse response = executeRequest(() -> {
            return this.client.execute(httppost);
        });

        Map<String, Object> mapResponse = response_to_map(response);
        return Token.fromResponse(mapResponse);
    }


    public Map<String, Token> patch(String endpoint, Map<String, Object> bodyparams) throws RiminderException {
        String final_url = gen_url_final(endpoint);
        URI uri;
		try {
			uri = new URIBuilder(final_url).build();
		} catch (URISyntaxException e) {
			throw new RiminderArgumentException(String.format("URI: %s is invalid", final_url), e);
		}

        String jsonbody = this.gson.toJson(bodyparams);

        HttpPatch httppatch = new HttpPatch(uri);
        try {
			httppatch.setEntity(new StringEntity(jsonbody));
		} catch (UnsupportedEncodingException e) {
			throw new RiminderRequestException("Cannot create request!", e);
		}
        httppatch.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpResponse response = executeRequest(() -> {
            return this.client.execute(httppatch);
        });

        Map<String, Object> mapResponse = response_to_map(response);
        return Token.fromResponse(mapResponse);
    }

    public static Map<String, Object> add_with_default(Map<String, Object> map, String key, Object value, Object def) {
        return add_with_default(map, key, value, def, true);
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

    public static Map<String, String> add_with_defaultso(Map<String, String> map, String key, Object value, Object def,
            Boolean acceptnull) {
        if (value != null)
            map.put(key, value.toString());
        else if (def != null)
            map.put(key, def.toString());
        else if (acceptnull)
            map.put(key, null);
        return map;
    }

}
