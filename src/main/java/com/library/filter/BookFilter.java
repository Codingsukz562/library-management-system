package com.library.filter;

import com.library.model.Book;
import java.util.function.Predicate;

public class BookFilter {
    
    public static Predicate<Book> byTitle(String title) {
        return book -> book.getTitle().toLowerCase().contains(title.toLowerCase());
    }
    
    public static Predicate<Book> byAuthor(String author) {
        return book -> book.getAuthor().toLowerCase().contains(author.toLowerCase());
    }
    
    public static Predicate<Book> byIsbn(String isbn) {
        return book -> book.getIsbn().equals(isbn);
    }
    
    public static Predicate<Book> byYearRange(int startYear, int endYear) {
        return book -> book.getPublicationYear() >= startYear && book.getPublicationYear() <= endYear;
    }
    
    public static Predicate<Book> availableOnly() {
        return Book::isAvailable;
    }
    
    public static Predicate<Book> borrowedOnly() {
        return book -> !book.isAvailable();
    }
    
    @SafeVarargs
    public static Predicate<Book> combine(Predicate<Book>... filters) {
        Predicate<Book> combined = book -> true;
        for (Predicate<Book> filter : filters) {
            combined = combined.and(filter);
        }
        return combined;
    }
}