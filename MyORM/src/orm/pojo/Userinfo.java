package orm.pojo;

import java.sql.*;
import java.util.*;

public class Userinfo {

	private String password;
	private Long id;
	private String username;


	public String getPassword() {
		return password;
	}
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password) {
		 this.password = password;
	}
	public void setId(Long id) {
		 this.id = id;
	}
	public void setUsername(String username) {
		 this.username = username;
	}
}
