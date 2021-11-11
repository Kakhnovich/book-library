package com.itechart.studets_lab.book_library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CommonService<T, K> {
    List<T> findAll();

    List<T> findByPage(int page);

    T findByKey(int key);

    T create(T entity);

    T update(T entity);

    int getCountOfPages();

    default List<T> checkOptionalList(Optional<List<T>> entities) {
        return entities.orElseGet(ArrayList::new);
    }
}
