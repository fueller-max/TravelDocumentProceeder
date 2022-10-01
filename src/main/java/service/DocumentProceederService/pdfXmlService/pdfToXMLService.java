package service.DocumentProceederService.pdfXmlService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import service.DocumentProceederService.pdfXmlService.PdfToXMLSEngine.PdfToXmls;
import service.DocumentProceederService.DocumentProceederService;
import service.DocumentProceederService.pdfXmlService.config.pdfToXMLSConfig;
import service.storageService.StorageService;

@Service
public class pdfToXMLService implements DocumentProceederService {

    private final String basicXMLS;//
    private final String modXMLS; //

    StorageService storageService;

    PdfToXmls pdfToXmls = new PdfToXmls();

    @Autowired
    public pdfToXMLService(pdfToXMLSConfig config,
                           StorageService storageService){
        this.storageService = storageService;
        this.basicXMLS = config.getBasicXMLSPath();
        this.modXMLS = config.getModifiedXMLSPath();
    }

    public Resource proceed(String pdfTask){
        return storageService.loadAsResource(pdfToXmls.proceedPdfToXmls(pdfTask,basicXMLS, modXMLS));
    }
}
