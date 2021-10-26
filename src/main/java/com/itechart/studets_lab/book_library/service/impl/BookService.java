package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.service.CommonService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/* CommonService это хорошо, но сами классы должны реализовывать интерфес, если у тебя в них будут какие то паблик методы, выходящие за уровень CommonService.
* А такие скорей всего будут, т.к. сервисы точно не могут быть унифицированы одним интерфейсом в полноценном приложении. */
public class BookService implements CommonService<Book> {
    private static final BookService INSTANCE = new BookService();
    private final BookDao bookDao;

    private BookService() {
        bookDao = new BookDao();
    }

    public static BookService getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<List<Book>> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Optional<List<Book>> findByPage(int page) {
        return bookDao.findByPageNumber(page);
    }

    @Override
    public Optional<Book> findByKey(int id) {
        return bookDao.findByKey(id);
    }

    @Override
    public Optional<Book> create(Book book) {
        return bookDao.create(book);
    }

    @Override
    public Optional<Book> update(Book book) {
        return bookDao.update(book);
    }

    @Override
    public int getCountOfPages() {
        return bookDao.getCountOfPages();
    }

    // Вот как раз пример паблик метода, который не представлен в интерфейсе
    public Optional<List<Book>> findByCriteria(BookCriteria bookCriteria) {
        return findAll().map(bookList -> bookList.stream()
                .filter(book -> bookCriteria.getTitle().equals("")
                        || book.getTitle().equals(bookCriteria.getTitle()))
                .filter(book -> bookCriteria.getAuthors().size() == 0
                        || book.getAuthors().containsAll(bookCriteria.getAuthors()))
                .filter(book -> bookCriteria.getGenres().size() == 0
                        || book.getGenres().containsAll(bookCriteria.getGenres()))
                .filter(book -> bookCriteria.getDescription().equals("")
                        || book.getDescription().equals(bookCriteria.getDescription()))
                .collect(Collectors.toList()));
    }
}
