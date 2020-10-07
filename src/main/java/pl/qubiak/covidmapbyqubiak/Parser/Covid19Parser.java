package pl.qubiak.covidmapbyqubiak.Parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.qubiak.covidmapbyqubiak.Model.Poit;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class Covid19Parser {

    private static final String confirmedInfectionsUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String deathsUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static final String recoveredUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    final LocalDate today = LocalDate.now();
    final LocalDate yesterday = today.minusDays(1);
    final LocalDate dayBeforeYesterday = today.minusDays(2);
    String yesterdayDate = yesterday.format(DateTimeFormatter.ofPattern("MM/d/YY"));
    String dayBeforeYesterdayDate = dayBeforeYesterday.format(DateTimeFormatter.ofPattern("MM/d/YY"));

    public List<Poit> getCovidData() throws IOException {

        List<Poit> poits = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        String confirmedInfectionsValues = restTemplate.getForObject(confirmedInfectionsUrl, String.class);
        String deathsValues = restTemplate.getForObject(deathsUrl, String.class);
        String recoveredValues = restTemplate.getForObject(recoveredUrl, String.class);

        StringReader stringReader = new StringReader(confirmedInfectionsValues);
        StringReader stringReader1 = new StringReader(deathsValues);
        StringReader stringReader2 = new StringReader(recoveredValues);

        CSVParser infections = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        CSVParser deaths = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader1);
        CSVParser recovered = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader2);

        List<CSVRecord> records = infections.getRecords();
        for (int i = 0; i < records.size(); i++) {
            double lat = Double.parseDouble(records.get(i).get("Lat"));
            double lon = Double.parseDouble(records.get(i).get("Lon"));
            String confirmedInfections = records.get(i).get(yesterdayDate);
            String Deaths = deaths.getRecords().get(i).get(yesterdayDate);
            String Recovered = recovered.getRecords().get(i).get(yesterdayDate);
            String confirmedInfectionsDayBefore = records.get(i).get(dayBeforeYesterdayDate);
            String DeathsDayBefore = deaths.getRecords().get(i).get(dayBeforeYesterdayDate);
            String RecoveredDayBefore = recovered.getRecords().get(i).get(dayBeforeYesterdayDate);

            Integer  confirmedInfectionsInteger = Integer.parseInt(confirmedInfections);
            Integer  DeathsInteger = Integer.parseInt(Deaths);
            Integer  RecoveredInteger = Integer.parseInt(Recovered);
            Integer  confirmedInfectionsDayBeforeInteger = Integer.parseInt(confirmedInfectionsDayBefore);
            Integer  DeathsDayBeforeInteger = Integer.parseInt(DeathsDayBefore);
            Integer  RecoveredDayBeforeInteger = Integer.parseInt(RecoveredDayBefore);

            poits.add(new Poit(lat, lon, "Liczba zakażonych: " + confirmedInfections + " (" + (confirmedInfectionsInteger-confirmedInfectionsDayBeforeInteger) + ")",
                    "Liczba zmarłych: " + Deaths + " (" + (DeathsInteger-DeathsDayBeforeInteger) + ")",
                    "Liczba ozdrowieńców: " + Recovered + " (" + (RecoveredInteger-RecoveredDayBeforeInteger)
                            + "Dane na dzień: " + yesterdayDate));
        }
        return poits;
    }
}