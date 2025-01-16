package org.acme.enviofluxo.rsaassinarservice;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

@ApplicationScoped
public class PDFService {

    public byte[] addSignatureToDocument(byte[] pdfBytes, String signature) throws IOException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDPage lastPage = document.getPage(document.getNumberOfPages() - 1);

            // Adiciona a assinatura no final do documento
            PDPageContentStream contentStream = new PDPageContentStream(
                    document,
                    lastPage,
                    PDPageContentStream.AppendMode.APPEND,
                    true,
                    true
            );

            // Adiciona uma linha separadora
            contentStream.moveTo(50, 50);
            contentStream.lineTo(550, 50);
            contentStream.stroke();

            // Adiciona o texto da assinatura
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 8);
            contentStream.newLineAtOffset(50, 30);
            contentStream.showText("Assinatura Digital: " + signature);
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    public byte[] getDocumentHash(byte[] pdfBytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(pdfBytes);
    }
}
