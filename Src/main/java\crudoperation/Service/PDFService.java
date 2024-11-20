package com.ums.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ums.entity.Booking;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PDFService {

    public String generateBookingDetailsPdf(Booking booking) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("E://airbnb-booking//booking-confirmation" + booking.getId() + ".pdf"));
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Booking Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // Create a table with 2 columns
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            // Add table headers
            table.addCell(createCell("Guest Name", booking.getGuestName()));
            table.addCell(createCell("Total Nights", String.valueOf(booking.getTotalNights())));
            table.addCell(createCell("Total Price", String.valueOf(booking.getTotalPrice())));
            table.addCell(createCell("Booking Date", booking.getBookingDate().toString()));
            table.addCell(createCell("Check-In Time", String.valueOf(booking.getCheckInTime())));

            document.add(table);
            return "E://airbnb-booking//booking-confirmation" + booking.getId() + ".pdf";

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }finally{
            if(document!= null){
                document.close();
            }
        }
        return null;

    }

   private PdfPCell createCell(String label,String value) {
       PdfPCell cell = new PdfPCell();
       cell.addElement(new Paragraph(label));
       cell.addElement(new Paragraph(value));
       return cell;
   }
}
