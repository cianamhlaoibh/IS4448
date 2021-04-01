package ie.app.a117362356_is4448_ca2.handlers;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.MyApplication;
import ie.app.a117362356_is4448_ca2.model.CovidStats;
import ie.app.a117362356_is4448_ca2.view.utils.VolleyCovidCallback;

public class CovidHttpHandler{

    public static void getStats(String url, final VolleyCovidCallback callback) {
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // add the request object to the queue to be executed
        MyApplication.getInstance().addToRequestQueue(req);
    }
}
