package gc.goormcinema.domain.events.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Event {
    private final Long event_id;
    private final String title;
    private final String poster_url;
    private final String venue_name;
    private final double avgRating;
    private final int hits;
    private final LocalDateTime startTime;

    public Event(Long event_id, String title, String poster_url, String venue_name, double avg_rating, int hits, LocalDateTime startTime) {
        this.event_id = event_id;
        this.title = title;
        this.poster_url = poster_url;
        this.venue_name = venue_name;
        this.avgRating = avg_rating;
        this.hits = hits;
        this.startTime = startTime;
    }
}
