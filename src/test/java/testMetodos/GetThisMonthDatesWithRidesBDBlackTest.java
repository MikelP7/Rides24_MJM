package testMetodos;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Stop;
import testOperations.TestDataAccess;

public class GetThisMonthDatesWithRidesBDBlackTest {

    // sut: System Under Test
    static DataAccess sut = new DataAccess();

    // operaciones auxiliares para preparar y limpiar la BD
    static TestDataAccess testDA = new TestDataAccess();
    
    @Test
    public void test1() throws ParseException {

        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1234";
        String from = "A";
        String to = "B";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
        
        float price = (float) 20.00;

        try {
        	
        	testDA.open();
        	
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);
            
			testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);
            sut.close();

            if (res.contains(rideDate)) {
            	assertTrue(true);
            }
            else {
            	fail();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } 
        finally {
            testDA.open();
            testDA.removeDriver(driverEmail);
            testDA.close();

        }
    }

    
	@Test
	public void test2() throws ParseException {

	    String driverEmail = "driver@gmail.com";
	    String driverName = "Driver";
	    String driverPassword = "1234";
	    String from = "A";
	    String to = "C";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
	    
	    float price = (float) 20.00;

	    try {
	        sut.open();

        	testDA.open();
        	
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);
            testDA.addStopToRide(r1, "B", price, 1);
			
			testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);
            sut.close();

	        if (res.contains(rideDate)) {
	        	assertTrue(true);
	        }
	        else {
	        	fail();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    } finally {
	        testDA.open();
	        testDA.removeDriver(driverEmail);
	        testDA.close();
	    }
	}
	

	@Test
	public void test3() throws ParseException {

	    String driverEmail = "driverq3@gmail.com";
	    String driverName = "DriverQ3";
	    String driverPassword = "1234";
	    String from = "C";
	    String to = "D";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
	    
	    float price = (float) 20.00;

	    try {
	        sut.open();

        	testDA.open();
        	
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addStopToRide(r1, "A", price, 2);
            testDA.addStopToRide(r1, "B", price, 2);
			
			testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);
            sut.close();

            if (res.contains(rideDate)) {
	        	assertTrue(true);
	        }
	        else {
	        	fail();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        fail();
	        
	    } finally {
	        testDA.open();
	        testDA.removeDriver(driverEmail);
	        testDA.close();
	    }
	}

	
	@Test
	public void test4() throws ParseException {

	    String from = "A";
	    String to = "B";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("05/10/2026");

	    try {
	        sut.open();
	        List<Date> res = sut.getThisMonthDatesWithRides(from, to, date);
	        sut.close();
	        
	        if (res.isEmpty()) {
	        	assertTrue(true);
	        }
	        else {
	        	fail();
	        }

	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    }
	}

	@Test
	public void test5() throws ParseException {

	    String from = null;
	    String to = "B";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("05/10/2026");
	    try {
	        sut.open();
	        List<Date> res = sut.getThisMonthDatesWithRides(from, to, date);
	        sut.close();
	        
	        if (res.isEmpty()) {
	        	assertTrue(true);
	        }
	        else {
	        	fail();
	        }

	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    }
	}

	@Test
	public void test6() throws ParseException {

	    String from = "A";
	    String to = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("05/10/2026");

	    try {
	        sut.open();
	        List<Date> res = sut.getThisMonthDatesWithRides(from, to, date);
	        sut.close();
	        
	        if (res.isEmpty()) {
	        	assertTrue(true);
	        }
	        else {
	        	fail();
	        }

	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    }
	}

	
	@Test
	public void test7() {

	    String from = "A";
	    String to = "B";
	    Date date = null;

	    try {
	        sut.open();
	        sut.getThisMonthDatesWithRides(from, to, date);
	        sut.close();
	        fail();
	    }
	    catch (NullPointerException e) {
	        assertTrue(true);
	    }
	}

	@Test
	public void test8() throws ParseException {

	    String from = "A";
	    String to = "A"; 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("05/10/2026");

	    List<Date> res = null;

	    try {
	        sut.open();
	        res = sut.getThisMonthDatesWithRides(from, to, date);
	        sut.close();
	        
	        if (res.isEmpty()) {
	        	assertTrue(true);
	        }
	        else {
	        	fail();
	        }

	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    }
	}
}
