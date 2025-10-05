import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.*;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;

public class GetStopsAndDestinationsTest {
	
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	private Driver driver; 
	
	@Test
	public void test1() throws ParseException {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = Date.valueOf("2025-10-10");;
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r1 = new Ride("Don", "Bil", rideDate, 4, price, d);
			Ride r2 = new Ride("Bil", "Gas", rideDate, 4, price, d); 
			
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addRideToDriver(driverEmail, r2);
			
			testDA.close();
			
			testDA.open();
			
			testDA.addStopToRide(r1, "Tol", price, 1);
			testDA.addStopToRide(r1, "Vil", price, 2);
			
			testDA.addStopToRide(r2, "Don", price, 2);
			testDA.addStopToRide(r2, "Iru", price, 3);
			testDA.addStopToRide(r2, "Tol", price, 4);
			
			testDA.close();			
			
			sut.open();
		    res = sut.getStopsAndDestinations("Don");
			sut.close();
			System.out.println(res);
			
			if(res.contains("Tol") && res.contains("Bil") && res.contains("Gas") && res.contains("Vil") && res.contains("Iru")) {
				assertTrue(true);
			}
			else {
				fail();
			}
			
		} 
		catch (Exception e) {
			System.out.println(e.toString());
			fail();
		} 
		
		finally {
				testDA.open();
				
				testDA.removeRide(driverEmail, "Don", "Bil", rideDate);
				testDA.removeRide(driverEmail, "Bil", "Gas", rideDate);
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}
	} 
	
	@Test
	public void test2() throws ParseException {
		
		List<String> res = new ArrayList<String>();
		
		try {
			sut.open();
		    res = sut.getStopsAndDestinations("Bil");
			sut.close();
			
			if(res.isEmpty()) {
				assertTrue(true);
			}
			else {
				fail();
			}
			
		} 
		catch (Exception e) {
			System.out.println(e.toString());
			fail();
		} 
	}
}
