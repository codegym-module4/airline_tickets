package com.codegym.airline_tickets.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class ExportPDFTicket {
    public String htmlToPdf(String processedHtml, HttpServletResponse response) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);

            DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, false);

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setFontProvider(defaultFont);

            HtmlConverter.convertToPdf(processedHtml, pdfwriter, converterProperties);

//            FileOutputStream fout = new FileOutputStream("/Users/lexuanngoc2207/Desktop/export/ticket.pdf");

            byteArrayOutputStream.writeTo(response.getOutputStream());
            byteArrayOutputStream.close();

            byteArrayOutputStream.flush();
//            fout.close();

            return null;

        } catch (Exception ex) {

            //exception occured
        }

        return null;
    }


}
