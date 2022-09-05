package pdfToXmls;


import util.SimplePDFReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class is dedicated to read given pdf file(task sheet) and
 * create a modified version of xmls file (travel report)
 */
public class PdfToXmls {

    private HashMap travelDetails= new HashMap();
    private ArrayList<String> pdfExtracted; //Each element => one page
    private HashMap<Integer, String[]> pdfPages = new HashMap<>(); // page => Array of strings

    public PdfToXmls(){
       travelDetails.put("Order", null);
       travelDetails.put("FuncLocation", null);
       travelDetails.put("Equipment", null);
       travelDetails.put("PO. No.", null);
   }

    public void proceedPdfToXmls(String pathPdfFile,
                                        String pathXmlsSrc,
                                        String pathXmlsDst)
    {

        try {
            //First read data from pdf file into ArrayList.
             pdfExtracted = SimplePDFReader.extractSimpleText(pathPdfFile);

            //put data to HashMap. Page => String[] of all lines
            if(pdfExtracted!=null) {
             for(int pages = 0; pages < pdfExtracted.size(); ++pages ){
                 pdfPages.put((pages + 1), pdfExtracted.get(pages).split("/n"));
             }
            }

            //DEBUG output
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


            } catch (IOException ex){
            ex.printStackTrace();
        }

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
