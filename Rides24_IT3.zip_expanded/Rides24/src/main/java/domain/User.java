package domain;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User {
	String username;
	String password;
	@Id
	String email;
	float balance;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Booking> booked = new Vector<Booking>();

	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.balance = 0;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String usuario) {
		this.username = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Booking> getBooked (){
		return booked;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
	


	
}

