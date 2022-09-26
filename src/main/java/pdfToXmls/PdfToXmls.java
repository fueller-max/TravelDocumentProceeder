package pdfToXmls;


import entity.Cells2Modify;
import entity.TravelDetails;
import util.SimplePDFReader;
import util.XLSModifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class is dedicated to read given pdf file(task sheet) and
 * create a modified version of xmls file (travel report)
 */
public class PdfToXmls {

    private TravelDetails travelDetails = new TravelDetails();
    private ArrayList<String> pdfExtracted; //Each element => one page
    private HashMap<Integer, String[]> pdfPages = new HashMap<>(); // page => Array of strings

    private final static String REGEX_ORDER_NUMBER_DETECT = "905[0-9]{6}";
    private final static String REGEX_ORDER_NUMBER_EXTRACT = null;

    private final static String REGEX_FUNCTIONAL_LOCATION_NUMBER_DETECT = "C3[0-9]{5}";
    private final static String REGEX_FUNCTIONAL_LOCATION_NUMBER_EXTRACT = null;

    private final static String REGEX_PO_NUM_DETECT = "PO. No.+[0-9]";
    private final static String REGEX_PO_NUM_EXTRACT = "[0-9_]+";

    private HashMap<String, String > cellsData = new HashMap<>();


    public PdfToXmls(){

   }

    public void proceedPdfToXmls(String pathPdfFile,
                                        String pathXmlsSrc,
                                        String pathXmlsDst)
    {

        try {
            //First read data from pdf file into ArrayList.
             pdfExtracted = SimplePDFReader.extractSimpleText(pathPdfFile);

            //put data to HashMap. Page => String[] of all lines
           /* if(pdfExtracted!=null) {
             for(int pages = 0; pages < pdfExtracted.size(); ++pages ){
                 pdfPages.put((pages + 1), pdfExtracted.get(pages).split("/n"));
             }
            }*/

            //DEBUG output
            {
                pdfPages.forEach((page, lines)->{
                    for(int line = 0; line < lines.length; ++line )
                    {
                        System.out.println(lines[line]);
                    }
                });

                //Test Regex
                System.out.println("Testing regex...");

                System.out.println(extractDataPoint(0,"905[0-9]{6}", null).get());
                System.out.println(extractDataPoint(0,"C3[0-9]{5}", null).get());
                System.out.println(extractDataPoint(0,"PO. No.+[0-9]", "[0-9_]+").get());
            }


           //Fill TravelDetails POJO class
            extractAllDataPoints();

            //Modify XMLS file

            cellsData.put(Cells2Modify.OrderNumber, travelDetails.getOrder());
            cellsData.put(Cells2Modify.LocationNo, travelDetails.getFuncLocation());
            cellsData.put(Cells2Modify.PONum, travelDetails.getPO_No());

            XLSModifier.modifyXLS("D:\\Dev\\Java\\Projects\\TravelDocumentProceeder\\src\\main\\resources\\time_report.xlsx",
                                     "D:\\Dev\\Java\\Projects\\TravelDocumentProceeder\\src\\main\\resources\\time_report_MOD.xlsx",
                                   cellsData);


            } catch (IOException ex){
            ex.printStackTrace();
        }

    }


    private void extractAllDataPoints(){
        travelDetails.setFuncLocation(extractDataPoint(0, REGEX_FUNCTIONAL_LOCATION_NUMBER_DETECT,
                REGEX_FUNCTIONAL_LOCATION_NUMBER_EXTRACT).get());
        travelDetails.setOrder(extractDataPoint(0, REGEX_ORDER_NUMBER_DETECT,
                REGEX_ORDER_NUMBER_EXTRACT).get());
        travelDetails.setPO_No(extractDataPoint(0, REGEX_PO_NUM_DETECT,
                REGEX_PO_NUM_EXTRACT).get());

    }

    private Optional<String> extractDataPoint(int page, String regexDetect, String regexExtract){
        Optional<String> result =  Optional.empty();
        if(pdfExtracted != null){
            Pattern detectPattern = Pattern.compile(regexDetect);
            String pageStr =pdfExtracted.get(page);
            Matcher detectMatcher = detectPattern.matcher(pageStr);
            String resultStr = new String();
            while(detectMatcher.find()){
                resultStr = pageStr.substring(detectMatcher.start(),detectMatcher.end());
            }
            if (regexExtract == null){
                return Optional.of(resultStr);}
            Pattern extractPattern = Pattern.compile(regexExtract);
            Matcher extractMatcher = extractPattern.matcher(resultStr);
            String resultExtract = new String();
            while(extractMatcher.find()){
                resultExtract = resultStr.substring(extractMatcher.start(),extractMatcher.end());
            }
            return Optional.of(resultExtract);
        }
        return result;
    }
}
