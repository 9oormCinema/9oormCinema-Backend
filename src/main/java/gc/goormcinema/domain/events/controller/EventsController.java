package gc.goormcinema.domain.events.controller;

import gc.goormcinema.domain.events.dto.request.SearchEventsRequest;
import gc.goormcinema.domain.events.dto.response.SearchEventsResponse;
import gc.goormcinema.domain.events.service.EventsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
