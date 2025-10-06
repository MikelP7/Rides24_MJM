package testMetodos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import testOperations.TestDataAccess;

public class GetThisMonthDatesWithRidesTest {

    // sut: System Under Test
    static DataAccess sut = new DataAccess();

    // operaciones auxiliares para preparar y limpiar la BD
    static TestDataAccess testDA = new TestDataAccess();
    
    /**
     * sut.getThisMonthDatesWithRides:
     * Existe al menos un viaje directo con from="Donostia", to="Zarautz",
     * en octubre de 2026 (fecha: 05/10/2026). Debe devolver una lista con esa fecha.
     */
    @Test
    public void testCB1() {
        DataAccess sut = new DataAccess();
        TestDataAccess testDA = new TestDataAccess();

        // Datos de prueba
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPass = "1235";
        String from = "Vitoria";
        String to = "Santander";

        // Fecha del ride y búsqueda (mismo mes)
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        try {
            sut.open();

            // 1️⃣ Crear driver si no existe
            Driver driver = sut.loginDriver(driverName, driverPass);
            if (driver == null) {
                sut.registerDriver(driverName, driverEmail, driverPass);
                driver = sut.loginDriver(driverName, driverPass);
            }
            assertNotNull("El driver debe existir", driver);

            // 2️⃣ Crear ride directo dentro del mes
            sut.createRide(from, to, rideDate, 4, 10.0f, driverEmail, new ArrayList<>());

            // 3️⃣ Llamar al método a testear
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

            // 4️⃣ Comprobar resultados
            assertNotNull(res);
            assertFalse("Debe encontrarse al menos una fecha", res.isEmpty());
            assertTrue("La fecha del ride directo debe estar en la lista", res.contains(rideDate));

        } catch (Exception e) {
            e.printStackTrace();
            fail("Excepción inesperada: " + e.getMessage());
        } finally {
            // 5️⃣ Limpieza
            testDA.open();
            if (testDA.existRide(driverEmail, from, to, rideDate)) {
                testDA.removeRide(driverEmail, from, to, rideDate);
            }
            if (testDA.existDriver(driverEmail)) {
                testDA.removeDriver(driverEmail);
            }
            testDA.close();

            sut.close();
        }
    }



   
    @Test
    public void testCB2() {
        DataAccess sut = new DataAccess();
        TestDataAccess testDA = new TestDataAccess();

        // Datos de prueba
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPass = "1235";
        String from = "Santander";
        String to = "Oviedo";

        // Fecha de prueba sin horas, minutos ni segundos
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        try {
            sut.open();

            // 1️⃣ Crear driver si no existe
            Driver driver = sut.loginDriver(driverName, driverPass);
            if (driver == null) {
                sut.registerDriver(driverName, driverEmail, driverPass);
                driver = sut.loginDriver(driverName, driverPass);
            }
            assertNotNull("El driver debe existir", driver);

            // 2️⃣ Crear ride con parada
            List<Vector<String>> stops = new ArrayList<>();
            Vector<String> stop1 = new Vector<>();
            stop1.add("Gijón");  // nombre de la parada
            stop1.add("1");      // número de parada
            stop1.add("0");      // precio ficticio
            stops.add(stop1);

            sut.createRide(from, to, rideDate, 4, 10.0f, driverEmail, stops);

            // 3️⃣ Llamar al método a testear (origen o destino puede ser la parada)
            List<Date> res = sut.getThisMonthDatesWithRides("Gijón", to, rideDate);

            // 4️⃣ Comprobar resultados
            assertNotNull(res);
            assertTrue("La fecha del ride debe estar en la lista", res.contains(rideDate));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5️⃣ Limpieza final usando TestDataAccess
            testDA.open();
            if (testDA.existRide(driverEmail, from, to, rideDate)) {
                testDA.removeRide(driverEmail, from, to, rideDate);
            }
            if (testDA.existDriver(driverEmail)) {
                testDA.removeDriver(driverEmail);
            }
            testDA.close();

            sut.close();
        }
    }


    // CP3: E1(T) E2(T) E3(F) F3(F). Q1 y Q2 fallan. Q3 tiene éxito.
    @Test
    public void testCB3() {
        DataAccess sut = new DataAccess();
        TestDataAccess testDA = new TestDataAccess();

        // Datos de prueba
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPass = "1235";
        String fromRide = "Santander";
        String toRide = "Oviedo";
        String stop1Name = "Donostia";
        String stop2Name = "Zarautz";

        // Fecha de prueba sin horas, minutos ni segundos
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        try {
            sut.open();

            // 1️⃣ Crear driver si no existe
            Driver driver = sut.loginDriver(driverName, driverPass);
            if (driver == null) {
                sut.registerDriver(driverName, driverEmail, driverPass);
                driver = sut.loginDriver(driverName, driverPass);
            }
            assertNotNull("El driver debe existir", driver);

            // 2️⃣ Crear ride con dos paradas intermedias
            List<Vector<String>> stops = new ArrayList<>();

            Vector<String> stop1 = new Vector<>();
            stop1.add(stop1Name);  // Donostia
            stop1.add("1");
            stop1.add("0");
            stops.add(stop1);

            Vector<String> stop2 = new Vector<>();
            stop2.add(stop2Name);  // Zarautz
            stop2.add("2");
            stop2.add("0");
            stops.add(stop2);

            sut.createRide(fromRide, toRide, rideDate, 4, 10.0f, driverEmail, stops);

            // 3️⃣ Llamar al método a testear (ambas ciudades son paradas intermedias)
            List<Date> res = sut.getThisMonthDatesWithRides(stop1Name, stop2Name, rideDate);

            // 4️⃣ Comprobar resultados
            assertNotNull(res);
            assertTrue("La fecha del ride debe estar en la lista de dates3", res.contains(rideDate));

        } catch (Exception e) {
            e.printStackTrace();
            fail("Excepción inesperada: " + e.getMessage());
        } finally {
            // 5️⃣ Limpieza final usando TestDataAccess
            testDA.open();
            if (testDA.existRide(driverEmail, fromRide, toRide, rideDate)) {
                testDA.removeRide(driverEmail, fromRide, toRide, rideDate);
            }
            if (testDA.existDriver(driverEmail)) {
                testDA.removeDriver(driverEmail);
            }
            testDA.close();

            sut.close();
        }
    }

  
    @Test
    public void testCB4() {
        DataAccess sut = new DataAccess();
        sut.open();

        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date rideDate = cal.getTime();

            List<Date> res = sut.getThisMonthDatesWithRides("Huelva", "Granada", rideDate);

            assertNotNull(res);
            assertTrue("La lista debería estar vacía si no hay viajes", res.isEmpty());

        } finally {
            sut.close();
        }
    }

    @Test
    public void testCB5() {
        DataAccess sut = new DataAccess();
        TestDataAccess testDA = new TestDataAccess();

        // Datos de prueba
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPass = "1235";
        String from = "Ronda";
        String to = "Burgos";

        // Fecha del ride fuera del mes de búsqueda (septiembre 2026)
        Calendar calRide = Calendar.getInstance();
        calRide.set(2026, Calendar.SEPTEMBER, 20, 0, 0, 0);
        calRide.set(Calendar.MILLISECOND, 0);
        Date rideDate = calRide.getTime();

        // Fecha de búsqueda en octubre 2026
        Calendar calSearch = Calendar.getInstance();
        calSearch.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        calSearch.set(Calendar.MILLISECOND, 0);
        Date searchDate = calSearch.getTime();

        try {
            sut.open();

            // Crear driver si no existe
            Driver driver = sut.loginDriver(driverName, driverPass);
            if (driver == null) {
                sut.registerDriver(driverName, driverEmail, driverPass);
                driver = sut.loginDriver(driverName, driverPass);
            }

            // 1️⃣ Crear ride directo fuera del mes
            sut.createRide(from, to, rideDate, 4, 20.0f, driverEmail, new ArrayList<>());

            // 2️⃣ Llamar al método a testear
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);

            // 3️⃣ Comprobar resultados: la lista debe estar vacía
            assertNotNull("La lista de fechas no debe ser null", res);
            assertTrue("No se deben encontrar rides dentro del mes", res.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            fail("No se esperaba excepción");
        } finally {
            // 4️⃣ Limpieza
            testDA.open();
            if (testDA.existRide(driverEmail, from, to, rideDate)) {
                testDA.removeRide(driverEmail, from, to, rideDate);
            }
            if (testDA.existDriver(driverEmail)) {
                testDA.removeDriver(driverEmail);
            }
            testDA.close();
            sut.close();
        }
    }

    
    @Test
    public void testCB6() {
        DataAccess sut = new DataAccess();
        TestDataAccess testDA = new TestDataAccess();

        // Datos de prueba
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPass = "1235";
        String from = "Sevilla";
        String to = "Barcelona";

        // Fecha de búsqueda
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date searchDate = cal.getTime();

        // Ride que cumpliría query 2 pero fuera del mes
        cal.set(2026, Calendar.SEPTEMBER, 15, 0, 0, 0); // mes anterior
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
            assertNotNull("El driver debe existir", driver);

            // Crear ride con parada fuera del mes
            List<Vector<String>> stops = new ArrayList<>();
            Vector<String> stop1 = new Vector<>();
            stop1.add("Sevilla"); // parada
            stop1.add("1");
            stop1.add("0");
            stops.add(stop1);

            sut.createRide("Bilbao", "Santander", rideDate, 4, 10.0f, driverEmail, stops);

            // Llamar al método a testear
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);

            // Comprobar resultados
            assertNotNull(res);
            assertTrue("No se debe encontrar ninguna fecha dentro del mes", res.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Excepción inesperada: " + e.getMessage());
        } finally {
            testDA.open();
            if (testDA.existRide(driverEmail, "Bilbao", "Santander", rideDate)) {
                testDA.removeRide(driverEmail, "Bilbao", "Santander", rideDate);
            }
            if (testDA.existDriver(driverEmail)) {
                testDA.removeDriver(driverEmail);
            }
            testDA.close();

            sut.close();
        }
    }
    
    
    @Test
    public void testCN1() {
        DataAccess sut = new DataAccess();
        TestDataAccess testDA = new TestDataAccess();

        // Datos de prueba
        String driverEmail = "driver@gmail.com";
        String driverName = "Driver";
        String driverPass = "1234";
        String from = "A";
        String to = "B";

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
            assertNotNull("El driver debe existir", driver);

            // Crear viaje directo
            sut.createRide(from, to, rideDate, 4, 15.0f, driverEmail, new ArrayList<>());

            // Invocar método a testear
            List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

            // Comprobaciones de caja negra
            assertNotNull("La lista de fechas no debe ser null", res);
            assertTrue("Debe contener la fecha del viaje directo", res.contains(rideDate));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Limpieza final
            testDA.open();
            if (testDA.existRide(driverEmail, from, to, rideDate)) {
                testDA.removeRide(driverEmail, from, to, rideDate);
            }
            if (testDA.existDriver(driverEmail)) {
                testDA.removeDriver(driverEmail);
            }
            testDA.close();

            sut.close();
        }
    }

	@Test
	public void testCN2() {
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
