package spring.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.library.models.Book;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AuthorDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<String> allAuthors() {
        return new HashSet<>(jdbcTemplate.query("SELECT author FROM my_library",
                new AuthorMapper()));
    }

    public List<Book> booksOfAuthor(String author){
       return jdbcTemplate.query("SELECT * FROM my_library WHERE author =?",
               new Object[]{author},
               new BookMapper());
    }
}
