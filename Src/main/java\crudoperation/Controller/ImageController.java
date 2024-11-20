package com.ums.controller;

import com.ums.entity.Image;
import com.ums.entity.Property;
import com.ums.repository.ImageRepository;
import com.ums.repository.PropertyRepository;
import com.ums.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private BucketService bucketService;
    private ImageRepository imageRepository;
    private PropertyRepository propertyRepository;

    public ImageController(BucketService bucketService, ImageRepository imageRepository, PropertyRepository propertyRepository) {
        this.bucketService = bucketService;
        this.imageRepository = imageRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping("/addImage")
    public ResponseEntity<Image>addImages(
            @RequestParam long propertyId,
            @RequestParam String bucketName,
            MultipartFile file
    ){
       String imageUrl = bucketService.uploadFile(file, bucketName);
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property =byId.get();
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setProperty(property);
        Image savesImage = imageRepository.save(image);
        return new ResponseEntity<>(savesImage, HttpStatus.CREATED);
    }
    @GetMapping("/propertyImages")
    public ResponseEntity<List<Image>> fetchPropertyImages(
            @RequestParam  long propertyId){
        List<Image> images = imageRepository.findByPropertyId(propertyId);
        return new ResponseEntity<>(images,HttpStatus.OK);
    }
}
