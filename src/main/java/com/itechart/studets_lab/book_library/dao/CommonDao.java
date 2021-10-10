package com.itechart.studets_lab.book_library.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T, K> {
    Optional<List<T>> findAll();

    Optional<T> findByKey(K key);

    int getCountOfPages();

    Optional<List<T>> findByPageNumber(int page);

    Optional<T> update(T entity);

    Optional<T> create(T entity);


}