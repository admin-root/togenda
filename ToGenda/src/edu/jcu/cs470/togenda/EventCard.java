package edu.jcu.cs470.togenda;

import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.fima.cardsui.objects.RecyclableCard;

public class EventCard extends RecyclableCard {

	@Override
	public void OnSwipeCard() {
		// TODO Auto-generated method stub
		//super.OnSwipeCard();
		//do nothing
	}

	public EventCard(String title){
		super(title);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
	}
}
