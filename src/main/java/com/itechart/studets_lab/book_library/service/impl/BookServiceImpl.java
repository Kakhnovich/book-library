package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    private static final BookServiceImpl INSTANCE = new BookServiceImpl();
    private final BookDao bookDao;

    private BookServiceImpl() {
        bookDao = new BookDao();
    }

    public static BookServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Book> findAll() {
        return checkOptionalList(bookDao.findAll());
    }

    @Override
    public List<Book> findByPage(int page) {
        return checkOptionalList(bookDao.findByPageNumber(page));
    }

    @Override
    public Book findByKey(int id) {
        return bookDao.findByKey(id).orElse(null);
    }

    @Override
    public Book create(Book book) {
        return bookDao.create(book).orElse(null);
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book).orElse(null);
    }

    @Override
    public int getCountOfPages() {
        return bookDao.getCountOfPages();
    }

    @Override
    public List<Book> findByCriteria(BookCriteria bookCriteria) {
        return findAll().stream()
                .filter(book -> bookCriteria.getTitle().equals("")
                        || book.getTitle().equals(bookCriteria.getTitle()))
                .filter(book -> bookCriteria.getAuthors().size() == 0
                        || book.getAuthors().containsAll(bookCriteria.getAuthors()))
                .filter(book -> bookCriteria.getGenres().size() == 0
                        || book.getGenres().containsAll(bookCriteria.getGenres()))
                .filter(book -> bookCriteria.getDescription().equals("")
                        || book.getDescription().equals(bookCriteria.getDescription()))
                .collect(Collectors.toList());
    }
}
