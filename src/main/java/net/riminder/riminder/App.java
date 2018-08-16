package net.riminder.riminder;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static class  pomm
    {
        public int lol;

        pomm(int lodddddl)
        {
            lol = lodddddl;
        }
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        Riminder api = new Riminder("ask_4b7fa33174a7113fbd16d806dbd21c07");
        try {
            Map<String, Token> tmp = api.getSource().list();
            Token tmp2 = tmp.get("data");
            List<Token> tmp3 = tmp2.getList();
            System.out.println(tmp3);
        } catch (RiminderTransferException e) {
            e.printStackTrace();
        } catch (RiminderResponseException e) {
            e.printStackTrace();
        } catch (RiminderResponseCastException e) {
            e.printStackTrace();
        }
    }
}
