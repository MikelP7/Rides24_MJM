package gui;

import java.awt.EventQueue;

import domain.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import businesslogic.*;

import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	private JButton registerButton = new JButton("Register");
	private JButton loginButton = new JButton("Login");
	
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel passwordLabel = new JLabel("Password:");
	
	private JLabel usertipe = new JLabel("User Tipe:");
	private JRadioButton userbutton = new JRadioButton("User");
	private JRadioButton driverbutton = new JRadioButton("Driver");
	
	
    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	
	
	
	public LoginGUI() {
		setTitle("Login");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		username = new JTextField();
		username.setBounds(161, 75, 205, 19);
		contentPane.add(username);
		username.setColumns(10);
		
		
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		usernameLabel.setBounds(48, 73, 142, 19);
		contentPane.add(usernameLabel);
		
		
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		passwordLabel.setBounds(48, 113, 154, 19);
		contentPane.add(passwordLabel);
		
		JLabel usertipe = new JLabel("User Tipe:");
		usertipe.setFont(new Font("Tahoma", Font.PLAIN, 17));
		usertipe.setBounds(48, 164, 154, 19);
		contentPane.add(usertipe);
				
		
		buttonGroup.add(userbutton);
		userbutton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		userbutton.setBounds(266, 165, 103, 21);
		contentPane.add(userbutton);
		
		
		buttonGroup.add(driverbutton);
		driverbutton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		driverbutton.setBounds(161, 165, 103, 21);
		contentPane.add(driverbutton);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(SystemColor.menu);
		textArea.setEditable(false);
		textArea.setForeground(Color.RED);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea.setBounds(157, 361, 209, 32);
		contentPane.add(textArea);
		
		password = new JPasswordField();
		password.setBounds(161, 116, 205, 20);
		contentPane.add(password);
		loginButton.setEnabled(false);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Evento producido sobre componente: " + e.getActionCommand());
				String user=username.getText();
				String pass= new String(password.getPassword());

				if (userbutton.isSelected()) {
					System.out.println(user + " " + pass);
					User u = appFacadeInterface.loginUser(user, pass);
					if(u!=null) {
						MainUserGUI mu = new MainUserGUI(u);
						mu.setVisible(true);
						dispose();
					}
					else {
						textArea.setText("ERROR: User not found!");
						driverbutton.setSelected(false);
					}
				}
				
					
				
				if (driverbutton.isSelected()) {
					System.out.println(user + " " + pass);
					Driver d= appFacadeInterface.loginDriver(user,pass);
					if (d!=null) { 
						MainGUI mg = new MainGUI(d);
						mg.setVisible(true);
						dispose();
					}
					else {
						textArea.setText("ERROR: Driver not found!");
						driverbutton.setSelected(false);
					}
				}
						
				
				

			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
		
				RegisterGUI r = new RegisterGUI();
				r.setVisible(true);

				
			}
		});
		
		
		userbutton.addChangeListener(new ChangeListener() {
			   
			public void stateChanged(ChangeEvent e) {
		    	if(userbutton.isSelected()) {
		    		loginButton.setEnabled(true);
		    	}
		    	else {
		    		loginButton.setEnabled(false);
		    	}
		    }
		});
		
		driverbutton.addChangeListener(new ChangeListener() {
			   
			public void stateChanged(ChangeEvent e) {
		    	if(driverbutton.isSelected()) {
		    		loginButton.setEnabled(true);
		    	}
		    	else {
		    		loginButton.setEnabled(false);
		    	}
		    }
		});
		
		
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		loginButton.setBounds(161, 319, 182, 32);
		contentPane.add(loginButton);
		
		
		registerButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		registerButton.setBounds(161, 233, 182, 32);
		contentPane.add(registerButton);
		
		JLabel lblNewLabel = new JLabel("Don't have an account?");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(157, 210, 186, 13);
		contentPane.add(lblNewLabel);
		
	}
}
