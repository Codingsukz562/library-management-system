package com.library.service;


import com.library.model.Book;
import com.library.patterns.SearchStrategy;
import java.util.List;

public class SearchService {
    private SearchStrategy strategy;
    
    public void setSearchStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<Book> performSearch(List<Book> books, SearchStrategy strategy, String query) {
        return strategy.search(books, query);
    }
    
    public List<Book> performSearch(List<Book> books, String query) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        return strategy.search(books, query);
    }
}
