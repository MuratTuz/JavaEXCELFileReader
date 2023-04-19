package main.java.org.example;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileWriter;
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
    private final int INDEX_X = 4;
    private final int INDEX_Y = 5;
    private final int INDEX_Z = 6;
    private final int INDEX_DISTANCE = 0;



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

//        Path myPath = Paths.get(track_path + filename);
        Path myPath = Paths.get(filename);

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
    public void readFileContent(String filename){

//        Path myPath = Paths.get(track_path + filename);
        Path myPath = Paths.get(filename);

        System.out.println("readFileStations filename_track_data::" + track_path + filename);

        double prev_X = 0.0;
        double prev_Y = 0.0;
        double prev_Z = 0.0;
        double prev_Distance = 0.0;

        try {
            File file = new File(track_path+"yeni.csv");
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);


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

                    if (prev_X == 0.0) { // the first line, so no need to calculate distance. it is starting point, that is 0.
                        prev_X = Double.parseDouble(line[INDEX_X]);
                        prev_Y = Double.parseDouble(line[INDEX_Y]);
                        prev_Z = Double.parseDouble(line[INDEX_Z]);
                        prev_Distance = Double.parseDouble(line[INDEX_DISTANCE]);
                        String [] newLine = line;
                        writer.writeNext(newLine);
                    } else {
                        double x = Double.parseDouble(line[INDEX_X]);
                        double y = Double.parseDouble(line[INDEX_Y]);
                        double z = Double.parseDouble(line[INDEX_Z]);

                        double distance = calculateDistance(prev_X,prev_Y,prev_Z,x,y,z);

                        prev_X = x;
                        prev_Y = y;
                        prev_Z = z;
                        prev_Distance = prev_Distance + distance;

                        String [] newLine = line;
                        newLine[INDEX_DISTANCE] = String.valueOf(prev_Distance);
                        writer.writeNext(newLine);
                    }
                }
            } else {
                System.out.println(filename+" file data is empty");
            }

            // close the reader
            reader.close();
            writer.close();
        }
        catch (IOException | CsvException e) {
            System.out.println("CsvException::by read the csv file::" + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     *
     * @param ox previous row's X value
     * @param oy
     * @param oz
     * @param x current row's X value
     * @param y
     * @param z
     * @return
     */
    public double calculateDistance(double ox, double oy, double oz, double x, double y, double z) {
        return Math.sqrt(Math.pow((x-ox),2) + Math.pow((y-oy),2) + Math.pow((z-oz),2));

}

}
