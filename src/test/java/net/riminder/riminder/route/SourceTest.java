package net.riminder.riminder.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import net.riminder.riminder.TestHelper;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.response.Token;

public class SourceTest
{
    @Test
    public void listTest() throws RiminderException
    {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Source().list();
    }

    
    @Test
    public void getTest() throws RiminderException, Exception
    {
        TestHelper tHelper = TestHelper.getInstance();

        Map<String, Token> resp = tHelper.getApi().Source().get(tHelper.getSourceID());
        assertEquals(tHelper.getSourceID(), resp.get("source_id").<String>as());
    }

    @Test
    public void get_badID_Test() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try {
            tHelper.getApi().Source().get("tHelper.getSourceID()");
            
        } catch (RiminderException e) {
            return;
        }
       fail("A RiminderException should have been raised.");
    }
}