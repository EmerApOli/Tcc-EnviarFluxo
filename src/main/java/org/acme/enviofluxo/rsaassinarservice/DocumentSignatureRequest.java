package org.acme.enviofluxo.rsaassinarservice;

public class DocumentSignatureRequest {
    private String pdfBase64;

    // Getters e Setters
    public String getPdfBase64() {
        return pdfBase64;
    }

    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }
}
