package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowRecord {
    private final String recordId;
    private final String patronId;
    private final String bookIsbn;
    private final LocalDate borrowDate;
    private LocalDate returnDate;
    private final LocalDate dueDate;
    
    public BorrowRecord(String recordId, String patronId, String bookIsbn, 
                       LocalDate borrowDate, LocalDate dueDate) {
        this.recordId = recordId;
        this.patronId = patronId;
        this.bookIsbn = bookIsbn;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }
    
    // Getters
    public String getRecordId() { return recordId; }
    public String getPatronId() { return patronId; }
    public String getBookIsbn() { return bookIsbn; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public LocalDate getDueDate() { return dueDate; }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    public boolean isActive() {
        return returnDate == null;
    }
    
    @Override
    public String toString() {
        return String.format("BorrowRecord{BookISBN='%s', PatronID='%s', Borrowed=%s, Due=%s, Returned=%s, Overdue=%s}",
                bookIsbn, patronId, borrowDate, dueDate, returnDate, isOverdue());
    }
}