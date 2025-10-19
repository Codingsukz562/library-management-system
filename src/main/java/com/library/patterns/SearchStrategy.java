package com.library.patterns;

import com.library.model.Book;
import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}