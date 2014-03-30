package com.fima.cardsui.objects;

import android.content.Context;
import android.view.View;

public abstract class AbstractCard {

	protected int image;
	protected String description, desc, title, titlePlay, time, color;
	String titleColor;
	protected Boolean hasOverflow, isClickable;
	protected int imageRes;
	protected Object data;
	

	public abstract View getView(Context context);

	public abstract View getView(Context context, boolean swipable);

	public String getTitle() {
		return title;
	}

	public String getTitlePlay() {
		return titlePlay;
	}

	public String getDesc() {
		return desc;
	}

	public String getDescription() {
		return description;
	}
	
	public String getTime() {
		return time;
	}

	public int getImage() {
		return image;
	}

	public String getColor() {
		return color;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public Boolean getHasOverflow() {
		return hasOverflow;
	}

	public Boolean getIsClickable() {
		return isClickable;
	}

	public int getImageRes() {
		return imageRes;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
