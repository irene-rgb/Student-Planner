import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import javax.swing.JTable;

public class CalendarScreen extends Screen {

	private static final long serialVersionUID = 1L;
	// private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JComboBox<String> sortComboBox;

	/**
	 * Create the frame. 
	 */
	public CalendarScreen(File profile) 
	{
		// Set up side menu bar 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 642);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JButton homeButton = new JButton();
		homeButton.setBounds(22, 11, 96, 66);
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
		contentPane.setLayout(null);
		homeButton.setFont(new Font(getFontName(), Font.PLAIN, 40));
		contentPane.add(homeButton);

		JButton ssButton = new JButton();
		ssButton.setBounds(22, 88, 96, 66);
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
		contentPane.add(ssButton);

		JButton calendarButton = new JButton();
		calendarButton.setBounds(22, 165, 96, 66);
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
		calendarButton.setEnabled(false);
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
		contentPane.add(calendarButton);

		/* 
		 * START 
		 */

		processFile(profile);

		
		JLabel calendarLabel = new JLabel("Calendar");
		calendarLabel.setBounds(355, 11, 132, 39);
		calendarLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
		calendarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(calendarLabel);

		JButton addButton = new JButton("Add");
		addButton.setBounds(616, 48, 89, 29);
		addButton.setFont(new Font(getFontName(), Font.PLAIN, 16));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				setVisible(false); 

				ProgramWindow pw = new ProgramWindow(profile, new Program());
				pw.setVisible(true);

				dispose();
			}
		});
		contentPane.add(addButton);

		JMonthChooser monthChooser = new JMonthChooser();
		monthChooser.setBounds(128, 48, 144, 29);
		monthChooser.setFont(new Font(getFontName(), Font.PLAIN, 18));
		contentPane.add(monthChooser);

		JYearChooser yearChooser = new JYearChooser();
		yearChooser.setBounds(282, 48, 96, 29);
		yearChooser.setFont(new Font(getFontName(), Font.PLAIN, 18));
		contentPane.add(yearChooser);

		sortComboBox = new JComboBox<>(new String[] {"Deadline", "Start Date"});
		sortComboBox.setFont(new Font(getFontName(), Font.PLAIN, 18));
		sortComboBox.setBounds(462, 48, 144, 29);
		contentPane.add(sortComboBox);
		sortComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String selection = (String) sortComboBox.getSelectedItem();
				sortPrograms(selection); 
				populateCalendar(monthChooser.getMonth(), yearChooser.getYear());
			}
		});

		/*
		System.out.println("month: " + monthChooser.getMonth());
		System.out.println("year: " + yearChooser.getYear()); 
		 */

		String[] columns = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		Object[][] data = new Object[6][7]; // up to 6 weeks shown
		model = new DefaultTableModel(data, columns) {
			public boolean isCellEditable(int row, int col) {
				return false; // prevent direct editing
			}
		};

		table = new JTable(model);
		table.setRowHeight(80); 
		table.setBounds(289, 129, 489, 480);
		table.setFont(new Font(getFontName(), Font.PLAIN, 18));
		contentPane.add(table);

		// Disable resizing for all columns
		table.getTableHeader().setResizingAllowed(false);

		// Prevent reordering of columns
		table.getTableHeader().setReorderingAllowed(false);
		
		table.getTableHeader().setFont(new Font(getFontName(), Font.BOLD, 12));

		// Wrap table in a scroll pane to show the header
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(128, 88, 577, 507);
		contentPane.add(scrollPane);
		
		JLabel instructionLabel = new JLabel("");
		instructionLabel.setBounds(22, 242, 46, 14);
		contentPane.add(instructionLabel);

		monthChooser.addPropertyChangeListener("month", evt -> populateCalendar(monthChooser.getMonth(), yearChooser.getYear()));
		yearChooser.addPropertyChangeListener("year", evt -> populateCalendar(monthChooser.getMonth(), yearChooser.getYear()));

		populateCalendar(monthChooser.getMonth(), yearChooser.getYear());

		table.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mouseClicked(java.awt.event.MouseEvent evt) 
			{
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				Object value = table.getValueAt(row, col);
				if (value != null && !value.toString().isEmpty()) 
				{
					int day = extractDayFromCell(value.toString());
					// System.out.println("Date: "+ day);
					// extract date might be wrong 
					if (day != -1) 
					{
						Date clickedDate = new GregorianCalendar(yearChooser.getYear(), monthChooser.getMonth(), day).getTime();

						//this part should open spreadsheet screen but only show the programs with the same deadline 
						SpreadsheetScreen ss = new SpreadsheetScreen(profile);
						ss.setVisible(true);
						dispose(); // closes the calendar
						ss.dayView(clickedDate, (String) sortComboBox.getSelectedItem()); 
					
					}
				}
			}
		});


		applyTheme(getTheme()); 

		applyFont(getFontName()); 

	}

	private void populateCalendar(int month, int year) 
	{
	    model.setRowCount(0); // clear all old rows

	    // Determine first day of month
	    Calendar cal = Calendar.getInstance();
	    cal.set(year, month, 1);
	    int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday=0
	    int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

	    int day = 1;
	    for (int row = 0; row < 6; row++) 
	    {
	        Object[] week = new Object[7];
	        for (int col = 0; col < 7; col++) 
	        {
	            if (row == 0 && col < firstDayOfWeek) 
	            {
	                week[col] = "";
	            } 
	            else if (day <= daysInMonth) 
	            {
	                // Build cell text
	                StringBuilder cellText = new StringBuilder("<html>" + day);
	                int count = 0;
	                String firstProgram = null;
	                boolean empty = true; 

	                Date cellDate = new GregorianCalendar(year, month, day).getTime();
	                for (Program p : getProgramList()) 
	                {
	                    String selection = (String) sortComboBox.getSelectedItem();

	                    if ("Deadline".equals(selection) && sameDay(p.getDeadline(), cellDate)) 
	                    {
	                        count++;
	                        if (firstProgram == null) 
	                        	firstProgram = p.getName();
	                        
	                        empty = false; 
	                    } 
	                    else if ("Start Date".equals(selection) && sameDay(p.getStartDate(), cellDate)) {
	                        count++;
	                        if (firstProgram == null) 
	                        	firstProgram = p.getName();
	                        
	                        empty = false; 
	                    }
	                }

	                if (count > 0) 
	                {
	                    cellText.append("<br><font color='blue'>").append(firstProgram).append("</font>");
	                    if (count > 1) 
	                    {
	                        cellText.append("<br><font color='gray'>+ ").append(count - 1).append(" more</font>");
	                    }
	                }
	                
	                if (empty)
	                	cellText.append("<br><br><br>");

	                cellText.append("</html>");
	                week[col] = cellText.toString();
	                day++;
	            } 
	            else 
	            {
	                week[col] = "";
	            }
	        }
	        model.addRow(week); // add the whole week as a row
	    }
	}


	// Helper: parse the day number from cell HTML
	private int extractDayFromCell(String cellText) 
	{
		try 
		{
	        // Replace <br> with a newline, then remove all other HTML
	        String noHtml = cellText.replaceAll("(?i)<br>", "\n").replaceAll("<.*?>", "");
	        
	        // Split into lines: first line will be just the day number
	        String[] lines = noHtml.split("\n");
	        return Integer.parseInt(lines[0].trim());
		} 
		catch (Exception e) 
		{
			return -1;
		}
	}


}
