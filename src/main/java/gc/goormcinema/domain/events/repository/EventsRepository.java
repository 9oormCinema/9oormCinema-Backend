package gc.goormcinema.domain.events.repository;

import gc.goormcinema.domain.events.dto.response.SearchEventsResponse;
import gc.goormcinema.domain.events.entity.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventsRepository {

    private final JdbcTemplate jdbcTemplate;

    public EventsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Event> searchEvents(String category, String q){
        System.out.println("repository 실행됨");
        String sql = "select * from performance WHERE category = ? AND title LIKE CONCAT('%', ?, '%')";
        return jdbcTemplate.query(sql, new RowMapper<Event>() {
            @Override
            public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long event_id = rs.getLong("performance_id");
                String title = rs.getString("title");
                String poster_url = rs.getString("poster_url");
                Long venue_id = rs.getLong("venue_id");

                System.out.println("event id = " + event_id);

                String sql = "select name from venue where venue_id = ?";
                String venue_name = jdbcTemplate.query(sql, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("name");
                    }
                }, venue_id).get(0);

                double avg_rating = rs.getDouble("avg_rating");
                int hits = rs.getInt("hits");
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();

                return new Event(event_id, title, poster_url, venue_name, avg_rating, hits, startTime);
            }
        }, category, q);
    }
}
