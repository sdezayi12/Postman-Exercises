package edu.leicester.co2103.part1s2.repo;

import edu.leicester.co2103.part1s2.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
