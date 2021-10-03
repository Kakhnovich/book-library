package by.itechart.studets_lab.book_library.command.search;

import by.itechart.studets_lab.book_library.command.Command;
import by.itechart.studets_lab.book_library.command.RequestContext;
import by.itechart.studets_lab.book_library.command.ResponseContext;
import by.itechart.studets_lab.book_library.command.page.ShowSearchPage;
import by.itechart.studets_lab.book_library.model.Book;
import by.itechart.studets_lab.book_library.model.BookCriteria;
import by.itechart.studets_lab.book_library.service.impl.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum FindBook implements Command {
    INSTANCE;

    private static final BookService bookService = BookService.getInstance();
    private static final String BOOKS_PARAMETER_NAME = "books";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";

    @Override
    public ResponseContext execute(RequestContext request) {
        final String title = String.valueOf(request.getParameter(TITLE_PARAMETER_NAME));
        final String authors = String.valueOf(request.getParameter(AUTHORS_PARAMETER_NAME));
        final String genres = String.valueOf(request.getParameter(GENRES_PARAMETER_NAME));
        final String description = String.valueOf(request.getParameter(DESCRIPTION_PARAMETER_NAME));
        final BookCriteria bookCriteria = createCriteria(title, authors, genres, description);
        Optional<List<Book>> books = bookService.findByCriteria(bookCriteria);
        books.ifPresent(bookList -> request.setAttribute(BOOKS_PARAMETER_NAME, bookList));
        return ShowSearchPage.INSTANCE.execute(request);
    }

    private BookCriteria createCriteria(String title, String authors, String genres, String description){
        BookCriteria.CriteriaBuilder bookCriteria = BookCriteria.builder();
        if(!title.equals("")){
            bookCriteria.title(title);
        }
        if(!authors.equals("")){
            bookCriteria.authors(parseStringIntoStringList(authors));
        }
        if(!genres.equals("")){
            bookCriteria.genres(parseStringIntoStringList(genres));
        }
        if(!description.equals("")){
            bookCriteria.description(description);
        }
        return bookCriteria.build();
    }

    private List<String> parseStringIntoStringList(String set){
        List<String> rezList = new ArrayList<>();
        for(String word: set.split(";")){
            rezList.add(word.trim());
        }
        return rezList;
    }
}
