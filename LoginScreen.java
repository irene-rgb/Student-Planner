import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;
import javax.swing.JPasswordField;

public class LoginScreen extends Screen {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPasswordField passwordField;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginScreen() {

		super(); 
		hashLogins(); 

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 375, 205);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel loginLabel = new JLabel("Login");
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		loginLabel.setBounds(65, 11, 243, 27);
		contentPane.add(loginLabel);

		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setBounds(10, 52, 118, 14);
		contentPane.add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 77, 103, 14);
		contentPane.add(passwordLabel);

		JButton signupButton = new JButton("Sign Up");
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				//open the signup window and close the login window
				setVisible(false); 

				SignupScreen ss = new SignupScreen();
				ss.setVisible(true);
				dispose();
			}
		});
		signupButton.setBounds(213, 134, 103, 23);
		contentPane.add(signupButton);

		JButton loginButton = new JButton("Log In");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String username = usernameTextField.getText();
				char[] pwChars = passwordField.getPassword();
				String password = new String(pwChars);

				if (getLogins().containsKey(username) && getLogins().get(username).equals(password))
				{
					setVisible(false); 
					File file = new File("users/" + username + ".txt");
					HomeScreen hs = new HomeScreen(file);
					hs.setVisible(true);
					dispose();
				}

				// get info from the login fields, if they can be found in the logins.txt file and the key matches the value, open the home screen and 
				// display the correct information based on that user's file 
				// open homescreen and display accurate info 
				else
				{
					// display error message (JMessageDialog?) 
					JOptionPane.showMessageDialog(null, "Invalid login!", "Login Error",  JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		loginButton.setBounds(140, 99, 103, 23);
		contentPane.add(loginButton);

		JLabel signupLabel = new JLabel("Don't Have an Account?");
		signupLabel.setBounds(53, 138, 177, 14);
		contentPane.add(signupLabel);

		usernameTextField = new JTextField();
		usernameTextField.setBounds(106, 49, 243, 20);
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(106, 74, 243, 20);
		contentPane.add(passwordField);

	}
}
