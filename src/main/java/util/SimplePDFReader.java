package util;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SimplePDFReader {

    private SimplePDFReader(){}; // Make ctor private to disable class instantiation

    public static ArrayList<String> extractSimpleText(String filepath) throws IOException {
        ArrayList<String> pdfExtracted = new ArrayList<>();
        try(PDDocument document = PDDocument.load(new File(filepath)) ) {

            AccessPermission ap = document.getCurrentAccessPermission();
            if(!ap.canExtractContent()){
                throw new IOException("No permission to extract the text from file...");
            }

            PDFTextStripper textStripper = new PDFTextStripper();

            //read all the sheets from the documents
            for(int page = 1; page <= document.getNumberOfPages(); ++page){
                textStripper.setStartPage(page);
                textStripper.setEndPage(page);
                String pageText = textStripper.getText(document);
                pdfExtracted.add(pageText);
            }

        }
        return pdfExtracted;
    }
}
