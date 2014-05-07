package edu.jcu.cs470.togenda;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fima.cardsui.objects.RecyclableCard;

//No longer used.
//a "card" object that would only display the date
//this is useful for placing strings, such as a date label, between cards in agenda view.
//We now use the stack label for this functionality.

public class DateHolder extends RecyclableCard {

	@Override
	protected void applyTo(View convertView) {
		// TODO Auto-generated method stub
		((TextView) convertView.findViewById(R.id.EventLabel)).setText(title);
		((LinearLayout) convertView.findViewById(R.id.background)).setBackgroundResource(R.color.card_border);
	}

	@Override
	protected int getCardLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.date_holder;
	}
	
	public DateHolder() {
		title = "date goes here";
	}
	public DateHolder(String displaytext){
		title = displaytext;
	}

}
