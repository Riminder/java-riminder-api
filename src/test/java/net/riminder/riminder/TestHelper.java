package net.riminder.riminder;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.response.Token;
import net.riminder.riminder.route.Profile.Listoptions;
import net.riminder.riminder.route.Profile.TrainingMetadata;
import net.riminder.riminder.route.Profile.Json.Education;
import net.riminder.riminder.route.Profile.Json.Experience;
import net.riminder.riminder.route.Profile.Json.LocationDetails;
import net.riminder.riminder.route.Profile.Json.ProfileJson;

public class TestHelper
{
    private Riminder api;
    private String secret_key;
    private String webhook_secret;
    private String source_name;
    private String source_id;
    private String profile_id;
    private String profile_reference;
    private String filter_id;
    private String filter_reference;
    private String cv_path;

    private TestHelper()
    {
        this.secret_key = "ask_110bf53876034bf0546b693d1a07a515";
        this.source_name = "sdk_test";
        this.webhook_secret = "lalallaalalala";
        this.api = new Riminder(this.secret_key, this.webhook_secret);
        this.cv_path = "/home/alexandre/Documents/cv_test09.png";
        this.source_id = "fe6d7a2aa9125259a5ecf7905154a0396a891c06";
    }

    private static TestHelper _instance = null;

    public static TestHelper getInstance()
    {
        if (TestHelper._instance == null)
            _instance = new TestHelper();
        return _instance;
    }

    public Riminder getApi()
    {return api; }

    public String getWebhookSecret()
    { return webhook_secret; }

    private void fetch_source() throws Exception
    {
        List<Token> sourcelist = this.api.Source().list();
        for (Token source: sourcelist)
        {
            Boolean is_valid = source.asMap().get("name").as() == this.source_name;
            if (this.source_name == null || is_valid)
            {
                this.source_id = source.asMap().get("source_id").as();
                return;
            }
        }
        throw new Exception("Cannot fetch source.");
    }

    private void fetch_filter() throws Exception {
        List<Token> filterlist = this.api.Filter().list();
        for (Token filter : filterlist) {
            String ref = filter.asMap().get("filter_reference").as();
           if (ref == null)
           {
               continue;
           }
           this.filter_id = filter.asMap().get("filter_id").as();
           this.filter_reference = filter.asMap().get("filter_reference").as();
           return;
        }
        throw new Exception("Cannot fetch filter.");
    }

    private void fetch_profile() throws Exception
    {
        Listoptions lo = new Listoptions();
        lo.source_ids.add(this.getSourceID());
        Map<String, Token> profilelist = this.api.Profile().list(lo);
        for (Token profile : profilelist.get("profiles").asList()) {
            if (profile.asMap().get("profile_reference").as() == null) {
                continue;
            }
            this.profile_id = profile.asMap().get("profile_id").as();
            this.profile_reference = profile.asMap().get("profile_reference").as();
            return;
        }
        throw new Exception("Cannot fetch profile.");
    }

    public String getProfileID() throws Exception
    {
        if (this.profile_id == null)
            fetch_profile();
        return this.profile_id;
    }

    public String getProfileRef() throws Exception
    {
        if (this.profile_reference == null)
            fetch_profile();
        return this.profile_reference;
    }

    public String getFilterID() throws Exception {
        if (this.filter_id == null)
            fetch_filter();
        return this.filter_id;
    }

    public String getFilterRef() throws Exception {
        if (this.filter_reference == null)
            fetch_filter();
        return this.filter_reference;
    }

    public String getSourceID() throws Exception {
        if (this.source_id == null)
            fetch_source();
        return this.source_id;
    }

    public String getCVpath()
    {
        return this.cv_path;
    }

    public ProfileJson gen_profile_json()
    {
        ProfileJson pjson = new ProfileJson();

        pjson.name = "Love Lover";
        pjson.email = "Lovelover@gmail.com";
        pjson.phone = "+3369895421";
        pjson.summary = "Les loeleleeleeldkffkjfjfffkfkffffkf.";
        pjson.locationDetails.text = "Mars, sector 7, 45e Cadran";
        Experience exp = new Experience();
        exp.start = "23/04/2015";
        exp.end = "12/06/2015";
        exp.title = "Some guy";
        exp.company = "Bad apple";
        exp.location = null;
        exp.location_details = new LocationDetails();
        exp.location_details.text = "Earth";
        exp.description = "Gigabad";
        pjson.experiences.add(exp);
        Education edu = new Education();
        edu.start = "23/04/2014";
        edu.end = "12/06/2014";
        edu.title = "Some student boi";
        edu.school = "Bad apple school (yes)";
        edu.location = null;
        edu.location_details = new LocationDetails();
        edu.location_details.text = "Earth";
        edu.description = "Gigabad";
        pjson.educations.add(edu);
        pjson.skills.add("cooking");
        pjson.skills.add("relatonship");
        pjson.skills.add("c++");
        pjson.languages.add("french");
        pjson.languages.add("english");
        pjson.interests.add("harder");
        pjson.interests.add("faster");
        pjson.interests.add("better");
        pjson.interests.add("stronger");
        return pjson;
    }

    public List<TrainingMetadata> gen_trainingmeta() throws Exception
    {
        Date date = new Date();
        TrainingMetadata trainingMetadata = new TrainingMetadata();
        trainingMetadata.filter_reference = this.getFilterRef();
        trainingMetadata.rating = 1;
        trainingMetadata.rating_timestamp = date.getTime();
        trainingMetadata.stage = Constants.Stage.LATER;
        trainingMetadata.stage_timestamp = date.getTime();

        List<TrainingMetadata> metadatas = new ArrayList<>();
        metadatas.add(trainingMetadata);
        return metadatas;
    }
}