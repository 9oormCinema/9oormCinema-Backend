package gc.goormcinema.domain.events.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SearchEventsResponse {
    private final Long event_id;
    private final String title;
    private final String poster_url;
    private final String venue_name;
    private final double avg_rating;
    private final int hits;

    public SearchEventsResponse(Long event_id, String title, String poster_url, String venue_name, double avg_rating, int hits) {
        this.event_id = event_id;
        this.title = title;
        this.poster_url = poster_url;
        this.venue_name = venue_name;
        this.avg_rating = avg_rating;
        this.hits = hits;
    }
}
