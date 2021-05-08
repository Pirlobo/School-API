package com.bezkoder.springjwt.book;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bookItem")
public class BookItems {

	@Id
	private Integer barcode;

	private Double rentalPrice;

	private Double sellingPrice;

	private boolean isForRent;

	@Enumerated(EnumType.STRING)
	private Format format;

	@OneToMany(mappedBy = "bookItems")
	@JsonIgnore
	private List<RO> rIitems = new ArrayList<RO>();

	@OneToMany(mappedBy = "bookItems")
	@JsonIgnore
	private List<PO> pIitems = new ArrayList<PO>();

	public Integer getBarcode() {
		return barcode;
	}

	public void setBarcode(Integer barcode) {
		this.barcode = barcode;
	}

	public Double getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(Double rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public BookItems() {
		super();
	}

	public List<RO> getrIitems() {
		return rIitems;
	}

	public void setrIitems(List<RO> rIitems) {
		this.rIitems = rIitems;
	}

	public List<PO> getpIitems() {
		return pIitems;
	}

	public void setpIitems(List<PO> pIitems) {
		this.pIitems = pIitems;
	}

	public boolean isForRent() {
		return isForRent;
	}

	public void setForRent(boolean isForRent) {
		this.isForRent = isForRent;
	}

	public BookItems(Integer barcode, Double rentalPrice, Double sellingPrice, boolean isForRent, Format format) {
		super();
		this.barcode = barcode;
		this.rentalPrice = rentalPrice;
		this.sellingPrice = sellingPrice;
		this.isForRent = isForRent;
		this.format = format;

	}

	public void addRO(RO ro) {
		rIitems.add(ro);
		ro.setBookItems(this);
	}

	public void addPO(PO po) {
		pIitems.add(po);
		po.setBookItems(this);
	}

}
