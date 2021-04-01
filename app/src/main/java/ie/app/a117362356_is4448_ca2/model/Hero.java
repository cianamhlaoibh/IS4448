package ie.app.a117362356_is4448_ca2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * https://medium.com/techmacademy/how-to-implement-and-use-a-parcelable-class-in-android-part-1-28cca73fc2d1
 * https://medium.com/techmacademy/how-to-implement-and-use-a-parcelable-class-in-android-part-2-1793ce358bd0
 */
public class Hero implements Parcelable {
    private int id;
    private String name;
    private String realname;
    private float rating;
    private String teamaffiliation;

    public Hero(String name, String realname, float rating, String teamaffiliation) {
        this.name = name;
        this.realname = realname;
        this.rating = rating;
        this.teamaffiliation = teamaffiliation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realname;
    }

    public void setRealName(String realName) {
        this.realname = realName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTeam() {
        return teamaffiliation;
    }

    public void setTeam(String team) {
        this.teamaffiliation = team;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(realname);
        dest.writeFloat(rating);
        dest.writeString(teamaffiliation);
    }

    protected Hero(Parcel in) {
        id = in.readInt();
        name = in.readString();
        realname = in.readString();
        rating = in.readFloat();
        teamaffiliation = in.readString();
    }

    public static final Creator<Hero> CREATOR = new Creator<Hero>() {
        @Override
        public Hero createFromParcel(Parcel in) {
            return new Hero(in);
        }

        @Override
        public Hero[] newArray(int size) {
            return new Hero[size];
        }
    };
}
