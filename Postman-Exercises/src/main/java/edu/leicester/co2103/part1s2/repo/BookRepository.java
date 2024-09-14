package edu.leicester.co2103.part1s2.repo;

import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {
    // Define a method to retrieve books written by a specific author
    List<Book> findByAuthorsContains(Author author);

    List<Author> findAuthorsByISBN(String ISBN);

    boolean existsByISBN(String ISBN);
}

