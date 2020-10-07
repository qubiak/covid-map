package pl.qubiak.covidmapbyqubiak.Model;

import java.util.Date;

public class Poit {

    private double lat;
    private double lon;
    private String confirmedInfections;
    private String deaths;
    private String recovered;

    public Poit(double lat, double lon, String confirmedInfections, String deaths, String recovered) {
        this.lat = lat;
        this.lon = lon;
        this.confirmedInfections = confirmedInfections;
        this.deaths = deaths;
        this.recovered = recovered;
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
}