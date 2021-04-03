package ie.app.a117362356_is4448_ca2.model.covid;

import java.util.Date;

public class StatSummary {
    private String ID;
    private String Country;
    private String CountryCode;
    private String Slug;
    private long NewConfirmed ;
    private long TotalConfirmed;
    private long NewDeaths;
    private long TotalDeaths;
    private long NewRecovered;
    private long TotalRecovered;
    private Date Date;
    private Premium Premium;

    public String getID() {
        return ID;
    }

    public String getCountry() {
        return Country;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getSlug() {
        return Slug;
    }

    public long getNewConfirmed() {
        return NewConfirmed;
    }

    public long getTotalConfirmed() {
        return TotalConfirmed;
    }

    public long getNewDeaths() {
        return NewDeaths;
    }

    public long getTotalDeaths() {
        return TotalDeaths;
    }

    public long getNewRecovered() {
        return NewRecovered;
    }

    public long getTotalRecovered() {
        return TotalRecovered;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public Premium getPremium() {
        return Premium;
    }

    public class Premium {
    }
}
