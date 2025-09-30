package dataAccess;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.logging.Logger;
import java.net.NoRouteToHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Stop;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;
	Logger logger = Logger.getLogger(getClass().getName());
	
	private ArrayList<String> ciudades = new ArrayList<String>(); 


	ConfigXML c=ConfigXML.getInstance();

     public DataAccess()  {
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();
			Path fileToDelete = Paths.get(fileName);
			try {
				Files.delete(fileToDelete);
				Path fileToDeleteTemp = Paths.get(fileName+"$");
				Files.deleteIfExists(fileToDeleteTemp);
				logger.info("File deleted");
			} catch (IOException e) {
				logger.info("Operation failed" + e.getMessage());
			}
		}
		open();
		
		if  (c.isDatabaseInitialized())initializeDB();
		
		logger.info("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}
     
    public DataAccess(EntityManager db) {
    	this.db=db;
    }

	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		
		db.getTransaction().begin();

		try {

		   Calendar today = Calendar.getInstance();
		   
		   int month=today.get(Calendar.MONTH);
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=1; year+=1;}  
	    
		   
		    //Create drivers 
			Driver driver1=new Driver("driver1@gmail.com","test1","Aitor Fernandez");
			Driver driver2=new Driver("driver2@gmail.com","test2","Ane Gaztañaga");
			Driver driver3=new Driver("driver3@gmail.com","test3","Test driver");
			Driver driver4=new Driver("driver4@gmail.com","111","Mikel");
			
			driver1.setBalance(10);
			
			User user1 = new User("Mikel Pallin","111","mikel@test.com");

			
			ciudades.add("Donostia"); 	//0
			ciudades.add("Bilbo");		//1
			ciudades.add("Gasteiz");	//2
			ciudades.add("iruña");		//3
			ciudades.add("Eibar");		//4
			
			//Create rides
			driver1.addRide(ciudades.get(0), ciudades.get(1), UtilDate.newDate(year,month,15), 4, 7);
			driver1.addRide(ciudades.get(0), ciudades.get(2), UtilDate.newDate(year,month,6), 4, 8);
			driver1.addRide(ciudades.get(1), ciudades.get(0), UtilDate.newDate(year,month,25), 4, 4);

			driver1.addRide(ciudades.get(0), ciudades.get(3), UtilDate.newDate(year,month,7), 4, 8);
			
			driver2.addRide(ciudades.get(0), ciudades.get(1), UtilDate.newDate(year,month,15), 3, 3);
			driver2.addRide(ciudades.get(1), ciudades.get(0), UtilDate.newDate(year,month,25), 2, 5);
			driver2.addRide(ciudades.get(4), ciudades.get(2), UtilDate.newDate(year,month,6), 2, 5);

			driver3.addRide(ciudades.get(1), ciudades.get(0), UtilDate.newDate(year,month,14), 1, 3);
			
//			driver4.addRide("A", "B", UtilDate.newDate(year,month,14), 4, 3);
//			driver4.addRide("B", "C", UtilDate.newDate(year,month,14), 4, 3);
			
			
			if(!db.contains(driver1)) db.persist(driver1);		
			if(!db.contains(driver2)) db.persist(driver2);	
			if(!db.contains(driver3)) db.persist(driver3);
			if(!db.contains(driver4)) db.persist(driver4);
			if(!db.contains(user1))	  db.persist(user1);
			
			
			
	
			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	public List<String> getDepartCities(){
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
			List<String> cities = query.getResultList();
			return cities;
		
	}
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList(); 
		return arrivingCities;
		
	}
	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail, List<Vector<String>> sl) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+" driver="+driverEmail+" date "+date);
		try {
			if(new Date().compareTo(date)>0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();
			
			Driver driver = db.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			//next instruction can be obviated
			
			if(!sl.isEmpty()) {
				for(Vector<String> vs : sl) {
					int id = 0;
					while (db.find(Stop.class, id) != null) {
						id++;
					}
					Stop s = new Stop(id, Integer.parseInt(vs.get(1).toString()) ,ride, vs.get(0),Float.parseFloat(vs.get(2).toString()),ride.getDate());
					ride.getStops().add(s);
					db.persist(s);
				}
			}
			
			db.persist(driver); 
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}
		
		
	}
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= "+from+" to= "+to+" date "+date);

		List<Ride> res = new ArrayList<>();	
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",Ride.class);   
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
	 	 for (Ride ride:rides){
		   res.add(ride);
		  }
	 	return res;
	}
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();	
		
		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
		Date lastDayMonthDate= UtilDate.lastDayMonth(date);		
		
		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   
		
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		System.out.println(dates);
		
		if(dates.isEmpty()) {
			TypedQuery<Date> query2 = db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class);   
			
			query2.setParameter(1, from);
			query2.setParameter(2, to);
			query2.setParameter(3, from);
			query2.setParameter(6, to);
			query2.setParameter(4, firstDayMonthDate);
			query2.setParameter(5, lastDayMonthDate);
			List<Date> dates2 = query2.getResultList();
			System.out.println(dates2);
			
			
			if(dates2.isEmpty()) {
				TypedQuery<Date> query3 = db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class);
				
				query3.setParameter(2, from);
				query3.setParameter(3, to);
				query3.setParameter(4, firstDayMonthDate);
				query3.setParameter(5, lastDayMonthDate);
				List<Date> dates3 = query3.getResultList();
				System.out.println(dates3);
				
				
				for (Date d:dates3) {
					res.add(d);
				}
			}
			else {
				for (Date d:dates2){
					res.add(d);
				}
			}
		}
		
		else {
	 	 for (Date d:dates){
		   res.add(d);
	 	}
	 	 
		  }
	 	return res;
	}
	

public void open(){
		
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			  db = emf.createEntityManager();
    	   }
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());

		
	}

	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}
	
	
	public Driver loginDriver(String username, String password) {
		
		TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.name= :username", Driver.class);
		query.setParameter("username", username);
		List<Driver> p = query.getResultList();
		for (Driver d : p) {
			if(d.getPassword().equals(password)) {
				System.out.println("Driver found for username: " + username);
				return d;
			}
		}
		System.out.println("Driver not found for username: " + username);
		return null;
	
	}
	
	public User loginUser(String username, String password) {
		
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username= :username", User.class);
		query.setParameter("username", username);
		List<User> p = query.getResultList();
		for (User u : p) {
			if(u.getPassword().equals(password)) {
				System.out.println("Driver found for username: " + username);
				return u;
			}
		}
		System.out.println("Driver not found for username: " + username);
		return null;
	
	}
	
	public boolean registerDriver(String username, String email, String password) {
		
		db.getTransaction().begin();
		
		Driver d = new Driver(email, password, username);
		TypedQuery<User> query = db.createQuery("SELECT d FROM Driver d WHERE d.name= :name", User.class);
		query.setParameter("name", username);
		List<User> p = query.getResultList();
		
		
		if(!db.contains(d) && p.isEmpty()) {	
			db.persist(d);
			db.getTransaction().commit();
			return true;
		}
		return false;
		
	}
	
	public boolean registerUser(String username, String email, String password) {
		db.getTransaction().begin();
		
		User u = new User(username, password, email);
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username= :username", User.class);
		query.setParameter("username", username);
		List<User> p = query.getResultList();
		
		if(!db.contains(u) && p.isEmpty()) {	
			db.persist(u);
			db.getTransaction().commit();
			return true;
		}
		return false;
		
	}
	
	
	public boolean addRideUser(User user, Integer rideNum, Integer seats) {
		return true;
	}
	
	
	public List<Ride> getRidesByEmail(String email,String type){
		if(type.equals("Driver")) {
			Driver d = db.find(Driver.class, email);
			List<Ride> l = d.getRides();
			return l;
		}
		else {
			return new Vector<Ride>();
		}
	}
	
	public List<Booking> getBookingByEmail (String email, String type){
		
		if(type.equals("Driver")) {
			Driver d = db.find(Driver.class, email);
			List<Booking> l = d.getBookings();
			System.out.println(l);
			return l;
		}
		else {
			User u = db.find(User.class, email);
			List<Booking> l = u.getBooked();
			return l;
		}
	}
	
	public boolean addBooking(User user, Ride ride, String from, String to ,Integer numSeats, Double prize, Stop stop) {
		db.getTransaction().begin();
		User u = db.find(User.class, user.getEmail());
		int id = 0;
		while (db.find(Booking.class, id) != null) {
			id++;
		}
		
		Booking b = new Booking(id, ride, user, ride.getDriver(), from, to, numSeats, prize);

		
		Driver d = db.find(Driver.class, ride.getDriver().getEmail());
		
		if(stop!=null) {
			b.setStop(stop);
		}
		
		u.getBooked().add(b);
		d.getBookings().add(b);
		
		db.persist(u);
		db.persist(d);
		db.persist(b);
		db.getTransaction().commit();
		return true;
	}
	
	public boolean addStopToRide(Ride ride, String name, float price, int num) {
		db.getTransaction().begin();
		Ride r = db.find(Ride.class, ride.getRideNumber());
		int id = 0;
		while (db.find(Stop.class, id) != null) {
			id++;
		}
		
		Stop s = new Stop(id,num,ride,name,price,r.getDate());
		r.getStops().add(s);
		db.persist(s);
		db.getTransaction().commit();
		return true;
	}
	
	
	public Ride findRide(Integer id) {
		Ride r = db.find(Ride.class, id);
		return r;
	}
	
	public boolean acceptBooking (Integer bookingId, float price) {
		db.getTransaction().begin();
		Booking b = db.find(Booking.class, bookingId);
		User u = db.find(User.class, b.getUser().getEmail());
		Driver d = db.find(Driver.class, b.getDriver().getEmail());
		
		
		if(b.getRide().getnPlaces()-b.getSeats()<0) {
			return false;
		}
		
		b.setAccepeted(1);
		d.setBalance(d.getBalance()+price);
		u.setBalance(u.getBalance()-price);
		b.getRide().setBetMinimum(b.getRide().getnPlaces()-b.getSeats());
		db.persist(b);
		db.getTransaction().commit();
		return true;
	}
	
	
	public void declineBooking (Integer bookingId, float price) {
		db.getTransaction().begin();
		Booking b = db.find(Booking.class, bookingId);
		User u = db.find(User.class, b.getUser().getEmail());
		Driver d = db.find(Driver.class, b.getDriver().getEmail());
		
		b.setAccepeted(2);
		
		db.persist(b);
		db.getTransaction().commit();
	}
	
	public float getBalanceByEmail(String email) {
		Driver d = db.find(Driver.class, email);
		if(d==null) {
			User u = db.find(User.class, email);
			return u.getBalance();
		}
		return d.getBalance();
	}
	
	public List<String> getStopNames() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT s.name FROM Stop s", String.class);
		List<String> stops = query.getResultList();
		return stops;
	}
	
	public void setStopBooking (Booking b, String s) {
		db.getTransaction().begin();
		Booking book = db.find(Booking.class, b.getBookingId());
		
		TypedQuery<Stop> query = db.createQuery("SELECT s FROM Stop s WHERE s.name =:s", Stop.class);
		query.setParameter("s", s);
		List<Stop> stops = query.getResultList();
		
		book.setStop(stops.get(0));
		db.persist(book);
		db.getTransaction().commit();
	}
	
	public List<Stop> getRideStops (Ride ride){
		Ride r = db.find(Ride.class, ride);
		List<Stop> l = r.getStops();
		return l;
	}
	
	public List<String> getStopsByOriginName(String origin){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT s.name FROM Stop s WHERE s.ride.from = :origin", String.class);
		query.setParameter("origin", origin);
		List<String> stops = query.getResultList();
		return stops;
	}
	
	public List<Ride> getRidesByStop(String from, String to, Date date){
		List<Ride> res = new ArrayList<>();	
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.stops.name=?2 AND r.date=?3",Ride.class);   
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
	 	 for (Ride ride:rides){
		   res.add(ride);
		  }
	 	return res;
	}
	
	public List<String> getStopsAndDestinations(String from){
		List<String> res = new ArrayList<String>();	
		int numStop = 0;
		
		TypedQuery<Ride> query1 = db.createQuery("SELECT r FROM Ride r WHERE r.stops.name=?1",Ride.class);
		TypedQuery<Ride> query2 = db.createQuery("SELECT r FROM Ride r WHERE r.from=?2",Ride.class);
		query1.setParameter(1, from);
		query2.setParameter(2, from);
		
		List<Ride> rides1 = query1.getResultList();
		List<Ride> rides2 = query2.getResultList();
		
		for(Ride r: rides1) {
			if(!res.contains(r.getTo())) {
				res.add(r.getTo());
			}
			
			for(Stop s: r.getStops()) {
				if(s.getName().equals(from)) {
					numStop = s.getNumStop();
				}
			}
			
			for(Stop s: r.getStops()) {
				if(s.getNumStop() > numStop) {
					if(!res.contains(s.getName())) {
						res.add(s.getName());
					}
				}
			}
			
			
		}
		
		for (Ride r: rides2) {
			if(!res.contains(r.getTo())) {
				res.add(r.getTo());
			}
			for(Stop s: r.getStops()) {
				if(!res.contains(s.getName())) {
					res.add(s.getName());
				}
			}
		}

		return res;
	}
	
	
	public List<Ride> getRidesByBothStops(String from, String to, Date date){
		TypedQuery<Ride> query = db.createQuery("SELECT DISTINCT r FROM Ride r JOIN r.stops s WHERE s.name IN (?1, ?2) and s.date=?3",Ride.class);   
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		return rides;
	}
}
