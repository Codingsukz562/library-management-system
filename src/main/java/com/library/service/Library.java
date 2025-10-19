package com.library.service;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.model.BorrowRecord;
import com.library.patterns.*;
import com.library.filter.BookFilter;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Library {
    private final Map<String, Book> books;
    private final Map<String, Patron> patrons;
    private final Map<String, BorrowRecord> activeBorrows;
    private final Map<String, ObservableBook> observableBooks;
    private final SearchService searchService;
    private int recordIdCounter;
    
    public Library() {
        this.books = new HashMap<>();
        this.patrons = new HashMap<>();
        this.activeBorrows = new HashMap<>();
        this.observableBooks = new HashMap<>();
        this.searchService = new SearchService();
        this.recordIdCounter = 1;
    }
    
    public boolean addBook(BookFactory.BookType type, String isbn, String title, 
                          String author, int publicationYear) {
        if (books.containsKey(isbn)) {
            return false;
        }
        Book book = BookFactory.createBook(type, isbn, title, author, publicationYear);
        books.put(isbn, book);
        observableBooks.put(isbn, new ObservableBook(book));
        return true;
    }
    
    public boolean addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            return false;
        }
        books.put(book.getIsbn(), book);
        observableBooks.put(book.getIsbn(), new ObservableBook(book));
        return true;
    }
    
    public List<Book> searchWithStrategy(SearchStrategy strategy, String query) {
        return searchService.performSearch(new ArrayList<>(books.values()), strategy, query);
    }
    
    public List<Book> searchByTitle(String title) {
        return searchWithStrategy(new TitleSearchStrategy(), title);
    }
    
    public List<Book> searchByAuthor(String author) {
        return searchWithStrategy(new AuthorSearchStrategy(), author);
    }
    
    public Optional<Book> searchByIsbn(String isbn) {
        List<Book> results = searchWithStrategy(new ISBNSearchStrategy(), isbn);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public void subscribeToBook(String patronId, String bookIsbn) {
        ObservableBook observable = observableBooks.get(bookIsbn);
        if (observable != null) {
            observable.addObserver(new PatronNotification(patronId));
            System.out.println("Patron " + patronId + " subscribed to book " + bookIsbn);
        }
    }
    
    public boolean checkoutBook(String patronId, String bookIsbn) {
        Patron patron = patrons.get(patronId);
        Book book = books.get(bookIsbn);
        
        if (patron == null || book == null || !book.isAvailable()) {
            return false;
        }
        
        if (patron.getCurrentlyBorrowedCount() >= 5) {
            return false;
        }
        
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusWeeks(3);
        
        String recordId = "REC-" + recordIdCounter++;
        BorrowRecord record = new BorrowRecord(recordId, patronId, bookIsbn, borrowDate, dueDate);
        
        activeBorrows.put(bookIsbn, record);
        patron.addBorrowRecord(record);
        book.setAvailable(false);
        
        return true;
    }

    public boolean returnBook(String bookIsbn) {
        BorrowRecord record = activeBorrows.get(bookIsbn);
        Book book = books.get(bookIsbn);
        
        if (record == null || book == null) {
            return false;
        }
        
        record.setReturnDate(LocalDate.now());
        activeBorrows.remove(bookIsbn);
        book.setAvailable(true);

        ObservableBook observable = observableBooks.get(bookIsbn);
        if (observable != null) {
            observable.notifyObservers("is now available for checkout");
        }
        
        return true;
    }
    
    public boolean addPatron(Patron patron) {
        if (patrons.containsKey(patron.getPatronId())) {
            return false;
        }
        patrons.put(patron.getPatronId(), patron);
        return true;
    }
    
    public Optional<Patron> updatePatron(String patronId, String name, String email, String phone) {
        Patron patron = patrons.get(patronId);
        if (patron != null) {
            if (name != null) patron.setName(name);
            if (email != null) patron.setEmail(email);
            if (phone != null) patron.setPhone(phone);
            return Optional.of(patron);
        }
        return Optional.empty();
    }
    
    public boolean removeBook(String isbn) {
        if (books.containsKey(isbn) && !activeBorrows.containsKey(isbn)) {
            books.remove(isbn);
            observableBooks.remove(isbn);
            return true;
        }
        return false;
    }
    
    public Optional<Book> updateBook(String isbn, String title, String author, Integer publicationYear) {
        Book book = books.get(isbn);
        if (book != null) {
            if (title != null) book.setTitle(title);
            if (author != null) book.setAuthor(author);
            if (publicationYear != null) book.setPublicationYear(publicationYear);
            return Optional.of(book);
        }
        return Optional.empty();
    }
    
    public List<Book> searchBooks(Predicate<Book> filter) {
        return books.values().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
    
    public List<Book> getAvailableBooks() {
        return searchBooks(BookFilter.availableOnly());
    }
    
    public List<Book> getBorrowedBooks() {
        return searchBooks(BookFilter.borrowedOnly());
    }
    
    public Map<String, Integer> getBorrowingStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalBooks", books.size());
        stats.put("availableBooks", getAvailableBooks().size());
        stats.put("borrowedBooks", getBorrowedBooks().size());
        stats.put("totalPatrons", patrons.size());
        stats.put("overdueBooks", getOverdueBooks().size());
        stats.put("activeBorrows", activeBorrows.size());
        return stats;
    }

    public List<BorrowRecord> getOverdueBooks() {
        return activeBorrows.values().stream()
                .filter(BorrowRecord::isOverdue)
                .collect(Collectors.toList());
    }
    
    public List<BorrowRecord> getBorrowingHistory(String patronId) {
        Patron patron = patrons.get(patronId);
        return patron != null ? patron.getBorrowingHistory() : Collections.emptyList();
    }
    
    // Getters
    public Collection<Book> getAllBooks() { 
        return Collections.unmodifiableCollection(books.values()); 
    }
    
    public Collection<Patron> getAllPatrons() { 
        return Collections.unmodifiableCollection(patrons.values()); 
    }
    
    public Collection<BorrowRecord> getActiveBorrows() { 
        return Collections.unmodifiableCollection(activeBorrows.values()); 
    }
}