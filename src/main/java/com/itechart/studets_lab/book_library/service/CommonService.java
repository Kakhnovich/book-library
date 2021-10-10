package com.itechart.studets_lab.book_library.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T, K> {
    Optional<List<T>> findAll();

    Optional<List<T>> findByPage(int page);

    Optional<T> findByKey(K key);

    Optional<T> create(T entity);

    Optional<T> update(T entity);

    int getCountOfPages();
}
