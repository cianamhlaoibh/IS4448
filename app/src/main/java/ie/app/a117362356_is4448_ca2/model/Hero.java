package ie.app.a117362356_is4448_ca2.model;

public class Hero {
    private String name;
    private String realName;
    private float rating;
    private String team;

    public Hero(String name, String realName, float rating, String team) {
        this.name = name;
        this.realName = realName;
        this.rating = rating;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
