package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowPeriodDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowStatusDao;
import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookDto;
import com.itechart.studets_lab.book_library.model.BookFactory;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.model.BorrowFactory;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.model.ReaderFactory;
import com.itechart.studets_lab.book_library.service.parser.BorrowParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BorrowServiceImplTest {
    List<BorrowDto> borrowDtoList;
    List<Borrow> borrowList;
    List<String> statuses = new ArrayList<>(Arrays.asList("returned", "returned and damaged", "lost", "not returned"));
    List<Integer> periods = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 12));
    BorrowServiceImpl borrowService;
    Book book;
    Reader reader1;
    Reader reader2;
    Reader reader3;
    String inputString;
    @Mock
    BookDao bookDao;
    @Mock
    BorrowPeriodDao borrowPeriodDao;
    @Mock
    BorrowStatusDao borrowStatusDao;
    @Mock
    BorrowDao borrowDao;
    @Mock
    ReaderDao readerDao;
    @Mock
    BorrowParser borrowParser;
    @Mock
    ReaderServiceImpl readerService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        borrowService = BorrowServiceImpl.getInstance();
        borrowService.setBookDao(bookDao);
        borrowService.setBorrowPeriodDao(borrowPeriodDao);
        borrowService.setBorrowStatusDao(borrowStatusDao);
        borrowService.setBorrowDao(borrowDao);
        borrowService.setReaderDao(readerDao);
        borrowService.setBorrowParser(borrowParser);
        borrowService.setReaderService(readerService);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        reader1 = ReaderFactory.getInstance().create(1, "email1", "firstName1", "lastName1", "male", 0);
        reader2 = ReaderFactory.getInstance().create(2, "email2", "firstName2", "lastName2", "male", 0);
        reader3 = ReaderFactory.getInstance().create(3, "email3", "firstName3", "lastName3", "female", 0);
        book = BookFactory.getInstance().create(new BookDto(1, 111_111, "first.png", "First Book", Arrays.asList("author1", "author2", "author3"), "publisher1", LocalDate.now(), Arrays.asList("genre2", "genre3"), 120, "First Library Book", 2));
        borrowDtoList = new ArrayList<>(Arrays.asList(
                new BorrowDto(1, 111_111, reader1, LocalDate.parse("2021-11-13", formatter), periods.get(2), LocalDate.now(), "comment1", statuses.get(3)),
                new BorrowDto(2, 111_112, reader2, LocalDate.parse("2021-11-02", formatter), periods.get(0), LocalDate.parse("2021-11-13", formatter), "", statuses.get(1)),
                new BorrowDto(3, 111_113, reader3, LocalDate.parse("2021-10-10", formatter), periods.get(1), LocalDate.now(), "comment2", statuses.get(3))));
        borrowList = new ArrayList<>();
        borrowList.add(BorrowFactory.getInstance().create(borrowDtoList.get(0), 3, 4));
        borrowList.add(BorrowFactory.getInstance().create(borrowDtoList.get(1), 1, 2));
        borrowList.add(BorrowFactory.getInstance().create(borrowDtoList.get(2), 2, 4));
        inputString = "1;111111;email1;firstName1 lastName1;2021-11-13;3;;comment1;not returned," +
                "2;111112;email2;firstName2 lastName2;2021-11-02;1;2021-11-13;;returned and damaged," +
                "3;111113;email3;firstName3 lastName3;2021-10-10;2;;comment2;not returned";
        when(borrowPeriodDao.findPeriod(1)).thenReturn(periods.get(0));
        when(borrowPeriodDao.findPeriod(2)).thenReturn(periods.get(1));
        when(borrowPeriodDao.findPeriod(3)).thenReturn(periods.get(2));
        when(borrowPeriodDao.findTimePeriodId(1)).thenReturn(1);
        when(borrowPeriodDao.findTimePeriodId(2)).thenReturn(2);
        when(borrowPeriodDao.findTimePeriodId(3)).thenReturn(3);
        when(borrowStatusDao.findStatus(2)).thenReturn(statuses.get(1));
        when(borrowStatusDao.findStatus(4)).thenReturn(statuses.get(3));
        when(borrowStatusDao.findStatusId(statuses.get(1))).thenReturn(2);
        when(borrowStatusDao.findStatusId(statuses.get(3))).thenReturn(4);
        when(readerDao.findByKey(1)).thenReturn(Optional.of(reader1));
        when(readerDao.findByKey(2)).thenReturn(Optional.of(reader2));
        when(readerDao.findByKey(3)).thenReturn(Optional.of(reader3));
    }

    @Test
    public void testFindAll_ShouldConvertToDtoAllBorrows() {
        when(borrowDao.findAll()).thenReturn(Optional.of(borrowList));
        List<BorrowDto> actual = borrowService.findAll();
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of borrows: " + borrowDtoList.size(), borrowDtoList.size(), actual.size());
        for (int i = 0; i < borrowDtoList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", borrowDtoList.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByPage_ShouldConvertToDtoAllBicycles() {
        when(borrowDao.findByPageNumber(1)).thenReturn(Optional.of(borrowList));
        List<BorrowDto> actual = borrowService.findByPage(1);
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of borrows: " + borrowDtoList.size(), borrowDtoList.size(), actual.size());
        for (int i = 0; i < borrowDtoList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", borrowDtoList.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByKey_ShouldReturnFirstBorrow_WhenKeyIsOne() {
        when(borrowDao.findByKey(1)).thenReturn(Optional.of(borrowList.get(0)));
        BorrowDto actual = borrowService.findByKey(1);
        assertNotNull("Borrow should be founded", actual);
        assertEquals(borrowDtoList.get(0), actual);
    }

    @Test
    public void testGetCountOfPages_ShouldReturnOne() {
        when(borrowDao.getCountOfPages()).thenReturn(1);
        int actual = borrowService.getCountOfPages();
        assertEquals(1, actual);
    }

    @Test
    public void testFindAllPeriods_shouldReturnPeriodsList() {
        when(borrowPeriodDao.findAllPeriods()).thenReturn(Optional.of(periods));
        List<Integer> actual = borrowService.findAllPeriods();
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of periods: " + periods.size(), periods.size(), actual.size());
        for (int i = 0; i < periods.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", periods.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindAllStatuses_shouldReturnStatusesList() {
        when(borrowStatusDao.findAllStatuses()).thenReturn(Optional.of(statuses));
        List<String> actual = borrowService.findAllStatuses();
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of statuses: " + statuses.size(), statuses.size(), actual.size());
        for (int i = 0; i < statuses.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", statuses.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindReaderBorrows_ShouldReturnSecondBorrow() {
        when(borrowDao.findAll()).thenReturn(Optional.of(borrowList));
        List<BorrowDto> actual = borrowService.findReaderBorrows(reader2.getEmail());
        assertEquals(1, actual.size());
        assertEquals(borrowDtoList.get(1), actual.get(0));
    }

    @Test
    public void testFindBorrowsOfBook_ShouldReturnThirdBorrow() {
        when(borrowDao.findAll()).thenReturn(Optional.of(borrowList));
        List<BorrowDto> actual = borrowService.findBorrowsOfBook(111_113);
        assertEquals(1, actual.size());
        assertEquals(borrowDtoList.get(2), actual.get(0));
    }

    @Test
    public void testCreate_ShouldReturnNull_WhenBookWasNotFounded() {
        when(bookDao.findByKey(anyInt())).thenReturn(Optional.empty());
        BorrowDto actual = borrowService.create(borrowDtoList.get(0));
        assertNull(actual);
        verify(borrowDao, times(0)).create(any(), anyInt());
    }

    @Test
    public void testCreate_ShouldReturnNull_WhenBorrowWasNotCreated() {
        when(bookDao.findByKey(111_111)).thenReturn(Optional.of(book));
        when(borrowDao.create(any(), anyInt())).thenReturn(Optional.empty());
        BorrowDto actual = borrowService.create(borrowDtoList.get(0));
        assertNull(actual);
        verify(borrowDao, times(1)).create(any(), anyInt());
    }

    @Test
    public void testCreate_ShouldReturnFirstBorrow() {
        when(bookDao.findByKey(111_111)).thenReturn(Optional.of(book));
        when(borrowDao.create(any(), anyInt())).thenReturn(Optional.of(borrowList.get(0)));
        BorrowDto actual = borrowService.create(borrowDtoList.get(0));
        assertEquals(borrowDtoList.get(0), actual);
    }

    @Test
    public void testUpdateBorrowList_ShouldReturnBorrowList() {
        when(borrowParser.parseString(anyString())).thenReturn(borrowDtoList);
        when(borrowDao.update(borrowList.get(0))).thenReturn(Optional.of(borrowList.get(0)));
        when(borrowDao.update(borrowList.get(1))).thenReturn(Optional.of(borrowList.get(1)));
        when(borrowDao.update(borrowList.get(2))).thenReturn(Optional.of(borrowList.get(2)));
        List<BorrowDto> actual = borrowService.updateBorrowList(inputString);
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of borrows: " + borrowDtoList.size(), borrowDtoList.size(), actual.size());
        for (int i = 0; i < borrowDtoList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", borrowDtoList.get(i), actual.get(i));
        }
    }

    @Test
    public void testUpdateBorrowList_ShouldReturnEmptyList_WhenDataIsEmpty() {
        List<BorrowDto> actual = borrowService.updateBorrowList("");
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testUpdateBorrowList_ShouldReturnEmptyList_WhenBorrowsWasNatCreated() {
        when(borrowParser.parseString(anyString())).thenReturn(borrowDtoList);
        when(borrowDao.update(any())).thenReturn(Optional.empty());
        List<BorrowDto> actual = borrowService.updateBorrowList(inputString);
        assertTrue(actual.isEmpty());
        verify(readerService, times(0)).update(any());
    }
}