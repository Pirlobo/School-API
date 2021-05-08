package com.bezkoder.springjwt.book;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.bezkoder.springjwt.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Orders {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer orderId;

	private Double totalPrice;

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)

	private List<RO> RItems = new ArrayList<RO>();

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)

	private List<PO> PItems = new ArrayList<PO>();

	@ManyToOne
	@JsonIgnore
	private User user;

	public List<RO> getRItems() {
		return RItems;
	}

	public void setRItems(List<RO> rItems) {
		RItems = rItems;
	}

	public List<PO> getPItems() {
		return PItems;
	}

	public void setPItems(List<PO> pItems) {
		PItems = pItems;
	}

	public Orders() {
		super();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Orders(User user, Double totalPrice) {
		super();

		this.user = user;
		this.totalPrice = totalPrice;
	}

	public void addRO(RO ro) {
		RItems.add(ro);
		ro.setOrders(this);
	}

	public void addPO(PO po) {
		PItems.add(po);
		po.setOrders(this);
	}

}
