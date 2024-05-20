package spring.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.library.dao.AuthorDAO;
import spring.library.dao.BookDAO;
import spring.library.dao.GenreDAO;
import spring.library.models.Book;

@Controller
@RequestMapping("/")
public class MainController {
    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;

    @Autowired
    public MainController(BookDAO bookDAO, AuthorDAO authorDAO, GenreDAO genreDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
    }

    @GetMapping()
    public String index(@ModelAttribute("a_book") Book book, Model model) {
        model.addAttribute("books", bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        return "books/index";
    }
}
