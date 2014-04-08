package edu.jcu.cs470.togenda;

import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.RecyclableCard;

public class DateHolder extends RecyclableCard {

	@Override
	protected void applyTo(View convertView) {
		// TODO Auto-generated method stub
		((TextView) convertView.findViewById(R.id.EventLabel)).setText(title);
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
