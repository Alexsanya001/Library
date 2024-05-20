package spring.library.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("genre");
    }
}
