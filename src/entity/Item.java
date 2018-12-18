package entity;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author shangtingli
 *Represents the one the results of one fetch, 100 datapoints
 */
public class Item {
	private String date;
	private String hour;
	private String minute;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	
	public Item(ItemBuilder builder) {
		this.date = builder.getDate();
		this.open = builder.getOpen();
		this.high = builder.getHigh();
		this.low = builder.getLow();
		this.close = builder.getClose();
		this.volume = builder.getVolume();
		this.hour = builder.getHour();
		this.minute = builder.getMinute();
	}
	
	public JSONObject toJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("date", this.date);
			obj.put("hour", this.hour);
			obj.put("minute", this.minute);
			obj.put("open", this.open);
			obj.put("high", this.high);
			obj.put("low", this.low);
			obj.put("close", this.close);
		}
		catch(JSONException e) {
			System.out.println("Something is WRONG with when transforming the JSON Object");
		}
		
		return obj;
	}
	
	public static class ItemBuilder{
		private String date;
		private String hour;
		private String minute;
		public String getHour() {
			return hour;
		}
		public void setHour(String hour) {
			this.hour = hour;
		}
		public String getMinute() {
			return minute;
		}
		public void setMinute(String minute) {
			this.minute = minute;
		}

		private double open;
		private double high;
		private double low;
		private double close;
		private double volume;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public double getOpen() {
			return open;
		}
		public void setOpen(double open) {
			this.open = open;
		}
		public double getHigh() {
			return high;
		}
		public void setHigh(double high) {
			this.high = high;
		}
		public double getLow() {
			return low;
		}
		public void setLow(double low) {
			this.low = low;
		}
		public double getClose() {
			return close;
		}
		public void setClose(double close) {
			this.close = close;
		}
		public double getVolume() {
			return volume;
		}
		public void setVolume(double volume) {
			this.volume = volume;
		}
		
		public Item build() {
			return new Item(this);
		}
	}
}
