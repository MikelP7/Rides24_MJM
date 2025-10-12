package gui;

import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import businesslogic.BLFacade;
import domain.Driver;
import domain.Ride;
import domain.Stop;
import domain.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;


public class BookRideGUI extends JFrame {
	private static final long serialVersionUID = 1L;


	private JComboBox<String> jComboBoxOrigin = new JComboBox<String>();
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();

	private JComboBox<String> jComboBoxDestination = new JComboBox<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();

	private JLabel jLabelOrigin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.LeavingFrom"));
	private JLabel jLabelDestination = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.GoingTo"));
	private final JLabel jLabelEventDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideDate"));
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides")); 

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	// Code for JCalendar
	private JCalendar jCalendar1 = new JCalendar();
	private Calendar calendarAnt = null;
	private Calendar calendarAct = null;
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private List<Date> datesWithRidesCurrentMonth = new Vector<Date>();

	private JTable tableRides= new JTable();

	private DefaultTableModel tableModelRides;
	
	private JComboBox<Integer> numberSeatsComboBox = new JComboBox<Integer>();
	
	private JButton bookButton = new JButton("Book");
	
	private JTextArea textArea = new JTextArea();


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Driver"), 
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.NPlaces"), 
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Price")
	};


	public BookRideGUI(User u)
	{
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle("Book Rides");

		jLabelEventDate.setBounds(new Rectangle(457, 6, 140, 25));
		jLabelEvents.setBounds(54, 231, 259, 16);

		this.getContentPane().add(jLabelEventDate, null);
		this.getContentPane().add(jLabelEvents);

		jButtonClose.setBounds(new Rectangle(387, 417, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton2_actionPerformed(e);
			}
		});
		BLFacade facade = LoginGUI.getBusinessLogic();
		List<String> origins=facade.getDepartCities();
		List<String> stops=facade.getStopNames();
		List<String> originNames = new ArrayList<String>();
		
		for(String stop: stops) {
			if(!originNames.contains(stop)) {
				originNames.add(stop);
			}
		}
		
		for(String o: origins) {
			if(!originNames.contains(o)) {
				originNames.add(o);
			}
		}
		
		for(String n: originNames) originLocations.addElement(n);

		jLabelOrigin.setBounds(new Rectangle(6, 56, 92, 20));
		jLabelDestination.setBounds(6, 81, 61, 16);
		getContentPane().add(jLabelOrigin);

		getContentPane().add(jLabelDestination);

		jComboBoxOrigin.setModel(originLocations);
		jComboBoxOrigin.setBounds(new Rectangle(103, 50, 172, 20));
		

		List<String> aCities=facade.getDestinationCities((String)jComboBoxOrigin.getSelectedItem());
		for(String aciti:aCities) {
			destinationCities.addElement(aciti);
		}
		
		jComboBoxOrigin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				destinationCities.removeAllElements();
				BLFacade facade = LoginGUI.getBusinessLogic();
				
				List<String> names = facade.getStopsAndDestinations((String)jComboBoxOrigin.getSelectedItem());
				
				for(String n:names) {
					destinationCities.addElement(n);
				}
				
				tableModelRides.getDataVector().removeAllElements();
				tableModelRides.fireTableDataChanged();

				
			}
		});


		jComboBoxDestination.setModel(destinationCities);
		jComboBoxDestination.setBounds(new Rectangle(103, 80, 172, 20));
		jComboBoxDestination.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,	new Color(210,228,238));

				BLFacade facade = LoginGUI.getBusinessLogic();

				
				datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
				paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);

			}
		});

		this.getContentPane().add(jButtonClose, null);
		this.getContentPane().add(jComboBoxOrigin, null);

		this.getContentPane().add(jComboBoxDestination, null);


		jCalendar1.setBounds(new Rectangle(300, 50, 225, 150));


		// Code for JCalendar
		jCalendar1.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent propertychangeevent)
			{

				if (propertychangeevent.getPropertyName().equals("locale"))
				{
					jCalendar1.setLocale((Locale) propertychangeevent.getNewValue());
				}
				else if (propertychangeevent.getPropertyName().equals("calendar"))
				{
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();
					

					
					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar1.getLocale());

					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);

					if (monthAct!=monthAnt) {
						if (monthAct==monthAnt+2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, devolvería 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							calendarAct.set(Calendar.MONTH, monthAnt+1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}						

						jCalendar1.setCalendar(calendarAct);

					}
					
					try {
						tableModelRides.setDataVector(null, columnNamesRides);
						tableModelRides.setColumnCount(4); // another column added to allocate ride objects

						BLFacade facade = LoginGUI.getBusinessLogic();
						List<Ride> rides = new Vector<Ride>();
						rides=facade.getRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),UtilDate.trim(jCalendar1.getDate()));

						if (rides.isEmpty()) rides = facade.getRidesByStop((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),UtilDate.trim(jCalendar1.getDate()));
						if (rides.isEmpty()) rides = facade.getRidesByBothStops((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),UtilDate.trim(jCalendar1.getDate()));
						
						
						else jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Rides")+ ": "+dateformat1.format(calendarAct.getTime()));
						for (domain.Ride ride:rides){
							Vector<Object> row = new Vector<Object>();
							row.add(ride.getDriver().getName());
							row.add(ride.getnPlaces());
							
							if(!jComboBoxDestination.getSelectedItem().toString().equals(ride.getTo()) || !jComboBoxOrigin.getSelectedItem().toString().equals(ride.getTo())) {
								List<Stop> ls = facade.getRideStops(ride);
								Double stopPrice = 0.0;
								
								for(Stop st: ls) {
									if(st.getName().equals(jComboBoxDestination.getSelectedItem().toString()) || st.getName().equals(jComboBoxOrigin.getSelectedItem().toString())) {
										stopPrice = Double.parseDouble(Float.toString(st.getPrice()));
									}
								}
								row.add(stopPrice);
							}
							else {
								row.add(ride.getPrice());
							}
							
							row.add(ride); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
					
							if(ride.getnPlaces()>0)tableModelRides.addRow(row);
								
						
						}
						datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
						paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);


					} catch (Exception e1) {

						e1.printStackTrace();
					}
					tableRides.getColumnModel().getColumn(0).setPreferredWidth(170);
					tableRides.getColumnModel().getColumn(1).setPreferredWidth(30);
					tableRides.getColumnModel().getColumn(1).setPreferredWidth(30);
					tableRides.getColumnModel().removeColumn(tableRides.getColumnModel().getColumn(3)); // not shown in JTable
				
					tableRides.setEnabled(true);
					bookButton.setEnabled(true);
					numberSeatsComboBox.setEnabled(true);
					textArea.setText("");
				}
			} 
			
		});
		
		this.getContentPane().add(jCalendar1, null);

		scrollPaneEvents.setBounds(new Rectangle(54, 257, 346, 150));

		scrollPaneEvents.setViewportView(tableRides);
		tableModelRides = new DefaultTableModel(null, columnNamesRides){

		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};

		tableRides.setModel(tableModelRides);

		tableModelRides.setDataVector(null, columnNamesRides);
		tableModelRides.setColumnCount(4); // another column added to allocate ride objects

		tableRides.getColumnModel().getColumn(0).setPreferredWidth(170);
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(30);

		tableRides.getColumnModel().removeColumn(tableRides.getColumnModel().getColumn(3)); // not shown in JTable

		this.getContentPane().add(scrollPaneEvents, null);
		datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
		paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);
		
		JLabel seatNum = new JLabel("Number of Seats:");
		seatNum.setBounds(410, 262, 107, 16);
		getContentPane().add(seatNum);
		
		
		numberSeatsComboBox.setBounds(new Rectangle(103, 80, 172, 20));
		numberSeatsComboBox.setBounds(410, 288, 172, 20);
		getContentPane().add(numberSeatsComboBox);
		numberSeatsComboBox.addItem(1);
		numberSeatsComboBox.addItem(2);
		numberSeatsComboBox.addItem(3);
		numberSeatsComboBox.addItem(4);
		
		
		
		bookButton.setBounds(new Rectangle(395, 417, 130, 30));
		bookButton.setBounds(172, 417, 130, 30);
		getContentPane().add(bookButton);
		
		
		textArea.setForeground(Color.RED);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea.setEditable(false);
		textArea.setBackground(SystemColor.menu);
		textArea.setBounds(410, 318, 209, 32);
		getContentPane().add(textArea);
	
		
		
		bookButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
		
				BLFacade facade = LoginGUI.getBusinessLogic();
				
				String driver = tableModelRides.getValueAt(tableRides.getSelectedRow(), 0).toString();
				String seats = tableModelRides.getValueAt(tableRides.getSelectedRow(), 1).toString();
				String cost = tableModelRides.getValueAt(tableRides.getSelectedRow(), 2).toString();
				String a = tableModelRides.getValueAt(tableRides.getSelectedRow(), 3).toString();
				
				String[] parts = a.split(";");
				Ride r = facade.findRide(Integer.parseInt(parts[0]));
				
				Boolean b = true;
				
				if(!jComboBoxDestination.getSelectedItem().toString().equals(r.getTo()) || !jComboBoxOrigin.getSelectedItem().toString().equals(r.getTo())) {
					List<Stop> ls = facade.getRideStops(r);
					Double stopPrice = 0.0;
					Stop sto = null;
					
					for(Stop st: ls) {
						if(st.getName().equals(jComboBoxDestination.getSelectedItem().toString()) || st.getName().equals(jComboBoxOrigin.getSelectedItem().toString())) {
							stopPrice = Double.parseDouble(Float.toString(st.getPrice()));
							sto = st;
						}
					}
					b = facade.addBooking(u,r, Integer.parseInt(numberSeatsComboBox.getSelectedItem().toString()));
				}
				else {
					b = facade.addBooking(u,r,Integer.parseInt(numberSeatsComboBox.getSelectedItem().toString()));
				}
				
				
				if(Boolean.TRUE.equals(b)) {
					
					textArea.setForeground(new Color(0,255,0));
					textArea.setText("Booked!");
					tableRides.setEnabled(false);
					bookButton.setEnabled(false);
					numberSeatsComboBox.setEnabled(false);
				}
				else {
					textArea.setForeground(new Color(255,0,0));
					textArea.setText("ERROR!");
				}
				

			
			}
		});
			
	}
	
	
	
	
	
	public static void paintDaysWithEvents(JCalendar jCalendar,List<Date> datesWithEventsCurrentMonth, Color color) {
		//		// For each day with events in current month, the background color for that day is changed to cyan.


		Calendar calendar = jCalendar.getCalendar();

		int month = calendar.get(Calendar.MONTH);
		int today=calendar.get(Calendar.DAY_OF_MONTH);
		int year=calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK);

		if (Locale.getDefault().equals(new Locale("es")))
			offset += 4;
		else
			offset += 5;


		for (Date d:datesWithEventsCurrentMonth){

			calendar.setTime(d);


			// Obtain the component of the day in the panel of the DayChooser of the
			// JCalendar.
			// The component is located after the decorator buttons of "Sun", "Mon",... or
			// "Lun", "Mar"...,
			// the empty days before day 1 of month, and all the days previous to each day.
			// That number of components is calculated with "offset" and is different in
			// English and Spanish
			//			    		  Component o=(Component) jCalendar.getDayChooser().getDayPanel().getComponent(i+offset);; 
			Component o = (Component) jCalendar.getDayChooser().getDayPanel()
					.getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(color);
		}

		calendar.set(Calendar.DAY_OF_MONTH, today);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);


	}
	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
