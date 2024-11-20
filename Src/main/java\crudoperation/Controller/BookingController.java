
package com.ums.controller;

import com.ums.entity.AppUser;
import com.ums.entity.Booking;
import com.ums.entity.Property;
import com.ums.repository.BookingRepository;
import com.ums.repository.PropertyRepository;
import com.ums.service.BucketService;
import com.ums.service.PDFService;
import com.ums.service.TwilioService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {


    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private TwilioService twilioService;
    private BucketService bucketService;
    private PDFService pdfService;

    public BookingController(BookingRepository bookingRepository, PropertyRepository propertyRepository, TwilioService twilioService, BucketService bucketService, PDFService pdfService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.twilioService = twilioService;
        this.bucketService = bucketService;
        this.pdfService = pdfService;
    }
      @PostMapping("/createBooking")
    public ResponseEntity<Booking>createBooking(
            @RequestParam long propertyId,
            @RequestBody Booking booking,
            @AuthenticationPrincipal AppUser user

    ){
        Property property = propertyRepository.findById(propertyId).get();
        int nightlyPrice =property.getNightlyPrice();
        int totalPrice = nightlyPrice* booking.getTotalNights();
        //double priceWithTax = totalPrice*(18/100);

        booking.setTotalPrice(totalPrice);
        booking.setProperty(property);
        booking.setAppUser(user);
        Booking savedBooking = bookingRepository.save(booking);
          String filePath = pdfService.generateBookingDetailsPdf(savedBooking);
          try {
              MultipartFile fileMultiPart = BookingController.convert(filePath);
              String fileUploadedUrl = bucketService.uploadFile(fileMultiPart, "vahidairbnb");
              System.out.println(fileUploadedUrl);
              sendMessage(fileUploadedUrl);
          } catch (IOException e) {
              e.printStackTrace();
          }
          //
          return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    public void sendMessage(String url){

        twilioService.sendSMS("+919121405593","Your Booking is confirmed. Click here:"+url);
    }


    public static MultipartFile convert(String filePath) throws IOException {
        // Read the file content into a byte array
        File file = new File(filePath);

        byte[] fileContent = Files.readAllBytes(file.toPath());

        Resource resource = new ByteArrayResource(fileContent);

        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }
            @Override
            public String getOriginalFilename() {
                return file.getName();
            }
            @Override
            public String getContentType() {
                return null;
            }
            @Override
            public boolean isEmpty() {
                return fileContent.length== 0;
            }
            @Override
            public long getSize() {
                return fileContent.length;
            }
            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }
            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }
            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
              Files.write(dest.toPath(),fileContent);
            }
        };
        return multipartFile;
    }

    }
