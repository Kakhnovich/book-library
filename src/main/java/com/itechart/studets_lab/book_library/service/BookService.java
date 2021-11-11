package com.itechart.studets_lab.book_library.service;

import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.model.BookDto;

import java.util.List;

public interface BookService extends CommonService<BookDto, Book> {
    List<BookDto> findByCriteria(BookCriteria bookCriteria);

    void deleteBookWithId(int id);
}
