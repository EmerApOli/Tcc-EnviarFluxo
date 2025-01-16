package org.acme.enviofluxo.rsaassinarservice;

public class SignatureResponse {
    private String signature;
    private String signedPdf;
    private String publicKey;
    private String message;

    // Construtor vazio necessário para Jackson
    public SignatureResponse() {}

    // Getters e Setters
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignedPdf() {
        return signedPdf;
    }

    public void setSignedPdf(String signedPdf) {
        this.signedPdf = signedPdf;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
