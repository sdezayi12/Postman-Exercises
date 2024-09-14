package edu.leicester.co2103.part1s2.controller;

import edu.leicester.co2103.part1s2.domain.Book;
import edu.leicester.co2103.part1s2.domain.Order;
import edu.leicester.co2103.part1s2.repo.AuthorRepository;
import edu.leicester.co2103.part1s2.repo.BookRepository;
import edu.leicester.co2103.part1s2.repo.OrderRepository;
import edu.leicester.co2103.part1s2.service.AuthorService;
import edu.leicester.co2103.part1s2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    //Listing all orders
    @GetMapping
    public ResponseEntity<?> listAllOrders() {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Validate the order
        if (order.getBooks() == null || order.getBooks().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Check if each book in the order exists
        for (Book book : order.getBooks()) {
            Optional<Book> optionalBook = bookRepository.findById(book.getISBN());
            if (!optionalBook.isPresent()) {
                // If any book doesn't exist, return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }

        // All books exist, proceed to save the order
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    //Retrieving order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    //Updating specific order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") Long id, @RequestBody Order orderUpdates) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            Order updatedOrder = optionalOrder.get();
            updatedOrder.setDatetime(orderUpdates.getDatetime());
            updatedOrder.setCustomerName(orderUpdates.getCustomerName());

            // Save the updated order using the repository
            updatedOrder = orderRepository.save(updatedOrder); // Update the order using the repository save method

            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Next methods depend on adding a books variable in order domain

    //Listing all books in order
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> listBooksOrders(@PathVariable("id") Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            List<Book> books = new ArrayList<>(order.get().getBooks());
            return new ResponseEntity<>(books, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Adding book to existing order
    @PostMapping("/{id}/books")
    public ResponseEntity<Order> addBookToOrder(@PathVariable("id") Long id, @RequestBody Book book) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.getBooks().add(book);
            orderRepository.save(updatedOrder);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Removing book from existing order
    @DeleteMapping("/{id}/books/{ISBN}")
    public ResponseEntity<Order> removeBookFromOrder(@PathVariable("id") Long id, @PathVariable("ISBN") String ISBN) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            List<Book> books = updatedOrder.getBooks();
            boolean bookRemoved = books.removeIf(book -> book.getISBN().equals(ISBN));
            if (!bookRemoved) {
                // Book not found in the order
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            orderRepository.save(updatedOrder);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

