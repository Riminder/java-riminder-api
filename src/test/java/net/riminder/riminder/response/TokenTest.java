package net.riminder.riminder.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.riminder.riminder.exp.RiminderResponseCastException;

public class TokenTest
{
    @Test
    public void fromResponseokTest() throws RiminderResponseCastException
    {
        Map<String, Object> testmap = new HashMap<>();
        testmap.put("zap", "zop");
        testmap.put("zip", "zep");
        List<String> tmp = new ArrayList<>();
        tmp.add("Salvor Hardin");
        tmp.add("Hober Mallow");
        testmap.put("maires", tmp);
        Map<String, Object> tmp2 = new HashMap<>();
        tmp2.put("rooor", 2345);
        testmap.put("tp2", tmp2);

        Map<String, Token> token = Token.fromResponse(testmap);
        assertEquals(testmap.get("zap"), token.get("zap").as());
        assertEquals(testmap.get("zip"), token.get("zip").as());
        assertEquals(((List)testmap.get("maires")).get(0), token.get("maires").asList().get(0).as());
        assertEquals(((List) testmap.get("maires")).get(1), token.get("maires").asList().get(1).as());
        assertEquals(((Map)testmap.get("tp2")).get("rooor"), token.get("tp2").asMap().get("rooor").as());
    }

    @Test
    public void fromResponsewithnullTest() throws RiminderResponseCastException {
        Map<String, Object> testmap = new HashMap<>();
        testmap.put("zap", null);
        testmap.put("zip", "zep");
        List<String> tmp = new ArrayList<>();
        tmp.add("Salvor Hardin");
        tmp.add("Hober Mallow");
        testmap.put("maires", tmp);
        Map<String, Object> tmp2 = new HashMap<>();
        tmp2.put("rooor", null);
        testmap.put("tp2", tmp2);

        Map<String, Token> token = Token.fromResponse(testmap);
        assertEquals(testmap.get("zap"), token.get("zap").as());
        assertEquals(testmap.get("zip"), token.get("zip").as());
        assertEquals(((List) testmap.get("maires")).get(0), token.get("maires").asList().get(0).as());
        assertEquals(((List) testmap.get("maires")).get(1), token.get("maires").asList().get(1).as());
        assertEquals(((Map) testmap.get("tp2")).get("rooor"), token.get("tp2").asMap().get("rooor").as());
    }

    @Test
    public void fromResponsenullinputTest() throws RiminderResponseCastException {
        Map<String, Object> testmap = null;

        Map<String, Token> token = Token.fromResponse(testmap);
        if (token == null)
            assertNull(token);
        else
            assertNull(token);
    }

    @Test
    public void fromResponseNestedTest() throws RiminderResponseCastException
    {
        Map<String, Object> testmap = new HashMap<>();
        testmap.put("zap", "zop");
        testmap.put("zip", "zep");
        List<String> tmp = new ArrayList<>();
        tmp.add("Salvor Hardin");
        tmp.add("Hober Mallow");
        Map<String, Object> tmp2 = new HashMap<>();
        tmp2.put("rooor", 1243);
        tmp2.put("maires", tmp);
        testmap.put("tp2", tmp2);
        
        Map<String, Token> token = Token.fromResponse(testmap);
        assertEquals(testmap.get("zap"), token.get("zap").as());
        assertEquals(testmap.get("zip"), token.get("zip").as());
        List<String> base_maire = (List<String>)((Map)testmap.get("tp2")).get("maires");
        List<Token> token_maire = token.get("tp2").asMap().get("maires").asList();

        assertEquals(base_maire.get(0), token_maire.get(0).as());
        assertEquals(base_maire.get(1), token_maire.get(1).as());
        assertEquals(((Map) testmap.get("tp2")).get("rooor"), token.get("tp2").asMap().get("rooor").as());
    }
}