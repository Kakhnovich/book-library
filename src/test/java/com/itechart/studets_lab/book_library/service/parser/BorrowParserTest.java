package com.itechart.studets_lab.book_library.service.parser;

import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.model.ReaderFactory;
import com.itechart.studets_lab.book_library.service.impl.ReaderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BorrowParserTest {
    BorrowParser borrowParser;
    List<BorrowDto> borrowDtoList;
    String inputString;
    Reader reader1;
    Reader reader2;
    Reader reader3;
    @Mock
    ReaderServiceImpl readerService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        borrowParser = BorrowParser.getInstance();
        borrowParser.setReaderService(readerService);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        reader1 = ReaderFactory.getInstance().create(1, "email1", "firstName1", "lastName1", "male", 0);
        reader2 = ReaderFactory.getInstance().create(2, "email2", "firstName2", "lastName2", "male", 0);
        reader3 = ReaderFactory.getInstance().create(3, "email3", "firstName3", "lastName3", "female", 0);
        borrowDtoList = new ArrayList<>(Arrays.asList(
                new BorrowDto(1, 111_111, reader1, LocalDate.parse("2021-11-13", formatter), 3, LocalDate.now(), "comment1", "not returned"),
                new BorrowDto(2, 111_112, reader2, LocalDate.parse("2021-11-02", formatter), 1, LocalDate.parse("2021-11-13", formatter), "", "returned and damaged"),
                new BorrowDto(3, 111_113, reader3, LocalDate.parse("2021-10-10", formatter), 2, LocalDate.now(), "comment2", "not returned")));
        inputString = "1;111111;email1;firstName1 lastName1;2021-11-13;3;;comment1;not returned," +
                "2;111112;email2;firstName2 lastName2;2021-11-02;1;2021-11-13;;returned and damaged," +
                "3;111113;email3;firstName3 lastName3;2021-10-10;2;;comment2;not returned";
    }

    @Test
    public void testParseString_ShouldCorrectParseAllBorrows() {
        when(readerService.findReaderByEmail(reader1.getEmail())).thenReturn(reader1);
        when(readerService.findReaderByEmail(reader2.getEmail())).thenReturn(reader2);
        when(readerService.findReaderByEmail(reader3.getEmail())).thenReturn(null);
        when(readerService.create(any())).thenReturn(reader3);
        List<BorrowDto> actual = borrowParser.parseString(inputString);
        assertEquals(borrowDtoList.size(), actual.size());
        for (int i = 0; i < borrowDtoList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", borrowDtoList.get(i), actual.get(i));
        }
    }
}