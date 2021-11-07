package com.itechart.studets_lab.book_library.service;

import com.itechart.studets_lab.book_library.model.Reader;

import java.util.HashMap;

public interface ReaderService extends CommonService<Reader, Reader> {
    HashMap<String, String> findEmailsWithNames();

    Reader findReaderByEmail(String email);
}
