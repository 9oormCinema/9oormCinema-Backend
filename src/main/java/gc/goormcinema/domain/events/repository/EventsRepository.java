package gc.goormcinema.domain.events.repository;

import gc.goormcinema.domain.events.dto.response.CreateReviewResponse;
import gc.goormcinema.domain.events.dto.response.ReviewListResponse;
import gc.goormcinema.domain.events.dto.response.SearchEventsResponse;
import gc.goormcinema.domain.events.entity.Event;
import gc.goormcinema.domain.events.entity.EventDetail;
import gc.goormcinema.domain.events.entity.Review;
import gc.goormcinema.domain.events.entity.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public Long findVenueIdByEventId(Long event_id){
        String sql = "select venue_id from performance where performance_id = ?";
        return jdbcTemplate.query(sql, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("venue_id");
            }
        }, event_id).get(0);
    }

    public Venue findVenueByVenueId(Long venueId){
        String sql = "select name, address from venue where venue_id = ?";
        return jdbcTemplate.query(sql, new RowMapper<Venue>() {
            @Override
            public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("name");
                String address = rs.getString("address");
                return new Venue(name, address);
            }
        }, venueId).get(0);
    }

    public EventDetail findEventDetailByEventId(Long event_id){
        String sql = "select * from performance where performance_id = ?";
        System.out.println("event-detail => repository 동작함");
        return jdbcTemplate.query(sql, new RowMapper<EventDetail>() {
            @Override
            public EventDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long eventId = rs.getLong("performance_id");
                String title = rs.getString("title");
                String posterUrl = rs.getString("poster_url");
                LocalDateTime startsAt = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endsAt = rs.getTimestamp("end_time").toLocalDateTime();
                Integer fee = rs.getInt("fee");
                Double avgRating = rs.getDouble("avg_rating");
                Integer hits = rs.getInt("hits");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                return new EventDetail(eventId,title,posterUrl,startsAt,endsAt,fee,avgRating,hits,createdAt);
            }
        }, event_id).get(0);
    }

    public void updateRating(Long event_id, Integer rating){
        String sql = "select * from performance where performance_id = ?";
        Double currentRating = jdbcTemplate.query(sql, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getDouble("avg_rating");
            }
        }, event_id).get(0);

        Integer hits = jdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("hits");
            }
        }, event_id).get(0);

        currentRating = (currentRating * hits + rating) / hits;
        hits++;
        String updateSql =
                "UPDATE performance " +
                        "SET avg_rating = ?, hits = ? " +
                        "WHERE performance_id = ?";
        jdbcTemplate.update(updateSql, currentRating, hits, event_id);
    }

    public CreateReviewResponse createReview(Long event_id, Long user_id, Integer rating, String comment){
        String insertSql = " INSERT INTO review (performance_id, user_id, rating, comment) VALUES (?, ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, event_id);
            ps.setLong(2, user_id);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            return ps;
        }, keyHolder);

        Long reviewId = keyHolder.getKey().longValue();
        LocalDateTime now = LocalDateTime.now();

        String findUserNameSql = "select * from users where user_id = ?";
        String userName = jdbcTemplate.query(findUserNameSql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        }, user_id).get(0);

        return new CreateReviewResponse(new Review(reviewId, userName, rating, comment, now));
    }

    public List<ReviewListResponse> getReviews(Long eventId){
        String sql = "SELECT r.review_id, u.name AS user_name, r.rating, r.comment, r.created_at" +
                "        FROM review r JOIN users u ON r.user_id = u.user_id" +
                "        WHERE r.performance_id = ? " +
                "        ORDER BY r.created_at DESC";
        return jdbcTemplate.query(sql, new RowMapper<ReviewListResponse>() {
            @Override
            public ReviewListResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                String user_name = rs.getString("user_name");
                Integer score = rs.getInt("rating");
                String content = rs.getString("comment");

                return new ReviewListResponse(user_name, score, content);
            }
        }, eventId);
    }

}
