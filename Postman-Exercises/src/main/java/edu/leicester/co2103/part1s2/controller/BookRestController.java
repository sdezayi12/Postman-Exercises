package edu.leicester.co2103.part1s2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Book;
import edu.leicester.co2103.part1s2.domain.Order;
import edu.leicester.co2103.part1s2.repo.AuthorRepository;
import edu.leicester.co2103.part1s2.repo.BookRepository;
import edu.leicester.co2103.part1s2.repo.OrderRepository;
import edu.leicester.co2103.part1s2.service.AuthorService;
import edu.leicester.co2103.part1s2.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@RestController
@RequestMapping("/books")
public class BookRestController {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    // Endpoint to list all books
    @GetMapping
    public ResponseEntity<List<Book>> listAllBooks() {
        List<Book> books = bookService.listAllBooks();

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(books);
        }
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        if (bookService.existByISBN(book.getISBN())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.created(null).body(createdBook);
    }

    //Retrieving book by ISBN
    @GetMapping("/{ISBN}")
    public ResponseEntity<Book> getBook(@PathVariable("ISBN") String ISBN) {
        Optional<Book> book = bookRepository.findById(ISBN);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    //Updating module by ISBN
    @PutMapping({"/{ISBN}"})
    public ResponseEntity<?> updateBook(@PathVariable("ISBN") String ISBN, @RequestBody Map<String, Object> bookUpdates) {
        Optional<Book> optionalBook = bookRepository.findById(ISBN);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            bookUpdates.forEach((key, value) -> {
                switch (key) {
                    case "title":
                        book.setTitle((String) value);
                        break;
                    case "publicationYear":
                        book.setPublicationYear((int) value);
                        break;
                    case "price":
                        book.setPrice((double) value);
                        break;
                    case "orders":
                        List<Order> orders = objectMapper.convertValue(value, new TypeReference<>() {
                        });
                        book.setOrders(orders);
                        break;
                }
            });

            Book updatedBook = bookRepository.save(book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Deleting module by ISBN
    @DeleteMapping({"/{ISBN}"})
    public ResponseEntity<?> deleteBook(@PathVariable("ISBN") String ISBN) {
        Optional<Book> optionalBook = bookRepository.findById(ISBN);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            // Remove book from author
            for (Author author : authorRepository.findAll()) {
                author.getBooks().remove(book);
                authorRepository.save(author);
            }
            // Remove order from book
            Iterator<Order> orderIterator = book.getOrders().iterator();
            while (orderIterator.hasNext()) {
                Order order = orderIterator.next();
                orderIterator.remove();
                orderRepository.delete(order);
            }

            bookRepository.delete(book);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{ISBN}/authors")
    public ResponseEntity<List<Author>> listBookAuthors(@PathVariable("ISBN") String ISBN) {
        Optional<Book> optionalBook = bookRepository.findById(ISBN);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            List<Author> authors = book.getAuthors();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Listing all orders of specific book
    @GetMapping("/{ISBN}/orders")
    public ResponseEntity<List<Order>> listBookOrders(@PathVariable("ISBN") String ISBN) {
        Optional<Book> optionalBook = bookRepository.findById(ISBN);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            List<Order> orders = book.getOrders();
            if (orders.isEmpty()) {
                // Return 204 No Content if there are no orders
                return ResponseEntity.notFound().build();
            } else {
                // Return 200 OK with the list of orders
                return ResponseEntity.ok(orders);
            }
        } else {
            // Return 404 Not Found if the book is not found
            return ResponseEntity.notFound().build();
        }
    }
}