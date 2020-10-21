package pl.qubiak.covidmapbyqubiak.Parser;

import org.apache.commons.csv.CSVFormat;
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
import java.util.Optional;

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

        StringReader confirmedReader = new StringReader(confirmedInfectionsValues);
        StringReader deathsReader = new StringReader(deathsValues);
        StringReader recoveredReader = new StringReader(recoveredValues);

        List<CSVRecord> infections = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(confirmedReader).getRecords();
        List<CSVRecord> deaths = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(deathsReader).getRecords();
        List<CSVRecord> recovered = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(recoveredReader).getRecords();

        infections.forEach(infectionsRecord -> {

            double lat = Double.parseDouble(infectionsRecord.get("Lat"));
            double lon = Double.parseDouble(infectionsRecord.get("Long"));
            int confirmedInfectionsNumber = Integer.parseInt(infectionsRecord.get(yesterdayDate));
            int confirmedInfectionsNumberDayBefore = Integer.parseInt(infectionsRecord.get(dayBeforeYesterdayDate));

            Optional<CSVRecord> deathsRecord =
                    deaths.stream().filter(record -> record.get("Province/State").equals(infectionsRecord.get("Province/State"))
                            && record.get("Country/Region").equals(infectionsRecord.get("Country/Region"))).findFirst();
            int recoveredNumber = 0, recoveredNumberDayBefore = 0, deathsNumber = 0, deathsNumberDayBefore = 0;
            if (deathsRecord.isPresent()) {
                deathsNumber = Integer.parseInt(deathsRecord.get().get(yesterdayDate));
                deathsNumberDayBefore = Integer.parseInt(deathsRecord.get().get(dayBeforeYesterdayDate));
            }

            Optional<CSVRecord> recoveredRecord =
                    recovered.stream().filter(record -> record.get("Province/State").equals(infectionsRecord.get("Province/State"))
                            && record.get("Country/Region").equals(infectionsRecord.get("Country/Region"))).findFirst();
            if (recoveredRecord.isPresent()) {
                recoveredNumber = Integer.parseInt(recoveredRecord.get().get(yesterdayDate));
                recoveredNumberDayBefore = Integer.parseInt(recoveredRecord.get().get(dayBeforeYesterdayDate));
            }

            poits.add(new Poit(lat, lon,
                    "Liczba zakażonych: " + confirmedInfectionsNumber + " (" + (confirmedInfectionsNumber - confirmedInfectionsNumberDayBefore)
                            + ")",
                    "Liczba zmarłych: " + deathsNumber + " (" + (deathsNumber - deathsNumberDayBefore) + ")",
                    "Liczba ozdrowieńców: " + recoveredNumber + " (" + (recoveredNumber - recoveredNumberDayBefore) + ")",
                    "Aktywne zakażenia : " + (confirmedInfectionsNumber - (deathsNumber + recoveredNumber)),
                    "Dane na dzień : " + yesterday));
        });
        return poits;
    }
}