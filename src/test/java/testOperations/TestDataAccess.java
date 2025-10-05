package testOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;
import domain.Stop;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String driverEmail) {
		System.out.println(">> TestDataAccess: removeRide");
		Driver d = db.find(Driver.class, driverEmail);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	public Driver createDriver(String email,String password, String name) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver=null;
			db.getTransaction().begin();
			try {
			    driver=new Driver(email,password,name);
				db.persist(driver);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return driver;
    }
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
		

		public Driver addDriverWithRide(String email, String password, String name, String from, String to,  Date date, int nPlaces, float price) {
			System.out.println(">> TestDataAccess: addDriverWithRide");
				Driver driver=null;
				db.getTransaction().begin();
				try {
					 driver = db.find(Driver.class, email);
					if (driver==null)
						driver=new Driver(name,password,email);
				    driver.addRide(from, to, date, nPlaces, price);
					db.getTransaction().commit();
					return driver;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return null;
	    }
		
		
		public boolean existRide(String email, String from, String to, Date date) {
			System.out.println(">> TestDataAccess: existRide");
			Driver d = db.find(Driver.class, email);
			if (d!=null) {
				return d.doesRideExists(from, to, date);
			} else 
			return false;
		}
		
		public Ride removeRide(String email, String from, String to, Date date ) {
			Driver d = db.find(Driver.class, email);
			if (d!=null) {
				db.getTransaction().begin();
				Ride r= d.removeRide(from, to, date);
				db.getTransaction().commit();
				return r;

			} else 
			return null;

		}
		
		public boolean removeAllRides(String email) {
		    Driver d = db.find(Driver.class, email);
		    if (d != null) {
		        db.getTransaction().begin();

		        List<Ride> ridesToRemove = d.getRides();
		        
		        for (Ride ride : ridesToRemove) {
		            d.getRides().remove(ride);
		            Ride r = db.find(Ride.class, ride.getRideNumber());
		            r.setStops(null);
		            db.remove(r);
		        }

		        db.getTransaction().commit();
		        return true;
		    }
		    return false;
		}
		
		public boolean addStopToRide(Ride ride, String name, float price, int num) {
			db.getTransaction().begin();
			Ride r = db.find(Ride.class, ride.getRideNumber());
			int id = 0;
			while (db.find(Stop.class, id) != null) {
				id++;
			}
			
			Stop s = new Stop(id,num,r,name,price,r.getDate());
			r.getStops().add(s);
			db.persist(s);
			db.getTransaction().commit();
			return true;
		}
		
		public boolean removeStop(Ride ride) {
			if (ride.getStops()!=null) {
				db.getTransaction().begin();
				Ride r = db.find(Ride.class, ride.getRideNumber());
				for (Stop stop : r.getStops()) {
					Stop s = db.find(Stop.class, stop.getStopId());
					db.remove(s);
				}
				db.getTransaction().commit();
				return true;

			} else 
			return false;
		}

		public boolean addRideToDriver(String driverEmail ,Ride r) {
			Driver d = db.find(Driver.class, driverEmail);
			if (d!=null) {
				db.getTransaction().begin();
				d.addRide(r.getFrom(), r.getTo(), r.getDate(), r.getnPlaces(), r.getPrice());
				db.persist(d);
				db.persist(r);
				db.getTransaction().commit();
				return true;

			} else 
			return false;
		}

		
}


