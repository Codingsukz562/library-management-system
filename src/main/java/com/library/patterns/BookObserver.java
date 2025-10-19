package com.library.patterns;

import com.library.model.Book;

public interface BookObserver {
    void update(Book book, String message);
}
