package edu.jcu.cs470.togenda;

import android.view.View;

import com.fima.cardsui.objects.RecyclableCard;

@SuppressWarnings("rawtypes")
public abstract class CardTemplate extends RecyclableCard implements Comparable{

	
	public CardTemplate(String title){
		super(title);
	}
	public CardTemplate()
	{
		super();
	}
	
	@Override
	protected void applyTo(View convertView) 
	{
	}

	@Override
	protected int getCardLayoutId() 
	{
		return 0;
	}

	public CardTemplate(String titlePlay, int imageRes, String description, String titleColor, Boolean hasOverflow, Boolean isClickable){
	super(titlePlay, imageRes, description, titleColor, hasOverflow, isClickable);
	}
	
	@Override
	public int compareTo(Object another) { //for sorting
		if (this.getStart() < ((EventCard) another).getStart())
		{
			return 1;
		}
		else if (this.getStart() > ((EventCard) another).getStart())
		{
			return -1;
		}
		else if (this.getStart() == ((EventCard) another).getStart())
		{
			if (this.getEnd() < ((EventCard) another).getEnd())
			{
				return 1;
			}
			else if (this.getEnd() < ((EventCard) another).getEnd())
			{
				return -1;
			}
		}
		return 0;
	}

	private long getEnd() {
		// TODO Auto-generated method stub
		return 0;
	}

	private long getStart() {
		// TODO Auto-generated method stub
		return 0;
	}

}
