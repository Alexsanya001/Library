package spring.library.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import spring.library.models.Book;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM my_library",
            new BookMapper());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM my_library WHERE id=?",
                new Object[]{id}, new BookMapper()).stream().findAny().orElse(null);
    }

   public int save(Book book) {
       GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
       String sql = "INSERT INTO my_library (title, author, genre) VALUES (?,?,?)";
       jdbcTemplate.update(con -> {
           PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           ps.setString(1, book.getTitle());
           ps.setString(2, book.getAuthor());
           ps.setString(3, book.getGenre());
           return ps;
       }, generatedKeyHolder);
       return (int) Objects.requireNonNull(generatedKeyHolder.getKeys()).get("id");
   }

    public void update( Book updatedBook, int id) {
        jdbcTemplate.update("UPDATE my_library SET title=?, author=?, genre=? WHERE id=?",
                updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getGenre(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM my_library WHERE id=?", id);
    }

    public List<Book> searchResults(String keyword) {
            return jdbcTemplate.query("SELECT * FROM my_library WHERE " +
                            "LOWER(title) LIKE LOWER(?) OR LOWER(author) LIKE LOWER(?) OR LOWER(genre) LIKE LOWER(?)",
                   new Object[]{"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%"}, new BookMapper());
    }

}
