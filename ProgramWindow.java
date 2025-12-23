import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;

public class ProgramWindow extends Screen {

	private static final long serialVersionUID = 1L;
	// private JPanel contentPane;
	private Program myProgram;
	private JTextField nameTextField;
	private JTextField keywordsTextField;
	private JTable keywordTable;
	private DefaultTableModel model; 
	private JTextField websiteTextField;
	private JTextArea responseTextArea;
	private JDateChooser startDateChooser;
	private ArrayList<String> keywordList;
	/**
	 * Create the frame.
	 */
	public ProgramWindow(File profile, Program program) 
	{
		myProgram = program; 

		processFile(profile); 


		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				SpreadsheetScreen ss = new SpreadsheetScreen(profile);
				ss.setVisible(true);

				dispose(); // now close this window
			}
		});
		setBounds(100, 100, 409, 401);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		if (myProgram == null)
		{
			myProgram = new Program();
		}

		JLabel programLabel = new JLabel(myProgram.getName());
		programLabel.setHorizontalAlignment(SwingConstants.CENTER);
		programLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		programLabel.setBounds(75, 11, 243, 27);
		contentPane.add(programLabel);

		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBounds(10, 52, 41, 14);
		contentPane.add(nameLabel);

		// set these textfields to display the text 
		nameTextField = new JTextField();
		nameTextField.setText(myProgram.getName());
		nameTextField.setBounds(75, 49, 113, 20);
		contentPane.add(nameTextField);
		nameTextField.setColumns(10);

		JLabel startDateLabel = new JLabel("Start Date: ");
		startDateLabel.setBounds(10, 79, 83, 14);
		contentPane.add(startDateLabel);

		JLabel keywordLabel = new JLabel("Keywords:");
		keywordLabel.setBounds(198, 52, 83, 14);
		contentPane.add(keywordLabel);

		keywordsTextField = new JTextField();
		keywordsTextField.setBounds(270, 49, 113, 20);
		contentPane.add(keywordsTextField);
		keywordsTextField.setColumns(10);

		JButton keywordButton = new JButton("Add");
		keywordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// get text from keywords textfield and add to the keywords array list, remove any leading or trailing spaces
				String text = keywordsTextField.getText().trim(); 

				if (text == null || text.equals("") || text.contains(","))
				{
					JOptionPane.showMessageDialog(null,"Invalid keyword! Note: Do not include commas.", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else 
				{
					keywordList.add(keywordsTextField.getText());
					// System.out.println("Keyword list after adding: " + keywordList);
					updateTable(); 
					keywordsTextField.setText(""); 
				}
			}
		});
		keywordButton.setBounds(294, 73, 57, 23);
		contentPane.add(keywordButton);

		String[] headings = {"Keywords"};
		model = new DefaultTableModel(headings, 0)
		{
			public boolean isCellEditable(int row, int col)
			{
				return false;
			}
		};
		keywordTable = new JTable(model);
		keywordTable.setFont(new Font(getFontName(), Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(keywordTable);
		scrollPane.setBounds(198, 106, 185, 89);
		contentPane.add(scrollPane);


		keywordList = myProgram.getKeywords(); 
		if (keywordList != null)
		{
			// for every keyword, add a new row to the table 
			for (int i = 0; i < keywordList.size(); i++)
			{
				String[] s = {keywordList.get(i)};
				model.addRow(s);
			}
		}

		keywordTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				if(! SwingUtilities.isRightMouseButton(event))
				{
					int row = keywordTable.getSelectedRow();
					int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this keyword?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) 
					{
						// delete from list
						keywordList.remove(keywordList.get(row));

						updateTable();

					}

				}
			}
		}); 

		startDateChooser = new JDateChooser();
		if (myProgram.getStartDate() != null) {
			startDateChooser.setDate(myProgram.getStartDate());
		}
		startDateChooser.setBounds(75, 77, 113, 20);
		contentPane.add(startDateChooser);

		JLabel deadlineLabel = new JLabel("Deadline:");
		deadlineLabel.setBounds(10, 103, 57, 14);
		contentPane.add(deadlineLabel);

		JDateChooser deadlineChooser = new JDateChooser();
		if (myProgram.getDeadline() != null) {
			deadlineChooser.setDate(myProgram.getDeadline());
		}
		deadlineChooser.setBounds(75, 103, 113, 20);
		contentPane.add(deadlineChooser);

		JLabel categoryLabel = new JLabel("Category:");
		categoryLabel.setBounds(10, 131, 57, 14);
		contentPane.add(categoryLabel);

		JLabel statusLabel = new JLabel("Status: ");
		statusLabel.setBounds(10, 156, 57, 14);
		contentPane.add(statusLabel);

		JLabel websiteLabel = new JLabel("Website: ");
		websiteLabel.setBounds(10, 181, 57, 14);
		contentPane.add(websiteLabel);

		JComboBox categoryComboBox = new JComboBox(new String[] {"None", "Academic", "Scholarship", "STEM", "Leadership"});
		categoryComboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "Academic", "Scholarship", "STEM", "Leadership"}));
		categoryComboBox.setBounds(75, 127, 113, 22);
		if (myProgram.getCategory() != null) {
			categoryComboBox.setSelectedItem(myProgram.getCategory());
		}
		contentPane.add(categoryComboBox);

		JComboBox statusComboBox = new JComboBox(new String[] {"None", "Not Started", "In Progress", "Completed"});
		statusComboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "Not Started", "In Progress", "Completed"}));
		statusComboBox.setBounds(75, 152, 113, 22);
		if (myProgram.getStatus() != null) {
			statusComboBox.setSelectedItem(myProgram.getStatus());
		}
		contentPane.add(statusComboBox);

		websiteTextField = new JTextField();
		websiteTextField.setColumns(10);
		websiteTextField.setBounds(75, 178, 113, 20);
		if (myProgram.getWebsite() != null) {
			websiteTextField.setText(myProgram.getWebsite());
		}
		contentPane.add(websiteTextField);

		JLabel responseLabel = new JLabel("Written Response:");
		responseLabel.setBounds(10, 204, 178, 14);
		contentPane.add(responseLabel);

		responseTextArea = new JTextArea();
		responseTextArea.setLineWrap(true);             // wrap long lines
		responseTextArea.setWrapStyleWord(true);        // wrap at word boundaries
		if (myProgram.getResponse() != null) {
			responseTextArea.setText(myProgram.getResponse());
		}

		// Put the text area inside a scroll pane
		JScrollPane responseScrollPane = new JScrollPane(responseTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		responseScrollPane.setBounds(10, 223, 373, 100);
		contentPane.add(responseScrollPane);

		responseTextArea.setColumns(10);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this program?", 
						"Delete Confirmation", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) 
				{
					getProgramList().remove(myProgram);
					writePrograms(profile);

					JOptionPane.showMessageDialog(null,"Program successfully deleted!", "Success!", JOptionPane.INFORMATION_MESSAGE);

					setVisible(false); 

					try 
					{
						// automatically process the user file upon saving
						SpreadsheetScreen ss = new SpreadsheetScreen(profile);
						ss.setVisible(true);
					} 
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}

					// close the program window
					dispose();  
				}

			}
		});
		deleteButton.setBounds(10, 334, 103, 23);
		contentPane.add(deleteButton);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{

				try 
				{
					// Validate inputs 
					if (nameTextField.getText() == null || nameTextField.getText().trim().isEmpty() ||
							startDateChooser.getDate() == null || deadlineChooser.getDate() == null ||
							categoryComboBox.getSelectedItem() == null || statusComboBox.getSelectedItem() == null ||
							websiteTextField.getText() == null || websiteTextField.getText().trim().isEmpty()) {

						JOptionPane.showMessageDialog(null, "One or more required fields are empty or incorrectly filled out.", 
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (startDateChooser.getDate().after(deadlineChooser.getDate())) 
					{
						JOptionPane.showMessageDialog(null, "Start date must be before deadline.", 
								"Start Date or Deadline Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					// Create program 
					Program newpro = new Program(nameTextField.getText(), startDateChooser.getDate(), deadlineChooser.getDate(),
							(String) categoryComboBox.getSelectedItem(), (String) statusComboBox.getSelectedItem(),
							websiteTextField.getText(), keywordList, responseTextArea.getText());
					
					boolean found = false; 
					int index = 0; 
					for (int i = 0; i < getProgramList().size(); i++) 
					{
						if (getProgramList().get(i).compareTo(program) == 0) 
						{
							index = i; 
							found = true;
							break;
						}
					}
					if (found)
						getProgramList().set(index, newpro);
					else
						getProgramList().add(newpro);

					writePrograms(profile);

					JOptionPane.showMessageDialog(null, "Program successfully saved!", "Success!", JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);

					SpreadsheetScreen ss = new SpreadsheetScreen(profile);
					ss.setVisible(true);
					dispose();

				} 
				catch (Exception e1) {
					JOptionPane.showMessageDialog(null, 
							"An unexpected error occurred while saving. Please check all fields and try again.", 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}

		});
		saveButton.setBounds(280, 334, 103, 23);
		contentPane.add(saveButton);

		applyTheme(getTheme()); 
		applyFont(getFontName()); 
	}

	private void updateTable()
	{
		model.setRowCount(0);
		for (int i = 0; i < keywordList.size(); i++)
		{
			String[] s = {keywordList.get(i)};
			model.addRow(s);
		}
	}
}
