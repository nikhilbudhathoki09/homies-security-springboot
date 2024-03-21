package homiessecurity.dtos.Ratings;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequestDto {
    private Integer rating;
    private String comment;
    private Integer userId;
    private Integer providerId;
}
