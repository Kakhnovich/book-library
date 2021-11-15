package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookGalleryDao {
    private static final Logger LOGGER = LogManager.getLogger(BookGenresDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_BOOK_PHOTOS_SQL = "select photo from book_gallery where book_id = ";
    private static final String ADD_BOOK_PHOTO_SQL = "insert into book_gallery(book_id, photo) value ";

    BookGalleryDao() {
    }

    public List<String> getBookGallery(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BOOK_PHOTOS_SQL + id)) {
            List<String> photos = new ArrayList<>();
            while(resultSet.next()){
                photos.add(resultSet.getString(1));
            }
            return photos;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find " + id + " Book galery: " + e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public boolean addBookPhoto(String photo, int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            statement.executeUpdate(ADD_BOOK_PHOTO_SQL + "(" + id + ",'" + photo + "')");
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to add " + id + " Book photo: " + e.getLocalizedMessage());
        }
        return false;
    }
}
