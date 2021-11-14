package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookAuthorDao;
import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.dao.impl.BookGenresDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDao;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.model.BookDto;
import com.itechart.studets_lab.book_library.model.BookFactory;
import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceImplTest {
    List<BookDto> bookDtoList;
    List<Book> bookList;
    BookServiceImpl bookService;
    @Mock
    BookDao bookDao;
    @Mock
    BookAuthorDao bookAuthorDao;
    @Mock
    BookGenresDao bookGenresDao;
    @Mock
    BorrowDao borrowDao;

    @BeforeClass
    public static void initConnectionPool() {
        ConnectionPool.getInstance().init();
    }

    @AfterClass
    public static void destroyConnectionPool() {
        ConnectionPool.getInstance().destroy();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        bookService = BookServiceImpl.getInstance();
        bookService.setBookDao(bookDao);
        bookService.setBookAuthorDao(bookAuthorDao);
        bookService.setBookGenresDao(bookGenresDao);
        bookService.setBorrowDao(borrowDao);
        bookDtoList = new ArrayList<>(Arrays.asList(
                new BookDto(1, 111_111, "first.png", "First Book", Arrays.asList("author1", "author2", "author3"), "publisher1", LocalDate.now(), Arrays.asList("genre2", "genre3"), 120, "First Library Book", 2),
                new BookDto(2, 111_112, "second.png", "Second Book", Arrays.asList("author4", "author5"), "publisher2", LocalDate.now(), Arrays.asList("genre1", "genre3"), 10, "Second Library Book", 1),
                new BookDto(3, 111_113, "third.png", "Third Book", Arrays.asList("author2", "author5"), "publisher3", LocalDate.now(), Arrays.asList("genre1", "genre2", "genre4"), 235, "Third Library Book", 1)));
        bookList = new ArrayList<>();
        for (BookDto bookDto : bookDtoList) {
            bookList.add(BookFactory.getInstance().create(bookDto));
        }
        when(bookAuthorDao.findBookAuthors(bookDtoList.get(0).getId())).thenReturn(bookDtoList.get(0).getAuthors());
        when(bookAuthorDao.findBookAuthors(bookDtoList.get(1).getId())).thenReturn(bookDtoList.get(1).getAuthors());
        when(bookAuthorDao.findBookAuthors(bookDtoList.get(2).getId())).thenReturn(bookDtoList.get(2).getAuthors());
        when(bookGenresDao.findBookGenres(bookDtoList.get(0).getId())).thenReturn(bookDtoList.get(0).getGenres());
        when(bookGenresDao.findBookGenres(bookDtoList.get(1).getId())).thenReturn(bookDtoList.get(1).getGenres());
        when(bookGenresDao.findBookGenres(bookDtoList.get(2).getId())).thenReturn(bookDtoList.get(2).getGenres());
    }

    @Test
    public void testFindAll_ShouldConvertToDtoAllBicycles() {
        when(bookDao.findAll()).thenReturn(Optional.of(bookList));
        List<BookDto> actual = bookService.findAll();
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of books: " + bookDtoList.size(), bookDtoList.size(), actual.size());
        for (int i = 0; i < bookDtoList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", bookDtoList.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByPage_ShouldConvertToDtoAllBicycles() {
        when(bookDao.findByPageNumber(1)).thenReturn(Optional.of(bookList));
        List<BookDto> actual = bookService.findByPage(1);
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of books: " + bookDtoList.size(), bookDtoList.size(), actual.size());
        for (int i = 0; i < bookDtoList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", bookDtoList.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByKey_ShouldReturnFirstBook_WhenKeyIsOne() {
        when(bookDao.findByKey(1)).thenReturn(Optional.of(bookList.get(0)));
        BookDto actual = bookService.findByKey(1);
        assertNotNull("Book should be founded", actual);
        assertEquals(bookDtoList.get(0), actual);
    }

    @Test
    public void testGetCountOfPages_ShouldReturnOne() {
        when(bookDao.getCountOfPages()).thenReturn(1);
        int actual = bookService.getCountOfPages();
        assertEquals(1, actual);
    }

    @Test
    public void testCreate_ShouldReturnNewBook() {
        when(bookDao.create(any(), any())).thenReturn(Optional.of(bookList.get(1)));
        when(bookDao.findByTitle(anyString())).thenReturn(Optional.of(bookList.get(1)));
        when(bookAuthorDao.addBookAuthors(any(), anyInt(), anyList())).thenReturn(true);
        when(bookGenresDao.addBookGenres(any(), anyInt(), anyList())).thenReturn(true);
        BookDto actual = bookService.create(bookDtoList.get(1));
        assertEquals(bookDtoList.get(1), actual);
    }

    @Test
    public void testCreate_ShouldReturnNull_WhenBookWasNotCreated() {
        when(bookDao.create(any(), any())).thenReturn(Optional.empty());
        BookDto actual = bookService.create(bookDtoList.get(1));
        verify(bookDao, times(0)).findByTitle(anyString());
        assertNull(actual);
    }

    @Test
    public void testCreate_ShouldReturnNull_WhenBookWasNotFoundAfterCreation() {
        when(bookDao.create(any(), any())).thenReturn(Optional.of(bookList.get(1)));
        when(bookDao.findByTitle(anyString())).thenReturn(Optional.empty());
        BookDto actual = bookService.create(bookDtoList.get(1));
        verify(bookDao, times(1)).findByTitle(anyString());
        verify(bookAuthorDao, times(0)).addBookAuthors(any(), anyInt(), anyList());
        assertNull(actual);
    }

    @Test
    public void testCreate_ShouldReturnNull_WhenBookGenresWasNotAdded() {
        when(bookDao.create(any(), any())).thenReturn(Optional.of(bookList.get(1)));
        when(bookDao.findByTitle(anyString())).thenReturn(Optional.of(bookList.get(1)));
        when(bookGenresDao.addBookGenres(any(), anyInt(), anyList())).thenReturn(false);
        BookDto actual = bookService.create(bookDtoList.get(1));
        verify(bookDao, times(1)).findByTitle(anyString());
        verify(bookGenresDao, times(1)).addBookGenres(any(), anyInt(), anyList());
        verify(bookAuthorDao, times(0)).addBookAuthors(any(), anyInt(), anyList());
        assertNull(actual);
    }

    @Test
    public void testFindByCriteria_ShouldReturnFirstBook() {
        when(bookDao.findAll()).thenReturn(Optional.of(bookList));
        BookCriteria bookCriteria = BookCriteria.builder()
                .title(bookDtoList.get(0).getTitle())
                .authors(bookDtoList.get(0).getAuthors())
                .genres(bookDtoList.get(0).getGenres())
                .description(bookDtoList.get(0).getDescription())
                .build();
        List<BookDto> actual = bookService.findByCriteria(bookCriteria);
        assertEquals(1, actual.size());
        assertEquals(Collections.singletonList(bookDtoList.get(0)), actual);
    }

    @Test
    public void testFindByCriteria_ShouldReturnAllBooks() {
        when(bookDao.findAll()).thenReturn(Optional.of(bookList));
        BookCriteria bookCriteria = BookCriteria.builder()
                .build();
        List<BookDto> actual = bookService.findByCriteria(bookCriteria);
        assertEquals(bookDtoList.size(), actual.size());
        assertEquals(bookDtoList, actual);
    }

    @Test
    public void testFindByCriteria_ShouldReturnEmptyList() {
        when(bookDao.findAll()).thenReturn(Optional.of(bookList));
        BookCriteria bookCriteria = BookCriteria.builder()
                .title("Title")
                .build();
        List<BookDto> actual = bookService.findByCriteria(bookCriteria);
        assertEquals(0, actual.size());
    }

    @Test
    public void testDeleteBookWithId_ShouldNotWorkTillTheEnd() {
        when(bookGenresDao.deleteBookGenres(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.deleteBookAuthors(any(), anyInt())).thenReturn(true);
        when(borrowDao.deleteByBookId(any(), anyInt())).thenReturn(false);
        bookService.deleteBookWithId(1);
        verify(bookDao, times(0)).delete(any(), anyInt());
    }

    @Test
    public void testDeleteBookWithId_ShouldWorkTillTheEnd() {
        when(bookGenresDao.deleteBookGenres(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.deleteBookAuthors(any(), anyInt())).thenReturn(true);
        when(borrowDao.deleteByBookId(any(), anyInt())).thenReturn(true);
        when(bookDao.delete(any(), anyInt())).thenReturn(true);
        bookService.deleteBookWithId(1);
        verify(bookDao, times(1)).delete(any(), anyInt());
    }

    @Test
    public void testUpdate_ShouldReturnThirdBook() {
        when(bookGenresDao.deleteBookGenres(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.deleteBookAuthors(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.addBookAuthors(any(), anyInt(), anyList())).thenReturn(true);
        when(bookGenresDao.addBookGenres(any(), anyInt(), anyList())).thenReturn(true);
        when(bookDao.update(any(), any())).thenReturn(Optional.of(bookList.get(2)));
        BookDto actual = bookService.update(bookDtoList.get(2));
        assertEquals(bookDtoList.get(2), actual);
    }

    @Test
    public void testUpdate_ShouldReturnNull_WhenGenresDataWasNotUdated() {
        when(bookGenresDao.deleteBookGenres(any(), anyInt())).thenReturn(false);
        when(bookAuthorDao.deleteBookAuthors(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.addBookAuthors(any(), anyInt(), anyList())).thenReturn(true);
        when(bookGenresDao.addBookGenres(any(), anyInt(), anyList())).thenReturn(true);
        when(bookDao.update(any(), any())).thenReturn(Optional.of(bookList.get(2)));
        BookDto actual = bookService.update(bookDtoList.get(2));
        assertNull(actual);
        verify(bookDao, times(0)).update(any(), any());
    }

    @Test
    public void testUpdate_ShouldReturnNull_WhenBookDataWasNotUpdated() {
        when(bookGenresDao.deleteBookGenres(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.deleteBookAuthors(any(), anyInt())).thenReturn(true);
        when(bookAuthorDao.addBookAuthors(any(), anyInt(), anyList())).thenReturn(true);
        when(bookGenresDao.addBookGenres(any(), anyInt(), anyList())).thenReturn(true);
        when(bookDao.update(any(), any())).thenReturn(Optional.empty());
        BookDto actual = bookService.update(bookDtoList.get(2));
        assertNull(actual);
        verify(bookDao, times(1)).update(any(), any());
    }
}