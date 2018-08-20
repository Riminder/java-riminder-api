package net.riminder.riminder.route;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

import com.google.gson.Gson;

import net.riminder.riminder.RestClientW;
import net.riminder.riminder.exp.RiminderArgumentException;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.exp.RiminderWebhookException;
import net.riminder.riminder.response.Token;

public class Webhooks {

    private RestClientW rclient;
    private String webhooksecret;
    private Map<String, MessageHandler> handlers;

    private static String HEADER_SIGNATURE_KEY = "HTTP-RIMINDER-SIGNATURE";

    public static class EventNames
    {
        public static final String PROFILE_PARSE_SUCCESS = "profile.parse.success";
        public static final String PROFILE_PARSE_ERROR = "profile.parse.error";
        public static final String PROFILE_SCORE_SUCCESS = "profile.score.success";
        public static final String PROFILE_SCORE_ERROR = "profile.score.error";
        public static final String FILTER_TRAIN_START = "filter.train.start";
        public static final String FILTER_TRAIN_SUCCESS = "filter.train.success";
        public static final String FILTER_TRAIN_ERROR = "filter.train.error";
        public static final String FILTER_SCORE_START = "filter.score.start";
        public static final String FILTER_SCORE_SUCCESS = "filter.score.success";
        public static final String FILTER_SCORE_ERROR = "filter.score.error";
        public static final String ACTION_RATING_SUCCESS = "action.rating.success";
        public static final String ACTION_RATING_ERROR = "action.rating.error";
        public static final String ACTION_STAGE_SUCCESS = "action.stage.success";
        public static final String ACTION_STAGE_ERROR = "action.stage.error";
    }

    public Webhooks(RestClientW rclient, String webhookSecret) {
        this.rclient = rclient;
        this.webhooksecret = webhookSecret;

        this.handlers = new HashMap<>();
        this.handlers.put(EventNames.PROFILE_PARSE_SUCCESS, null);
        this.handlers.put(EventNames.PROFILE_PARSE_ERROR, null);
        this.handlers.put(EventNames.PROFILE_SCORE_SUCCESS, null);
        this.handlers.put(EventNames.PROFILE_SCORE_ERROR, null);
        this.handlers.put(EventNames.FILTER_TRAIN_START, null);
        this.handlers.put(EventNames.FILTER_TRAIN_SUCCESS, null);
        this.handlers.put(EventNames.FILTER_TRAIN_ERROR, null);
        this.handlers.put(EventNames.FILTER_SCORE_START, null);
        this.handlers.put(EventNames.FILTER_SCORE_SUCCESS, null);
        this.handlers.put(EventNames.FILTER_SCORE_ERROR, null);
        this.handlers.put(EventNames.ACTION_RATING_SUCCESS, null);
        this.handlers.put(EventNames.ACTION_RATING_ERROR, null);
        this.handlers.put(EventNames.ACTION_STAGE_SUCCESS, null);
        this.handlers.put(EventNames.ACTION_STAGE_ERROR, null);

    }



    public static interface MessageHandler
    {
        public void handle(String eventName, Map<String, Token> token);
    }

    public Map<String,Token> check() throws RiminderException {
        return rclient.get("webhook/check").get("data").getAsMap();
    }

    public void setHandler(String eventName, MessageHandler handler) throws RiminderArgumentException
    {
        if (!handlers.containsKey(eventName))
            throw new RiminderArgumentException(String.format("'%s' is not a valid event name.", eventName));
        handlers.put(eventName, handler);
    }

    public Boolean isHandlerPresent(String eventName) throws RiminderArgumentException
    {
        if (!handlers.containsKey(eventName))
            throw new RiminderArgumentException(String.format("'%s' is not a valid event name.", eventName));
        return handlers.get(eventName) != null;
    }

    public void removeHandler(String eventName) throws RiminderArgumentException
    {
        if (!handlers.containsKey(eventName))
            throw new RiminderArgumentException(String.format("'%s' is not a valid event name.", eventName));
        handlers.put(eventName, null);
    }

    public void handle(Map<String, String> headers) throws RiminderArgumentException, RiminderWebhookException
    {
        if (!headers.containsKey(HEADER_SIGNATURE_KEY))
            throw new RiminderArgumentException(String.format("'%s' does not contain '%s'.", headers.toString(), HEADER_SIGNATURE_KEY));
        handle(headers.get(HEADER_SIGNATURE_KEY));
    }

    private String base64Deecode(String input) throws RiminderWebhookException
    {
        byte[] byte_input = Base64.getDecoder().decode(input);
        try {
			return new String(byte_input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
            throw new RiminderWebhookException("Cannot decode webhook message.", e); 
		}
    }

    private String customStrStr(String input, String to_change, String to)
    {
        String res = "";
        for (int i = 0; i < input.length(); i++)
        {
            char in_c = input.charAt(i);
            for (int j = 0; j < to_change.length(); j++)
            {
                char c_to_replace = to_change.charAt(j);
                if (in_c == c_to_replace && j < to.length())
                {
                    in_c = to.charAt(j);
                    break;
                }
            }
            res += in_c;
        }
        return res;
    }    

    private String base64urlDecode(String input) throws RiminderWebhookException
    {
        return base64Deecode(customStrStr(input, "-_", "+/"));
    }

    private Boolean isSignatureValid(String payload, String sign)
    {
        MessageDigest hasher;
		try {
			hasher = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			return false;
		}

        byte[] expected_sign_byte = hasher.digest(payload.getBytes());
        byte[] sign_byte = sign.getBytes();

        return expected_sign_byte.equals(sign_byte);
    }

    public void handle(String signatureHeader) throws RiminderWebhookException
    {
        String[] tmp = signatureHeader.split("\\.", 2);
        String sign = base64urlDecode(tmp[0]);
        String payload_json = base64urlDecode(tmp[1]);

        if (!isSignatureValid(payload_json, sign))
            throw new RiminderWebhookException("Invalid Signature.");
        
        Gson gson = new Gson();
        Map<String, Object> payload = new HashMap<>();
        payload = gson.fromJson(payload_json, payload.getClass());

        if (payload == null)
            throw new RiminderWebhookException("Invalid Message: no payload.");
        if (!payload.containsKey("type"))
            throw new RiminderWebhookException("Invalid Message: no type found.");
        if (!handlers.containsKey(payload.get("type")))
            throw new RiminderWebhookException(String.format("Invalid Message: type '%s' is invalid.", payload.get("type")));
        
        MessageHandler handler = handlers.get(payload.get("type"));
        if (handler != null)
            handler.handle((String)payload.get("type"), Token.fromResponse(payload));
        
    }
}