package gc.goormcinema.domain.events.service;

import gc.goormcinema.domain.events.dto.response.CreateReviewResponse;
import gc.goormcinema.domain.events.dto.response.EventDetailResponse;
import gc.goormcinema.domain.events.dto.response.ReviewListResponse;
import gc.goormcinema.domain.events.dto.response.SearchEventsResponse;
import gc.goormcinema.domain.events.entity.Event;
import gc.goormcinema.domain.events.entity.EventDetail;
import gc.goormcinema.domain.events.entity.Venue;
import gc.goormcinema.domain.events.enums.Sort;
import gc.goormcinema.domain.events.repository.EventsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class EventsService {
    private final EventsRepository eventsRepository;

    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public List<SearchEventsResponse> searchEvents(Long category_id, String q, String sortTmp){
        System.out.println("service 실행됨");
        Sort sort = Sort.valueOf(sortTmp.toUpperCase());
        String category="";
        if (category_id == 1) {
            category = "뮤지컬";
        } else if (category_id == 2) {
            category = "연극";
        } else if (category_id == 3) {
            category = "콘서트";
        }

        List<Event> list = eventsRepository.searchEvents(category, q);

        if (sort != null) {
            list.sort((a, b) -> {
                switch (sort) {
                    case LATEST -> {
                        return b.getStartTime().compareTo(a.getStartTime()); // 최신순
                    }
                    case HITS -> {
                        return Integer.compare(b.getHits(), a.getHits()); // 조회수 내림차순
                    }
                    case SCORE -> {
                        return Double.compare(b.getAvgRating(), a.getAvgRating()); // 평점 내림차순
                    }
                    default -> {
                        return 0; // 정렬 없음
                    }
                }
            });
        }

        System.out.println("service 종료");
        System.out.println("list 갯수 : " + list.size());
        return list.stream()
                .map(event -> new SearchEventsResponse(
                        event.getEvent_id(),
                        event.getTitle(),
                        event.getPoster_url(),
                        event.getVenue_name(),  // venue는 연관 엔티티라고 가정
                        event.getAvgRating(),
                        event.getHits()
                ))
                .collect(Collectors.toList());
    }

    public EventDetailResponse getEventDetail(Long event_id){
        System.out.println("event-detail => service 동작함");
        Long venue_id = eventsRepository.findVenueIdByEventId(event_id);
        Venue venue = eventsRepository.findVenueByVenueId(venue_id);
        EventDetail eventDetail = eventsRepository.findEventDetailByEventId(event_id);

        return new EventDetailResponse(eventDetail.getEventId(),
                eventDetail.getTitle(),
                eventDetail.getPosterUrl(),
                venue, eventDetail.getStartsAt(),
                eventDetail.getEndsAt(), eventDetail.getFee(),
                eventDetail.getAvgRating(), eventDetail.getHits(),
                eventDetail.getCreatedAt());
    }

    public CreateReviewResponse createReview(Long event_id, Long user_id, Integer rating, String comment){
        eventsRepository.updateRating(event_id, rating);
        return eventsRepository.createReview(event_id, user_id, rating, comment);
    }

    public List<ReviewListResponse> getReviews(Long eventId){
        return eventsRepository.getReviews(eventId);
    }

}
