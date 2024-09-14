package edu.leicester.co2103.part1s2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Timestamp datetime;
    private String customerName;

    @ManyToMany
    @JoinTable(
            name = "order_books",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_isbn")
    )
    @JsonIgnoreProperties("orders")
    private List<Book> books;

    public Order(long id, Timestamp datetime, String customerName, List<Book> books) {
        this.id = id;
        this.datetime = datetime;
        this.customerName = customerName;
        this.books = books;
    }

    public Order() {

    }

    @JsonProperty("Id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", customerName='" + customerName + '\'' +
                ", books=" + books +
                '}';
    }
}
