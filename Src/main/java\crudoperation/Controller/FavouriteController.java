package com.ums.controller;

import com.ums.entity.AppUser;
import com.ums.entity.Favourite;
import com.ums.entity.Property;
import com.ums.repository.FavouriteRepository;
import com.ums.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favourite")
public class FavouriteController {

    private FavouriteRepository fr;
    private PropertyRepository pr;

    public FavouriteController(FavouriteRepository fr, PropertyRepository pr) {
        this.fr = fr;
        this.pr = pr;
    }
    @PostMapping("                    ")
    public ResponseEntity<Favourite> addFavourite(
            @AuthenticationPrincipal AppUser user,
            @RequestParam long propertyId
            ){
        Optional<Property> byId = pr.findById(propertyId);
        Property property = byId.get();
        Favourite favourite= new Favourite();
        favourite.setAppUser(user);
        favourite.setProperty(property);
        favourite.setIsFavourite(true);
        Favourite savedFavourite = fr.save(favourite);
        return new ResponseEntity<>(savedFavourite, HttpStatus.CREATED);
    }
    @GetMapping("/userFavouriteList")
    public ResponseEntity<List<Favourite>> getAllFavouriteOfUser(
            @AuthenticationPrincipal AppUser user
    ){
         List<Favourite> favourites=  fr.getFavourites(user);
        return new ResponseEntity<>(favourites,HttpStatus.OK);
    }
}
