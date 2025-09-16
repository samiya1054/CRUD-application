# CRUD-application
# Library Management System

A simple Java console application for managing library books and operations.

## Features

- **Add Books**: Add new books to the library inventory
- **View Books**: Display all books with their current status
- **Update Books**: Modify existing book information
- **Remove Books**: Delete books from the library system
- **Search Books**: Find books by title, author, or category
- **Borrow/Return**: Handle book borrowing and returning operations

## How to Run

1. Make sure you have Java installed on your system
2. Download the `LibrarySystem.java` file
3. Open terminal/command prompt
4. Navigate to the file location
5. Compile the program:
   ```
   javac LibrarySystem.java
   ```
6. Run the program:
   ```
   java LibrarySystem
   ```

## Usage

The program starts with a menu-driven interface:

```
Library Menu:
1. Add New Book
2. Show All Books
3. Update Book
4. Remove Book
5. Search Books
6. Borrow/Return Book
7. Exit
```

Simply enter the number corresponding to your desired operation and follow the prompts.

## Sample Data

The program comes with some sample books:
- To Kill a Mockingbird by Harper Lee
- Java Programming by John Smith
- Data Structures by Robert Sedgewick
- The Great Gatsby by F. Scott Fitzgerald

## Technologies Used

- **Java**: Core programming language
- **ArrayList**: For storing book data
- **Scanner**: For user input handling
- **Object-Oriented Programming**: Book class with encapsulation

## Project Structure

- `Book` class: Represents individual books with properties like ID, title, author, category, and availability status
- `LibrarySystem` class: Main class containing all CRUD operations and user interface logic

## Future Enhancements

- File-based data storage
- User management system
- Due date tracking for borrowed books
- Fine calculation system
- GUI interface
