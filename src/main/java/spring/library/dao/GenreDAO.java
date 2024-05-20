package spring.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.library.models.Book;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDAO {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public GenreDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<String> allGenres(){
        return new HashSet<>(jdbcTemplate.query("SELECT genre FROM my_Library",
                new GenreMapper()));
    }

    public List<Book> booksOfGenre(String genre){
        return jdbcTemplate.query("SELECT * FROM my_library WHERE genre=?",
                new Object[]{genre}, new BookMapper());
    }

}
