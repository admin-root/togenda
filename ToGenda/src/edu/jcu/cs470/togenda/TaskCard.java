package edu.jcu.cs470.togenda;

import android.view.View;
import android.widget.TextView;
import com.fima.cardsui.objects.RecyclableCard;

public class TaskCard extends RecyclableCard {

	public TaskCard(String title){
		super(title);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.EventLabel)).setText(title);
	}
}
