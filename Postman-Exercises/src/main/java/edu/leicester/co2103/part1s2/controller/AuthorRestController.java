package edu.leicester.co2103.part1s2.controller;

import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Book;
import edu.leicester.co2103.part1s2.repo.AuthorRepository;
import edu.leicester.co2103.part1s2.repo.BookRepository;
import edu.leicester.co2103.part1s2.repo.OrderRepository;
import edu.leicester.co2103.part1s2.service.AuthorService;
import org.antlr.v4.runtime.atn.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> listAllAuthors() {
        List<Author> authors = authorService.listAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(authors);
        }
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        if (author != null) {
            return ResponseEntity.ok(author);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        Author existingAuthor = authorService.getAuthorById(id);
        if (existingAuthor != null) {
            // Update the existing author with the provided data
            existingAuthor.setName(updatedAuthor.getName());
            existingAuthor.setBirthyear(updatedAuthor.getBirthyear());
            existingAuthor.setNationality(updatedAuthor.getNationality());

            // Save the updated author
            authorService.updateAuthor(existingAuthor);

            return ResponseEntity.ok(existingAuthor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        ResponseEntity<?> responseEntity = authorService.deleteAuthor(id);
        if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to list all books written by a specific author
    //Listing all books written by author ID
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> listAuthorBooks(@PathVariable("id") Long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(value -> new ResponseEntity<>(value.getBooks(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }
}