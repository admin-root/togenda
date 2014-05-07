/* Saeed Tawil, Danny Gonzalez
 * Spring 2014
 * Description: Generates getters and setter for the database elements
 */
package edu.jcu.cs470.togenda;

public class Tasks {
	private long id;
	private String name;
	private String comment;
	private long dueDate;
	private String colorID;
	private int priority;
	
	public long getId() 
	{
		return id;
	}
	
	public void setId(long id) 
	{
		this.id = id;
	}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getComment() 
	{
		return comment;
	}
	
	public void setComment(String comment) 
	{
		this.comment = comment;
	}

	public long getDueDate() 
	{
		return dueDate;
	}

	public void setDueDate(long dueDate) 
	{
		this.dueDate = dueDate;
	}

	public String getColorID() 
	{
		return colorID;
	}

	public void setColorID(String colorID) 
	{
		this.colorID = colorID;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) 
	{
		this.priority = priority;
	}

	@Override
	public String toString() 
	{
		return comment;
	}	
}