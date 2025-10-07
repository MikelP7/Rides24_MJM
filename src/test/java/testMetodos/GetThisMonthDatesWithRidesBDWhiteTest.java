package testMetodos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import java.util.Calendar;
import java.util.Date;
import java.util.List;



import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import testOperations.TestDataAccess;

public class GetThisMonthDatesWithRidesBDWhiteTest {

    // sut: System Under Test
    static DataAccess sut = new DataAccess();

    // operaciones auxiliares para preparar y limpiar la BD
    static TestDataAccess testDA = new TestDataAccess();
    
    @Test
    public void test1() {

        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Vitoria";
        String to = "Santander";

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();
        
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
    public void test2() {

        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Santander";
        String to = "Oviedo";

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();
        
        float price = (float) 20.00;

        try {
        	
        	testDA.open();
        	
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addStopToRide(r1, "Gijón", price, 1);

            testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides("Gijón", to, rideDate);
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
    public void test3() {
    	
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Santander";
        String to = "Oviedo";
        String stop1Name = "Donostia";
        String stop2Name = "Zarautz";

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();
        
        float price = (float) 20.00;

        try {

        	testDA.open();
        	
			Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
			Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addStopToRide(r1, "Donostia", price, 1);
			testDA.addStopToRide(r1, "Zarautz", price, 2);

            testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(stop1Name, stop2Name, rideDate);
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
    public void test4() {

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();
        
        try {

        	sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides("Huelva", "Granada", rideDate);
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
    public void test5() {

        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Ronda";
        String to = "Burgos";

        Calendar calRide = Calendar.getInstance();
        calRide.set(2026, Calendar.SEPTEMBER, 20, 0, 0, 0);
        calRide.set(Calendar.MILLISECOND, 0);
        Date rideDate = calRide.getTime();

        Calendar calSearch = Calendar.getInstance();
        calSearch.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        calSearch.set(Calendar.MILLISECOND, 0);
        Date searchDate = calSearch.getTime();
        
        float price = (float) 20.00;

        try {
        	testDA.open();
        	
        	Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
        	Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);

			testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);
            sut.close();
            
            if (res.isEmpty()) {
            	assertTrue(true);
            }
            else {
            	fail();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("No se esperaba excepción");
        } finally {
            testDA.open();
            testDA.removeDriver(driverEmail);
            testDA.close();
        }
    }

    
    @Test
    public void test6() {

        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Sevilla";
        String to = "Barcelona";

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date searchDate = cal.getTime();

        cal.set(2026, Calendar.SEPTEMBER, 15, 0, 0, 0); 
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        float price = (float) 20.00;	
        
        try {

        	testDA.open();

        	Driver d = testDA.createDriver(driverEmail, driverPassword, driverName);
        	Ride r1 = new Ride(from, to, rideDate, 4, price, d);
			testDA.addRideToDriver(driverEmail, r1);
			testDA.addStopToRide(r1, "Sevilla", price, 1);
        	
        	testDA.close();

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);
            sut.close();
            
            if (res.isEmpty()) {
            	assertTrue(true);
            }
            else {
            	fail();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Excepción inesperada: " + e.getMessage());
        } finally {
            testDA.open();
            testDA.removeDriver(driverEmail);
            testDA.close();
        }
    }
    
}
