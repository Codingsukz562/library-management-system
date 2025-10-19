package com.library.main;


import com.library.model.Book;
import com.library.model.Patron;
import com.library.model.BorrowRecord;
import com.library.service.Library;
import com.library.patterns.BookFactory;
import com.library.patterns.*;
import com.library.filter.BookFilter;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class LibraryCLI {
    private Library library;
    private Scanner scanner;
    private boolean running;
    
    public LibraryCLI() {
        this.library = new Library();
        this.scanner = new Scanner(System.in);
        this.running = true;
        initializeSampleData();
    }
    
    private void initializeSampleData() {

        library.addBook(BookFactory.BookType.STANDARD, "978-0451524935", "1984", "George Orwell", 1949);
        library.addBook(BookFactory.BookType.BESTSELLER, "978-0061120084", "To Kill a Mockingbird", "Harper Lee", 1960);
        library.addBook(BookFactory.BookType.STANDARD, "978-0141439518", "Pride and Prejudice", "Jane Austen", 1813);
        library.addBook(BookFactory.BookType.BESTSELLER, "978-0544003415", "The Hobbit", "J.R.R. Tolkien", 1937);
        library.addBook(BookFactory.BookType.REFERENCE, "978-0000000001", "Library Reference Guide", "Library Staff", 2023);

        library.addPatron(new Patron("P001", "Alice Johnson", "alice@email.com", "555-0101"));
        library.addPatron(new Patron("P002", "Bob Smith", "bob@email.com", "555-0102"));
        library.addPatron(new Patron("P003", "Carol Davis", "carol@email.com", "555-0103"));
       
        library.subscribeToBook("P002", "978-0451524935");
        library.subscribeToBook("P003", "978-0061120084");
    }
    
    public void start() {
        System.out.println(" Welcome to Library Management System!");
        System.out.println("========================================");
        
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1": addBook(); break;
                case "2": addPatron(); break;
                case "3": searchBooks(); break;
                case "4": checkoutBook(); break;
                case "5": returnBook(); break;
                case "6": viewAllBooks(); break;
                case "7": viewAllPatrons(); break;
                case "8": viewStatistics(); break;
                case "9": testDesignPatterns(); break;
                case "0": exit(); break;
                default: System.out.println(" Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Add New Book");
        System.out.println("2. Add New Patron");
        System.out.println("3. Search Books");
        System.out.println("4. Checkout Book");
        System.out.println("5. Return Book");
        System.out.println("6. View All Books");
        System.out.println("7. View All Patrons");
        System.out.println("8. View Statistics");
        System.out.println("9. Test Design Patterns");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    
    private void addBook() {
        System.out.println("\n===== ADD NEW BOOK =====");
        
        try {
            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine();
            
            System.out.print("Enter Title: ");
            String title = scanner.nextLine();
            
            System.out.print("Enter Author: ");
            String author = scanner.nextLine();
            
            System.out.print("Enter Publication Year: ");
            int year = Integer.parseInt(scanner.nextLine());
            
            System.out.println("Select Book Type:");
            System.out.println("1. STANDARD");
            System.out.println("2. BESTSELLER");
            System.out.println("3. REFERENCE");
            System.out.print("Choose type (1-3): ");
            String typeChoice = scanner.nextLine();
            
            BookFactory.BookType type;
            switch (typeChoice) {
                case "1": type = BookFactory.BookType.STANDARD; break;
                case "2": type = BookFactory.BookType.BESTSELLER; break;
                case "3": type = BookFactory.BookType.REFERENCE; break;
                default: 
                    System.out.println(" Invalid type. Using STANDARD.");
                    type = BookFactory.BookType.STANDARD;
            }
            
            boolean success = library.addBook(type, isbn, title, author, year);
            if (success) {
                System.out.println(" Book added successfully!");
            } else {
                System.out.println(" Book with this ISBN already exists!");
            }
            
        } catch (Exception e) {
            System.out.println(" Error adding book: " + e.getMessage());
        }
    }
    
    private void addPatron() {
        System.out.println("\n===== ADD NEW PATRON =====");
        
        try {
            System.out.print("Enter Patron ID: ");
            String patronId = scanner.nextLine();
            
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine();
            
            Patron patron = new Patron(patronId, name, email, phone);
            boolean success = library.addPatron(patron);
            
            if (success) {
                System.out.println(" Patron added successfully!");
            } else {
                System.out.println(" Patron with this ID already exists!");
            }
            
        } catch (Exception e) {
            System.out.println(" Error adding patron: " + e.getMessage());
        }
    }
    
    private void searchBooks() {
        System.out.println("\n===== SEARCH BOOKS =====");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ISBN");
        System.out.println("4. Show Available Books");
        System.out.println("5. Show Borrowed Books");
        System.out.print("Choose search type: ");
        
        String searchType = scanner.nextLine();
        
        try {
            List<Book> results;
            
            switch (searchType) {
                case "1":
                    System.out.print("Enter title to search: ");
                    String title = scanner.nextLine();
                    results = library.searchByTitle(title);
                    break;
                    
                case "2":
                    System.out.print("Enter author to search: ");
                    String author = scanner.nextLine();
                    results = library.searchByAuthor(author);
                    break;
                    
                case "3":
                    System.out.print("Enter ISBN to search: ");
                    String isbn = scanner.nextLine();
                    results = library.searchByIsbn(isbn)
                            .map(List::of)
                            .orElse(List.of());
                    break;
                    
                case "4":
                    results = library.getAvailableBooks();
                    break;
                    
                case "5":
                    results = library.searchBooks(BookFilter.borrowedOnly());
                    break;
                    
                default:
                    System.out.println(" Invalid search type.");
                    return;
            }
            
            if (results.isEmpty()) {
                System.out.println(" No books found.");
            } else {
                System.out.println(" Found " + results.size() + " book(s):");
                results.forEach(book -> {
                    String status = book.isAvailable() ? " Available" : " Borrowed";
                    System.out.println(" - " + book.getTitle() + " by " + book.getAuthor() + " (" + status + ")");
                });
            }
            
        } catch (Exception e) {
            System.out.println(" Error searching books: " + e.getMessage());
        }
    }
    
    private void checkoutBook() {
        System.out.println("\n===== CHECKOUT BOOK =====");
        
        try {
            System.out.print("Enter Patron ID: ");
            String patronId = scanner.nextLine();
            
            System.out.print("Enter Book ISBN: ");
            String isbn = scanner.nextLine();
            
            boolean success = library.checkoutBook(patronId, isbn);
            
            if (success) {
                System.out.println(" Book checked out successfully!");
                
                library.subscribeToBook(patronId, isbn);
                System.out.println(" You will be notified when this book is returned.");
            } else {
                System.out.println(" Checkout failed. Possible reasons:");
                System.out.println("   - Patron or book not found");
                System.out.println("   - Book already borrowed");
                System.out.println("   - Patron has reached borrowing limit (5 books)");
            }
            
        } catch (Exception e) {
            System.out.println(" Error during checkout: " + e.getMessage());
        }
    }
    
    private void returnBook() {
        System.out.println("\n===== RETURN BOOK =====");
        
        try {
            System.out.print("Enter Book ISBN to return: ");
            String isbn = scanner.nextLine();
            
            boolean success = library.returnBook(isbn);
            
            if (success) {
                System.out.println(" Book returned successfully!");
                System.out.println(" Subscribers have been notified.");
            } else {
                System.out.println(" Return failed. Book may not be currently borrowed.");
            }
            
        } catch (Exception e) {
            System.out.println(" Error during return: " + e.getMessage());
        }
    }
    
    private void viewAllBooks() {
        System.out.println("\n===== ALL BOOKS =====");
        List<Book> allBooks = library.getAllBooks().stream().toList();
        
        if (allBooks.isEmpty()) {
            System.out.println("No books in library.");
        } else {
            System.out.println("Total books: " + allBooks.size());
            allBooks.forEach(book -> {
                String type = "Standard";
                if (book.getTitle().contains("[Bestseller]")) type = "Bestseller";
                if (!book.isAvailable() && book.getTitle().contains("Reference")) type = "Reference";
                
                String status = book.isAvailable() ? " Available" : " Borrowed";
                System.out.printf(" - %s by %s [%s] %s%n", 
                    book.getTitle(), book.getAuthor(), type, status);
            });
        }
    }
    
    private void viewAllPatrons() {
        System.out.println("\n===== ALL PATRONS =====");
        List<Patron> allPatrons = library.getAllPatrons().stream().toList();
        
        if (allPatrons.isEmpty()) {
            System.out.println("No patrons registered.");
        } else {
            System.out.println("Total patrons: " + allPatrons.size());
            allPatrons.forEach(patron -> {
                System.out.printf(" - %s (%s) - Currently borrowing: %d books%n",
                    patron.getName(), patron.getPatronId(), patron.getCurrentlyBorrowedCount());
            });
        }
    }
    
    private void viewStatistics() {
        System.out.println("\n===== LIBRARY STATISTICS =====");
        Map<String, Integer> stats = library.getBorrowingStatistics();
        
        stats.forEach((key, value) -> {
            String formattedKey = key.replaceAll("([A-Z])", " $1").toLowerCase();
            formattedKey = formattedKey.substring(0, 1).toUpperCase() + formattedKey.substring(1);
            System.out.printf("%-20s: %d%n", formattedKey, value);
        });
        
        // Show overdue books
        List<BorrowRecord> overdue = library.getOverdueBooks();
        if (!overdue.isEmpty()) {
            System.out.println("\n Overdue Books:");
            overdue.forEach(record -> {
                System.out.printf(" - Book ISBN: %s, Patron: %s, Days overdue: %d%n",
                    record.getBookIsbn(), record.getPatronId(), record.getDaysOverdue());
            });
        }
    }
    
    private void testDesignPatterns() {
        System.out.println("\n===== DESIGN PATTERNS DEMO =====");
        
        // Strategy Pattern Demo
        System.out.println("\n STRATEGY PATTERN - Different Search Methods:");
        System.out.println("Searching by Title 'kill':");
        library.searchWithStrategy(new TitleSearchStrategy(), "kill")
              .forEach(b -> System.out.println("   - " + b.getTitle()));
        
        System.out.println("Searching by Author 'Orwell':");
        library.searchWithStrategy(new AuthorSearchStrategy(), "Orwell")
              .forEach(b -> System.out.println("   - " + b.getTitle()));
        
        // Factory Pattern Demo
        System.out.println("\n FACTORY PATTERN - Different Book Types:");
        System.out.println("All books with their types:");
        library.getAllBooks().forEach(book -> {
            String type = "Standard";
            if (book.getTitle().contains("[Bestseller]")) type = "Bestseller";
            if (!book.isAvailable() && !book.getTitle().contains("Bestseller")) type = "Reference";
            System.out.printf("   - %s [%s]%n", book.getTitle(), type);
        });
        
        // Observer Pattern Demo
        System.out.println("\n👀 OBSERVER PATTERN - Testing Notifications:");
        System.out.println("Checking out '1984' to trigger notifications...");
        library.checkoutBook("P001", "978-0451524935");
        System.out.println("Returning '1984' to trigger observer notifications...");
        library.returnBook("978-0451524935");
        
        System.out.println("\n Design Patterns demo completed!");
    }
    
    private void exit() {
        System.out.println("\nThank you for using Library Management System!");
        System.out.println("Goodbye!");
        running = false;
        scanner.close();
    }
    
    public static void main(String[] args) {
        LibraryCLI cli = new LibraryCLI();
        cli.start();
    }
}
