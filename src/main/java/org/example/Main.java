package main.java.org.example;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String fileLocation = "src/main/java/data/BU_5187161.xlsx";

        try {
            ReadFileEXCEL readFileEXCEL = new ReadFileEXCEL(fileLocation);
            List<ValueEXCEL> valueEXCELList;
            valueEXCELList = readFileEXCEL.readFile();
            System.out.println("Number of missing lines : "+valueEXCELList.size());
            valueEXCELList.stream().forEach(s -> System.out.println(s));

        }
        catch (IOException e) {
            System.out.println("Dummy  csv file file read exception::" + e.getMessage());
            e.printStackTrace();
        }

    }
}