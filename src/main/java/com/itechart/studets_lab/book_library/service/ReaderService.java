package com.itechart.studets_lab.book_library.service;

import com.itechart.studets_lab.book_library.model.Reader;

import java.util.List;

public interface ReaderService extends CommonService<Reader, Reader> {
    List<String> findAllEmails();
}
