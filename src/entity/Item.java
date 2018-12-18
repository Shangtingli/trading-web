package entity;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author shangtingli
 *
 */
public class Item {
	private String date;
	private int hour;
	private int minute;
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
	
	public void print() {
		System.out.print("Date: ");
		System.out.print(this.date);
		System.out.println();
		System.out.print("Hour: ");
		System.out.print(this.hour);
		System.out.println();
		System.out.print("Minute: ");
		System.out.print(this.minute);
		System.out.println();
		System.out.print("Open: ");
		System.out.print(this.open);
		System.out.println();
		System.out.print("Close: ");
		System.out.print(this.close);
		System.out.println();
	}
	public String getDate() {
		return date;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public double getOpen() {
		return open;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	public double getVolume() {
		return volume;
	}
	public static class ItemBuilder{
		private String date;
		private int hour;
		private int minute;
		private double open;
		private double high;
		private double low;
		private double close;
		private double volume;
		public int getHour() {
			return hour;
		}
		public void setHour(int hour) {
			this.hour = hour;
		}
		public int getMinute() {
			return minute;
		}
		public void setMinute(int minute) {
			this.minute = minute;
		}
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


