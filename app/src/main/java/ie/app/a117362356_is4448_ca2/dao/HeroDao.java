package ie.app.a117362356_is4448_ca2.dao;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import ie.app.a117362356_is4448_ca2.handlers.HeroHttpHandler;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.model.JsonHero;
import ie.app.a117362356_is4448_ca2.view.utils.VolleyHeroCallback;

/**
 * References
 * - Project: IS4448BeerServiceDemo
 * - Creator: Michael Gleeson on 14/02/2019
 * <p>
 * https://howtodoinjava.com/gson/gson-parse-json-array/
 */
public class HeroDao {

    public static final String connURI = "https://gleeson.io/IS4447/HeroAPI/v1/Api.php?apicall=";

    public void selectHeroes(final Handler handler) {
        String getConnURI = connURI + "getheroes";
        HeroHttpHandler.getHeroes(getConnURI, new VolleyHeroCallback() {
            @Override
            public void onSuccessObjectResponse(JSONObject result) {
                Gson gson = new Gson();
                JsonHero results = gson.fromJson(String.valueOf(result), JsonHero.class);
                Message msg = new Message();
                msg.obj = results.getHeroes();
                handler.sendMessage(msg);
            }

            @Override
            public void onSuccessStringResponse(String result) {

            }
        });
    }

    public void insertHero(Hero hero, final Handler handler) throws JsonSyntaxException, JSONException {
        String insertConnURI = connURI + "createhero";
        HeroHttpHandler.postHero(insertConnURI, hero, new VolleyHeroCallback() {
            @Override
            public void onSuccessObjectResponse(JSONObject result) {

            }

            @Override
            public void onSuccessStringResponse(String result) {
                Gson gson = new Gson();
                JsonHero j = gson.fromJson(result, JsonHero.class);
                Message msg = new Message();
                if (j.getError().equals("true"))
                    msg.obj = true;
                else
                    msg.obj = false;
                handler.sendMessage(msg);
            }
        });
    }

    public void updateHero(Hero hero, final Handler handler) throws JsonSyntaxException {
        String updateConnURI = connURI + "updatehero&id=" + hero.getId();
        HeroHttpHandler.putHero(updateConnURI, hero, new VolleyHeroCallback() {
            @Override
            public void onSuccessObjectResponse(JSONObject result) {

            }

            @Override
            public void onSuccessStringResponse(String result) {
                Gson gson = new Gson();
                JsonHero j = gson.fromJson(result, JsonHero.class);
                Message msg = new Message();
                if (j.getError().equals("true"))
                    msg.obj = true;
                else
                    msg.obj = false;
                handler.sendMessage(msg);
            }
        });
    }

    public void deleteHero(int heroId, final Handler handler) throws JsonSyntaxException {
        String deleteConnURI = connURI + "deletehero&id=" + heroId;
        HeroHttpHandler.deleteHero(deleteConnURI, new VolleyHeroCallback() {
            @Override
            public void onSuccessObjectResponse(JSONObject result) {

            }

            @Override
            public void onSuccessStringResponse(String result) {
                Gson gson = new Gson();
                JsonHero j = gson.fromJson(result, JsonHero.class);
                Message msg = new Message();
                if (j.getError().equals("true"))
                    msg.obj = true;
                else
                    msg.obj = false;
                handler.sendMessage(msg);
            }
        });
    }
}
