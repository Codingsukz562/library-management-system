package com.library.patterns;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;

public class ObservableBook {
    private final Book book;
    private final List<BookObserver> observers;
    
    public ObservableBook(Book book) {
        this.book = book;
        this.observers = new ArrayList<>();
    }
    
    public void addObserver(BookObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(BookObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(String message) {
        for (BookObserver observer : observers) {
            observer.update(book, message);
        }
    }
    
    public Book getBook() {
        return book;
    }
}