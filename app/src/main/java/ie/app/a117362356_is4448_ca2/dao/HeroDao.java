package ie.app.a117362356_is4448_ca2.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.handlers.HeroHttpHandler;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.model.JsonHero;

/**
 * References
 * - Project: IS4448BeerServiceDemo
 * - Creator: Michael Gleeson on 14/02/2019
 */
public class HeroDao {

    public static final String connURI = "https://gleeson.io/IS4447/HeroAPI/v1/Api.php?apicall=";

    public static final ArrayList<Hero> selectHeroes() {
        String getConnURI = connURI + "getheroes";
        String result = HeroHttpHandler.getHeroes(getConnURI);
        Gson gson = new Gson();
        JsonHero results = gson.fromJson(result, JsonHero.class);
        return results.getHeroes();
    }

    public static final Hero insertHero(Hero hero) throws JsonSyntaxException {
        String insertConnURI = connURI + "createhero";
        Gson gson = new Gson();
        String payload = gson.toJson(hero, Hero.class);
        String result = HeroHttpHandler.postHero(insertConnURI, payload);
        hero = gson.fromJson(result, Hero.class);
        return hero;
    }

    public static final Hero updateHero(Hero hero) throws JsonSyntaxException {
        String updateConnURI = connURI + "updatehero&id=" + hero.getId();
        Gson gson = new Gson();
        String payload = gson.toJson(hero, Hero.class);
        String result = HeroHttpHandler.putHero(updateConnURI, payload);
        hero = gson.fromJson(result, Hero.class);
        return hero;
    }

    public static final boolean deleteHero(int heroId) throws JsonSyntaxException {
        String deleteConnURI = connURI + "deletehero&id=" + heroId;
        String result = HeroHttpHandler.deleteHero(deleteConnURI);
        Gson gson = new Gson();
        JsonHero j = gson.fromJson(result, JsonHero.class);
        if(j.getError().equals("true"))
            return true;
        else
            return false;
    }
}
