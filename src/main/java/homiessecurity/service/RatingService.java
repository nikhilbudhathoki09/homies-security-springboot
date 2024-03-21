package homiessecurity.service;

import homiessecurity.dtos.Ratings.RatingRequestDto;
import homiessecurity.entities.Rating;
import homiessecurity.payload.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    Rating getRatingById(Integer ratingId);

    Rating addRating(RatingRequestDto rating);

    Rating updateRating(Rating rating);

    ApiResponse deleteRating(Integer ratingId);

    List<Rating> getRatingsByProviderId(Integer providerId);

    List<Rating> getRatingsByUserId(Integer userId);
}
