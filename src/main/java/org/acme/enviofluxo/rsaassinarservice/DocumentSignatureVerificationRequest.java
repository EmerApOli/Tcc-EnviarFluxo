package org.acme.enviofluxo.rsaassinarservice;

public class DocumentSignatureVerificationRequest {
    private String pdfBase64;
    private String signature;

    // Getters e Setters
    public String getPdfBase64() {
        return pdfBase64;
    }

    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

