package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Patron 
{
	private final String patronId;
	private String name;
	private String email;
	private String phone;
	private final List<BorrowRecord> borrowingHistory;

	public Patron(String patronId, String name, String email, String phone) {
		this.patronId = patronId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.borrowingHistory = new ArrayList<>();
	}

	// Getters and setters
	public String getPatronId() { return patronId; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public List<BorrowRecord> getBorrowingHistory() { return new ArrayList<>(borrowingHistory); }

	public void addBorrowRecord(BorrowRecord record) {
		borrowingHistory.add(record);
	}

	public int getCurrentlyBorrowedCount() {
		return (int) borrowingHistory.stream()
				.filter(record -> record.getReturnDate() == null)
				.count();
	}

	@Override
	public String toString() {
		return String.format("Patron{ID='%s', Name='%s', Email='%s', Phone='%s', CurrentlyBorrowed=%d}", 
				patronId, name, email, phone, getCurrentlyBorrowedCount());
	}
}