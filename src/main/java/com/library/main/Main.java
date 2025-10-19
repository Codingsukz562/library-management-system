package com.library.main;

import com.library.service.Library;
import com.library.model.Patron;
import com.library.patterns.BookFactory;
import java.util.Scanner;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(" Library Management System");
        System.out.println("============================");
        System.out.println("Choose how to run:");
        System.out.println("1. Interactive CLI (Scanner-based)");
        System.out.println("2. Automated Demo");
        System.out.print("Enter choice (1 or 2): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice == 1) {
                LibraryCLI cli = new LibraryCLI();
                cli.start();
            } else {
                runAutomatedDemo();
            }
        } catch (Exception e) {
            System.out.println("Invalid choice. Running automated demo...");
            runAutomatedDemo();
        } finally {
            scanner.close();
        }
    }
    
    private static void runAutomatedDemo() {
        System.out.println("\n Running Automated Demo...");
        
        Library library = new Library();

        library.addBook(BookFactory.BookType.STANDARD, 
                       "978-0451524935", "1984", "George Orwell", 1949);
        library.addBook(BookFactory.BookType.BESTSELLER, 
                       "978-0061120084", "To Kill a Mockingbird", "Harper Lee", 1960);

        library.addPatron(new Patron("P001", "Alice Johnson", "alice@email.com", "555-0101"));

        System.out.println("\n Demo Searches:");
        System.out.println("Search by 'kill': " + library.searchByTitle("kill").size() + " books found");
        System.out.println("Search by 'Orwell': " + library.searchByAuthor("Orwell").size() + " books found");

        System.out.println("\n Demo Checkout:");
        boolean checkoutSuccess = library.checkoutBook("P001", "978-0451524935");
        System.out.println("Checkout result: " + (checkoutSuccess ? "SUCCESS" : "FAILED"));

        System.out.println("\n Library Statistics:");
        Map<String, Integer> stats = library.getBorrowingStatistics();
        System.out.println("Total Books: " + stats.get("totalBooks"));
        System.out.println("Available Books: " + stats.get("availableBooks"));
        System.out.println("Borrowed Books: " + stats.get("borrowedBooks"));
        
        System.out.println("\n Automated Demo Completed!");
    }
}