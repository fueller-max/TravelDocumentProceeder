package util;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class XLSModifier {


    public static void modifyXLS(String fileReadPath, String fileWritePath, Map<String,String> cells2Modify) throws IOException {
        // Declare XMLS book from apache poi
        XSSFWorkbook XMLSBook;

        //Read xmls file
        try(FileInputStream ExcelFile = new FileInputStream(fileReadPath) ){

            XMLSBook = new XSSFWorkbook(ExcelFile); // Load file to RAM as DOM object

            //Extract common info
            System.out.println("The book name is: " + XMLSBook.getSheetName(0) + "\n" +
                               "The number of sheets is: " + XMLSBook.getNumberOfSheets());

            //Extracting all the sheets names
            ArrayList<String> sheetsName = new ArrayList<>();
            for(int sheet = 0; sheet < XMLSBook.getNumberOfSheets(); ++sheet){
                String sheetName = XMLSBook.getSheetName(sheet);
                sheetsName.add(sheetName);
            }

            //Modify all cell from cells2Modify map on the first page
            if(!sheetsName.isEmpty()){
                Sheet sheet = XMLSBook.getSheet(sheetsName.get(0));
                cells2Modify.entrySet().stream().forEach((data)->{
                            CellReference cellRef = new CellReference(data.getKey());
                            Row row = sheet.getRow(cellRef.getRow());
                            Cell cell2Modify = row.getCell(cellRef.getCol());
                            if(cell2Modify != null){
                                cell2Modify.setCellValue(data.getValue());
                            }else {
                                System.err.println("Cannot access the cell specified");
                            }
                        }

                );
            }
        }

        //Write the data to output file
        try(FileOutputStream fileOutput = new FileOutputStream(fileWritePath)){
            if(XMLSBook != null){
                XMLSBook.write(fileOutput);
            }
        }
        XMLSBook.close();
    }
}
