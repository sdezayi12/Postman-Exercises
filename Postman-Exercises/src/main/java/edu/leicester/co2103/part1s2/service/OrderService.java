package edu.leicester.co2103.part1s2.service;

import edu.leicester.co2103.part1s2.domain.Order;
import edu.leicester.co2103.part1s2.repo.AuthorRepository;
import edu.leicester.co2103.part1s2.repo.BookRepository;
import edu.leicester.co2103.part1s2.repo.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public OrderService(AuthorRepository authorRepository, BookRepository bookRepository, OrderRepository orderRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }


}
