package ie.app.a117362356_is4448_ca2.model.covid;

import java.util.List;

public class GlobalSummary {
    private String ID;
    private String Message;
    private StatSummary Global;
    private List<StatSummary> Countries;

    public List<StatSummary> getCountries() {
        return Countries;
    }

    public void setGlobal(StatSummary global) {
        Global = global;
    }
}
