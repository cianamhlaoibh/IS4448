package ie.app.a117362356_is4448_ca2.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.handlers.CovidHttpHandler;
import ie.app.a117362356_is4448_ca2.model.CovidStats;


public class CovidDao {
    public static final String connURI = "https://api.covid19api.com/live/country/";

    public static final ArrayList<CovidStats> selectCountryStats(String country) {
        String getConnURI = connURI + country;
        String result = CovidHttpHandler.getStats(getConnURI);
        Gson gson = new Gson();
        Type CovidStatType = new TypeToken<ArrayList<CovidStats>>(){}.getType();
        ArrayList<CovidStats> covidStatsList = gson.fromJson(result, CovidStatType);
        return covidStatsList;
    }

}
