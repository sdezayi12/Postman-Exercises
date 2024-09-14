package edu.leicester.co2103.part1s2.domain;

import com.fasterxml.jackson.annotation.*;
import edu.leicester.co2103.part1s2.domain.Author;
import edu.leicester.co2103.part1s2.domain.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    private String ISBN;
    private String title;
    private int publicationYear;
    private double price;

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("books")
    private List<Author> authors = new ArrayList<>();

    @ManyToMany(mappedBy = "books")
    private List<Order> orders;

    public Book(String ISBN, String title, int publicationYear, double price, List<Author> authors, List<Order> orders) {
        this.ISBN = ISBN;
        this.title = title;
        this.publicationYear = publicationYear;
        this.price = price;
        this.authors = authors;
        this.orders = orders;
    }

    public Book() {

    }

    @JsonProperty("ISBN")
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }




    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", price=" + price +
                ", authors=" + authors +
                ", orders=" + orders +
                '}';
    }
}
