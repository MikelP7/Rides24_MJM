package gui;

import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import businessLogic.BLFacade;
import domain.Booking;
import domain.Ride;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;


public class YourRidesGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JScrollPane scrollPaneEvents = new JScrollPane();


	private JTable tableRides= new JTable();
	

	private DefaultTableModel tableModelRides;
	private JLabel balanceLbl = new JLabel("Balance:");


	private String[] columnNamesRides = new String[] {
			"Driver", "From", "To", "Places", "Price", "Status"
	};


	public YourRidesGUI()
	{

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));

		jButtonClose.setBounds(new Rectangle(274, 419, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton2_actionPerformed(e);
			}
		});
		BLFacade facade = LoginGUI.getBusinessLogic();
		

		this.getContentPane().add(jButtonClose, null);

		scrollPaneEvents.setBounds(new Rectangle(71, 61, 539, 348));

		scrollPaneEvents.setViewportView(tableRides);
		tableModelRides = new DefaultTableModel(null, columnNamesRides);

		tableRides.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Driver", "From", "To", "Places", "Price", "Status"
			}
		) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		
		tableRides.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(25);
		tableRides.getColumnModel().getColumn(2).setPreferredWidth(25);
		tableRides.getColumnModel().getColumn(3).setPreferredWidth(15);
		tableRides.getColumnModel().getColumn(4).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(5).setPreferredWidth(30);
		
		

		tableModelRides.setDataVector(null, columnNamesRides);
		tableModelRides.setColumnCount(5);

		this.getContentPane().add(scrollPaneEvents, null);
		
		scrollPaneEvents.setViewportView(tableRides);
		
		tableModelRides = new DefaultTableModel(null, columnNamesRides);

		tableRides.setModel(tableModelRides);
		
		JLabel lblNewLabel = new JLabel("Your Rides:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(71, 26, 159, 25);
		getContentPane().add(lblNewLabel);
		
		balanceLbl.setFont(new Font("Tahoma", Font.BOLD, 13));
		balanceLbl.setBounds(497, 26, 113, 25);
		getContentPane().add(balanceLbl);
		
		
		if(MainGUI.getDriver()!=null) {
			List<Ride> l = facade.getRidesByEmail(MainGUI.getDriver().getEmail(), new String("Driver"));
			
			float dbal = facade.getBalanceByEmail(MainGUI.getDriver().getEmail());
			balanceLbl.setText("Balance: " + dbal + "€");
			
			if(l!=null) {
				for (Ride ride:l){
					Vector<Object> row = new Vector<Object>();
					row.add(ride.getDriver().getName());
					row.add(ride.getFrom());
					row.add(ride.getTo());
					row.add(ride.getnPlaces());
					row.add(ride.getPrice());
					tableModelRides.addRow(row);
				}
			}
			tableModelRides.fireTableDataChanged();
		}
		
		if(MainUserGUI.getUser()!=null) {
			List<Booking> l = facade.getBookingByEmail(MainUserGUI.getUser().getEmail(), new String("User"));
			
			float dbal = facade.getBalanceByEmail(MainUserGUI.getUser().getEmail());
			balanceLbl.setText("Balance: " + dbal + "€");
			
			if(l!=null) {
				for (Booking booking:l){
					Vector<Object> row = new Vector<Object>();
					row.add(booking.getDriver().getName());
					row.add(booking.getFrom());
					row.add(booking.getTo());
					row.add(booking.getSeats());
					row.add(booking.getPrice());
					if(booking.getAccepeted()==1) {
						row.add(new String("Accepted"));
					}
					else if (booking.getAccepeted()==0) {
						row.add(new String("Pending..."));
					}
					else {
						row.add(new String("Declined"));
					}
					
					System.out.println(row);
					tableModelRides.addRow(row);
				}
			}
			tableModelRides.fireTableDataChanged();
		}

	}
	
	
	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
