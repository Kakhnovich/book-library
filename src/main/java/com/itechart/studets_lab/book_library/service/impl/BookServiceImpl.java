package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookAuthorDao;
import com.itechart.studets_lab.book_library.dao.impl.BookAuthorDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.dao.impl.BookDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BookGenresDao;
import com.itechart.studets_lab.book_library.dao.impl.BookGenresDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDaoFactory;
import com.itechart.studets_lab.book_library.error.TransactionException;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.model.BookDto;
import com.itechart.studets_lab.book_library.model.BookFactory;
import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import com.itechart.studets_lab.book_library.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    private static final BookServiceImpl INSTANCE = new BookServiceImpl();
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(BookServiceImpl.class);
    private BookDao bookDao;
    private BookAuthorDao bookAuthorDao;
    private BookGenresDao bookGenresDao;
    private BorrowDao borrowDao;

    private BookServiceImpl() {
        BookDaoFactory bookDaoFactory = BookDaoFactory.getInstance();
        bookDao = bookDaoFactory.getDao();
        BookAuthorDaoFactory bookAuthorDaoFactory = BookAuthorDaoFactory.getInstance();
        bookAuthorDao = bookAuthorDaoFactory.getDao();
        BookGenresDaoFactory bookGenresDaoFactory = BookGenresDaoFactory.getInstance();
        bookGenresDao = bookGenresDaoFactory.getDao();
        BorrowDaoFactory borrowDaoFactory = BorrowDaoFactory.getInstance();
        borrowDao = borrowDaoFactory.getDao();
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void setBookAuthorDao(BookAuthorDao bookAuthorDao) {
        this.bookAuthorDao = bookAuthorDao;
    }

    public void setBookGenresDao(BookGenresDao bookGenresDao) {
        this.bookGenresDao = bookGenresDao;
    }

    public void setBorrowDao(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    public static BookServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<BookDto> findAll() {
        return checkOptionalList(
                bookDao.findAll()
                        .map(books -> books.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookDto> findByPage(int page) {
        return checkOptionalList(
                bookDao.findByPageNumber(page)
                        .map(books -> books.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public BookDto findByKey(int id) {
        return bookDao.findByKey(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public BookDto create(BookDto bookDto) {
        BookDto createdBook = null;
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try {
                Book newBook = BookFactory.getInstance().create(bookDto);
                Optional<Book> book = bookDao.create(conn, newBook);
                if (!book.isPresent()) {
                    throw new TransactionException("Can't create new book");
                }
                conn.commit();
                createdBook = bookDao.findByTitle(bookDto.getTitle()).map(this::convertToDto).orElse(null);
                if (createdBook == null) {
                    throw new TransactionException("Can't find new book");
                }
                if (!addBookGenresAndAuthors(statement, createdBook.getId(), bookDto)) {
                    createdBook = null;
                    throw new TransactionException("Can't add book genres and authors");
                }
                conn.commit();
            } catch (SQLException | TransactionException e) {
                LOGGER.error("Exception while trying to update " + bookDto.getId() + " book: " + e.getLocalizedMessage());
                conn.rollback(savepoint);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
        }
        return createdBook;
    }

    @Override
    public BookDto update(BookDto bookDto) {
        if (bookDto.getId() == 0) {
            return create(bookDto);
        }
        BookDto updatedBook = null;
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try {
                if (!deleteBookGenresAndAuthors(statement, bookDto.getId()) ||
                        !addBookGenresAndAuthors(statement, bookDto.getId(), bookDto)) {
                    throw new TransactionException("Can't update book genres and authors");
                }
                Book newBook = BookFactory.getInstance().create(bookDto);
                updatedBook = bookDao.update(conn, newBook).map(this::convertToDto).orElse(null);
                if (updatedBook == null) {
                    throw new TransactionException("Can't update book data");
                }
                conn.commit();
            } catch (SQLException | TransactionException e) {
                LOGGER.error("Exception while trying to update " + bookDto.getId() + " book: " + e.getLocalizedMessage());
                conn.rollback(savepoint);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
        }
        return updatedBook;
    }

    private boolean deleteBookGenresAndAuthors(Statement statement, int id) {
        return bookGenresDao.deleteBookGenres(statement, id) &&
                bookAuthorDao.deleteBookAuthors(statement, id);
    }

    private boolean addBookGenresAndAuthors(Statement statement, int bookId, BookDto bookDto) {
        return bookGenresDao.addBookGenres(statement, bookId, bookDto.getGenres()) &&
                bookAuthorDao.addBookAuthors(statement, bookId, bookDto.getAuthors());
    }

    @Override
    public int getCountOfPages() {
        return bookDao.getCountOfPages();
    }

    @Override
    public List<BookDto> findByCriteria(BookCriteria bookCriteria) {
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

    @Override
    public void deleteBookWithId(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try {
                if (!deleteBookGenresAndAuthors(statement, id) ||
                        !borrowDao.deleteByBookId(statement, id) ||
                        !bookDao.delete(statement, id)) {
                    throw new TransactionException("Can't delete book");
                }
                conn.commit();
            } catch (SQLException | TransactionException e) {
                LOGGER.error("Exception while trying to delete " + id + " book: " + e.getLocalizedMessage());
                conn.rollback(savepoint);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
        }
    }

    private BookDto convertToDto(Book book) {
        List<String> authors = bookAuthorDao.findBookAuthors(book.getId());
        List<String> genres = bookGenresDao.findBookGenres(book.getId());
        return new BookDto(book.getId(), book.getIsbn(), book.getCoverLink(), book.getTitle(), authors, book.getPublisher(),
                book.getPublishDate(), genres, book.getPageCount(), book.getDescription(), book.getTotalAmount());
    }
}
