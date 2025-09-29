package domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Booking {

	@Id
	private Integer BookingId;

	private Ride ride;

	private User user;
	private Driver driver;

	private String from;
	private String to;

	private Integer seats;
	private Double price;

	private Integer accepeted;	// 0=Pending 1=Accepted 2=Declined
	
	private Stop stop;

	public Booking(Integer id, Ride ride, User user, Driver driver, String from, String to, Integer seats,Double price) {
		this.BookingId = id;
		this.user = user;
		this.driver = driver;
		this.ride = ride;
		this.from = from;
		this.to = to;
		this.seats = seats;
		this.price = price;
		this.accepeted = 0;
		this.stop = null;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public Integer getAccepeted() {
		return accepeted;
	}

	public void setAccepeted(Integer accepeted) {
		this.accepeted = accepeted;
	}

	public Integer getBookingId() {
		return BookingId;
	}

	public void setBookingId(Integer bookingId) {
		BookingId = bookingId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double prize) {
		this.price = prize;
	}
	
	public Stop getStop() {
		return this.stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public String toString(){
		return BookingId+";"+ride+";"+user+";"+driver+";"+from+";"+to+";"+seats+";"+price+";"+accepeted;  
	}

}