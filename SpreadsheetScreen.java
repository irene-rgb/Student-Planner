import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class SpreadsheetScreen extends Screen {

	private static final long serialVersionUID = 1L;
	// private JPanel contentPane;
	private JTextField searchBar;
	private JTable table;
	private DefaultTableModel model; 
	private boolean searched;
	private JButton resetButton;
	private JLabel spreadsheetLabel;

	/**
	 * Create the frame.
	 */
	public SpreadsheetScreen(File profile) 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 907, 438);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);	

		//contentPane = new JPanel(new BorderLayout());


		JButton homeButton = new JButton();
		try 
		{
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("homeicon.png"));
			ImageIcon icon = new ImageIcon(myImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			homeButton.setIcon(icon);
		} 
		catch (IOException e) 
		{

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

			e.printStackTrace();
		}
		ssButton.setEnabled(false);
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

		spreadsheetLabel = new JLabel("Spreadsheet");
		spreadsheetLabel.setFont(new Font(getFontName(), Font.PLAIN, 18));
		spreadsheetLabel.setHorizontalAlignment(SwingConstants.CENTER);
		spreadsheetLabel.setBounds(323, 11, 379, 39);
		contentPane.add(spreadsheetLabel);

		JButton addButton = new JButton("Add");
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
		addButton.setBounds(779, 41, 102, 29);
		contentPane.add(addButton);

		String[] headings = {"Name", "Start Date", "Deadline", "Category", "Status", "Website", "Keywords"};
		model = new DefaultTableModel(headings, 0)
		{
			public boolean isCellEditable(int row, int col)
			{
				return false;
			}
		};

		table = new JTable(model);
		table.setFont(new Font(getFontName(), Font.PLAIN, 12));
		table.getTableHeader().setFont(new Font(getFontName(), Font.BOLD, 12));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(128, 69, 753, 319);
		contentPane.add(scrollPane);
		setTableFormatting();

		sortPrograms("deadline"); 
		searched = true; 

		displayTable(); 



		JComboBox<String> sortComboBox = new JComboBox<>(new String[] {"Deadline", "Start Date", "Category", "Status"});
		sortComboBox.setFont(new Font(getFontName(), Font.PLAIN, 18));
		sortComboBox.setBounds(601, 44, 149, 22);
		contentPane.add(sortComboBox);
		sortComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String selection = (String) sortComboBox.getSelectedItem();

				sortPrograms(selection); 

				searched = true; 
				model.setRowCount(0);

				displayTable();

			}
		});

		searchBar = new JTextField();
		searchBar.setText("Search by Keyword");
		searchBar.setForeground(Color.GRAY);
		// this should work very similarly to what is in the original program 
		searchBar.setFont(new Font(getFontName(), Font.PLAIN, 12));
		searchBar.setBounds(128, 45, 206, 20);
		contentPane.add(searchBar);
		searchBar.setColumns(10);

		resetButton = new JButton("Reset Table");
		resetButton.setVisible(false);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sortPrograms("deadline"); 

				searched = true; 
				model.setRowCount(0);

				searchBar.setForeground(Color.GRAY);
				searchBar.setText("Search");

				spreadsheetLabel.setText("Spreadsheet");
				
				displayTable(); 

				resetButton.setVisible(false);
			}
		});
		resetButton.setBounds(128, 23, 102, 20);
		contentPane.add(resetButton);

		JButton searchButton = new JButton("Search");
		searchButton.setFont(new Font(getFontName(), Font.PLAIN, 16));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String input = searchBar.getText().toLowerCase();

				setFilteredProgramList( findPrograms(input) );  

				model.setRowCount(0);

				displayTable(); 
				searched = true;

				resetButton.setVisible(true);
			}
		});
		searchButton.setBounds(348, 44, 89, 23);
		contentPane.add(searchButton);

		table.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				if(! SwingUtilities.isRightMouseButton(event))
				{
					int row = table.getSelectedRow();
					Program pro = getProgramList().get(row); 
					if (searched)
						pro = getFilteredProgramList().get(row);
					ProgramWindow pw = new ProgramWindow(profile, pro);
					pw.setVisible(true);
					dispose();
				}
			}
		}); 

		// this is for if you searched something and need to use a different list because it should only show the filtered list
		applyTheme(getTheme()); 
		applyFont(getFontName()); 


	}

	private void setTableFormatting()
	{
		// the table is way too small right now 
		// TODO: how can you allow the user to rescale the size of the table? 

		TableColumnModel columnModel = table.getColumnModel();

		// to center columns
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		columnModel.getColumn(1).setCellRenderer(centerRenderer);

		//to keep from moving the columns around
		table.getTableHeader().setReorderingAllowed(false);
		// Disable resizing for all columns
		table.getTableHeader().setResizingAllowed(false);
	}

	private ArrayList<Program> findPrograms(String input)
	{
		ArrayList<Program> pl = new ArrayList<Program>(); 
		for (Program p : getProgramList() )
		{
			if (p.getKeywords().contains(input))
			{
				pl.add(p);
			}
		}
		return pl;
	}

	private ArrayList<Program> findProgramsByStartDate(Date input)
	{
		ArrayList<Program> pl = new ArrayList<Program>(); 
		for (Program p : getProgramList() )
		{
			if (sameDay(p.getStartDate(), input))
			{
				pl.add(p);
			}
		}
		return pl;
	}

	private ArrayList<Program> findProgramsByDeadline(Date input)
	{
		ArrayList<Program> pl = new ArrayList<Program>(); 
		//System.out.println("Full program list: " + getProgramList());

		for (Program p : getProgramList() )
		{
			//System.out.println(sameDay(p.getDeadline(), input));
			if (sameDay(p.getDeadline(), input))
			{
				pl.add(p);
			}
		}
		// System.out.println("Deadline type programs: " + pl);
		return pl;
	}

	private void displayTable()
	{

		for (int i = 0; i < getFilteredProgramList().size(); i++)
		{
			model.addRow(getFilteredProgramList().get(i).getProgramInfo());
		}

	}

	public void dayView(Date date, String sortType)
	{
		searched = true;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String d = formatter.format(date);
		if (sortType.equals("Start Date"))
		{
			spreadsheetLabel.setText("Programs starting on " + d);
			setFilteredProgramList( findProgramsByStartDate(date) );  
			// System.out.println("Start Date type programs: " + getFilteredProgramList());
		}
		else if (sortType.equals("Deadline"))
		{
			spreadsheetLabel.setText("Programs due on " + d);
			setFilteredProgramList( findProgramsByDeadline(date) );  
			//System.out.println("Deadline type programs: " + getFilteredProgramList());
		}
		model.setRowCount(0);
		displayTable(); 
		resetButton.setVisible(true);
	}
}
