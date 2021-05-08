package com.bezkoder.springjwt.security.service;

import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.Orders;
@Service
public interface IOrderService {
	// persist order
	void save(Orders orders);
}
