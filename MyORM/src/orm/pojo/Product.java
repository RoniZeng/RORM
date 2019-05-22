package orm.pojo;

import java.sql.*;
import java.util.*;

public class Product {

	private Double price;
	private String name;
	private Integer id;


	public Double getPrice() {
		return price;
	}
	public String getName() {
		return name;
	}
	public Integer getId() {
		return id;
	}
	public void setPrice(Double price) {
		 this.price = price;
	}
	public void setName(String name) {
		 this.name = name;
	}
	public void setId(Integer id) {
		 this.id = id;
	}
}
