package net.riminder.riminder.response;

import net.riminder.riminder.exp.RiminderResponseCastException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Token {
    private Object value;

    public Token(Object v)
    {
        value = v;
    }

    @Override
    public String toString()
    {
        if (value == null)
            return null;
        return value.toString();
    }

    public <T> T as() throws RiminderResponseCastException
    {
        try
        {
            return (T)value;
        }catch (ClassCastException e)
        {
            throw new RiminderResponseCastException("Can't getAs response value element: invalid type.", e);
        }
    }

    public Map<String, Token> asMap() throws RiminderResponseCastException
    {
        return this.as();
    }

    public List<Token> asList() throws RiminderResponseCastException
    {
        return this.as();
    }

    private static Token responseValueToToken(Object obj)
    {
        if (obj instanceof Map)
            return (new Token(responseMapToTokenMap((Map<String, Object>)obj)));
        if (obj instanceof List)
            return (new Token(responseListToTokenList((List<Object>)obj)));
        return new Token(obj);
    }

    private static List<Token> responseListToTokenList(List<Object> input)
    {
        List<Token> res = new LinkedList<>();
        for (Object obj : input)
        {
            res.add(responseValueToToken(obj));
        }
        return res;
    }

    private static Map<String, Token> responseMapToTokenMap(Map<String, Object> input)
    {
        HashMap<String, Token> res = new HashMap<>();
        for (Map.Entry<String, Object> entry: input.entrySet())
        {
            res.put(entry.getKey(), responseValueToToken(entry.getValue()));
        }
        return res;
    }

    public static Map<String, Token> fromResponse(Map<String, Object> input)
    {
        if (input == null)
            return null;
        return responseMapToTokenMap(input);
    }
}
