package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import domain.Driver;
import domain.User;
import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainUserGUI extends JFrame {
	
    private static User user;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonBookQuery;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	
	public static User getUser() {
		return user;
	}
	
	protected JLabel jLabelSelectOption;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton yourRidesButton;
	
	/**
	 * This is the default constructor
	 */
	public MainUserGUI(User u) {
		super();

		user=u;
		
		
		appFacadeInterface = LoginGUI.getBusinessLogic();
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);
	
		panel = new JPanel();
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jButtonBookQuery = new JButton();
		jButtonBookQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.BookRide"));
		jButtonBookQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new BookRideGUI(u);
				a.setVisible(true);
			}
		});
		
		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.QueryRides"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new FindRidesGUI();

				a.setVisible(true);
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(5, 1, 0, 0));
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonBookQuery);
		jContentPane.add(jButtonQueryQueries);
		
		yourRidesButton = new JButton("Your Rides\r\n");
		jContentPane.add(yourRidesButton);
		jContentPane.add(panel);
		yourRidesButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new YourRidesGUI();
				a.setVisible(true);
			}
		});
		
		
		
		setContentPane(jContentPane);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.MainTitle") + " - User :"+user.getUsername());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.QueryRides"));
		jButtonBookQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.BookRide"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainUserGUI.MainTitle")+ " - User :"+user.getUsername());
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

