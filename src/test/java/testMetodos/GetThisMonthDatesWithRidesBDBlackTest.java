package testMetodos;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void test1() {

        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPassword = "1234";
        String from = "A";
        String to = "B";

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
	    DataAccess sut = new DataAccess();
	    TestDataAccess testDA = new TestDataAccess();

	    // Datos de prueba
	    String driverEmail = "driver@gmail.com";
	    String driverName = "Driver";
	    String driverPass = "1234";
	    String from = "C";
	    String to = "D";

	    // Fecha de prueba sin horas, minutos ni segundos
	    Calendar cal = Calendar.getInstance();
	    cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date rideDate = cal.getTime();

	    try {
	        sut.open();

	        // Crear driver si no existe
	        Driver driver = sut.loginDriver(driverName, driverPass);
	        if (driver == null) {
	            sut.registerDriver(driverName, driverEmail, driverPass);
	            driver = sut.loginDriver(driverName, driverPass);
	        }

	        // Crear viaje A→C donde B es parada
	        List<Vector<String>> stops = new ArrayList<>();
	        Vector<String> stopB = new Vector<>();
	        stopB.add("D");  // parada intermedia
	        stopB.add("1");  // número de parada
	        stopB.add("0");  // precio ficticio
	        stops.add(stopB);

	        sut.createRide("C", "E", rideDate, 4, 15.0f, driverEmail, stops);

	        // Llamada al método
	        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

	        // Comprobaciones: lista solo contiene fechas de Q2
	        assertNotNull("La lista de fechas no debe ser null", res);
	        assertTrue("Debe contener la fecha del viaje con parada", res.contains(rideDate));

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Limpieza
	        testDA.open();
	        if (testDA.existRide(driverEmail, "C", "E", rideDate)) {
	            testDA.removeRide(driverEmail, "C", "E", rideDate);
	        }
	        if (testDA.existDriver(driverEmail)) {
	            testDA.removeDriver(driverEmail);
	        }
	        testDA.close();

	        sut.close();
	    }
	}
	

	@Test
	public void testCN3() {
	    DataAccess sut = new DataAccess();
	    TestDataAccess testDA = new TestDataAccess();

	    // Datos de prueba
	    String driverEmail = "driverq3@gmail.com";
	    String driverName = "DriverQ3";
	    String driverPass = "1234";

	    String from = "F";
	    String to = "G";

	    // Fecha de prueba sin horas, minutos ni segundos
	    Calendar cal = Calendar.getInstance();
	    cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date rideDate = cal.getTime();

	    try {
	        sut.open();

	        // Crear driver si no existe
	        Driver driver = sut.loginDriver(driverName, driverPass);
	        if (driver == null) {
	            sut.registerDriver(driverName, driverEmail, driverPass);
	            driver = sut.loginDriver(driverName, driverPass);
	        }

	        // Crear viaje H→I donde F y G son paradas intermedias
	        List<Vector<String>> stops = new ArrayList<>();

	        Vector<String> stopF = new Vector<>();
	        stopF.add("F"); // parada intermedia origen buscado
	        stopF.add("1");
	        stopF.add("0");
	        stops.add(stopF);

	        Vector<String> stopG = new Vector<>();
	        stopG.add("G"); // parada intermedia destino buscado
	        stopG.add("2");
	        stopG.add("0");
	        stops.add(stopG);

	        sut.createRide("H", "I", rideDate, 4, 20.0f, driverEmail, stops);

	        // Llamada al método
	        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

	        // Comprobaciones: lista solo contiene fechas de Q3
	        assertNotNull("La lista de fechas no debe ser null", res);
	        assertTrue("Debe contener la fecha del viaje con ambas paradas", res.contains(rideDate));

	    } catch (Exception e) {
	        e.printStackTrace();
	        fail("Excepción inesperada: " + e.getMessage());
	    } finally {
	        // Limpieza de datos de prueba
	        testDA.open();
	        if (testDA.existRide(driverEmail, "H", "I", rideDate)) {
	            testDA.removeRide(driverEmail, "H", "I", rideDate);
	        }
	        if (testDA.existDriver(driverEmail)) {
	            testDA.removeDriver(driverEmail);
	        }
	        testDA.close();

	        sut.close();
	    }
	}

	@Test
	public void testCN4() {
	    DataAccess sut = new DataAccess();
	    TestDataAccess testDA = new TestDataAccess();

	    String from = "J";
	    String to = "K";

	    // Fecha de prueba sin horas, minutos ni segundos
	    Calendar cal = Calendar.getInstance();
	    cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date date = cal.getTime();

	    try {
	        sut.open();

	        // No insertamos ningún viaje en la BD
	        // Simplemente llamamos al método para que devuelva []

	        List<Date> res = sut.getThisMonthDatesWithRides(from, to, date);

	        // Comprobación: lista vacía
	        assertNotNull("La lista no debe ser null", res);
	        assertTrue("La lista debe estar vacía al no existir rutas", res.isEmpty());

	    } catch (Exception e) {
	        e.printStackTrace();
	        fail("Excepción inesperada: " + e.getMessage());
	    } finally {
	        sut.close();
	    }
	}

	@Test
	public void testCN5() {
	    DataAccess sut = new DataAccess();

	    String from = null;
	    String to = "L";

	    Calendar cal = Calendar.getInstance();
	    cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date date = cal.getTime();

	    try {
	        sut.open();

	        // Este método debe lanzar NullPointerException al usar from = null
	        sut.getThisMonthDatesWithRides(from, to, date);

	    } finally {
	        sut.close();
	        assertTrue(true);
	    }
	}

	@Test
	public void testCN6() {
	    DataAccess sut = new DataAccess();

	    String from = "M";
	    String to = null;

	    Calendar cal = Calendar.getInstance();
	    cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date date = cal.getTime();

	    try {
	        sut.open();
	        sut.getThisMonthDatesWithRides(from, to, date);
	    } finally {
	        sut.close();
	        assertTrue(true);
	    }
	}

	@Test(expected = NullPointerException.class)
	public void testCN7() {
	    DataAccess sut = new DataAccess();

	    String from = "N";
	    String to = "O";
	    Date date = null;

	    try {
	        sut.open();
	        sut.getThisMonthDatesWithRides(from, to, date);
	    } finally {
	        sut.close();
	    }
	}

	@Test
	public void testCN8() {
	    DataAccess sut = new DataAccess();

	    String from = "A";
	    String to = "A"; // from = to
	    Calendar cal = Calendar.getInstance();
	    cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date date = cal.getTime();

	    List<Date> result = null;

	    try {
	        sut.open();
	        result = sut.getThisMonthDatesWithRides(from, to, date);
	    } finally {
	        sut.close();
	    }

	    assertNotNull("La lista devuelta no debe ser null", result);
	    assertTrue("La lista debe estar vacía cuando no hay viajes con from=to", result.isEmpty());
	}

}
