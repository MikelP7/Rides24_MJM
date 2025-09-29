package gui;

import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import businesslogic.BLFacade;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import domain.Stop;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class AddStopsGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	
	private Driver driver;

	private JScrollPane scrollPaneEvents = new JScrollPane();
	
	private JTable tableRides= new JTable();
	private DefaultTableModel tableModelRides;
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	
	private List<Date> datesWithEventsCurrentMonth;
	private JTextField stopName;
	private JTextField stopNum;
	private JTextField price;
	
	private JComboBox<Ride> dRides = new JComboBox<Ride>();
	
	private List<Vector<String>> stopList = new Vector<Vector<String>>();


	public AddStopsGUI(Driver driver) {

		this.driver=driver;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(763, 583));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.CreateRide"));
		scrollPaneEvents.setBounds(new Rectangle(100, 320, 539, 216));
		jButtonClose.setBounds(new Rectangle(289, 510, 130, 30));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});

		jLabelMsg.setBounds(new Rectangle(275, 214, 305, 20));
		jLabelMsg.setForeground(Color.red);

		jLabelError.setBounds(new Rectangle(6, 191, 320, 20));
		jLabelError.setForeground(Color.red);

		this.getContentPane().add(jLabelMsg, null);
		this.getContentPane().add(jLabelError, null);

		this.getContentPane().add(jButtonClose, null);
		
		
		scrollPaneEvents.setBounds(new Rectangle(100, 290, 539, 210));
		scrollPaneEvents.setViewportView(tableRides);
		
		tableModelRides = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Stop Name", "Number", "Price",
			}
		) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tableRides.setModel(tableModelRides);
		
		tableRides.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(25);
		tableRides.getColumnModel().getColumn(2).setPreferredWidth(25);
		
		tableModelRides.setColumnCount(3);
		
		tableRides.setModel(tableModelRides);

		this.getContentPane().add(scrollPaneEvents, null);
		
		scrollPaneEvents.setViewportView(tableRides);
		
		
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
		JButton addStop = new JButton("Add Stop");
		
		addStop.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stopName.getText().length()>0 && stopNum.getText().length()>0 && price.getText().length()>0) {
					try {
						Integer n = Integer.parseInt(stopNum.getText());
						Float p = Float.parseFloat(price.getText());
						
						Ride r=null;
						for (Ride ride : facade.getRidesByEmail(driver.getEmail(), "Driver")) {
							if(dRides.getSelectedItem().toString().equals(ride.toString())) {
								r=ride;
							}
						}
						

						Vector<String> row = new Vector<String>();
						row.add(stopName.getText());
						row.add(stopNum.getText());
						row.add(price.getText());
						tableModelRides.addRow(row);
						tableModelRides.fireTableDataChanged();
						facade.addStopToRide(r, stopName.getText(), Float.parseFloat(price.getText()),Integer.parseInt(stopNum.getText()));
						stopName.setText("");
						stopNum.setText("");
						price.setText("");

					}
					catch(NumberFormatException ne) {
						stopName.setText("");
						stopNum.setText("");
						price.setText("");
					}
					
				}
			}
		});
		addStop.setBounds(547, 244, 92, 30);
		getContentPane().add(addStop);
		
		stopName = new JTextField();
		stopName.setColumns(10);
		stopName.setBounds(100, 244, 130, 26);
		getContentPane().add(stopName);
		
		stopNum = new JTextField();
		stopNum.setColumns(10);
		stopNum.setBounds(240, 244, 77, 26);
		getContentPane().add(stopNum);
		
		price = new JTextField();
		price.setColumns(10);
		price.setBounds(327, 244, 92, 26);
		getContentPane().add(price);
		
		JLabel lblStopCity = new JLabel("Stop Name:");
		lblStopCity.setBounds(new Rectangle(6, 56, 92, 20));
		lblStopCity.setBounds(100, 214, 92, 20);
		getContentPane().add(lblStopCity);
		
		JLabel lblStopNumber = new JLabel("Stop Number:");
		lblStopNumber.setBounds(new Rectangle(6, 56, 92, 20));
		lblStopNumber.setBounds(240, 214, 92, 20);
		getContentPane().add(lblStopNumber);
		
		JLabel lblStopNumber_2 = new JLabel("Price:");
		lblStopNumber_2.setBounds(new Rectangle(6, 56, 92, 20));
		lblStopNumber_2.setBounds(336, 214, 92, 20);
		getContentPane().add(lblStopNumber_2);
		
		JLabel lblYourRides = new JLabel("Your Rides");
		lblYourRides.setBounds(new Rectangle(6, 56, 92, 20));
		lblYourRides.setBounds(100, 60, 92, 20);
		getContentPane().add(lblYourRides);
		
		
		dRides.setBounds(100, 90, 319, 20);
		getContentPane().add(dRides);
		
		List<Ride> ridesList = facade.getRidesByEmail(driver.getEmail(), "Driver");
		
		for(Ride r : ridesList) {
			dRides.addItem(r);
		}
		
		dRides.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				tableModelRides.getDataVector().removeAllElements();
				tableModelRides.fireTableDataChanged();
				
				BLFacade facade = LoginGUI.getBusinessLogic();
				Ride r=null;
				for (Ride ride : facade.getRidesByEmail(driver.getEmail(), "Driver")) {
					if(dRides.getSelectedItem().toString().equals(ride.toString())) {
						r=ride;
					}
				}
				
				List<Stop> stops = facade.getRideStops(r);
				System.out.println(stops);
				
				for (Stop s: stops) {
					Vector<String> row = new Vector<String>();
					row.add(s.getName());
					row.add(s.getNumStop().toString());
					row.add(Float.toString(s.getPrice()));
					tableModelRides.addRow(row);
					tableModelRides.fireTableDataChanged();
				}

				
			}
		});
	}	 
	

	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
