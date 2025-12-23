import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Screen extends JFrame {

	private static final long serialVersionUID = 1L;
	protected JPanel contentPane;
	private File myProfile;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String theme;
	private String font;
	private String profilePicture;
	private Map<String, String> logins;
	private Map<String, Color[]> themes;
	private ArrayList<Program> programList = new ArrayList<Program>(); 
	private ArrayList<Program> filteredProgramList = new ArrayList<Program>();

	public Screen()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);	

		themes = new HashMap<>();
		themes.put("Default", new Color[]{Color.WHITE, Color.BLACK}); // background, foreground
		themes.put("Dark", new Color[]{Color.DARK_GRAY, Color.WHITE});
		themes.put("Light Blue", new Color[]{new Color(173, 216, 230), Color.BLACK});
		themes.put("Light Pink", new Color[]{new Color(255, 182, 193), Color.BLACK});
	}

	public void processFile(File profile) 
	{
		// read the user's file and process their info 
		processProfile(profile);
		//read the user's file and process all the programs into programList 
		processProgramList(profile); 
	}

	public void processProfile(File profile)
	{
		myProfile = profile;

		try 
		{
			FileReader fileReader = new FileReader(myProfile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			while ((line != null) && !(line.contains("Program List: ")  ) ) 
			{
				// process the user's profile lines
				if (line.contains("First Name: "))
				{
					firstName = line.substring(line.lastIndexOf(":") + 2); 
				}
				else if (line.contains("Last Name: "))
				{
					lastName = line.substring(line.lastIndexOf(":") + 2);
				} 
				else if (line.contains("Username: "))
				{
					username = line.substring(line.lastIndexOf(":") + 2);
				} 
				else if (line.contains("Password: "))
				{
					password = line.substring(line.lastIndexOf(":") + 2);
				} 
				else if (line.contains("Theme: "))
				{
					theme = line.substring(line.lastIndexOf(":") + 2);
				} 
				else if (line.contains("Font: "))
				{
					font = line.substring(line.lastIndexOf(":") + 2);
				} 
				else if (line.contains("Profile Picture: "))
				{
					profilePicture = line.substring(line.indexOf(":") + 2);
				} 
				line = bufferedReader.readLine();
			}
			fileReader.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void processProgramList(File profile)
	{
		myProfile = profile;

		// TEST
		// System.out.println("List Size before processing: " + programList.size() + ", " + programList);

		try 
		{
			FileReader fileReader = new FileReader(myProfile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();

			// keep going to the next line until reach the program list label or until null (program list is empty) 
			while (line != null && !line.contains("Program List: ")) 
			{ 
				//stops on the program list line 
				line = bufferedReader.readLine(); 
			}

			while (line != null) {
				if (line.startsWith("Name: ")) {
					String name = line.substring(line.indexOf(":") + 2);

					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					// Start Date
					line = bufferedReader.readLine();
					Date startdate = null;
					try 
					{
						startdate = sdf.parse(line.substring(line.indexOf(":") + 2));
					} 
					catch (ParseException e) { e.printStackTrace(); }

					// Deadline
					line = bufferedReader.readLine();
					Date deadline = null;
					try 
					{
						deadline = sdf.parse(line.substring(line.indexOf(":") + 2));
					} 
					catch (ParseException e) { e.printStackTrace(); }

					// Category
					line = bufferedReader.readLine();
					String category = line.substring(line.indexOf(":") + 2);

					// Status
					line = bufferedReader.readLine();
					String status = line.substring(line.indexOf(":") + 2);

					// Website
					line = bufferedReader.readLine();
					String website = line.substring(line.indexOf(":") + 2);

					// Keywords
					line = bufferedReader.readLine();
					ArrayList<String> keywords = new ArrayList<>();
					String kw = line.substring(line.indexOf(":") + 2);
					if (!kw.isEmpty()) {
						keywords.addAll(Arrays.asList(kw.split(",")));
					}

					// Response (may be multiline)
					line = bufferedReader.readLine();
					StringBuilder responseBuilder = new StringBuilder();
					if (line != null && line.startsWith("Response: ")) {
						responseBuilder.append(line.substring(line.indexOf(":") + 2));

						// read until EOF or next "Name: "
						while ((line = bufferedReader.readLine()) != null &&
								!line.startsWith("Name: ")) {
							responseBuilder.append("\n").append(line);
						}
					}

					String response = responseBuilder.toString();
					programList.add(new Program(name, startdate, deadline, category, status, website, keywords, response));

					// ⬅️ IMPORTANT: do NOT consume `line` again here
					// If we broke because of "Name: ", the outer loop will handle it
					continue;
				}

				line = bufferedReader.readLine(); // advance if not a Name
			}   
			bufferedReader.close();
			fileReader.close();

			// TEST
			// System.out.println("List Size after processing: " + programList.size() + ", " + programList);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

	// read the logins file 
	// set logins equal to the text file with all the logins 	
	public void hashLogins()
	{
		logins = new HashMap<String, String>(); 

		try 
		{
			File file = new File("logins.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null && line.contains(" ")) 
			{
				String[] values = line.split(" ");
				logins.put(values[0], values[1]);
			}
			fileReader.close();

		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public boolean containsNonAlphanumeric(String input) 
	{
		return !input.matches("[a-zA-Z0-9]*");
	}
	
	public ArrayList<Program> sortPrograms(String sortBy) 
	{
	    filteredProgramList.clear();

	    if (getProgramList() != null) {
	        // Copy all programs
	        for (Program pro : getProgramList()) {
	            filteredProgramList.add(new Program(
	                pro.getName(), pro.getStartDate(), pro.getDeadline(),
	                pro.getCategory(), pro.getStatus(), pro.getWebsite(),
	                pro.getKeywords(), pro.getResponse()
	            ));
	        }

	        quickSort(filteredProgramList, 0, filteredProgramList.size() - 1, sortBy);
	    }

	    return filteredProgramList;
	}
	
	private void quickSort(ArrayList<Program> list, int low, int high, String sortBy) 
	{
	    if (low < high) 
	    {
	        int pivotIndex = partition(list, low, high, sortBy);
	        quickSort(list, low, pivotIndex - 1, sortBy);
	        quickSort(list, pivotIndex + 1, high, sortBy);
	    }
	}
	
	private int partition(ArrayList<Program> list, int low, int high, String sortBy) 
	{
	    Program pivot = list.get(high);
	    int i = low - 1;

	    for (int j = low; j < high; j++) 
	    {
	        boolean condition = false;

	        if (sortBy.equalsIgnoreCase("deadline"))
	            condition = list.get(j).getDeadline().compareTo(pivot.getDeadline()) <= 0;
	        else if (sortBy.equalsIgnoreCase("start date"))
	            condition = list.get(j).getStartDate().compareTo(pivot.getStartDate()) <= 0;
	        else if (sortBy.equalsIgnoreCase("category"))
	            condition = list.get(j).getCategory().compareToIgnoreCase(pivot.getCategory()) <= 0;
	        else if (sortBy.equalsIgnoreCase("status")) 
	            condition = list.get(j).getStatus().compareToIgnoreCase(pivot.getStatus()) <= 0;

	        if (condition) 
	        {
	            i++;
	            swap(list, i, j);
	        }
	    }

	    swap(list, i + 1, high);
	    return i + 1;
	}
	
	private void swap(ArrayList<Program> list, int i, int j) 
	{
	    Program temp = list.get(i);
	    list.set(i, list.get(j));
	    list.set(j, temp);
	}
	

	// write the programs to the document 
	public void writePrograms(File profile) {
		myProfile = profile;

		try (RandomAccessFile file = new RandomAccessFile(myProfile, "rw")) 
		{
			String line;
			long posAfterProgramList = -1;

			// Find "Program List:" line
			while ((line = file.readLine()) != null) 
			{
				if (line.startsWith("Program List:")) 
				{
					posAfterProgramList = file.getFilePointer();
					break;
				}
			}
			if (posAfterProgramList == -1) 
			{
				System.err.println("No 'Program List:' section found in file!");
				return;
			}
			// Truncate everything after "Program List:"
			file.setLength(posAfterProgramList);
			// Go back to position and append the current program list
			file.seek(posAfterProgramList);
			for (Program pro : programList) 
			{
				for (String info : pro.getProgramWritingInfo()) 
				{
					file.writeBytes(info + "\n");
				}
				file.writeBytes("\n"); // blank line between programs
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public void saveProfile(File profile, String f, String l, String u, String p, Object t, Object fo, String pp) 
	{
		myProfile = profile;

		// reload existing programs before overwrite
		programList.clear();
		processProgramList(profile);

		// if username changes, update logins + rename file
		if (u != null && p != null && (!u.equals(getUsername()) || !p.equals(getPassword()))) 
		{
			if (!u.equals(getUsername())) 
			{
				File newFile = new File("users/" + u + ".txt");
				myProfile.renameTo(newFile);
				myProfile = newFile;
				logins.remove(getUsername());
			}
			logins.put(u, p);
			setUsername(u);
			setPassword(p);
			updateLogins();
		}

		try (FileWriter writer = new FileWriter(myProfile)) 
		{
			// write profile info
			writer.write("First Name: " + f + "\n" +
					"Last Name: " + l + "\n" +
					"Username: " + u + "\n" +
					"Password: " + p + "\n" +
					"Theme: " + t + "\n" +
					"Font: " + fo + "\n" +
					"Profile Picture: " + pp + "\n" +
					"Program List: \n");

			setFirstName(f);
			setLastName(l);
			setTheme((String) t);
			setFont(fo);
			setProfilePicture(pp);

			writer.flush(); // make sure header is written

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		// append the preserved programs
		writePrograms(myProfile);
	}

	public void addProgram(File profile, Program program)
	{
		programList.add(program);
		writePrograms(profile); 
	}

	public void updateLogins()
	{
		// copy everything in the map to the file 
		try
		{   
			// clear the file, write everything from the logins map to the file 
			RandomAccessFile file = new RandomAccessFile("logins.txt", "rw");

			file.setLength(0); 

			for(String key: logins.keySet())
			{
				file.writeBytes(key + " " + logins.get(key) + "\n");
			}
			file.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public boolean hasValidImageExtension(String path) 
	{
		String[] extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
		String lowerPath = path.toLowerCase();
		for (String ext : extensions) 
		{
			if (lowerPath.endsWith(ext)) 
			{
				return true;
			}
		}
		return false;
	}

	public boolean isValidLocalImage(String path) 
	{
		if (!hasValidImageExtension(path)) 
		{
			return false;
		}

		try 
		{
			BufferedImage img = ImageIO.read(this.getClass().getResource(path));
			return img != null;
		} 
		catch (Exception e) 
		{
			return false;
		}
	}

	public boolean usernameTaken(String usernameToCheck, String currentUser) 
	{
		return logins.containsKey(usernameToCheck) && !usernameToCheck.equals(currentUser);
	}


	public boolean sameDay(Date d1, Date d2) 
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
	}

	public void applyTheme(String themeName) 
	{
		Color[] colors = themes.getOrDefault(themeName, themes.get("Default"));
		Color bg = colors[0];
		Color fg = colors[1];

		contentPane.setBackground(bg);

		// loop through all components and change background/foreground
		for (Component c : contentPane.getComponents()) 
		{
			if (c instanceof JLabel || c instanceof JTextField || c instanceof JTextArea 
					|| c instanceof JComboBox || c instanceof JTable || c instanceof JButton) 
			{
				c.setBackground(bg);
				c.setForeground(fg);

			}
			if (c instanceof JTable) 
			{
				((JTable) c).setBackground(bg);
				((JTable) c).setForeground(fg);
				((JTable) c).getTableHeader().setBackground(bg.darker());
				((JTable) c).getTableHeader().setForeground(fg);
			}
		}
	}
	
	public void applyTheme() 
	{
		applyTheme(getTheme());
	}

	private void applyFontToComponent(Component c, String fontName) 
	{
		Font font = new Font(fontName, Font.PLAIN, 12);
		if (c instanceof JComponent) 
		{
			c.setFont(font);
		}
		if (c instanceof Container) 
		{
			for (Component child : ((Container) c).getComponents()) 
			{
				applyFontToComponent(child, fontName);
			}
		}
	}

	public void applyFont(String font) 
	{
		if (contentPane != null) 
		{
			applyFontToComponent(contentPane, font);
			contentPane.revalidate();
			contentPane.repaint();
		}
	}

	// Getters
	public String getFirstName() 
	{
		return firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}

	public String getUsername() 
	{
		return username;
	}

	public String getPassword() 
	{
		return password;
	}

	public String getTheme() 
	{
		return theme;
	}

	public String getFontName() 
	{
		return font;
	}

	public String getProfilePicture() 
	{
		return profilePicture;
	}

	public File getMyProfile() 
	{
		return myProfile;
	}

	public ArrayList<Program> getProgramList() 
	{
		return programList;
	}

	public ArrayList<Program> getFilteredProgramList() 
	{
		return filteredProgramList;
	}

	public JPanel getContentPane()
	{
		return contentPane; 
	}

	public Map<String, String> getLogins()
	{
		return logins; 
	}

	// Setters
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public void setTheme(String theme) 
	{
		this.theme = theme;
	}

	public void setFont(Object fo) 
	{
		this.font = (String) fo;
	}

	public void setProfilePicture(String profilePicture) 
	{
		this.profilePicture = profilePicture;
	}

	public void setMyProfile(File myProfile) 
	{
		this.myProfile = myProfile;
	}

	public void setProgramList(ArrayList<Program> programList) 
	{
		this.programList = programList;
	}

	public void setFilteredProgramList(ArrayList<Program> filteredProgramList) 
	{
		this.filteredProgramList = filteredProgramList;
	}

	public void setLogins(String user, String pass)
	{
		logins.put(user, pass);
	}


}
