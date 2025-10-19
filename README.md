# 📚 Library Management System

A comprehensive Java-based Library Management System that demonstrates Object-Oriented Programming principles, SOLID principles, and design patterns. This system helps librarians manage books, patrons, and lending processes efficiently.

## 🚀 Features

### Book Management
- ✅ Add, remove, and update books in the library inventory
- ✅ Search functionality to find books by title, author, or ISBN
- ✅ Track book availability status

### Patron Management
- ✅ Add new patrons and update their information
- ✅ Track patron borrowing history
- ✅ Enforce borrowing limits (max 5 books per patron)

### Lending Process
- ✅ Book checkout and return functionalities
- ✅ Automatic due date calculation (3-week loan period)
- ✅ Overdue book tracking

### Inventory Management
- ✅ Track available and borrowed books
- ✅ Generate library statistics and reports
- ✅ Real-time availability updates

## 🏗️ System Architecture & Class Diagram
┌─────────────────────────────────────────────────────────────────────────────┐
│                           LIBRARY MANAGEMENT SYSTEM                          │
└─────────────────────────────────────────────────────────────────────────────┘

                                    ╔═══════════════════════════════════════════╗
                                    ║               INTERFACES                  ║
                                    ╚═══════════════════════════════════════════╝

┌───────────────────┐          ┌───────────────────┐
│  SearchStrategy   │          │   BookObserver    │
│  <<interface>>    │          │   <<interface>>   │
├───────────────────┤          ├───────────────────┤
│ +search()         │          │ +update()         │
└───────────────────┘          └───────────────────┘
          △                                 △
          │ implements                      │ implements
          │                                 │
    ┌─────┴─────┐                    ┌─────────────┐
    │           │                    │PatronNotification│
┌─────────┐ ┌─────────┐              ├─────────────┤
│Title    │ │Author   │              │ -patronId   │
│Search   │ │Search   │              │ +update()   │
└─────────┘ └─────────┘              └─────────────┘
    │           │
┌─────────┐     │
│ISBN     │     │
│Search   │     │
└─────────┘     │
          ┌─────┴─────┐
          │SearchService│
          ├─────────────┤
          │ -strategy   │
          │ +setStrategy()│
          │ +performSearch()│
          └─────────────┘

                                    ╔═══════════════════════════════════════════╗
                                    ║              CORE CLASSES                 ║
                                    ╚═══════════════════════════════════════════╝

┌──────────────┐             ┌───────────────────┐
│  BookFactory │             │   ObservableBook  │
├──────────────┤             ├───────────────────┤
│ +createBook()│             │ -book             │
└──────────────┘             │ -observers        │
         │                   │ +addObserver()    │
         │ creates           │ +removeObserver() │
         │                   │ +notifyObservers()│
         ▽                   └───────────────────┘
┌──────────────┐                      △
│    Book      │                      │ contains
├──────────────┤                      │
│ -isbn        │              ┌──────────────┐
│ -title       │              │   Library    │
│ -author      │              ├──────────────┤
│ -year        │              │ -books       │
│ -available   │              │ -patrons     │
└──────────────┘              │ -activeBorrows│
         △                    │ -observableBooks│
         │ 1                  ├──────────────┤
         │ *                  │ +addBook()   │
┌──────────────┐              │ +searchBooks()│
│ BorrowRecord │              │ +checkoutBook()│
├──────────────┤              │ +returnBook() │
│ -recordId    │              │ +addPatron()  │
│ -patronId    │              └──────────────┘
│ -bookIsbn    │                      △
│ -borrowDate  │                      │ manages
│ -returnDate  │                      │
│ -dueDate     │              ┌──────────────┐
├──────────────┤              │    Patron    │
│ +isOverdue() │              ├──────────────┤
└──────────────┘              │ -patronId    │
         △                    │ -name        │
         │ 1                  │ -email       │
         │ *                  │ -phone       │
         │                    │ -borrowingHistory│
┌──────────────┐              ├──────────────┤
│    Patron    │              │ +addBorrowRecord()│
└──────────────┘              └──────────────┘

                                    ╔═══════════════════════════════════════════╗
                                    ║               UTILITIES                   ║
                                    ╚═══════════════════════════════════════════╝

┌──────────────┐
│  BookFilter  │
├──────────────┤
│ +byTitle()   │
│ +byAuthor()  │
│ +byIsbn()    │
│ +availableOnly()│
│ +borrowedOnly()│
│ +combine()   │
└──────────────┘

┌──────────────┐
│   BookType   │
│ <<enumeration>>│
├──────────────┤
│ STANDARD     │
│ BESTSELLER   │
│ REFERENCE    │
└──────────────┘
## 🔧 Design Patterns Implemented

### 1. Strategy Pattern
- **Purpose**: Different search algorithms
- **Implementation**: `SearchStrategy` interface with concrete implementations
- **Classes**: `TitleSearchStrategy`, `AuthorSearchStrategy`, `ISBNSearchStrategy`

### 2. Factory Pattern  
- **Purpose**: Create different types of books
- **Implementation**: `BookFactory` with `BookType` enum
- **Book Types**: STANDARD, BESTSELLER, REFERENCE

### 3. Observer Pattern
- **Purpose**: Notify patrons when books become available
- **Implementation**: `BookObserver` interface with `PatronNotification`

## 🎯 SOLID Principles Applied

- **Single Responsibility**: Each class has a single purpose
- **Open/Closed**: Easy to extend with new search strategies
- **Liskov Substitution**: All search strategies are interchangeable
- **Interface Segregation**: Small, focused interfaces
- **Dependency Inversion**: High-level modules depend on abstractions
