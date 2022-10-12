package zerobase.weather.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    // save
    public void save(Memo memo) {
        String sql = "insert into memo values (?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
    }

    /**
     * RowMapper : db에서 값을 가져와서 객체에 맞게 매핑해주는것
     */
    private RowMapper<Memo> memoRowMapper() {
        return (rs, rowNum) -> new Memo(
                rs.getLong("id"),
                rs.getString("text")
        );
    }

    // findAll
    public List<Memo> findAll() {
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper()); // 쿼리 조회해서 객체 형태로 가져옴
    }

    // findOne
    public Optional<Memo> findById(int id) {
        String sql = "select * from memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }
}
