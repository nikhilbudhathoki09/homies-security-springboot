package homiessecurity.controllers;

import homiessecurity.dtos.Ratings.RatingRequestDto;
import homiessecurity.entities.Rating;
import homiessecurity.service.RatingService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ratings")
@CrossOrigin("*")
public class RatingsController {

    private final RatingService ratingService;

    @Autowired
    public RatingsController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @PostMapping("/")
    public ResponseEntity<Rating> addRating(@RequestBody RatingRequestDto request,
                                            @RequestParam Integer userId,
                                            @RequestParam Integer providerId
                                            ){
        request.setUserId(userId);
        request.setProviderId(providerId);
        return new ResponseEntity<>(this.ratingService.addRating(request), HttpStatus.CREATED);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Integer ratingId){
        return new ResponseEntity<>(this.ratingService.getRatingById(ratingId), HttpStatus.OK);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer ratingId){
        return new ResponseEntity<>(this.ratingService.deleteRating(ratingId), HttpStatus.OK);
    }

//    @PutMapping("/{ratingId}")
//    public ResponseEntity<Rating> updateRating(@PathVariable Integer ratingId, @ModelAttribute RatingRequestDto request){
//        Rating rating = this.ratingService.getRatingById(ratingId);
//        rating.setRating(request.getRating());
//        rating.setComment(request.getComment());
//        return new ResponseEntity<>(this.ratingService.updateRating(rating), HttpStatus.OK);
//    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<?> getRatingsByProviderId(@PathVariable Integer providerId){
        return new ResponseEntity<>(this.ratingService.getRatingsByProviderId(providerId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRatingsByUserId(@PathVariable Integer userId){
        return new ResponseEntity<>(this.ratingService.getRatingsByUserId(userId), HttpStatus.OK);
    }
}
