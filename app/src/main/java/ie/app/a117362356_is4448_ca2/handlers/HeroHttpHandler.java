package ie.app.a117362356_is4448_ca2.handlers;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ie.app.a117362356_is4448_ca2.MyApplication;
import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.view.utils.VolleyHeroCallback;

/**
 * References
 * - Project: IS4448BeerServiceDemo
 * - Creator: Michael Gleeson on 14/02/2019
 */
public class HeroHttpHandler {

    public static void getHeroes(String url, final VolleyHeroCallback callback) {
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessObjectResponse(response);
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
    //https://stackoverflow.com/questions/25998529/send-form-urlencoded-parameters-in-post-request-android-volley
    public static void postHero(String url, final Hero hero, final VolleyHeroCallback callback) {
        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessStringResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", hero.getName());
                params.put("realname", hero.getRealName());
                params.put("rating", String.valueOf(hero.getRating()));
                params.put("teamaffiliation", hero.getTeam());
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(jsonObjRequest);
    }

    public static void putHero(String url, final Hero hero, final VolleyHeroCallback callback) {
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessStringResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(hero.getId()));
                params.put("name", hero.getName());
                params.put("realname", hero.getRealName());
                params.put("rating", String.valueOf(hero.getRating()));
                params.put("teamaffiliation", hero.getTeam());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(jsonObjRequest);
    }

    public static void deleteHero(String url,final VolleyHeroCallback callback) {
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessStringResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
        };
        MyApplication.getInstance().addToRequestQueue(jsonObjRequest);
    }
}
