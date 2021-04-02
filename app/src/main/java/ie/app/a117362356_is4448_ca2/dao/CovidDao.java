package ie.app.a117362356_is4448_ca2.dao;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.handlers.CovidHttpHandler;
import ie.app.a117362356_is4448_ca2.model.CovidCountryStats;
import ie.app.a117362356_is4448_ca2.view.utils.VolleyCovidCallback;


/**
 * https://howtodoinjava.com/gson/gson-parse-json-array/
 */
public class CovidDao {
    public static final String connURI = "https://api.covid19api.com/live/country/";

    public void selectCountryStats(String country, final Handler handler) throws JsonSyntaxException {
        String getConnURI = connURI + country;
        CovidHttpHandler.getStats(getConnURI, new VolleyCovidCallback() {
            @Override
            public void onSuccessResponse(JSONArray result) {
                Gson gson = new Gson();
                Type userListType = new TypeToken<ArrayList<CovidCountryStats>>() {}.getType();
                ArrayList<CovidCountryStats> covidStatsList = gson.fromJson(String.valueOf(result), userListType);
                Message msg = new Message();
                msg.obj = covidStatsList;
                handler.sendMessage(msg);
            }
        });
    }
}
