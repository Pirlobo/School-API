package com.bezkoder.springjwt.dto;

public class BookItemDto {

	private Integer barcode;

	private boolean isRented;

	private boolean isSold;

	private Integer sellQuantity;

	public Integer getBarcode() {
		return barcode;
	}

	public void setBarcode(Integer barcode) {
		this.barcode = barcode;
	}

	public Integer getSellQuantity() {
		return sellQuantity;
	}

	public void setSellQuantity(Integer sellQuantity) {
		this.sellQuantity = sellQuantity;
	}

	public boolean isRented() {
		return isRented;
	}

	public void setRented(boolean isRented) {
		this.isRented = isRented;
	}

	public boolean isSold() {
		return isSold;
	}

	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}

	public BookItemDto() {
		super();
	}

	public BookItemDto(Integer barcode, boolean isRented, boolean isSold, Integer sellQuantity) {
		super();
		this.barcode = barcode;
		this.isRented = isRented;
		this.isSold = isSold;
		this.sellQuantity = sellQuantity;
	}

}
