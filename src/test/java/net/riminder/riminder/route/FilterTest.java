package net.riminder.riminder.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.riminder.riminder.Ident;
import net.riminder.riminder.TestHelper;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.response.Token;

public class FilterTest
{
    @Test
    public void listTest() throws RiminderException
    {
        TestHelper thelper = TestHelper.getInstance();

        thelper.getApi().Filter().list();
    }

    @Test
    public void getTest() throws RiminderException, Exception
    {
        TestHelper tHelper = TestHelper.getInstance();

        Map<String,Token> resp = tHelper.getApi().Filter().get(new Ident.ID(Ident.Filter, tHelper.getFilterID()));
        assertEquals(tHelper.getFilterID(), resp.get("filter_id").<String>as());
        assertEquals(tHelper.getFilterRef(), resp.get("filter_reference").<String>as());
    }

    @Test
    public void getTestRef() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        Map<String, Token> resp = tHelper.getApi().Filter().get(new Ident.Reference(Ident.Filter, tHelper.getFilterRef()));
        assertEquals(tHelper.getFilterID(), resp.get("filter_id").as());
        assertEquals(tHelper.getFilterRef(), resp.get("filter_reference").as());
    }

    @Test
    public void getTestWithInvalidID() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try {
            tHelper.getApi().Filter().get(new Ident.ID(Ident.Filter, "not an id"));
        } catch (RiminderException e) {
            return;
        }
        fail("A RiminderException should have been raised.");
    }
}