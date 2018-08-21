package net.riminder.riminder.route;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import net.riminder.riminder.Constants;
import net.riminder.riminder.Ident;
import net.riminder.riminder.TestHelper;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.route.Profile.Listoptions;
import net.riminder.riminder.route.Profile.TrainingMetadata;

public class ProfileTest
{

    @Test
    public void listminTest() throws Exception
    {
        TestHelper tHelper = TestHelper.getInstance();

        Listoptions listoptions = new Listoptions();
        listoptions.source_ids.add(tHelper.getSourceID());
        
        tHelper.getApi().Profile().list(listoptions);
    }

    @Ignore("Ignored due to symfony error.")
    @Test
    public void listnofilterTest() throws Exception
    {
        TestHelper tHelper = TestHelper.getInstance();
        Date date = new Date();

        Listoptions listoptions = new Listoptions();
        listoptions.source_ids.add(tHelper.getSourceID());
        listoptions.seniority = Constants.Seniority.JUNIOR;
        listoptions.date_end = date.getTime();
        listoptions.date_start = new Long(1503305283);
        listoptions.page = 2;
        listoptions.limit = 2;
        listoptions.sort_by = Constants.SortBy.RECEPTION;
        listoptions.order_by = Constants.OrderBy.DESC;

        tHelper.getApi().Profile().list(listoptions);
    }

    @Ignore("Ignored due to symfony error.")
    @Test
    public void listfullargsrefTest() throws Exception
    {
        TestHelper tHelper = TestHelper.getInstance();
        Date date = new Date();

        Listoptions listoptions = new Listoptions();
        listoptions.source_ids.add(tHelper.getSourceID());
        listoptions.seniority = Constants.Seniority.JUNIOR;
        listoptions.date_end = date.getTime();
        listoptions.date_start = new Long(1503305283);
        listoptions.page = 2;
        listoptions.limit = 2;
        listoptions.sort_by = Constants.SortBy.RECEPTION;
        listoptions.order_by = Constants.OrderBy.DESC;
        listoptions.filter_reference = tHelper.getFilterRef();
        listoptions.stage = Constants.Stage.NEW;
        listoptions.rating = 1;

        tHelper.getApi().Profile().list(listoptions);
    }

    @Test
    public void listfullargsidTest() throws Exception {
        TestHelper tHelper = TestHelper.getInstance();
        Date date = new Date();

        Listoptions listoptions = new Listoptions();
        listoptions.source_ids.add(tHelper.getSourceID());
        listoptions.seniority = Constants.Seniority.JUNIOR;
        listoptions.date_end = date.getTime();
        listoptions.date_start = new Long(1503305283);
        listoptions.page = 2;
        listoptions.limit = 2;
        listoptions.sort_by = Constants.SortBy.RECEPTION;
        listoptions.order_by = Constants.OrderBy.DESC;
        listoptions.filter_id = tHelper.getFilterID();
        listoptions.stage = Constants.Stage.NEW;
        listoptions.rating = 1;

        tHelper.getApi().Profile().list(listoptions);
    }

    @Test
    public void listfullargsidentTest() throws Exception {
        TestHelper tHelper = TestHelper.getInstance();
        Date date = new Date();

        Listoptions listoptions = new Listoptions();
        listoptions.source_ids.add(tHelper.getSourceID());
        listoptions.seniority = Constants.Seniority.JUNIOR;
        listoptions.date_end = date.getTime();
        listoptions.date_start = new Long(1503305283);
        listoptions.page = 2;
        listoptions.limit = 2;
        listoptions.sort_by = Constants.SortBy.RECEPTION;
        listoptions.order_by = Constants.OrderBy.DESC;
        listoptions.filter_ident = new Ident.ID(Ident.Filter, tHelper.getFilterID());
        listoptions.stage = Constants.Stage.NEW;
        listoptions.rating = 1;

        tHelper.getApi().Profile().list(listoptions);
    }

    @Test
    public void addminargTest() throws RiminderException, Exception
    {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().add(tHelper.getSourceID(), tHelper.getCVpath(), null, null, null);

    }

    @Test
    public void addinvalidfileTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try
        {
            tHelper.getApi().Profile().add(tHelper.getSourceID(), "/juouo/dezee/fdefegg/notafile", null, null, null);
        }catch (RiminderException e){
            return;
        }
        fail("A RiminderException should have been raised.");
    }

    @Test
    public void addfullTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();
        Random rnd = new Random();
        String ref = new Integer(rnd.nextInt(99999)).toString();
        Date date = new Date();

        TrainingMetadata trainingMetadata = new TrainingMetadata();
        trainingMetadata.filter_reference = tHelper.getFilterRef();
        trainingMetadata.rating = 1;
        trainingMetadata.rating_timestamp = date.getTime();
        trainingMetadata.stage = Constants.Stage.LATER;
        trainingMetadata.stage_timestamp = date.getTime();

        List<TrainingMetadata> metadatas = new ArrayList<>();


        tHelper.getApi().Profile().add(tHelper.getSourceID(), tHelper.getCVpath(), ref, date.getTime(), metadatas);
    }

    @Test
    public void getTest() throws RiminderException, Exception
    {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().get(tHelper.getSourceID(), new Ident.ID(Ident.Profile, tHelper.getProfileID()));
    }

    @Test
    public void getrefTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().get(tHelper.getSourceID(), new Ident.Reference(Ident.Profile, tHelper.getProfileRef()));
    }

    @Test
    public void getbadTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try {
            tHelper.getApi().Profile().get("tHelper.getSourceID()",
                    new Ident.Reference(Ident.Profile, "tHelper.getProfileRef()"));
        } catch (RiminderException e) {
            return;
        }
        fail("A RiminderException should have been raised.");
    }

    @Test
    public void getDocumentTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Document().list(tHelper.getSourceID(), 
            new Ident.ID(Ident.Profile, tHelper.getProfileID()));
    }

    @Test
    public void getDocumentrefTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Document().list(tHelper.getSourceID(),
                new Ident.Reference(Ident.Profile, tHelper.getProfileRef()));
    }

    @Test
    public void getDocumentbadTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try {
            tHelper.getApi().Profile().Document().list("tHelper.getSourceID()",
                    new Ident.Reference(Ident.Profile, "tHelper.getProfileRef()"));
        } catch (RiminderException e) {
            return;
        }
        fail("A RiminderException should have been raised.");
    }

    @Test
    public void getParsingTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Parsing().get(tHelper.getSourceID(),
                new Ident.ID(Ident.Profile, tHelper.getProfileID()));
    }

    @Test
    public void getParsingrefTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Parsing().get(tHelper.getSourceID(),
                new Ident.Reference(Ident.Profile, tHelper.getProfileRef()));
    }

    @Test
    public void getParsingbadTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try {
            tHelper.getApi().Profile().Parsing().get("tHelper.getSourceID()",
                    new Ident.Reference(Ident.Profile, "tHelper.getProfileRef()"));
        } catch (RiminderException e) {
            return;
        }
        fail("A RiminderException should have been raised.");
    }

    @Test
    public void getScoringTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Scoring().list(tHelper.getSourceID(),
                new Ident.ID(Ident.Profile, tHelper.getProfileID()));
    }

    @Test
    public void getScoringrefTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Scoring().list(tHelper.getSourceID(),
                new Ident.Reference(Ident.Profile, tHelper.getProfileRef()));
    }

    @Test
    public void getScoringbadTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try
        {
            tHelper.getApi().Profile().Scoring().list("tHelper.getSourceID()",
                    new Ident.Reference(Ident.Profile, "tHelper.getProfileRef()"));
        }catch (RiminderException e)
        {
            return;
        }
        fail("A RiminderException should have been raised.");
    }

    public void checkJsonTest() throws RiminderResponseCastException, RiminderException, Exception
    {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Json().check(tHelper.gen_profile_json(), tHelper.gen_trainingmeta());
    }

    public void addJsonMaxTest() throws RiminderResponseCastException, RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();
        Random rnd = new Random();
        String ref = new Integer(rnd.nextInt(99999)).toString();
        Date date = new Date();

        tHelper.getApi().Profile().Json().add(tHelper.getSourceID(), tHelper.gen_profile_json(), ref, date.getTime(), tHelper.gen_trainingmeta());
    }

    public void addJsonMinTest() throws RiminderResponseCastException, RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();
        Random rnd = new Random();
        String ref = new Integer(rnd.nextInt(99999)).toString();
        Date date = new Date();

        tHelper.getApi().Profile().Json().add(tHelper.getSourceID(), tHelper.gen_profile_json(), null, null, null);
    }

    public void setStageidTest() throws RiminderException, Exception
    {
        TestHelper tHelper = TestHelper.getInstance(); 

        tHelper.getApi().Profile().Stage().set(tHelper.getSourceID(), Constants.Stage.NO,
            new Ident.ID(Ident.Profile, tHelper.getProfileID()), 
            new Ident.ID(Ident.Filter, tHelper.getFilterID()));
    }
    
    public void setStagerefTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Stage().set(tHelper.getSourceID(),  Constants.Stage.NO,
                new Ident.Reference(Ident.Profile, tHelper.getFilterRef()), 
                new Ident.Reference(Ident.Filter, tHelper.getFilterRef()));
    }

    public void setStagebadTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try{
            tHelper.getApi().Profile().Stage().set("tHelper.getSourceID()", Constants.Stage.NO,
                    new Ident.Reference(Ident.Profile, " tHelper.getFilterRef()"),
                    new Ident.Reference(Ident.Filter, "tHelper.getFilterRef()"));

        }catch (RiminderException e)
        {
            return;
        }
        fail("A RiminderException should have been raised.");
    }

    public void setRatingidTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Rating().set(tHelper.getSourceID(), 1,
                new Ident.ID(Ident.Profile, tHelper.getProfileID()), new Ident.ID(Ident.Filter, tHelper.getFilterID()));
    }

    public void setRatingrefTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        tHelper.getApi().Profile().Rating().set(tHelper.getSourceID(), 1,
                new Ident.Reference(Ident.Profile, tHelper.getFilterRef()),
                new Ident.Reference(Ident.Filter, tHelper.getFilterRef()));
    }

    public void setRatingbadTest() throws RiminderException, Exception {
        TestHelper tHelper = TestHelper.getInstance();

        try {
            tHelper.getApi().Profile().Rating().set("tHelper.getSourceID()", 1,
                    new Ident.Reference(Ident.Profile, " tHelper.getFilterRef()"),
                    new Ident.Reference(Ident.Filter, "tHelper.getFilterRef()"));

        } catch (RiminderException e) {
            return;
        }
        fail("A RiminderException should have been raised.");
    }
}