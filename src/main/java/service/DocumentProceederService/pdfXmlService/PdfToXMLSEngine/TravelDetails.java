package service.DocumentProceederService.pdfXmlService.PdfToXMLSEngine;

public class TravelDetails {
    private String order;
    private String FuncLocation;
    private String Equipment;
    private String PO_No;

    public String getOrder() {
        return order;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public String getEquipment() {
        return Equipment;
    }

    public String getPO_No() {
        return PO_No;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public void setPO_No(String PO_No) {
        this.PO_No = PO_No;
    }
}
