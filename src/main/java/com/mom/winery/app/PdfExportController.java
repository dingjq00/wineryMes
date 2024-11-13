package com.mom.winery.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
public class PdfExportController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestBody String htmlContent) throws IOException {
        // Generate the PDF from the HTML content
        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

        // Set the headers and return the PDF as a response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "export.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
