package gc.goormcinema.domain.events.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventDetail {
    private final Long eventId;
    private final String title;
    private final String posterUrl;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final Integer fee;
    private final  Double avgRating;
    private final  Integer hits;
    private final  LocalDateTime createdAt;

    public EventDetail(Long eventId, String title, String posterUrl, LocalDateTime startsAt, LocalDateTime endsAt, Integer fee, Double avgRating, Integer hits, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.fee = fee;
        this.avgRating = avgRating;
        this.hits = hits;
        this.createdAt = createdAt;
    }
}
