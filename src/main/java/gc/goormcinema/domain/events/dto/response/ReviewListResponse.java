package gc.goormcinema.domain.events.dto.response;

import lombok.Getter;

@Getter
public class ReviewListResponse {
    private final String username;
    private final Integer score;
    private final String content;

    public ReviewListResponse(String username, Integer score, String content) {
        this.username = username;
        this.score = score;
        this.content = content;
    }
}
