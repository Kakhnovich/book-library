package com.itechart.studets_lab.book_library.service;

import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;

import java.util.List;

public interface BookService extends CommonService<Book, Book> {
    List<Book> findByCriteria(BookCriteria bookCriteria);

    void deleteBookWithId(int id);
}
