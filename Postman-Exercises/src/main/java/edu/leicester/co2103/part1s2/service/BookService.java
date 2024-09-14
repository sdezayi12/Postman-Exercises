package edu.leicester.co2103.part1s2.service;

import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Book;
import edu.leicester.co2103.part1s2.repo.AuthorRepository;
import edu.leicester.co2103.part1s2.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // Method to list all books
    public List<Book> listAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }


    public boolean existByISBN(String ISBN) {
        return bookRepository.existsByISBN(ISBN);
    }

    public Book createBook(Book book) {
        List<Author> authors = new ArrayList<>();

        for (Author author : book.getAuthors()) {
            // Check if the author already exists in the database
            Author existingAuthor = authorRepository.findById(author.getId()).orElse(null);

            if (existingAuthor != null) {
                // If the author exists, add the book to their list of books
                existingAuthor.getBooks().add(book);
                authors.add(existingAuthor); // Add the existing author to the list
            } else {
                // If the author doesn't exist, save the new author along with the book
                author.getBooks().add(book); // Add the book to the author's list of books
                authors.add(author); // Add the new author to the list
            }
        }

        // Set the updated list of authors to the book
        book.setAuthors((ArrayList<Author>) authors);

        // Save the book
        return bookRepository.save(book);
    }

    }