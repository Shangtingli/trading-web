package entity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author shangtingli
 *
 */
public class Item {
	private String time;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	
	public Item(ItemBuilder builder) {
		this.open = builder.getOpen();
		this.high = builder.getHigh();
		this.low = builder.getLow();
		this.close = builder.getClose();
		this.volume = builder.getVolume();
		this.time = builder.getTime();
	}
	
	public JSONObject toJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", this.time);
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
		System.out.print("Time: ");
		System.out.print(this.time);
		System.out.println();
		System.out.print("Open: ");
		System.out.print(this.open);
		System.out.println();
		System.out.print("Close: ");
		System.out.print(this.close);
		System.out.println();
	}
	public String getTime() {
		return time;
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
	/**
	 * @author shangtingli
	 *
	 */
	public static class ItemBuilder{
		private String time;
		private double open;
		private double high;
		private double low;
		private double close;
		private double volume;
		
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
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public void setVolume(double volume) {
			this.volume = volume;
		}
		public Item build() {
			return new Item(this);
		}
	}
}


