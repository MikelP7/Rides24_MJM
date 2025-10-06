package testMetodos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;

public class GetThisMonthDatesWithRidesMock {

    DataAccess sut;

    @Mock
    EntityManager db;

    @Mock
    TypedQuery<Date> query;
    
    @Mock
    TypedQuery<Date> queryDirect;

    @Mock
    TypedQuery<Date> queryStops;
    
    @Mock
    TypedQuery<Date> queryBothStops;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new DataAccess(db); // Constructor que acepta EntityManager mock
    }

    @After
    public void tearDown() {
        sut.close();
    }

    @Test
    public void testCB1() throws ParseException {
        // Datos del test
        String from = "Donostia";
        String to = "Zarautz";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date rideDate = sdf.parse("05/10/2026");

        // Lista que simula la consulta directa Q1
        List<Date> dates = new ArrayList<>();
        dates.add(rideDate);

        // Simular TypedQuery y comportamiento de EntityManager
        Mockito.when(db.createQuery(
                Mockito.anyString(),
                Mockito.eq(Date.class)
        )).thenReturn(query);

        Mockito.when(query.setParameter(Mockito.anyInt(), Mockito.any()))
                .thenReturn(query); // permite encadenar setParameter

        Mockito.when(query.getResultList())
                .thenReturn(dates); // Q1 tiene un resultado

        // Invocar el método a testear
        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        // Comprobaciones
        assertEquals(1, res.size());
        assertTrue(res.contains(rideDate));
    }
    
    @Test
    public void testCB2() {
        // Datos de prueba
        String from = "Vitoria";
        String to = "Santander";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        // Listas simuladas
        List<Date> datesDirect = new ArrayList<>(); // no hay viaje directo
        List<Date> datesStops = new ArrayList<>();
        datesStops.add(rideDate); // el viaje con parada que queremos que devuelva

        // Mock de consulta directa
        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
                .thenReturn(queryDirect)   // primero para Q1
                .thenReturn(queryStops);   // luego para Q2

        // Parámetros y encadenamiento
        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);

        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);

        // Ejecutar método
        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        // Comprobar que se devolvió la lista de Q2
        assertEquals(1, res.size());
        assertTrue(res.contains(rideDate));
    }
    
    @Test
    public void testCB3() {
        // Datos de prueba
        String from = "Donostia";
        String to = "Zarautz";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        // Listas simuladas
        List<Date> datesDirect = new ArrayList<>(); // Q1 vacío
        List<Date> datesStops = new ArrayList<>();  // Q2 vacío
        List<Date> dates3 = new ArrayList<>();      // Q3 con resultado
        dates3.add(rideDate);

        // Mock de consultas
        TypedQuery<Date> queryDirect = Mockito.mock(TypedQuery.class);
        TypedQuery<Date> queryStops = Mockito.mock(TypedQuery.class);
        TypedQuery<Date> queryBothStops = Mockito.mock(TypedQuery.class);

        // Secuencia de createQuery: Q1, Q2, Q3
        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
                .thenReturn(queryDirect)      // Q1
                .thenReturn(queryStops)       // Q2
                .thenReturn(queryBothStops);  // Q3

        // Encadenamiento de setParameter
        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);

        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);

        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);
        Mockito.when(queryBothStops.getResultList()).thenReturn(dates3);

        // Ejecutar método
        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        // Comprobaciones
        assertEquals(1, res.size());
        assertTrue(res.contains(rideDate));
    }

    @Test
    public void testCB4() {
        // Datos del test
        String from = "Huelva";
        String to = "Granada";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        // Listas vacías simulando que no hay viajes en ninguna consulta
        List<Date> datesQ1 = new ArrayList<>();
        List<Date> datesQ2 = new ArrayList<>();
        List<Date> datesQ3 = new ArrayList<>();

        // Configurar el comportamiento de EntityManager para devolver los 3 mocks en orden (Q1, Q2, Q3)
        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)    // Q1
               .thenReturn(queryStops)     // Q2
               .thenReturn(queryBothStops); // Q3

        // Encadenamiento de setParameter para cada mock
        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        // Simular que ninguna consulta devuelve resultados
        Mockito.when(queryDirect.getResultList()).thenReturn(datesQ1);
        Mockito.when(queryStops.getResultList()).thenReturn(datesQ2);
        Mockito.when(queryBothStops.getResultList()).thenReturn(datesQ3);

        // Ejecutar método a probar
        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        // Comprobaciones
        assertNotNull(res);
        assertTrue("La lista debería estar vacía si no hay viajes", res.isEmpty());
    }

    
    
    @Test
    public void testCB5() {
        // Datos del test
        String from = "Donostia";
        String to = "Zarautz";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date searchDate = cal.getTime();

        // Simulamos que existe un viaje directo, pero fuera del mes
        List<Date> datesDirect = new ArrayList<>(); // Q1 devuelve vacío
        List<Date> datesStops = new ArrayList<>();  // Q2 vacío
        List<Date> datesBothStops = new ArrayList<>(); // Q3 vacío

        // Configurar createQuery para devolver los mocks en orden
        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)      // Q1
               .thenReturn(queryStops)       // Q2
               .thenReturn(queryBothStops);  // Q3

        // Encadenamiento de setParameter
        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        // getResultList devuelve listas vacías
        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);
        Mockito.when(queryBothStops.getResultList()).thenReturn(datesBothStops);

        // Ejecutar método
        List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);

        // Comprobaciones
        assertNotNull(res);
        assertTrue("La lista debería estar vacía porque el viaje directo está fuera del mes", res.isEmpty());
    }

    @Test
    public void testCB6() {
        // Datos del test
        String from = "Donostia";
        String to = "Zarautz";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date searchDate = cal.getTime();

        // Simular listas vacías
        List<Date> datesDirect = new ArrayList<>(); // Q1 falla → vacío
        List<Date> datesStops = new ArrayList<>();  // Q2 fuera del mes → vacío
        List<Date> datesBothStops = new ArrayList<>(); // Q3 vacío

        // Configurar createQuery para devolver los mocks en orden
        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)      // Q1
               .thenReturn(queryStops)       // Q2
               .thenReturn(queryBothStops);  // Q3

        // Encadenamiento de setParameter
        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        // getResultList devuelve listas vacías
        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);
        Mockito.when(queryBothStops.getResultList()).thenReturn(datesBothStops);

        // Ejecutar método
        List<Date> res = sut.getThisMonthDatesWithRides(from, to, searchDate);

        // Comprobaciones
        assertNotNull(res);
        assertTrue("La lista debería estar vacía porque el viaje con parada está fuera del mes", res.isEmpty());
    }

    @Test
    public void testCN1() {
        String from = "A";
        String to = "B";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        List<Date> datesDirect = new ArrayList<>();
        datesDirect.add(rideDate);
        List<Date> datesStops = new ArrayList<>();
        List<Date> datesBothStops = new ArrayList<>();

        // Configurar mocks para las tres consultas en orden
        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)
               .thenReturn(queryStops)
               .thenReturn(queryBothStops);

        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);
        Mockito.when(queryBothStops.getResultList()).thenReturn(datesBothStops);

        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        assertNotNull(res);
        assertTrue(res.contains(rideDate));
    }

    @Test
    public void testCN2() {
        String from = "C";
        String to = "D";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        List<Date> datesDirect = new ArrayList<>();
        List<Date> datesStops = new ArrayList<>();
        datesStops.add(rideDate);
        List<Date> datesBothStops = new ArrayList<>();

        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)
               .thenReturn(queryStops)
               .thenReturn(queryBothStops);

        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);
        Mockito.when(queryBothStops.getResultList()).thenReturn(datesBothStops);

        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        assertNotNull(res);
        assertTrue(res.contains(rideDate));
    }

    @Test
    public void testCN3() {
        String from = "F";
        String to = "G";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        List<Date> datesDirect = new ArrayList<>();
        List<Date> datesStops = new ArrayList<>();
        List<Date> datesBothStops = new ArrayList<>();
        datesBothStops.add(rideDate);

        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)
               .thenReturn(queryStops)
               .thenReturn(queryBothStops);

        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        Mockito.when(queryDirect.getResultList()).thenReturn(datesDirect);
        Mockito.when(queryStops.getResultList()).thenReturn(datesStops);
        Mockito.when(queryBothStops.getResultList()).thenReturn(datesBothStops);

        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        assertNotNull(res);
        assertTrue(res.contains(rideDate));
    }

    @Test
    public void testCN4() {
        String from = "J";
        String to = "K";
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 5, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date rideDate = cal.getTime();

        List<Date> emptyList = new ArrayList<>();

        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)
               .thenReturn(queryStops)
               .thenReturn(queryBothStops);

        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        Mockito.when(queryDirect.getResultList()).thenReturn(emptyList);
        Mockito.when(queryStops.getResultList()).thenReturn(emptyList);
        Mockito.when(queryBothStops.getResultList()).thenReturn(emptyList);

        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testCN5() {
        sut.getThisMonthDatesWithRides(null, "L", new Date());
    }

    @Test(expected = NullPointerException.class)
    public void testCN6() {
        sut.getThisMonthDatesWithRides("M", null, new Date());
    }

    @Test(expected = NullPointerException.class)
    public void testCN7() {
        sut.getThisMonthDatesWithRides("N", "O", null);
    }

    @Test
    public void testCN8() {
        String from = "A";
        String to = "A";
        Date rideDate = new Date();

        List<Date> emptyList = new ArrayList<>();

        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Date.class)))
               .thenReturn(queryDirect)
               .thenReturn(queryStops)
               .thenReturn(queryBothStops);

        Mockito.when(queryDirect.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryDirect);
        Mockito.when(queryStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryStops);
        Mockito.when(queryBothStops.setParameter(Mockito.anyInt(), Mockito.any())).thenReturn(queryBothStops);

        Mockito.when(queryDirect.getResultList()).thenReturn(emptyList);
        Mockito.when(queryStops.getResultList()).thenReturn(emptyList);
        Mockito.when(queryBothStops.getResultList()).thenReturn(emptyList);

        List<Date> res = sut.getThisMonthDatesWithRides(from, to, rideDate);

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }
}
