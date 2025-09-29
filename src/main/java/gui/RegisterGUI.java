package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import businesslogic.*;

import javax.swing.JToolBar;
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

public class RegisterGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	private JButton registerButton = new JButton("Register");
	
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel passwordLabel = new JLabel("Password:");
	
	private JLabel usertipe = new JLabel("User Tipe:");
	private JRadioButton userbutton = new JRadioButton("User");
	private JRadioButton driverbutton = new JRadioButton("Driver");
	
	private final JLabel emailLabel = new JLabel("Email:");
	private JPasswordField repassword;
	private JTextField email;
	
	private JButton back = new JButton("RETURN");
	
//	private BLFacadeImplementation bl = new BLFacadeImplementation();
	
//	public static BLFacade getBusinessLogic(){
//		return bl;
//	}
//	 
//	public static void setBussinessLogic (BLFacadeImplementation b){
//		bl=b;
//	}
//	
	
	public RegisterGUI() {
		setTitle("Register");
		
		BLFacade appFacadeInterface = LoginGUI.getBusinessLogic();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		username = new JTextField();
		username.setBounds(191, 76, 205, 19);
		contentPane.add(username);
		username.setColumns(10);
		
		
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		usernameLabel.setBounds(48, 73, 142, 19);
		contentPane.add(usernameLabel);
		
		
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		passwordLabel.setBounds(48, 133, 154, 19);
		contentPane.add(passwordLabel);
		
		JLabel usertipe = new JLabel("User Tipe:");
		usertipe.setFont(new Font("Tahoma", Font.PLAIN, 17));
		usertipe.setBounds(48, 288, 154, 19);
		contentPane.add(usertipe);
				
		
		buttonGroup.add(userbutton);
		userbutton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		userbutton.setBounds(263, 288, 103, 21);
		contentPane.add(userbutton);
		
		
		buttonGroup.add(driverbutton);
		driverbutton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		driverbutton.setBounds(161, 288, 103, 21);
		contentPane.add(driverbutton);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(SystemColor.menu);
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setForeground(Color.RED);
		textArea.setBounds(129, 361, 237, 32);
		contentPane.add(textArea);
		
		password = new JPasswordField();
		password.setBounds(191, 136, 205, 20);
		contentPane.add(password);
		registerButton.setEnabled(false);
		
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String user = username.getText();
				String ema = email.getText();
				String pass = new String(password.getPassword());
				String repass = new String(repassword.getPassword());
				
				if(!pass.equals(repass)) {
					textArea.setText("ERROR: passwords don't match");
				}
				
				else if(userbutton.isSelected()) {
					Boolean b = appFacadeInterface.registerUser(user,ema,pass);	
					
					if(b) {
						registerButton.setEnabled(false);
						textArea.setForeground(new Color(0,255,0));
						textArea.setText("Successfully registered!");
					}
					else {
						textArea.setText("ERROR: User already exists!");
					}	
				}
				else if(driverbutton.isSelected()) {
					Boolean b = appFacadeInterface.registerDriver(user,ema,pass);	
					
					if(b) {
						registerButton.setEnabled(false);
						textArea.setForeground(new Color(0,255,0));
						textArea.setText("Successfully registered!");
					}
					else {
						textArea.setText("ERROR: Driver already exists!");
					}	
				}
				
			}
		});
		
		back.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//LoginGUI log = new LoginGUI();
				//log.setVisible(true);
				dispose();
				
			}
		});
		
		userbutton.addChangeListener(new ChangeListener() {
			   
			public void stateChanged(ChangeEvent e) {
		    	if(userbutton.isSelected()) {
		    		registerButton.setEnabled(true);
		    	}
		    	else {
		    		registerButton.setEnabled(false);
		    	}
		    }
		});
		
		driverbutton.addChangeListener(new ChangeListener() {
			   
			public void stateChanged(ChangeEvent e) {
		    	if(driverbutton.isSelected()) {
		    		registerButton.setEnabled(true);
		    	}
		    	else {
		    		registerButton.setEnabled(false);
		    	}
		    }
		});
		
		
		registerButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		registerButton.setBounds(161, 319, 182, 32);
		contentPane.add(registerButton);
		emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		emailLabel.setBounds(48, 104, 142, 19);
		
		contentPane.add(emailLabel);
		
		repassword = new JPasswordField();
		repassword.setBounds(191, 162, 205, 20);
		contentPane.add(repassword);
		
		JLabel repasswordLabel = new JLabel("Repeat password:");
		repasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		repasswordLabel.setBounds(48, 162, 154, 19);
		contentPane.add(repasswordLabel);
		
		email = new JTextField();
		email.setColumns(10);
		email.setBounds(191, 105, 205, 19);
		contentPane.add(email);
		
		
		back.setFont(new Font("Tahoma", Font.PLAIN, 11));
		back.setBounds(10, 10, 87, 21);
		contentPane.add(back);
		
	}
}
