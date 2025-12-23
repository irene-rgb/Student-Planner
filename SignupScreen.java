import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SignupScreen extends Screen {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField usernameTextField;
	private JTextField passwordTextField;



	/**
	 * Create the frame.
	 */
	public SignupScreen() {
		
		super(); 
		hashLogins(); 

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
		@Override
		public void windowClosing(java.awt.event.WindowEvent e) {
		LoginScreen ls = new LoginScreen();
		ls.setVisible(true);

		dispose(); // now close this window
		}
		});
		setBounds(100, 100, 320, 224);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel signupLabel = new JLabel("Sign Up");
		signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
		signupLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		signupLabel.setBounds(36, 11, 243, 27);
		contentPane.add(signupLabel);

		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setBounds(10, 99, 97, 14);
		contentPane.add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 124, 97, 14);
		contentPane.add(passwordLabel);

		JButton signupButton = new JButton("Sign Up");
		signupButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// capitalize the first letter of the first and last names 
				String firstName = firstNameTextField.getText();
				firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
				
				String lastName = lastNameTextField.getText();
				lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
				
				String username = usernameTextField.getText();
				String password = passwordTextField.getText();
				
				// check that all fields are filled out correctly 
				
				// check that the username and passwords DO NOT HAVE SPACES!!! invalid if have spaces
				if(containsNonAlphanumeric(username) || containsNonAlphanumeric(password))
				{
					JOptionPane.showMessageDialog(null, "Username and Passwords can only contain letters or numbers.", "Username or Password Error",  JOptionPane.INFORMATION_MESSAGE);
				}
				// check the username is not taken
				else if(getLogins().containsKey(username))
				{
					// for whatever reason, the logins did not update 
					// System.out.println(getLogins());
					JOptionPane.showMessageDialog(null, "That username is taken! Please choose another one.", "Username Error",  JOptionPane.INFORMATION_MESSAGE);
				}
				else 
				{
					//put the person in the map, and then write to file 
					setLogins(username, password);
					
					// write the user to the file 
					try 
					{
						FileWriter writer = new FileWriter("logins.txt", true);
						writer.write(username + " " + password + "\n");
						writer.close();		
					} 
					catch (IOException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try 
					{
						File userFile = new File("users/" + username + ".txt");
						userFile.createNewFile();
						
						setFont("Tahoma");
						
						FileWriter writer = new FileWriter(userFile);
						writer.write("First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n" + "Username: " + username + "\n" + "Password: " + password + "\n" + "Theme: Default" + "\n" 
						+ "Font: Tahoma" + "\n" + "Profile Picture: defaultProfileIcon.jpg" + "\n" + "Program List: " + "\n");
						writer.close();		

						//clear the fields 
						firstNameTextField.setText("");
						lastNameTextField.setText("");
						usernameTextField.setText("");
						passwordTextField.setText("");
						
						//close the signup screen and open the homescreen
						setVisible(false); 
						
						HomeScreen hs = new HomeScreen(userFile);
						hs.setVisible(true);
						
						dispose(); 
					} 
					catch (IOException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					catch (Exception e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		signupButton.setBounds(110, 151, 103, 23);
		contentPane.add(signupButton);

		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setBounds(10, 74, 97, 14);
		contentPane.add(lastNameLabel);

		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setBounds(10, 49, 97, 14);
		contentPane.add(firstNameLabel);

		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(88, 45, 206, 20);
		contentPane.add(firstNameTextField);
		firstNameTextField.setColumns(10);

		lastNameTextField = new JTextField();
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(88, 70, 206, 20);
		contentPane.add(lastNameTextField);

		usernameTextField = new JTextField();
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(88, 95, 206, 20);
		contentPane.add(usernameTextField);

		passwordTextField = new JTextField();
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(88, 120, 206, 20);
		contentPane.add(passwordTextField);


	}
}