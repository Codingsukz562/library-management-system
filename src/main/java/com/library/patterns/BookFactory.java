package com.library.patterns;

import com.library.model.Book;

public class BookFactory {
    
    public enum BookType {
        STANDARD,
        REFERENCE, 
        BESTSELLER 
    }
    
    public static Book createBook(BookType type, String isbn, String title, 
                                 String author, int publicationYear) {
        switch (type) {
            case REFERENCE:
                Book referenceBook = new Book(isbn, title, author, publicationYear);
                referenceBook.setAvailable(false);
                return referenceBook;
                
            case BESTSELLER:
                return new Book(isbn, title + " [Bestseller]", author, publicationYear);
                
            case STANDARD:
            default:
                return new Book(isbn, title, author, publicationYear);
        }
    }
    
    public static Book createBookFromData(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 4) {
            return new Book(parts[0].trim(), parts[1].trim(), parts[2].trim(), 
                           Integer.parseInt(parts[3].trim()));
        }
        throw new IllegalArgumentException("Invalid book data format");
    }
}
