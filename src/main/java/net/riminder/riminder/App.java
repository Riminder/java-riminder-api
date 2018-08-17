package net.riminder.riminder;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import net.riminder.riminder.exp.RiminderException;
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
    public static void main( String[] args ) {
        System.out.println("Hello World!");

        Riminder api = new Riminder("ask_110bf53876034bf0546b693d1a07a515");
        try {
            Map<String, Token> tmp = api.Profile().Stage().set("fe6d7a2aa9125259a5ecf7905154a0396a891c06", 
                new Ident.ID(Ident.Profile, "91b2efbde125bf179a6db4ea5ad4dca3c4ee04d4"),
                new Ident.Reference(Ident.Filter, "dddd"),
                Constants.Stage.NEW);
            System.out.println(tmp);
        } catch (RiminderException e) {
            e.printStackTrace();
        }
    }
}
