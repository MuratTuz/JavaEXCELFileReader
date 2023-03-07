package main.java.org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ReadFileEXCEL {

    public ReadFileEXCEL(String fileLocation) {
        this.fileLocation = fileLocation;

    }

    String fileLocation;

    public List<ValueEXCEL> readFile() throws IOException {
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new XSSFWorkbook(file);

        ArrayList<ValueEXCEL> foundStations = new ArrayList<>();

        // starts from 2. sheets because the first sheet does not have values
        for (int j = 1; j < workbook.getNumberOfSheets(); j++)
        {
            Sheet sheet = workbook.getSheetAt(j);

            // starts from row 7 which is beginning of the station list
            for (int i = 6; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(2);

                // if sheet has no value then pass to next sheet
                if (cell == null || cell.getRichStringCellValue().getString() == ""){
                    break;
                }
//                System.out.println("ROW : "+sheet.getLastRowNum());
                cell = row.getCell(10);

                if (cell == null || cell.getRichStringCellValue().getString() == ""){
                    ValueEXCEL valueEXCEL = new ValueEXCEL();
                    valueEXCEL.sheet = workbook.getSheetName(j);

                    valueEXCEL.platform = row.getCell(3).getCellType() == CellType.STRING ?
                            Integer.parseInt(row.getCell(3).getRichStringCellValue().getString()) :
                            (int) row.getCell(3).getNumericCellValue();

                    String station = row.getCell(2).getRichStringCellValue().getString();
                    int spaceIdx = station.indexOf(" ");
                    valueEXCEL.stationName = station.substring(spaceIdx);

                    valueEXCEL.line = i + 1;

                    Row rowNext = sheet.getRow(i+1);
                    valueEXCEL.nextPlatform = rowNext.getCell(3).getCellType() == CellType.STRING ?
                            Integer.parseInt(rowNext.getCell(3).getRichStringCellValue().getString()) :
                            (int) rowNext.getCell(3).getNumericCellValue();

                    station = rowNext.getCell(2).getRichStringCellValue().getString();
                    spaceIdx = station.indexOf(" ");
                    valueEXCEL.nextStationName = station.substring(spaceIdx);

//                    System.out.println(valueEXCEL);
                    foundStations.add(valueEXCEL);
                }
            }
        }
//        System.out.println("SAYI :: "+workbook.getNumberOfSheets());

        workbook.close();
        return foundStations;
    }


    /**
     * This function prevents collections from NullPointerException
     * @param c generic collection
     * @return
     * @param <T> empty list or generic collection which is input parameter
     */
    public static <T> Collection<T> nullSafe(Collection<T> c) {
        return (c == null) ? Collections.<T>emptyList() : c;
    }



}
