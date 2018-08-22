package net.riminder.riminder.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import net.riminder.riminder.TestHelper;
import net.riminder.riminder.exp.RiminderArgumentException;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.exp.RiminderWebhookException;
import net.riminder.riminder.response.Token;
import net.riminder.riminder.route.Profile.Listoptions;

public class WebhookTest
{
    public Integer AA;
    // @Test
    // public void webhookCheckTest() throws RiminderException
    // {
    //     TestHelper thelper = TestHelper.getInstance();

    //     thelper.getApi().Webhooks().check();
    // }

    private class TestPayload
    {
        Integer a;
        String type;
        String pomme;
    }
    
    private Map<String, String> gen_encoded_header(String key) throws NoSuchAlgorithmException, InvalidKeyException
    {
        Gson gson = new Gson();
        TestPayload payload = new TestPayload();
        payload.a = 25;
        payload.pomme = "rouge";
        payload.type = Webhooks.EventNames.PROFILE_PARSE_SUCCESS;

        String json_payload = gson.toJson(payload);


        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String sign = Base64.encodeBase64String(sha256_HMAC.doFinal(json_payload.getBytes()));
        String encoded_payload = Base64.encodeBase64String(json_payload.getBytes());
        String headerValue = sign + "." + encoded_payload;

        Map<String, String> result = new HashMap<>();
        result.put("HTTP-RIMINDER-SIGNATURE", headerValue);
        return result;
    }

    @Test    
    public void handlingTest() throws InvalidKeyException, NoSuchAlgorithmException, RiminderArgumentException, RiminderWebhookException
    {
        TestHelper thelper = TestHelper.getInstance();
        AA = 0;
        
        Map<String, String> header = gen_encoded_header(thelper.getWebhookSecret());
        thelper.getApi().Webhooks().setHandler(Webhooks.EventNames.PROFILE_PARSE_SUCCESS, (String eventName, Map<String, Token> body) -> {
            assertEquals(Webhooks.EventNames.PROFILE_PARSE_SUCCESS, eventName);
            AA = 1;
            try {
                assertEquals("rouge", body.get("pomme").<String>as());
			} catch (RiminderResponseCastException e) {
				fail("Cast should not fail.");
			}
        });

        thelper.getApi().Webhooks().handle(header);
        assertEquals(new Integer(1), AA);
    }

    @Test
    public void handlingwthStringTest()
            throws InvalidKeyException, NoSuchAlgorithmException, RiminderArgumentException, RiminderWebhookException {
        TestHelper thelper = TestHelper.getInstance();
        AA = 0;

        Map<String, String> header = gen_encoded_header(thelper.getWebhookSecret());
        thelper.getApi().Webhooks().setHandler(Webhooks.EventNames.PROFILE_PARSE_SUCCESS,
                (String eventName, Map<String, Token> body) -> {
                    assertEquals(Webhooks.EventNames.PROFILE_PARSE_SUCCESS, eventName);
                    AA = 1;
                    try {
                        assertEquals("rouge", body.get("pomme").<String>as());
                    } catch (RiminderResponseCastException e) {
                        fail("Cast should not fail.");
                    }
                });

        thelper.getApi().Webhooks().handle(header.get("HTTP-RIMINDER-SIGNATURE"));
        assertEquals(new Integer(1), AA);
    }

    @Test
    public void HelperHandlerTest()
            throws InvalidKeyException, NoSuchAlgorithmException, RiminderArgumentException, RiminderWebhookException {
        TestHelper thelper = TestHelper.getInstance();

        Map<String, String> header = gen_encoded_header(thelper.getWebhookSecret());
        thelper.getApi().Webhooks().setHandler(Webhooks.EventNames.PROFILE_PARSE_SUCCESS,
                (String eventName, Map<String, Token> body) -> {
                    fail("There should not be any handler.");
                });
        assertTrue(thelper.getApi().Webhooks().isHandlerPresent(Webhooks.EventNames.PROFILE_PARSE_SUCCESS));
        thelper.getApi().Webhooks().removeHandler(Webhooks.EventNames.PROFILE_PARSE_SUCCESS);
        thelper.getApi().Webhooks().handle(header.get("HTTP-RIMINDER-SIGNATURE"));
    }
}