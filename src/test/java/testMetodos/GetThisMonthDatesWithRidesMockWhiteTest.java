package testMetodos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import domain.Driver;
import domain.Ride;
import domain.Stop;

public class GetThisMonthDatesWithRidesMockWhiteTest {

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

    @Test
    public void test1() throws ParseException {

    	String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Vitoria";
        String to = "Santander";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
        
        float price = (float) 20.00;

        try {
        	
			Driver d = new Driver(driverEmail, driverPassword, driverName);
			d.addRide(from, to, rideDate, 2, price);
			
			
			List<Date> resq1 = new ArrayList<Date>();
			List<Date> resq2 = new ArrayList<Date>();
			List<Date> resq3 = new ArrayList<Date>();
			
			resq1.add(d.getRides().get(0).getDate());
			
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query2 = mock(TypedQuery.class); 
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query3 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class)).thenReturn(query);
			Mockito.when(db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query2);
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query3);
			
			Mockito.when(query.getResultList()).thenReturn(resq1);
			Mockito.when(query2.getResultList()).thenReturn(resq2);
			Mockito.when(query3.getResultList()).thenReturn(resq3);

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);
            sut.close();
            
            System.out.println(res);

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

    }
    
    
    @Test
    public void test2() throws ParseException {
    	
    	String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Santander";
        String to = "Oviedo";
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
        
        float price = (float) 20.00;

        try {
        	
			Driver d = new Driver(driverEmail, driverPassword, driverName);
			d.addRide(from, to, rideDate, 2, price);
			List<Stop> sl = new ArrayList<Stop>();
			sl.add(new Stop(1, 1, d.getRides().get(0), "Gijón", price, rideDate));
			d.getRides().get(0).setStops(sl);
			
			List<Date> resq1 = new ArrayList<Date>();
			List<Date> resq2 = new ArrayList<Date>();
			List<Date> resq3 = new ArrayList<Date>();
			
			resq2.add(d.getRides().get(0).getDate());
			
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query2 = mock(TypedQuery.class); 
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query3 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class)).thenReturn(query);
			Mockito.when(db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query2);
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query3);
			
			Mockito.when(query.getResultList()).thenReturn(resq1);
			Mockito.when(query2.getResultList()).thenReturn(resq2);
			Mockito.when(query3.getResultList()).thenReturn(resq3);

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, "Gijón", rideDate);
            sut.close();
            
            System.out.println(res);

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
    }
    
    
    @Test
    public void test3() throws ParseException {

    	String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Santander";
        String to = "Oviedo";
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
        
        float price = (float) 20.00;

        try {
        	
			Driver d = new Driver(driverEmail, driverPassword, driverName);
			d.addRide(from, to, rideDate, 2, price);
			List<Stop> sl = new ArrayList<Stop>();
			sl.add(new Stop(1, 1, d.getRides().get(0), "Donostia", price, rideDate));
			sl.add(new Stop(2, 2, d.getRides().get(0), "Zarautz", price, rideDate));
			d.getRides().get(0).setStops(sl);
			
			
			List<Date> resq1 = new ArrayList<Date>();
			List<Date> resq2 = new ArrayList<Date>();
			List<Date> resq3 = new ArrayList<Date>();
			
			resq3.add(d.getRides().get(0).getDate());
			
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query2 = mock(TypedQuery.class); 
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query3 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class)).thenReturn(query);
			Mockito.when(db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query2);
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query3);
			
			Mockito.when(query.getResultList()).thenReturn(resq1);
			Mockito.when(query2.getResultList()).thenReturn(resq2);
			Mockito.when(query3.getResultList()).thenReturn(resq3);

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);
            sut.close();
            
            System.out.println(res);

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
    }

    
    @Test
    public void test4() throws ParseException {

        String from = "Donostia";
        String to = "Zarautz";
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
    	
        try {
        
 			List<Date> resq1 = new ArrayList<Date>();
 			List<Date> resq2 = new ArrayList<Date>();
 			List<Date> resq3 = new ArrayList<Date>();
 			
 			@SuppressWarnings("unchecked")
 			TypedQuery<Date> query = mock(TypedQuery.class);
 			@SuppressWarnings("unchecked")
 			TypedQuery<Date> query2 = mock(TypedQuery.class); 
 			@SuppressWarnings("unchecked")
 			TypedQuery<Date> query3 = mock(TypedQuery.class); 
 			
 			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class)).thenReturn(query);
 			Mockito.when(db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query2);
 			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query3);
 			
 			Mockito.when(query.getResultList()).thenReturn(resq1);
 			Mockito.when(query2.getResultList()).thenReturn(resq2);
 			Mockito.when(query3.getResultList()).thenReturn(resq3);

             sut.open();
             List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);
             sut.close();
             
             System.out.println(res);

             if (res.isEmpty()) {
             	assertTrue(true);
             }
             else {
             	fail();
             }

         } catch (Exception e) {
             e.printStackTrace();
             fail();
         }  
    }

    
    @Test
    public void test5() throws ParseException {
    	
    	String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Donostia";
        String to = "Zarautz";
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Date searchDate = sdf1.parse("20/09/2026");
        
        
        float price = (float) 20.00;

        try {
        	
			Driver d = new Driver(driverEmail, driverPassword, driverName);
			d.addRide(from, to, rideDate, 2, price);
			
			
			List<Date> resq1 = new ArrayList<Date>();
			List<Date> resq2 = new ArrayList<Date>();
			List<Date> resq3 = new ArrayList<Date>();
			
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query2 = mock(TypedQuery.class); 
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query3 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class)).thenReturn(query);
			Mockito.when(db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query2);
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query3);
			
			Mockito.when(query.getResultList()).thenReturn(resq1);
			Mockito.when(query2.getResultList()).thenReturn(resq2);
			Mockito.when(query3.getResultList()).thenReturn(resq3);

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);
            sut.close();
            
            System.out.println(res);

            if (res.isEmpty()) {
            	assertTrue(true);
            }
            else {
            	fail();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }  
    }

    
    @Test
    public void test6() throws ParseException {
    	String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1235";
        String from = "Donostia";
        String to = "Zarautz";
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Date searchDate = sdf1.parse("20/09/2026");
        
        
        float price = (float) 20.00;

        try {
        	
			Driver d = new Driver(driverEmail, driverPassword, driverName);
			d.addRide(from, to, rideDate, 2, price);
			
			
			List<Date> resq1 = new ArrayList<Date>();
			List<Date> resq2 = new ArrayList<Date>();
			List<Date> resq3 = new ArrayList<Date>();
			
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query = mock(TypedQuery.class);
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query2 = mock(TypedQuery.class); 
			@SuppressWarnings("unchecked")
			TypedQuery<Date> query3 = mock(TypedQuery.class); 
			
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class)).thenReturn(query);
			Mockito.when(db.createQuery("SELECT r.date FROM Ride r WHERE (r.from=?1 OR r.to=?2) AND (r.stops.name=?3 OR r.stops.name=?6) AND r.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query2);
			Mockito.when(db.createQuery("SELECT DISTINCT r.date FROM Ride r JOIN r.stops s WHERE s.name IN (?2, ?3) and s.date BETWEEN ?4 AND ?5",Date.class)).thenReturn(query3);
			
			Mockito.when(query.getResultList()).thenReturn(resq1);
			Mockito.when(query2.getResultList()).thenReturn(resq2);
			Mockito.when(query3.getResultList()).thenReturn(resq3);

            sut.open();
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);
            sut.close();
            
            System.out.println(res);

            if (res.isEmpty()) {
            	assertTrue(true);
            }
            else {
            	fail();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }  
    }

}
