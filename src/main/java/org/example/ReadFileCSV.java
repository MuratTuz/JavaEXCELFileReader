package org.example;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFileCSV {

    public ReadFileCSV(String track_path) {
        this.track_path = track_path;
    }

    String track_path;
    CSVParser parser;
    private final char CSV_SEPARATOR=';';
    private final int INDEX_CSVHeader = 1;
    private final int INDEX_STATION = 9;

    /**
     * Reads all files in a given path
     * @return all unique file names in the directory or null
     */
    public Set<String> getAllFileNames() {
        try (Stream<Path> stream = Files.list(Paths.get(track_path))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
        catch (Exception e){
            System.out.println("getAllFileNames::by read all the files::" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads a single track data file
     * @param filename
     * @return station names list
     */
    public List<String> readFileStations(String filename){

        Path myPath = Paths.get(track_path + filename);
        System.out.println("readFileStations filename_track_data::" + track_path + filename);

        List<String> stationNames = new ArrayList<>();

        try {

            Reader reader = Files.newBufferedReader(myPath);

            // create csv bean reader
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .withSkipLines(INDEX_CSVHeader)
                    .build();

            List<String[]> records = csvReader.readAll();

            if (!records.isEmpty()) {

                for (String[] record : records) {
                    // csvReader returns 1 length string array. Parse it to a string array.
                    String[] line = Arrays.toString(record).replace("[", "").replace("]", "").split(",");
                    // remove parentheses in "Frankenstein (Platz 1)" station name since there is no such names in station index file (Haltestelleliste.csv)
                    String stationName = line[INDEX_STATION].replaceAll(" \\(.*?\\)","");
                    if (!stationName.equalsIgnoreCase("FALSE")) {
                        stationNames.add(stationName);
                    }
                }
            } else {
                System.out.println(filename+" file data is empty");
                return Collections.emptyList();
            }

            // close the reader
            reader.close();

        }
        catch (IOException | CsvException e) {
            System.out.println("CsvException::by read the csv file::" + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }

        return stationNames;
    }


}
