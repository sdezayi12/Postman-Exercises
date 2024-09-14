package edu.leicester.co2103.part1s2;

import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Book;
import edu.leicester.co2103.part1s2.domain.Order;
import edu.leicester.co2103.part1s2.service.AuthorService;
import edu.leicester.co2103.part1s2.service.BookService;
import edu.leicester.co2103.part1s2.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Part1s2Application {

    public static void main(String[] args) {
        SpringApplication.run(Part1s2Application.class, args);
    }

    @Bean
    public CommandLineRunner loadDummyData(AuthorService authorService, BookService bookService, OrderService orderService) {
        return args -> {
            // Create an author
            Author author = new Author();
            author.setName("John Doe");
            author.setBirthyear(1980);
            author.setNationality("American");

            // Save the author
            authorService.createAuthor(author);

            Author author2 = new Author();
            author2.setName("Joshua Eye");
            author2.setBirthyear(1967);
            author2.setNationality("American");

            // Save the author
            authorService.createAuthor(author2);

            // Create a book
            Book book = new Book();
            book.setISBN("ISBN-553-632-503");
            book.setTitle("Hi");
            book.setPublicationYear(2020);
            book.setPrice(29.99);
            List<Author> authors = new ArrayList<>();
            authors.add(author);
            book.setAuthors((ArrayList<Author>) authors);

            // Save the book
            bookService.createBook(book);

            Book book2 = new Book();
            book2.setISBN("ISBN-545-632-506");
            book2.setTitle("Hello");
            book2.setPublicationYear(2019);
            book2.setPrice(30.99);
            List<Author> authors2 = new ArrayList<>();
            authors.add(author2);
            book2.setAuthors((ArrayList<Author>) authors2);

            // Save the book
            bookService.createBook(book2);

            // Create an order
            Order order = new Order();
            order.setDatetime(new Timestamp(System.currentTimeMillis()));
            order.setCustomerName("John Doe");
            List<Book> books = new ArrayList<>();
            books.add(book);
            order.setBooks(books);

            // Save the order
            orderService.createOrder(order);

            Order order2 = new Order();
            order2.setDatetime(new Timestamp(System.currentTimeMillis()));
            order2.setCustomerName("Pearl Otoneks");
            List<Book> books2 = new ArrayList<>();
            books2.add(book2);
            order2.setBooks(books2);

            // Save the order
            orderService.createOrder(order2);
        };

    }
}
