package spring.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.library.dao.AuthorDAO;
import spring.library.dao.BookDAO;
import spring.library.dao.GenreDAO;
import spring.library.models.Book;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;

    @Autowired
    public BookController(BookDAO bookDAO, AuthorDAO authorDAO, GenreDAO genreDAO) {
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


    @GetMapping("/allBooks")
    public String allBooks(Model model, @ModelAttribute("a_book") Book book) {
        model.addAllAttributes(bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        model.addAttribute("books", bookDAO.index());
        return "books/allBooks";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("a_book") Book book, Model model) {
        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("books", bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        return "books/show";
    }

    @GetMapping("/anAuthor/{author}")
    public String showAuthor(@ModelAttribute("a_book") Book book, @PathVariable("author") String author, Model model) {
        model.addAttribute("authorBooks", authorDAO.booksOfAuthor(author));
        model.addAttribute("books", bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        return "books/anAuthor/author";
    }

    @GetMapping("aGenre/{genre}")
    public String showGenre(@ModelAttribute("a_book") Book book, @PathVariable("genre") String genre, Model model) {
        model.addAttribute("genreBooks", genreDAO.booksOfGenre(genre));
        model.addAttribute("books", bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        return "books/aGenre/genre";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("previousPage", referer);
        return "books/edit";
    }

    @PostMapping()
    public String create(@ModelAttribute("a_book") Book book, Model model) {
        model.addAttribute("id",  bookDAO.save(book));
        return "redirect:/books/{id}";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id, @RequestParam(value = "previousPage", required = false) String previousPage) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookDAO.update(book, id);
        if (previousPage != null && !previousPage.isEmpty())
            return "redirect:" + previousPage;
        else
            return "redirect:/books/{id}";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookDAO.delete(id);
        if (referer.endsWith(String.valueOf(id)))
            return "redirect:/books/allBooks";

        return "redirect:"+ referer;
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("book") Book book, @ModelAttribute("a_book") Book a_book,
                         @RequestParam("query") String query, Model model ) {
        model.addAttribute("books", bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        model.addAttribute("query", query);
        model.addAttribute("searchResults", bookDAO.searchResults(query));
        return "books/search";
    }

    @GetMapping("/authors")
    public String authors(Model model, @ModelAttribute("a_book") Book a_book) {
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute("genres", genreDAO.allGenres());
        model.addAttribute("books", bookDAO.index());
        model.addAttribute(authorDAO);
        return "books/authors";
    }

    @GetMapping("/genres")
    public String genres(Model model, @ModelAttribute("a_book") Book a_book) {
        model.addAttribute("genres", genreDAO.allGenres());
        model.addAttribute("books", bookDAO.index());
        model.addAttribute("authors", authorDAO.allAuthors());
        model.addAttribute(genreDAO);
        return "books/genres";
    }
}
