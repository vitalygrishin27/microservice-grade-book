package com.gradeBook.service;

import java.util.List;


public interface CRUDService<T> {
    List<T> findAll(Boolean needToSort, String search);

    T create(T t);

    T update(T t);

    void delete(Long id);
}
