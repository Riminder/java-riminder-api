package net.riminder.riminder.route;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import net.riminder.riminder.Constants;
import net.riminder.riminder.Ident;
import net.riminder.riminder.RestClientW;
import net.riminder.riminder.exp.RiminderException;
import net.riminder.riminder.exp.RiminderResponseCastException;
import net.riminder.riminder.exp.RiminderResponseException;
import net.riminder.riminder.exp.RiminderTransferException;
import net.riminder.riminder.response.Token;

public class Profile {

    private RestClientW rclient;

    private Stage stage;
    private Rating rating;
    private Document document;
    private Parsing parsing;
    private Scoring scoring;
    private Json json;
    private Reveal reveal;

    // 08/17/2012
    private static final String DEFAULT_DATE_START = "1345208382";

    public Profile(RestClientW rclient)
    {
        this.rclient = rclient;
        this.stage = new Stage(this.rclient);
        this.rating = new Rating(this.rclient);
        this.document = new Document(this.rclient);
        this.parsing = new Parsing(this.rclient);
        this.scoring = new Scoring(this.rclient);
        this.json = new Json(this.rclient);
        this.reveal = new Reveal(this.client);
    }

    public Stage Stage()
    {
        return this.stage;
    }

    public Rating Rating()
    {
        return this.rating;
    }

    public Document Document()
    {
        return this.document;
    }

    public Parsing Parsing()
    {
        return this.parsing;
    }

    public Scoring Scoring()
    {
        return this.scoring;
    }

    public Json Json()
    {
        return this.json;
    }

    public Reveal Reveal()
    {
        return this.reveal;
    }

    static public class TrainingMetadata {
        public String filter_reference;
        public String stage;
        public Long stage_timestamp;
        public Integer rating;
        public Long rating_timestamp;

        public Boolean isvalid() {
            return filter_reference != null;
        }
    }

    static public class Listoptions
    {
        public List<String> source_ids;
        public String seniority;
        public String filter_id;
        public String filter_reference;
        public Ident filter_ident;
        public String stage;
        public Integer rating;
        public Long date_start;
        public Long date_end;
        public Integer page;
        public Integer limit;
        public String sort_by;
        public String order_by;

        // Avoid nullptr exp
        public Listoptions()
        {
            this.source_ids = new LinkedList<>();
            this.filter_ident = new Ident.Reference(Ident.Filter, "");
        }
    }



    public Map<String, Token> list(Listoptions options) throws RiminderException
    {
        Gson gson = new Gson();
        Date date = new Date();
        Map<String, String> query = new HashMap<>();

        String source_ids = gson.toJson(options.source_ids);
        query.put("source_ids", source_ids);
        query = RestClientW.add_with_default(query, "seniority", options.seniority, Constants.Seniority.ALL);
        query = RestClientW.add_with_default(query, "filter_id", options.filter_id, null, false);
        query = RestClientW.add_with_default(query, "filter_reference", options.filter_reference, null, false);
        query = RestClientW.add_with_default(query, options.filter_ident.getName(), options.filter_ident.getValue(), null, false);
        query = RestClientW.add_with_default(query, "stage", options.stage, null, false);
        query = RestClientW.add_with_defaultso(query, "rating", options.rating, null, false);
        query = RestClientW.add_with_defaultso(query, "date_start", options.date_start, DEFAULT_DATE_START, false);
        query = RestClientW.add_with_defaultso(query, "date_end", options.date_end, new Long(date.getTime()).toString(), false);
        query = RestClientW.add_with_defaultso(query, "page", options.page, null, false);
        query = RestClientW.add_with_defaultso(query, "limit", options.limit, null, false);
        query = RestClientW.add_with_default(query, "sort_by", options.sort_by, Constants.SortBy.RANKING, false);
        query = RestClientW.add_with_default(query, "order_by", options.order_by, null, false);

        return rclient.get("profiles", query).get("data").asMap();
    }

    public Map<String, Token> add(String source_id, String filepath, String profile_reference, Long timestamp_reception, List<TrainingMetadata> training_metadatas) throws RiminderException
    {
        Map<String, Object> bodyparams = new HashMap<>();

        bodyparams.put("source_id", source_id);
        bodyparams = RestClientW.add_with_default(bodyparams, "profile_reference", profile_reference, null, false);
        bodyparams = RestClientW.add_with_default(bodyparams, "training_metadata", training_metadatas, null, false);
        bodyparams = RestClientW.add_with_default(bodyparams, "profile_reference", profile_reference, null, false);

        return rclient.postfile("profile", bodyparams, filepath).get("data").asMap();
    }

    public Map<String, Token> get(String source_id, Ident profile_ident) throws RiminderException
    {
        Map<String, String> query = new HashMap<>();

        query.put("source_id", source_id);
        query.put(profile_ident.getName(), profile_ident.getValue());

        return rclient.get("profile", query).get("data").asMap();
    }

    static public class Document
    {
        private RestClientW rclient;

        public Document(RestClientW rclient)
        {
            this.rclient = rclient;
        }

        public List<Token> list(String source_id, Ident profile_ident) throws RiminderException
        {
            Map<String, String> query = new HashMap<>();

            query.put("source_id", source_id);
            query.put(profile_ident.getName(), profile_ident.getValue());

            return rclient.get("profile/documents", query).get("data").asList();
        }
    }

    static public class Parsing {
        private RestClientW rclient;

        public Parsing(RestClientW rclient) {
            this.rclient = rclient;
        }

        public Map<String, Token> get(String source_id, Ident profile_ident) throws RiminderException {
            Map<String, String> query = new HashMap<>();

            query.put("source_id", source_id);
            query.put(profile_ident.getName(), profile_ident.getValue());

            return rclient.get("profile/parsing", query).get("data").asMap();
        }
    }

    static public class Scoring {
        private RestClientW rclient;

        public Scoring(RestClientW rclient) {
            this.rclient = rclient;
        }

        public List<Token> list(String source_id, Ident profile_ident) throws RiminderException {
            Map<String, String> query = new HashMap<>();

            query.put("source_id", source_id);
            query.put(profile_ident.getName(), profile_ident.getValue());

            return rclient.get("profile/scoring", query).get("data").asList();
        }
    }

    static public class Json
    {
        private RestClientW rclient;

        public Json(RestClientW rclient) {
            this.rclient = rclient;
        }

        static public class LocationDetails
        {
            public String text;
        }

        static public class Experience
        {
            public String start;
            public String end;
            public String title;
            public String company;
            public String location;
            public LocationDetails location_details = new LocationDetails();
            public String description;
        }

        static public class Education
        {
            public String start;
            public String end;
            public String title;
            public String school;
            public String location;
            public LocationDetails location_details = new LocationDetails();
            public String description;
        }

        static public class Urls
        {
            public List<String> from_resume = new ArrayList<>();
            public String linkedin;
            public String twitter;
            public String facebook;
            public String github;
            public String picture;
        }

        static public class ProfileJson
        {
            public String name;
            public String email;
            public String phone;
            public String summary;
            public LocationDetails locationDetails = new LocationDetails();
            public List<Experience> experiences = new ArrayList<>();
            public List<Education> educations = new ArrayList<>();
            public List<String> skills = new ArrayList<>();
            public List<String> languages = new ArrayList<>();
            public List<String> interests = new ArrayList<>();
            public Urls urls = new Urls();
        }
        
        public Map<String, Token> check(ProfileJson profile_data, List<TrainingMetadata> training_metadatas) throws RiminderResponseCastException, RiminderException 
        {
            Map<String, Object> bodyparams = new HashMap<>();

            bodyparams.put("profile_json", profile_data);
            bodyparams = RestClientW.add_with_default(bodyparams, "training_metadata", training_metadatas, null, false);

            return rclient.post("profile/json/check", bodyparams).get("data").asMap();
        }

        public Map<String, Token> add(String source_id, ProfileJson profile_data, String profile_reference, Long timestamp_reception, List<TrainingMetadata> training_metadatas) throws RiminderException {
            Map<String, Object> bodyparams = new HashMap<>();

            bodyparams.put("source_id", source_id);
            bodyparams.put("profile_json", profile_data);
            bodyparams = RestClientW.add_with_default(bodyparams, "profile_reference", profile_reference, null, false);
            bodyparams = RestClientW.add_with_default(bodyparams, "training_metadata", training_metadatas, null, false);
            bodyparams = RestClientW.add_with_default(bodyparams, "profile_reference", profile_reference, null, false);

            return rclient.post("profile/json", bodyparams).get("data").asMap();
        }
    }

    static public class Stage
    {
        private RestClientW rclient;

        public Stage(RestClientW rclient) {
            this.rclient = rclient;
        }

        public Map<String, Token> set(String source_id, String stage, Ident profile_ident, Ident filter_ident) throws RiminderException
        {
            Map<String, Object> bodyparams = new HashMap<>();

            bodyparams.put("source_id", source_id);
            bodyparams.put(profile_ident.getName(), profile_ident.getValue());
            bodyparams.put(filter_ident.getName(), filter_ident.getValue());
            bodyparams.put("stage", stage);

            return rclient.patch("profile/stage", bodyparams).get("data").asMap();
        }
    }

    static public class Rating {
        private RestClientW rclient;

        public Rating(RestClientW rclient) {
            this.rclient = rclient;
        }

        public Map<String, Token> set(String source_id, int rating, Ident profile_ident, Ident filter_ident) throws RiminderException {
            Map<String, Object> bodyparams = new HashMap<>();

            bodyparams.put("source_id", source_id);
            bodyparams.put(profile_ident.getName(), profile_ident.getValue());
            bodyparams.put(filter_ident.getName(), filter_ident.getValue());
            bodyparams.put("rating", rating);

            return rclient.patch("profile/rating", bodyparams).get("data").asMap();
        }
    }

    static public class Reveal {
        private RestClientW rclient;

        public Reveal(RestClientW rclient) {
            this.rclient = rclient;
        }

        public Map<String, Token> set(String source_id, Ident profile_ident, Ident filter_ident) throws RiminderException {
            Map<String, Object> query = new HashMap<>();

            query.put("source_id", source_id);
            query.put(profile_ident.getName(), profile_ident.getValue());
            query.put(filter_ident.getName(), filter_ident.getValue());

            return rclient.get("profile/interpretability", query).get("data").asMap();
        }
    }

}