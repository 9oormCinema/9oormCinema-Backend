package gc.goormcinema.domain.events.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Review {
    private final Long reviewId;
    private final  String userName;
    private final   Integer rating;
    private final  String comment;
    private final   LocalDateTime createdAt;

    public Review(Long reviewId, String userName, Integer rating, String comment, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
