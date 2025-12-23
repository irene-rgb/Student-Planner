import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ProfileWindow extends Screen {

	private static final long serialVersionUID = 1L;
	// private JPanel contentPane;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	private JTextField profilePictureTextField;

	/**
	 * Create the frame.
	 */
	public ProfileWindow(File profile) 
	{
		super(); 
		hashLogins(); 
		
		processFile(profile);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 236);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel editProfileLabel = new JLabel("Edit Profile");
		editProfileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		editProfileLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
		editProfileLabel.setBounds(121, 11, 243, 27);
		contentPane.add(editProfileLabel);

		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setBounds(10, 49, 95, 14);
		contentPane.add(firstNameLabel);

		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setBounds(242, 49, 80, 14);
		contentPane.add(lastNameLabel);

		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setBounds(10, 74, 110, 14);
		contentPane.add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(242, 74, 80, 14);
		contentPane.add(passwordLabel);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// use JOptionPane to do popup 
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel? This will disgard all unsaved changes.", "Cancel Confirmation", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION)
				{
					setVisible(false); 

					try 
					{
						HomeScreen hs = new HomeScreen(getMyProfile());
						hs.setVisible(true);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					dispose();
				}
				else
				{
					// close the popup and do nothing 
				}
			}
		});
		cancelButton.setBounds(140, 158, 103, 23);
		contentPane.add(cancelButton);

		JLabel themeLabel = new JLabel("Theme:");
		themeLabel.setBounds(10, 102, 95, 14);
		contentPane.add(themeLabel);

		JLabel fontLabel = new JLabel("Font:");
		fontLabel.setBounds(242, 102, 80, 14);
		contentPane.add(fontLabel);

		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(83, 43, 152, 20);
		contentPane.add(firstNameTextField);
		firstNameTextField.setColumns(10);
		firstNameTextField.setText(getFirstName());

		lastNameTextField = new JTextField();
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(308, 43, 152, 20);
		contentPane.add(lastNameTextField);
		lastNameTextField.setText(getLastName());

		usernameTextField = new JTextField();
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(83, 71, 152, 20);
		contentPane.add(usernameTextField);
		usernameTextField.setText(getUsername());

		passwordTextField = new JTextField();
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(308, 71, 152, 20);
		contentPane.add(passwordTextField);
		passwordTextField.setText(getPassword());

		String[] options = {"Default", "Dark", "Light Blue", "Light Pink"};
		JComboBox<String> themeComboBox = new JComboBox<>(options);
		themeComboBox.setModel(new DefaultComboBoxModel(options));
		themeComboBox.setBounds(71, 99, 164, 22);
		contentPane.add(themeComboBox);
		themeComboBox.setSelectedItem(getTheme());

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();
		JComboBox<String> fontComboBox = new JComboBox<>(fontNames);
		fontComboBox.setBounds(296, 99, 164, 22);
		contentPane.add(fontComboBox);
		fontComboBox.setSelectedItem(getFontName());

		JLabel profilePictureLabel = new JLabel("Profile Picture:");
		profilePictureLabel.setBounds(10, 127, 164, 14);
		contentPane.add(profilePictureLabel);

		profilePictureTextField = new JTextField();
		profilePictureTextField.setText((String) null);
		profilePictureTextField.setColumns(10);
		profilePictureTextField.setBounds(106, 127, 354, 20);
		contentPane.add(profilePictureTextField);
		profilePictureTextField.setText(getProfilePicture());

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// determine if valid 
				if(containsNonAlphanumeric(usernameTextField.getText()) || containsNonAlphanumeric(passwordTextField.getText()))
				{
					JOptionPane.showMessageDialog(null, "Username and Passwords can only contain letters or numbers.", "Username or Password Error",  JOptionPane.INFORMATION_MESSAGE);
				}
				// check the username is not taken (but make sure that it isn't this current persons' login)
				else if(usernameTaken(usernameTextField.getText(), getUsername()))
				{
					JOptionPane.showMessageDialog(null, "That username is taken! Please choose another one.", "Username Error",  JOptionPane.INFORMATION_MESSAGE);
				}
				// this check doesn't work 
				else if ( !isValidLocalImage(profilePictureTextField.getText()))
				{
					JOptionPane.showMessageDialog(null, "That profile picture is invalid! Please choose another one.", "Profile Picture Error",  JOptionPane.INFORMATION_MESSAGE);
				}
				// make sure all fields have a selection 
				else if (firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("") ||
				usernameTextField.getText().equals("") || passwordTextField.getText().equals("") ||
				themeComboBox.getSelectedItem().equals("") || fontComboBox.getSelectedItem().equals("") ||
				profilePictureTextField.getText().equals("")) 
				{
					JOptionPane.showMessageDialog(null, "Please make sure all fields are filled out.", "Empty Field Error",  JOptionPane.INFORMATION_MESSAGE);
				}
				else 
				{

					// make sure first and last names are capitalized (make a separate method?)
					
					String firstName = firstNameTextField.getText();
					firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
					
					String lastName = lastNameTextField.getText();
					lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
					
					
					saveProfile(profile, firstName, lastName, 
							usernameTextField.getText(), passwordTextField.getText(), 
							themeComboBox.getSelectedItem(), fontComboBox.getSelectedItem(),
							profilePictureTextField.getText());

					JOptionPane.showMessageDialog(null, "Profile successfully saved!", "Success!",  JOptionPane.INFORMATION_MESSAGE);

					setVisible(false); 

					try 
					{
						HomeScreen hs = new HomeScreen(getMyProfile());
						hs.setVisible(true);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					dispose();
				}
			}
		});
		saveButton.setBounds(261, 158, 103, 23);
		contentPane.add(saveButton);
		


		applyTheme(getTheme()); 

		applyFont(getFontName()); 
	}
	
}
