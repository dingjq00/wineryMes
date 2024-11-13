package com.mom.winery.app;

import io.jmix.core.metamodel.annotation.Comment;
import org.springframework.stereotype.Component;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PdfService {

    public byte[] generatePdfFromHtml(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, outputStream);
        return outputStream.toByteArray();
    }
}
