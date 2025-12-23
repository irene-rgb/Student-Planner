import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class HomeScreen extends Screen {

	private static final long serialVersionUID = 1L;
	private int index; 
	// private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public HomeScreen(File profile) 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 643, 381);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);	

		JButton homeButton = new JButton();
		try 
		{
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("homeicon.png"));
			ImageIcon icon = new ImageIcon(myImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			homeButton.setIcon(icon);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		homeButton.setEnabled(false);

		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				setVisible(false); 

				File file = new File("users/" + getUsername() + ".txt");

				HomeScreen hs = new HomeScreen(file);
				hs.setVisible(true);

				dispose();

			}
		});
		homeButton.setFont(new Font(getFontName(), Font.PLAIN, 40));
		homeButton.setBounds(22, 11, 96, 66);
		contentPane.add(homeButton);

		JButton ssButton = new JButton();
		try 
		{
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("spreadsheeticon.png"));
			ImageIcon icon = new ImageIcon(myImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			ssButton.setIcon(icon);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ssButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// open spreadsheetscreen and close home 
				setVisible(false); 

				File file = new File("users/" + getUsername() + ".txt");

				SpreadsheetScreen ss = new SpreadsheetScreen(file);
				ss.setVisible(true);
				dispose();
			}
		});
		ssButton.setBounds(22, 88, 96, 66);
		contentPane.add(ssButton);

		JButton calendarButton = new JButton();
		try 
		{
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("calendaricon.png"));
			ImageIcon icon = new ImageIcon(myImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			calendarButton.setIcon(icon);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// open calendarscreen and close home
				setVisible(false); 

				File file = new File("users/" + getUsername() + ".txt");

				CalendarScreen cs = new CalendarScreen(file);
				cs.setVisible(true);

				dispose();

			}
		});
		calendarButton.setBounds(22, 165, 96, 66);
		contentPane.add(calendarButton);

		processFile(profile);

		JLabel homeLabel = new JLabel("Home");
		homeLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
		homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		homeLabel.setBounds(315, 11, 67, 39);
		contentPane.add(homeLabel);

		JLabel welcomeLabel = new JLabel("Welcome, " + getFirstName() + " " + getLastName() + "!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
		welcomeLabel.setBounds(212, 55, 282, 22);
		contentPane.add(welcomeLabel);

		JButton editButton = new JButton("Edit");
		editButton.setFont(new Font(getFontName(), Font.PLAIN, 16));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				setVisible(false); 
				ProfileWindow pw = new ProfileWindow(profile);
				pw.setVisible(true);
				dispose();
			}
		});
		editButton.setBounds(476, 85, 114, 29);
		contentPane.add(editButton);

		JButton logoutButton = new JButton("Log Out");
		logoutButton.setFont(new Font(getFontName(), Font.PLAIN, 16));
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// use JOptionPane to do popup 
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log Out Confirmation", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION)
				{
					// log out and open log in screen
					setVisible(false); 

					LoginScreen ls = new LoginScreen();
					ls.setVisible(true);

					dispose();
				}
				else
				{
					// close the popup and do nothing 
				}
			}
		});
		logoutButton.setBounds(476, 125, 114, 29);
		contentPane.add(logoutButton);

		if (getProfilePicture() == null || !isValidLocalImage(getProfilePicture()))
		{
			setProfilePicture("defaultProfileIcon.jpg");
			saveProfile(profile, getFirstName(), getLastName(), getUsername(), getPassword(), (Object) getTheme(), (Object) getFont(), getProfilePicture()); 
		}

		JLabel profilePicture = new JLabel();
		try 
		{
			BufferedImage pfp = ImageIO.read(this.getClass().getResource(getProfilePicture()));
			ImageIcon icon = new ImageIcon(pfp.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
			profilePicture.setIcon(icon);

			profilePicture.setForeground(new Color(0, 0, 0));
			profilePicture.setBounds(222, 88, 129, 103);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}

		contentPane.add(profilePicture);

		// write all the programs to the file 

		if (getProgramList() != null && !getProgramList().isEmpty())
		{ 
			JLabel upcomingLabel = new JLabel("Next Upcoming Program:");
			upcomingLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
			upcomingLabel.setBounds(148, 202, 282, 29);
			contentPane.add(upcomingLabel);

			setProgramList(sortPrograms("deadline")); 
			// get current date and the deadline and count the number of days difference 
			LocalDate today = LocalDate.now();
			int upcomingIndex = -1;
			long daysDiff = 0;

			for (int i = 0; i < getProgramList().size(); i++) {
				LocalDate deadline = getProgramList().get(i).getDeadline()
						.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate();

				long diff = ChronoUnit.DAYS.between(today, deadline);

				if (diff >= 0) {
					upcomingIndex = i;
					daysDiff = diff;
					break; // found the next upcoming program
				}
			}

			if (upcomingIndex == -1) 
			{
				upcomingLabel.setText("No upcoming programs.");
			} 
			else 
			{  
				Program nextProgram = getProgramList().get(upcomingIndex);

				JLabel nameLabel = new JLabel("Name: " + nextProgram.getName());
				nameLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
				nameLabel.setBounds(148, 241, 234, 29);
				contentPane.add(nameLabel);

				JLabel deadlineLabel = new JLabel("Deadline: " + nextProgram.getDeadlinePrint());
				deadlineLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
				deadlineLabel.setBounds(418, 241, 199, 29);
				contentPane.add(deadlineLabel);

				JLabel countdownLabel = new JLabel(daysDiff == 0 ? "Due TODAY!" : daysDiff + " days remaining!"); //shorter way of doing an if else to determine the text displayed
				countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
				countdownLabel.setBounds(430, 208, 143, 23);
				contentPane.add(countdownLabel);

				JButton goToButton = new JButton("Go To Program");
				goToButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) 
					{
						setVisible(false); 

						ProgramWindow pw = new ProgramWindow(profile, nextProgram);
						// disable spreadsheet while program window is open
						setEnabled(false);

						// re-enable spreadsheet when program window closes
						pw.addWindowListener(new java.awt.event.WindowAdapter() {
							@Override
							public void windowClosed(java.awt.event.WindowEvent e) {
								HomeScreen.this.setEnabled(true);
								HomeScreen.this.toFront(); // bring it back to focus
							}
						});

						pw.setVisible(true);

						dispose();
					}
				});
				goToButton.setFont(new Font(getFontName(), Font.PLAIN, 18));
				goToButton.setBounds(252, 308, 217, 23);
				contentPane.add(goToButton);
			}
		}

		applyTheme(getTheme()); 

		applyFont(getFontName()); 
	}
}
