package gc.goormcinema.domain.events.dto.request;

import gc.goormcinema.domain.events.enums.Sort;
import lombok.Getter;

@Getter
public class SearchEventsRequest {
    private final Long category_id;
    private final String q;
    private final String sort;

    public SearchEventsRequest(Long category_id, String q, String sort) {
        this.category_id = category_id;
        this.q = q;
        this.sort = sort;
    }

}
