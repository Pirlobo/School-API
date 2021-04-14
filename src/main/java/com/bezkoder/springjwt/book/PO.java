package com.bezkoder.springjwt.book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "purchaseOrder")
public class PO {

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne
	@JsonIgnore
	private Orders orders;

	private Double quantity;

	@ManyToOne
	private BookItems bookItems;

	public BookItems getBookItems() {
		return bookItems;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public void setBookItems(BookItems bookItems) {
		this.bookItems = bookItems;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PO() {
		super();
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public PO(double quantity) {
		super();
		this.quantity = quantity;

	}

}
