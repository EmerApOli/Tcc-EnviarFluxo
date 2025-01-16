package org.acme.enviofluxo.rsaassinarservice;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.*;
import java.util.Base64;

@ApplicationScoped
public class RSAService {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAService() {
        try {
            generateKeys(2048);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar chaves RSA", e);
        }
    }

    private void generateKeys(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Armazena as chaves
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();

        // Imprime a chave privada
     //   System.out.println("Chave Privada:");
      //  System.out.println(Base64.getEncoder().encodeToString(this.privateKey.getEncoded()));

        // Imprime a chave pública
      //  System.out.println("Chave Pública:");
       // System.out.println(Base64.getEncoder().encodeToString(this.publicKey.getEncoded()));


        // Imprime a chave privada em formato PEM
        System.out.println("Chave Privada (PEM):");
        System.out.println(convertToPEM(this.privateKey.getEncoded(), "PRIVATE KEY"));

        // Imprime a chave pública em formato PEM
        System.out.println("Chave Pública (PEM):");
        System.out.println(convertToPEM(this.publicKey.getEncoded(), "PUBLIC KEY"));
    }

    private String convertToPEM(byte[] key, String keyType) {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(keyType).append("-----\n");
        pem.append(Base64.getEncoder().encodeToString(key));
        pem.append("\n-----END ").append(keyType).append("-----");
        return pem.toString();




    }

    public String signDocument(byte[] documentHash) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(documentHash);

        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }  

    public boolean verifyDocumentSignature(byte[] documentHash, String signatureStr) throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(documentHash);

        return signature.verify(signatureBytes);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
