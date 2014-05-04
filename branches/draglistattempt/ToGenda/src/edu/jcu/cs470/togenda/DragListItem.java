package edu.jcu.cs470.togenda;

public class DragListItem {

    private String title, desc;
    private Long due = (long) 0;
    private int color = 15;
    private int priority;

     public DragListItem(String string, String string2, long long1, int int1, int int2) {
		// TODO Auto-generated constructor stub
    	 this.setTitle(string);
    	 this.setDesc(string2);
    	 this.setDue(due);
    	 this.setColor(int1);
         this.setPriority(int2);
	}

	public String getTitle() {
          return title;
      }

    public void setTitle(String title) {
         this.title = title;
    }

	public Long getDue() {
		return due;
	}

	public void setDue(Long due) {
		this.due = due;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}