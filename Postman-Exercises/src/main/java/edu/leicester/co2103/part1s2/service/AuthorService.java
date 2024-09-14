package edu.leicester.co2103.part1s2.service;

import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Book;
import edu.leicester.co2103.part1s2.domain.Order;
import edu.leicester.co2103.part1s2.repo.AuthorRepository;
import edu.leicester.co2103.part1s2.repo.BookRepository;
import edu.leicester.co2103.part1s2.repo.OrderRepository;
import org.antlr.v4.runtime.atn.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository, OrderRepository orderRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public List<Author> listAllAuthors() {
        return (List<Author>) authorRepository.findAll();
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public void updateAuthor(Author author) {
        authorRepository.save(author);
    }

    //Deleting author by ID
    public ResponseEntity<?> deleteAuthor(@PathVariable("id") Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            List<Book> booksWrittenByAuthor = author.getBooks();
            List<Book> booksCopy = new ArrayList<>(booksWrittenByAuthor);

            for (Book book : booksCopy) {
                List<Order> authorsWritingBook = book.getOrders();
                if (authorsWritingBook != null && authorsWritingBook.size() == 1 && authorsWritingBook.get(0).equals(author)) {
                    List<Order> ordersOfBook = book.getOrders();
                    orderRepository.deleteAll(ordersOfBook);
                    author.getBooks().remove(book);
                    bookRepository.delete(book);
                } else {
                    author.getBooks().remove(book);
                }
            }

            authorRepository.delete(author);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

