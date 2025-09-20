package domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stop {

	@Id
	private Integer stopId;
	private Integer numStop;
	private Ride ride;
	private String name;
	private Date date;
	private float price;
	
	
	public Stop(Integer stopId, Integer numStop, Ride ride, String name, float price, Date date) {
		this.stopId = stopId;
		this.numStop = numStop;
		this.ride = ride;
		this.name = name;
		this.price = price;
		this.date = date;
	}


	public Integer getStopId() {
		return stopId;
	}


	public void setStopId(Integer stopId) {
		this.stopId = stopId;
	}


	public Ride getRide() {
		return ride;
	}


	public void setRide(Ride ride) {
		this.ride = ride;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public Integer getNumStop() {
		return numStop;
	}

	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public void setNumStop(Integer numStop) {
		this.numStop = numStop;
	}
	


}