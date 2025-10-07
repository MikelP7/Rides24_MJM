package testMetodos;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.*;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class GetStopsAndDestinationsMockWhiteTest {
	static DataAccess sut;
	
	protected MockedStatic <Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);		
    }
	
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	Driver driver;
	
	@Test
	public void test1() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
					
			 driver = new Driver(driverEmail,driverPassword,driverName);
			 
			 driver.addRide("Don", "Bil", rideDate, 2, price);
			 driver.addRide("Bil", "Gas", rideDate, 3, price);
			 driver.addRide("Don", "Gas", rideDate, 4, price);
			 
			 List<Stop> sl1 = new ArrayList<Stop>(); 
			 List<Stop> sl2 = new ArrayList<Stop>(); 
			 List<Stop> sl3 = new ArrayList<Stop>(); 
			 
			 sl1.add(new Stop(1, 1, driver.getRides().get(0), "Tol", price, rideDate));
			 sl1.add(new Stop(2, 2, driver.getRides().get(0), "Vil", price, rideDate));
			 
			 sl2.add(new Stop(3, 1, driver.getRides().get(1), "Don", price, rideDate));
			 sl2.add(new Stop(4, 2, driver.getRides().get(1), "Iru", price, rideDate));
			 sl2.add(new Stop(5, 3, driver.getRides().get(1), "Tol", price, rideDate));
			 
			 sl3.add(new Stop(6, 1, driver.getRides().get(2), "Don", price, rideDate));
			 sl3.add(new Stop(7, 2, driver.getRides().get(2), "Iru", price, rideDate));
			 sl3.add(new Stop(8, 3, driver.getRides().get(2), "Tol", price, rideDate));
			 
			 driver.getRides().get(0).setStops(sl1);
			 driver.getRides().get(1).setStops(sl2);
			 driver.getRides().get(2).setStops(sl3);
			 
			 
			//configure the state through mocks  
			String from = "Don";
			
			List<Ride> resq1 = new ArrayList<Ride>();
			List<Ride> resq2 = new ArrayList<Ride>();
			
			resq1.add(driver.getRides().get(1));
			resq1.add(driver.getRides().get(2));
			resq2.add(driver.getRides().get(0));
			resq2.add(driver.getRides().get(2));
			
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query1 = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query2 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.stops.name=?1", Ride.class)).thenReturn(query1);
	        Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.from=?2", Ride.class)).thenReturn(query2);
	        
	        Mockito.when(query1.getResultList()).thenReturn(resq1);
	        Mockito.when(query2.getResultList()).thenReturn(resq2);
			
	        
			//invoke System Under Test (sut)  
			sut.open();
		    res = sut.getStopsAndDestinations(from);
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
			   fail();
		   }
		
	} 
	
	
	@Test
	public void test2() {
		
		List<String> res = new ArrayList<String>();
		
		try {
			//configure the state through mocks  
			
			String from = "Bil";
			
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query1 = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query2 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.stops.name=?1", Ride.class)).thenReturn(query1);
	        Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.from=?2", Ride.class)).thenReturn(query2);
	        
	        Mockito.when(query1.getResultList()).thenReturn(new ArrayList<Ride>());
	        Mockito.when(query2.getResultList()).thenReturn(new ArrayList<Ride>());
			
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
	public void test3() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
					
			 driver = new Driver(driverEmail,driverPassword,driverName);
			 
			 driver.addRide("Don", "Bil", rideDate, 2, price);
			 
			 List<Stop> sl1 = new ArrayList<Stop>(); 
			 
			 sl1.add(new Stop(1, 1, driver.getRides().get(0), "Tol", price, rideDate));
			 sl1.add(new Stop(2, 2, driver.getRides().get(0), "Vil", price, rideDate));
			 
			 driver.getRides().get(0).setStops(sl1);
			 
			//configure the state through mocks  
			String from = "Don";
			
			List<Ride> resq1 = new ArrayList<Ride>();
			List<Ride> resq2 = new ArrayList<Ride>();
			
			resq2.add(driver.getRides().get(0));
			
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query1 = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query2 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.stops.name=?1", Ride.class)).thenReturn(query1);
	        Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.from=?2", Ride.class)).thenReturn(query2);
	        
	        Mockito.when(query1.getResultList()).thenReturn(resq1);
	        Mockito.when(query2.getResultList()).thenReturn(resq2);
			
	        
			//invoke System Under Test (sut)  
			sut.open();
		    res = sut.getStopsAndDestinations(from);
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
			   fail();
		   }
		
	} 
	
	
	@Test
	public void test4() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
					
			 driver = new Driver(driverEmail,driverPassword,driverName);
			 
			 driver.addRide("Bil", "Gas", rideDate, 3, price);
			 
			 List<Stop> sl2 = new ArrayList<Stop>(); 
			 
			 sl2.add(new Stop(1, 1, driver.getRides().get(0), "Don", price, rideDate));
			 sl2.add(new Stop(2, 2, driver.getRides().get(0), "Iru", price, rideDate));
			 sl2.add(new Stop(3, 3, driver.getRides().get(0), "Tol", price, rideDate));
			 
			 driver.getRides().get(0).setStops(sl2);

			//configure the state through mocks  
			String from = "Don";
			
			List<Ride> resq1 = new ArrayList<Ride>();
			List<Ride> resq2 = new ArrayList<Ride>();
			
			resq1.add(driver.getRides().get(0));
			
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query1 = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query2 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.stops.name=?1", Ride.class)).thenReturn(query1);
	        Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.from=?2", Ride.class)).thenReturn(query2);
	        
	        Mockito.when(query1.getResultList()).thenReturn(resq1);
	        Mockito.when(query2.getResultList()).thenReturn(resq2);
			
	        
			//invoke System Under Test (sut)  
			sut.open();
		    res = sut.getStopsAndDestinations(from);
			sut.close();
			
			System.out.println(res);
			
			if(res.contains("Tol") && res.contains("Iru") && res.contains("Gas")) {
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
	public void test5() {
		
		String driverEmail="driverTest1@gmail.com";
		String driverName="DriverTest1";
		String driverPassword="test1";
		
		Date rideDate = Date.valueOf("2025-10-10");
		
		float price = (float) 20.50;
		
		List<String> res = new ArrayList<String>();
		
		try {
					
			 driver = new Driver(driverEmail,driverPassword,driverName);
			 
			 driver.addRide("Don", "Bil", rideDate, 2, price);
			 
			//configure the state through mocks  
			String from = "Don";
			
			List<Ride> resq1 = new ArrayList<Ride>();
			List<Ride> resq2 = new ArrayList<Ride>();
			
			resq2.add(driver.getRides().get(0));
			
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query1 = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Ride> query2 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.stops.name=?1", Ride.class)).thenReturn(query1);
	        Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.from=?2", Ride.class)).thenReturn(query2);
	        
	        Mockito.when(query1.getResultList()).thenReturn(resq1);
	        Mockito.when(query2.getResultList()).thenReturn(resq2);
			
	        
			//invoke System Under Test (sut)  
			sut.open();
		    res = sut.getStopsAndDestinations(from);
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
			   fail();
		   }
		
	} 
	
}
