package testOperations;

import java.util.Date;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;

public class TestBusinessLogic {
	TestDataAccess dbManagerTest;
 	
    
	   public TestBusinessLogic()  {
			
			System.out.println("Creating TestBusinessLogic instance");
			ConfigXML c=ConfigXML.getInstance();
			dbManagerTest=new TestDataAccess(); 
			dbManagerTest.close();
		}
		
		 
		public boolean removeDriver(String driverEmail) {
			dbManagerTest.open();
			boolean b=dbManagerTest.removeDriver(driverEmail);
			dbManagerTest.close();
			return b;

		}
		
		public Driver createDriver(String email,String password, String name) {
			dbManagerTest.open();
			Driver driver=dbManagerTest.createDriver(email, password, name);
			dbManagerTest.close();
			return driver;

		}
		
		public boolean existDriver(String email) {
			dbManagerTest.open();
			boolean existDriver=dbManagerTest.existDriver(email);
			dbManagerTest.close();
			return existDriver;

		}
		
		public Driver addDriverWithRide(String email, String password, String name, String from, String to,  Date date, int nPlaces, float price) {
			dbManagerTest.open();
			Driver driver=dbManagerTest.addDriverWithRide(email, password, name, from, to, date, nPlaces, price);
			dbManagerTest.close();
			return driver;

		}
		public boolean existRide(String email, String from, String to, Date date) {
			dbManagerTest.open();
			boolean b=dbManagerTest.existRide(email, from, to, date);
			dbManagerTest.close();
			return b;
		}
		public Ride removeRide(String email,String from, String to, Date date ) {
			dbManagerTest.open();
			Ride r=dbManagerTest.removeRide( email, from,  to,  date );
			dbManagerTest.close();
			return r;
		}
		
		public boolean addStopToRide(Ride ride, String name, float price, int num) {
			dbManagerTest.open();
			boolean b = dbManagerTest.addStopToRide(ride, name, price, num);
			dbManagerTest.close();
			return b;
		}
		
		public boolean removeStop(Ride r) {
			dbManagerTest.open();
			boolean b = dbManagerTest.removeStop(r);
			dbManagerTest.close();
			return b;
		}
		


}
