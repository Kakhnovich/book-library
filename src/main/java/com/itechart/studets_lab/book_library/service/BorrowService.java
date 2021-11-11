package com.itechart.studets_lab.book_library.service;

import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.model.BorrowDto;

import java.util.List;

public interface BorrowService extends CommonService<BorrowDto, Borrow> {
    List<BorrowDto> findBorrowsOfBook(int id);

    List<BorrowDto> findReaderBorrows(String email);

    List<Integer> findAllPeriods();

    List<String> findAllStatuses();

    List<BorrowDto> updateBorrowList(String borrowsData);
}
