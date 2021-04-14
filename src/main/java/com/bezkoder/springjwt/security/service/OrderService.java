package com.bezkoder.springjwt.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.Orders;
import com.bezkoder.springjwt.persistence.OrderRepository;

@Service
public class OrderService implements IOrderService {
	@Autowired
	private OrderRepository orderRepository;

	public void save(Orders orders) {
		orderRepository.save(orders);
	}
}
