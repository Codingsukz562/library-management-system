package com.library.patterns;

import com.library.model.Book;

public class PatronNotification implements BookObserver {
    private final String patronId;
    
    public PatronNotification(String patronId) {
        this.patronId = patronId;
    }
    
    @Override
    public void update(Book book, String message) {
        System.out.println(" Notification for Patron " + patronId + 
                          ": Book '" + book.getTitle() + "' - " + message);

    }
}