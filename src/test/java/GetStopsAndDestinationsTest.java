import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.*;
import testOperations.TestDataAccess;

public class GetStopsAndDestinationsTest {
	
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();
	
	@Test
	public void testCB1() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r1 = new Ride("Don", "Bil", rideDate, 4, price, d);
			Ride r2 = new Ride("Bil", "Gas", rideDate, 4, price, d); 
			Ride r3 = new Ride("Don", "Gas", rideDate, 4, price, d); 
			
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addRideToDriver(driverEmail, r2);
			testDA.addRideToDriver(driverEmail, r3);
			
			testDA.addStopToRide(r1, "Tol", price, 1);
			testDA.addStopToRide(r1, "Vil", price, 2);
			
			testDA.addStopToRide(r2, "Don", price, 1);
			testDA.addStopToRide(r2, "Iru", price, 2);
			testDA.addStopToRide(r2, "Tol", price, 3);
			
			testDA.addStopToRide(r3, "Don", price, 1);
			testDA.addStopToRide(r3, "Iru", price, 2);
			testDA.addStopToRide(r3, "Tol", price, 3);
			
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
				
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}
	} 
	
	
	@Test
	public void testCB2() {
		
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
	
	
	@Test
	public void testCB3() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r1 = new Ride("Don", "Bil", rideDate, 4, price, d);

			
			testDA.addRideToDriver(driverEmail, r1);

			
			testDA.addStopToRide(r1, "Tol", price, 1);
			testDA.addStopToRide(r1, "Vil", price, 2);
			
			testDA.close();			
			
			sut.open();
		    res = sut.getStopsAndDestinations("Don");
			sut.close();
			System.out.println(res);
			
			if(res.contains("Tol") && res.contains("Bil") && res.contains("Vil")) {
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
				
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}
	} 
	
	
	
	@Test
	public void testCB4() {
		

		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");;
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r2 = new Ride("Bil", "Gas", rideDate, 4, price, d); 
			
			testDA.addRideToDriver(driverEmail, r2);
			
			testDA.addStopToRide(r2, "Don", price, 1);
			testDA.addStopToRide(r2, "Iru", price, 2);
			testDA.addStopToRide(r2, "Tol", price, 3);

			testDA.close();			
			
			sut.open();
		    res = sut.getStopsAndDestinations("Don");
			sut.close();
			System.out.println(res);
			
			if(res.contains("Iru") && res.contains("Tol")) {
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
				
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}

	}
	
	
	
	
	@Test
	public void testCB5() {
		

		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r4 = new Ride("Don", "Bil", rideDate, 4, price, d);
			
			testDA.addRideToDriver(driverEmail, r4);

			testDA.close();			
			
			sut.open();
		    res = sut.getStopsAndDestinations("Don");
			sut.close();
			System.out.println(res);
			
			if(res.contains("Bil")) {
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
				
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}

	}
	
	
	@Test
	public void testCN1() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r1 = new Ride("Don", "Bil", rideDate, 4, price, d);
			Ride r2 = new Ride("Bil", "Gas", rideDate, 4, price, d); 
			Ride r3 = new Ride("Don", "Gas", rideDate, 4, price, d); 
			
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addRideToDriver(driverEmail, r2);
			testDA.addRideToDriver(driverEmail, r3);
			
			testDA.addStopToRide(r1, "Tol", price, 1);
			testDA.addStopToRide(r1, "Vil", price, 2);
			
			testDA.addStopToRide(r2, "Don", price, 1);
			testDA.addStopToRide(r2, "Iru", price, 2);
			testDA.addStopToRide(r2, "Tol", price, 3);
			
			testDA.addStopToRide(r3, "Don", price, 1);
			testDA.addStopToRide(r3, "Iru", price, 2);
			testDA.addStopToRide(r3, "Tol", price, 3);
			
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
				
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}
	} 
	
	
	@Test
	public void testCN2() {
		List<String> res = new ArrayList<String>();
		
		try {		
			sut.open();
		    sut.getStopsAndDestinations(null);
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
	
	@Test
	public void testCN3() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
			
			testDA.open();
			
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			
			Ride r1 = new Ride("Don", "Bil", rideDate, 4, price, d);
			
			testDA.addRideToDriver(driverEmail, r1);

			testDA.addStopToRide(r1, "Tol", price, 1);
			testDA.addStopToRide(r1, "Vil", price, 2);
			
			testDA.close();			
			
			sut.open();
		    res = sut.getStopsAndDestinations("");
			sut.close();
			System.out.println(res);
			
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
		
		finally {
				testDA.open();
				
				testDA.removeDriver(driverEmail);
				
		        testDA.close();
		}
	} 
	
	
	@Test
	public void testCN4() {
		List<String> res = new ArrayList<String>();
		
		try {		
			sut.open();
		    sut.getStopsAndDestinations("Don");
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
