import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import com.toedter.calendar.JCalendar;

public class Program implements Comparable<Program>
{
	private String name; 
	private Date startDate; 
	private Date deadline; 
	private String category; 
	private String status; 
	private String website; 
	private ArrayList<String> keywords; 
	private String response; 

	public Program()
	{
		name = "New Program";
		keywords = new ArrayList<String>(); 
	}

	public Program(String name, Date startDate, Date deadline, Object category, Object status, String website, ArrayList<String> keywords, String response)
	{
		this.name = name; 
		this.startDate = startDate; 
		this.deadline = deadline; 
		this.category = (String) category; 
		this.status = (String) status; 
		this.website = website; 
		this.keywords = keywords; 
		this.response = response; 
	}

	public String[] getProgramInfo()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String sdString = formatter.format(startDate);
		String dString = formatter.format(deadline);

		String kString = "";
		if (keywords != null && keywords.size() > 0 )
		{
			// add each keyword from the keyword array as a separate string 
			kString = keywords.get(0);

			for (int i = 1; i < keywords.size(); i++)
			{
				kString = kString + "," + keywords.get(i);
			}
		}
		String[] result = {name, sdString, dString, category, status, website, kString, response};

		return result;
	}

	public String[] getProgramWritingInfo()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String sdString = formatter.format(startDate);
		String dString = formatter.format(deadline);

		String kString = "";
		if (keywords != null && keywords.size() > 0)
		{
			// add each keyword from the keyword array as a separate string 
			kString = keywords.get(0);

			for (int i = 1; i < keywords.size(); i++)
			{
				kString = kString + "," + keywords.get(i);
			}
			// System.out.println("Keyword string: " + kString);

		}
		String[] result = {"Name: " + name, "Start Date: " + sdString, "Deadline: " + dString, 
				"Category: " + category, "Status: " + status, "Website: " + website, "Keywords: " + kString,
				"Response: " + response};

		return result;
	}


	public String getName()
	{
		return name;
	}

	// use these two methods when you want to get a date object 
	public Date getStartDate()
	{
		return startDate;
	}

	public Date getDeadline()
	{
		return deadline;
	}

	//use these two methods when you want to display the start or deadline
	public String getStartDatePrint()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(startDate);
	}

	public String getDeadlinePrint()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(deadline);
	}

	public String getCategory()
	{
		return category;
	}

	public String getStatus()
	{
		return status;
	}

	public String getWebsite()
	{
		return website;
	}

	public ArrayList<String> getKeywords() 
	{
		return keywords; 
	}

	public String getResponse() 
	{
		return response; 
	}

	public String toString()
	{
		return name; 
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Program)) return false;

		Program other = (Program) obj;
		return Objects.equals(name, other.name)
				&& Objects.equals(startDate, other.startDate)
				&& Objects.equals(deadline, other.deadline);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, startDate, deadline);
	}

	@Override
	public int compareTo(Program other) {
		if (other == null) return 1; // null is "less than" everything

		// null-safe compare method
		int compare = nullSafeCompare(name, other.getName());
		if (compare != 0) return compare;

		compare = nullSafeCompare(status, other.getStatus());
		if (compare != 0) return compare;

		compare = nullSafeCompare(website, other.getWebsite());
		if (compare != 0) return compare;

		compare = nullSafeCompare(category, other.getCategory());
		if (compare != 0) return compare;

		// Dates should not be null if properly set
		compare = nullSafeCompare(deadline, other.getDeadline());
		if (compare != 0) return compare;

		return nullSafeCompare(startDate, other.getStartDate());
	}

	// Helper methods
	private int nullSafeCompare(String s1, String s2) {
		if (s1 == null && s2 == null) return 0;
		if (s1 == null) return -1;
		if (s2 == null) return 1;
		return s1.compareTo(s2);
	}

	private int nullSafeCompare(Date d1, Date d2) {
		if (d1 == null && d2 == null) return 0;
		if (d1 == null) return -1;
		if (d2 == null) return 1;
		return d1.compareTo(d2);
	}
}
