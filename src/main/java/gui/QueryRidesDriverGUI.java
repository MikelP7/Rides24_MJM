package gui;

import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import businesslogic.BLFacade;
import domain.Booking;
import domain.Ride;
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


public class QueryRidesDriverGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JScrollPane scrollPaneEvents = new JScrollPane();


	private JTable tableRides= new JTable();
	

	private DefaultTableModel tableModelRides;
	
	private JButton acceptButton = new JButton("Accept");
	private JButton btnDecline = new JButton("Decline");
	
	private JLabel balanceLbl = new JLabel("Balance:");


	public QueryRidesDriverGUI()
	{

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));

		jButtonClose.setBounds(new Rectangle(10, 423, 130, 30));

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
		
		tableModelRides = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"User", "From", "To", "Places", "Price", "BookingId", "Stop", "Status", ""
			}
		) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tableRides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		ListSelectionModel selectionModel = tableRides.getSelectionModel();

		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		    	if (tableModelRides.getValueAt(tableRides.getSelectedRow(), 6).toString().equals("Accepted")) {
			    	acceptButton.setEnabled(false);
			    	btnDecline.setEnabled(false);
		    	}
		    	else {
			    	acceptButton.setEnabled(true);
			    	btnDecline.setEnabled(true);
		    	}

		    }
		});
		
		tableRides.setModel(tableModelRides);
		
		tableRides.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(25);
		tableRides.getColumnModel().getColumn(2).setPreferredWidth(25);
		tableRides.getColumnModel().getColumn(3).setPreferredWidth(15);
		tableRides.getColumnModel().getColumn(4).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(5).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(6).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(7).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(8).setPreferredWidth(30);
		
		tableModelRides.setColumnCount(8);
		
		tableRides.setModel(tableModelRides);

		this.getContentPane().add(scrollPaneEvents, null);
		
		scrollPaneEvents.setViewportView(tableRides);
		
		JLabel lblNewLabel = new JLabel("Bookings:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(71, 26, 159, 25);
		getContentPane().add(lblNewLabel);
		
		
		acceptButton.setEnabled(false);
		acceptButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		acceptButton.setForeground(new Color(0, 128, 0));
		acceptButton.setBounds(new Rectangle(10, 423, 130, 30));
		acceptButton.setBounds(325, 423, 130, 30);
		getContentPane().add(acceptButton);
		

		
		
		btnDecline.setEnabled(false);
		btnDecline.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDecline.setForeground(new Color(255, 0, 0));
		btnDecline.setBounds(new Rectangle(10, 423, 130, 30));
		btnDecline.setBounds(465, 423, 130, 30);
		getContentPane().add(btnDecline);
		
		
		balanceLbl.setFont(new Font("Tahoma", Font.BOLD, 13));
		balanceLbl.setBounds(497, 26, 113, 25);
		getContentPane().add(balanceLbl);
		
		float dbal = facade.getBalanceByEmail(MainGUI.getDriver().getEmail());
		balanceLbl.setText("Balance: " + dbal + "€");
		

			List<Booking> l = facade.getBookingByEmail(MainGUI.getDriver().getEmail(), new String("Driver"));
			
			
			if(l!=null) {
				for (Booking booking:l){
					Vector<Object> row = new Vector<Object>();
					row.add(booking.getUser().getUsername());
					row.add(booking.getFrom());
					row.add(booking.getTo());
					row.add(booking.getSeats());
					row.add(booking.getPrice());
					row.add(booking.getBookingId());
					
					if(booking.getStop()==null) {
						row.add("No");
					}
					else {
						row.add(booking.getStop().getName());
					}
					
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
			
			
			acceptButton.addActionListener(new ActionListener() { 
				
				  public void actionPerformed(ActionEvent e) { 
				    acceptButton.setEnabled(false);
				    btnDecline.setEnabled(false);
				    
					String booking = tableModelRides.getValueAt(tableRides.getSelectedRow(), 5).toString();
					
					boolean b = facade.acceptBooking(Integer.parseInt(booking), Float.parseFloat(tableRides.getValueAt(tableRides.getSelectedRow(), 4).toString()));
					
					if(b) tableModelRides.setValueAt("Accepted", tableRides.getSelectedRow(), 7);
					else tableModelRides.setValueAt("ERROR!", tableRides.getSelectedRow(), 7);
						
					float dbal = facade.getBalanceByEmail(MainGUI.getDriver().getEmail());
					balanceLbl.setText("Balance: " + dbal + "€");
	
				  }
				  
				});
			
			btnDecline.addActionListener(new ActionListener() { 
				
				  public void actionPerformed(ActionEvent e) { 
				    acceptButton.setEnabled(false);
				    btnDecline.setEnabled(false);
				    
					String booking = tableModelRides.getValueAt(tableRides.getSelectedRow(), 5).toString();
					
					facade.declineBooking(Integer.parseInt(booking), Float.parseFloat(tableRides.getValueAt(tableRides.getSelectedRow(), 4).toString()));
					tableModelRides.setValueAt("Declined", tableRides.getSelectedRow(), 7);
					
					float dbal = facade.getBalanceByEmail(MainGUI.getDriver().getEmail());
					balanceLbl.setText("Balance: " + dbal + "€");
	
				  }
				  
				});
			
		}

	
	
	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
