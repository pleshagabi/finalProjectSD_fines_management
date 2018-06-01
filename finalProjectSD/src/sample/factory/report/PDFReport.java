package sample.factory.report;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import data.server.model.Driver;
import data.server.model.Fine;

import java.io.IOException;
import java.util.List;

/**
 * Created by plesha on 09-Apr-18.
 */

public class PDFReport implements Report {
    @Override
    public void createReportFile( List<Fine> fineList, List<Driver> driverLis ) throws IOException {


        String destination = "E:\\Police Office Report.pdf";

        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(destination);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);

        // Create a PdfFont
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        // Add a Paragraph
        document.add(new Paragraph("Generated report with drivers who did not paid the fine until the deadline date ").add(" \n").setFont(font));

        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlCurrentDate = new java.sql.Date(utilDate.getTime());

        Boolean printOnce;

        for( Driver driver : driverLis ) {
            printOnce = false;
            for (Fine fine : fineList) {
                if (fine.getIsPayd() == 0 && (fine.getDriver_idDriver() == driver.getIdDriver()) ) {
                    if (fine.getDeadlineDate().compareTo(sqlCurrentDate) < 0) {

                        Paragraph paragraph = new Paragraph();
                        if( !printOnce ) {
                            paragraph.add("\n\n\nDriver________________________________________________" + "\n")
                                    .add("Driver Id: " + driver.getIdDriver() + "\n")
                                    .add("Driver Name: " + driver.getName() + "\n")
                                    .add("Driver Age: " + driver.getAge() + "\n")
                                    .add("Driver Address: " + driver.getAddress() + "\n")
                                    .add("Driver E-mail: " + driver.getEmail() + "\n")
                                    .add("Driver Phone: " + driver.getPhoneNumber() + "\n")
                                    .add("Fine(s)___________________________________________________\n");
                            printOnce = true;
                        }
                        paragraph.add("Fine Id: " + fine.getIdFine() + "\n")
                                .add("Date Crime Committed: " + fine.getDateFineCommited() + "\n")
                                .add("Crime Type: " + fine.getCrimeType() + "\n")
                                .add("Fine Price: " + fine.getPrice() + " lei\n")
                                .add("Fine Payment Deadline Date: " + fine.getDeadlineDate() + "\n")
                                .add("Driver Id: " + fine.getDriver_idDriver() + "\n")
                                .add("_____________________________________________________" + "\n");

                        document.add(paragraph);
                    }
                }
            }
        }

        //Close document
        document.close();
    }
}

