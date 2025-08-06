package gc.goormcinema.domain.events.controller;

import gc.goormcinema.domain.events.dto.request.SearchEventsRequest;
import gc.goormcinema.domain.events.dto.response.CreateReviewResponse;
import gc.goormcinema.domain.events.dto.response.EventDetailResponse;
import gc.goormcinema.domain.events.dto.response.ReviewListResponse;
import gc.goormcinema.domain.events.dto.response.SearchEventsResponse;
import gc.goormcinema.domain.events.service.EventsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EventsController {
    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping("/events")
    public List<SearchEventsResponse> searchEvents(SearchEventsRequest request){
        System.out.println("controller 수행됨");
        System.out.println(request.getCategory_id());
        return eventsService.searchEvents(request.getCategory_id(), request.getQ(), request.getSort());
    }

    @GetMapping("/event-detail")
    public EventDetailResponse getEventDetail(@RequestParam("event_id") Long event_id){
        System.out.println("event-detail => controller 동작함");
        return eventsService.getEventDetail(event_id);
    }

    @PostMapping("/events/{eventId}/reviews")
    public CreateReviewResponse createReview(
            @PathVariable Long eventId,
            @RequestBody Map<String, Object> payload
    ) {
        // Map에서 바로 꺼내 쓰기
        Long userId  = ((Number) payload.get("userId")).longValue();
        int  rating  = ((Number) payload.get("rating")).intValue();
        String comment = (String) payload.get("comment");

        // 서비스 호출 (기존 시그니처에 맞춰서)
        return eventsService.createReview(eventId, userId, rating, comment);
    }

    @GetMapping("/reviews")
    public List<ReviewListResponse> getReviews(@RequestParam("event_id") Long eventId){
        return eventsService.getReviews(eventId);
    }

}
