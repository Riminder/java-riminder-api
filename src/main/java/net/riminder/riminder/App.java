package net.riminder.riminder;


import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.route.Profile.Listoptions;

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

        try {
           Riminder api = new Riminder("ask_110bf53876034bf0546b693d1a07a515");
           Listoptions lo = new Listoptions();
           lo.source_ids.add("fe6d7a2aa9125259a5ecf7905154a0396a891c06");
            Object resp = api.Profile().Rating().set("fe6d7a2aa9125259a5ecf7905154a0396a891c06", new Ident.ID(Ident.Profile, "7cd6d0447da2b988b4c00aee0ce2175fec08d6c6"), new Ident.Reference(Ident.Filter, "12345"), 1);
            System.out.println(resp);

        } catch (RiminderException e) {
            e.printStackTrace();
        }
    }
}
