package homiessecurity.service.impl;

import homiessecurity.dtos.Ratings.RatingRequestDto;
import homiessecurity.entities.Rating;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.User;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.RatingRepository;
import homiessecurity.service.ProviderService;
import homiessecurity.service.RatingService;
import homiessecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepo;
    private final ProviderService providerService;
    private final UserService userService;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepo,ProviderService providerService,
                             UserService userService){
        this.ratingRepo = ratingRepo;
        this.providerService = providerService;
        this.userService = userService;
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        return this.ratingRepo.findById(ratingId).orElseThrow(() ->
                new ResourceNotFoundException("Rating", "ratingId", ratingId));
    }

    @Override
    public Rating addRating(RatingRequestDto request) {
        ServiceProvider provider = providerService.getProviderById(request.getProviderId());
        User user = userService.getRawUserById(request.getUserId());

        System.out.println(request.getComment() + " " + request.getRating() + " " + request.getProviderId() + " " + request.getUserId());

        Rating rating = Rating.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .provider(provider)
                .user(user)
                .reviewDate(LocalDateTime.now())
                .build();
        return this.ratingRepo.save(rating);
    }

    @Override
    public Rating updateRating(Rating rating) {
        return null;
    }

    @Override
    public ApiResponse deleteRating(Integer ratingId) {
        return this.ratingRepo.findById(ratingId).map(rating -> {
            this.ratingRepo.delete(rating);
            return new ApiResponse("Rating deleted successfully",true);
        }).orElseThrow(() -> new ResourceNotFoundException("Rating", "ratingId", ratingId));
    }

    @Override
    public List<Rating> getRatingsByProviderId(Integer providerId) {
        return ratingRepo.findAllByProviderId(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "providerId", providerId));
    }

    @Override
    public List<Rating> getRatingsByUserId(Integer userId) {
        return ratingRepo.findAllByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "userId", userId));
    }
}
