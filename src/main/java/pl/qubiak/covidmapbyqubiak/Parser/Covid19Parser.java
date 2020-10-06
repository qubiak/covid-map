package pl.qubiak.covidmapbyqubiak.Parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.qubiak.covidmapbyqubiak.Model.Poit;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class Covid19Parser {

    private static final String confirmedInfectionsUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String deathsUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static final String recoveredUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    final LocalDate today = LocalDate.now();
    final LocalDate yesterday = today.minusDays(1);
    String todayDate = today.format(DateTimeFormatter.ofPattern("MM/dd/YY"));
    String yesterdayDate = today.format(DateTimeFormatter.ofPattern("MM/dd/YY"));

    public List<Poit> getCovidData() throws IOException {
        List<Poit> poits = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String confirmedInfectionsValues = restTemplate.getForObject(confirmedInfectionsUrl, String.class);
        String deathsValues = restTemplate.getForObject(deathsUrl, String.class);
        String recoveredValues = restTemplate.getForObject(recoveredUrl, String.class); //dodać umarłych i ozdrowieńców

        StringReader stringReader = new StringReader(confirmedInfectionsValues);
        StringReader stringReader1 = new StringReader(deathsValues);
        StringReader stringReader2 = new StringReader(recoveredValues);
        CSVParser parse = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader); // jak parsować trzy zmienne?

        for (CSVRecord strings : parse) {
            double lat = Double.parseDouble(strings.get("Lat"));
            double lon = Double.parseDouble(strings.get("Long"));
            String confirmedInfections = strings.get(yesterdayDate); //wstawic zmienną odpowiadającą za datę dzisiejszą
            String deaths = strings.get("3/15/20");
            String recovered = strings.get("3/15/20");
            poits.add(new Poit(lat, lon, "Liczba zakażonych: " + confirmedInfections, "Liczba zmarłych: " + deaths,
                    "Liczba ozdrowieńców: " + recovered));
        }
        return poits;
    }
}