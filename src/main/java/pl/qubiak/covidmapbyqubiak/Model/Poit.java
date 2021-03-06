package pl.qubiak.covidmapbyqubiak.Model;

public class Poit {

    private double lat;
    private double lon;
    private String confirmedInfections;
    private String deaths;
    private String recovered;
    private String dataForDay;
    private String activeCases;


    public String getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(String activeCases) {
        this.activeCases = activeCases;
    }

    public Poit(double lat, double lon, String confirmedInfections, String deaths, String recovered, String activeCases, String dataForDay) {
        this.lat = lat;
        this.lon = lon;
        this.confirmedInfections = confirmedInfections;
        this.deaths = deaths;
        this.recovered = recovered;
        this.dataForDay = dataForDay;
        this.activeCases = activeCases;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getConfirmedInfections() {
        return confirmedInfections;
    }

    public void setConfirmedInfections(String confirmedInfections) {
        this.confirmedInfections = confirmedInfections;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getDataForDay() {
        return dataForDay;
    }

    public void setDataForDay(String dataForDay) {
        this.dataForDay = dataForDay;
    }
}