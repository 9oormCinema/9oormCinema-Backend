package gc.goormcinema.domain.events.dto.response;

import gc.goormcinema.domain.events.entity.Review;
import lombok.Getter;

@Getter
public class CreateReviewResponse {
    Review review;

    public CreateReviewResponse(Review review) {
        this.review = review;
    }
}
