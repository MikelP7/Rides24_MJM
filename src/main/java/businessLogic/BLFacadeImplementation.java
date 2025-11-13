package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Ride;
import domain.Stop;
import domain.User;
import domain.Booking;
import domain.Driver;
import iterator.*;
import exceptions.RideMustBeLaterThanTodayException;
import iterator.ExtendedIterator;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager = new DataAccess();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    @WebMethod public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	@WebMethod public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail, List<Vector<String>> sl ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail, sl);		
		dbManager.close();
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	
	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}
	
	public Driver loginDriver(String username, String password) {
		dbManager.open();
		Driver b = dbManager.loginDriver(username,password);
		dbManager.close();
		return b;
	}
	
	public User loginUser(String username, String password) {
		dbManager.open();
		User u = dbManager.loginUser(username,password);
		dbManager.close();
		return u;
	}
	
	public boolean registerDriver(String username, String email, String password) {
			dbManager.open();
			boolean b = dbManager.registerDriver(username,email,password);
			dbManager.close();
			return b;
	}
	
	
	public boolean registerUser(String username, String email, String password) {
			dbManager.open();
			boolean b = dbManager.registerUser(username,email,password);
			dbManager.close();
			return b;
	}
	
	public boolean addRideUser(User user, Integer rideNum, Integer seats) {
		dbManager.open();
		boolean b = dbManager.addRideUser(user, rideNum, seats);
		dbManager.close();
		return b;
	}
	
	public List<Ride> getRidesByEmail(String email, String type){
		dbManager.open();
		List<Ride> l = dbManager.getRidesByEmail(email,type);
		dbManager.close();
		return l;
	}
	
	public List<Booking> getBookingByEmail (String email, String type){
		dbManager.open();
		List<Booking> l = dbManager.getBookingByEmail(email, type);
		dbManager.close();
		return l;
	}
	
	public boolean addBooking(User user, Ride ride, Integer numSeats) {
		dbManager.open();
		boolean b = dbManager.addBooking(user, ride, numSeats);
		dbManager.close();
		return b;
	}
	
	public boolean addStopToRide(Ride ride, String name, float price, int num) {
		dbManager.open();
		boolean b = dbManager.addStopToRide(ride, name, price, num);
		dbManager.close();
		return b;
	}
	
	public Ride findRide(Integer id) {
		dbManager.open();
		Ride r = dbManager.findRide(id);
		dbManager.close();
		return r;
	}
	
	public boolean acceptBooking (Integer bookingId, float price) {
		dbManager.open();
		boolean b = dbManager.acceptBooking(bookingId, price);
		dbManager.close();
		return b;
	}
	
	public void declineBooking (Integer bookingId, float price) {
		dbManager.open();
		dbManager.declineBooking(bookingId, price);
		dbManager.close();
	}
	
	public float getBalanceByEmail(String email) {
		dbManager.open();
		float b = dbManager.getBalanceByEmail(email);
		dbManager.close();
		return b;
	}
	
	public List<String> getStopNames () {
		dbManager.open();
		List<String> l = dbManager.getStopNames();
		dbManager.close();
		return l;
	}
	
	public void setStopBooking (Booking b, String s) {
		dbManager.open();
		dbManager.setStopBooking(b,s);
		dbManager.close();
	}
	
	public List<Stop> getRideStops (Ride ride){
		dbManager.open();
		List<Stop> l = dbManager.getRideStops(ride);
		dbManager.close();
		return l;
	}
	
	public List<String> getStopsByOriginName(String origin){
		dbManager.open();
		List<String> l = dbManager.getStopsByOriginName(origin);
		dbManager.close();
		return l;
	}

	
	public List<Ride> getRidesByStop(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRidesByStop(from, to, date);
		dbManager.close();
		return rides;
	}
	
	public List<String> getStopsAndDestinations(String from){
		dbManager.open();
		List<String> names = dbManager.getStopsAndDestinations(from);
		dbManager.close();
		return names;
	}
	
	public List<Ride> getRidesByBothStops(String from, String to, Date date){
		dbManager.open();
		List<Ride> rides = dbManager.getRidesByBothStops(from, to, date);
		dbManager.close();
		return rides;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}

	@Override
	public ExtendedIterator<String> getDepartingCitiesIterator() {
		List<String> cities = this.getDepartCities();
		return new DepartingCitiesIterator(cities);
	}
    

}

