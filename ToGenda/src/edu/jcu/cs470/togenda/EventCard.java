package edu.jcu.cs470.togenda;

import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.RecyclableCard;
import com.fima.cardsui.objects.Card.OnCardSwiped;

public class EventCard extends RecyclableCard {

	public EventCard(String title){
		super(title);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.TimeLabel)).setText(title);
	}
	
	@Override
	public void setOnCardSwipedListener(OnCardSwiped onEpisodeSwipedListener) {
        //this.onCardSwipedListener = onEpisodeSwipedListener;
		//Do nothing
    }
}