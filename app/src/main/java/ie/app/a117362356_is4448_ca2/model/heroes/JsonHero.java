package ie.app.a117362356_is4448_ca2.model.heroes;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.model.heroes.Hero;

public class JsonHero {
    private String error;
    private String message;
    private ArrayList<Hero> heroes;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(ArrayList<Hero> heroes) {
        this.heroes = heroes;
    }
}
