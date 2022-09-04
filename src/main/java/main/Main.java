package main;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import pdfToXmls.PdfToXmls;
import util.*;


public class Main {
    public static void main(String[] args) throws IOException {
        PdfToXmls pdfToXmls = new PdfToXmls();

        pdfToXmls.proceedPdfToXmls("C:\\Dev\\Java\\Projects\\TravelDocumentProceeder\\src\\main\\resources\\travel-test.pdf",
                 null, null);





        // HashMap<String, String> cellsToModify = new HashMap<String, String>();
       // cellsToModify.put("I14", "OOO \"Perdushok\"");

        /*XLSModifier.modifyXLS("C:\\Dev\\Java\\Projects\\TravelDocumentProceeder\\" +
                        "src\\main\\resources\\Time Report_Ovchinnikov_Saransk_Mechta_CW35.xlsx",
                "C:\\Dev\\Java\\Projects\\TravelDocumentProceeder\\src" +
                        "\\main\\resources\\Time Report_Ovchinnikov_Saransk_Mechta_CW35_MOD.xlsx",
                cellsToModify);*/
    }
    }

