package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection; 
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


import entity.Item;

import java.util.*;
import external.AlphaVantageAPIUtil;
import entity.Item.ItemBuilder;

public class AlphaVantageAPI {
	private static final String URL = "https://www.alphavantage.co/query?";
	private static final String API_KEY =  "IJ02XXB3R3J8DSDZ";
	private static final String INTRA_DAY = "TIME_SERIES_INTRADAY";
	private static final String INTERVAL = "1min";
	private static final String COMPACT_MODE = "compact";
	private static final String FULL_MODE = "full";
	private static final String DAILY_ADJUSTED = "TIME_SERIES_DAILY_DAJUSTED";
	private static final String DEFAULT_ASSET = "MSFT";
	//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&outputsize=full&apikey=demo
	public String constructQuery(String asset) {
		return URL + "function=" + INTRA_DAY + "&symbol=" + asset + "&interval=" + INTERVAL + "&outputsize=" +COMPACT_MODE+ "&apikey=" + API_KEY;
	}
	
	public JSONObject getResponse(String asset) {
		if (asset == null || asset.length() == 0) {
			asset = "MSFT";
		}
		String query = constructQuery(asset);
		System.out.println(query);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(query).openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			System.out.println("Sending 'GET' Request to URL:"+URL);
			System.out.println("Response Code: " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Response is an input with regard to client, output with regard to server
			//BufferedReader is used to prevent if the stream is very big
			//connection.getOutputStream(); Request is an output with regard to client, input with regard to server
			StringBuilder response =  new StringBuilder();
			String inputLine;
			while ((inputLine =in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
			JSONObject obj = new JSONObject(response.toString());
			String metaKey = "Time Series (" + INTERVAL + ")";
			System.out.println(metaKey);
			if (!obj.isNull(metaKey))
			{
				JSONObject timeSeries = obj.getJSONObject(metaKey);
//				System.out.println(timeSeries);
				return timeSeries;
			}
		}
		catch (Exception e) {
			System.out.println("Something is Wrong with the getResponse Method");
		}
		return new JSONObject();
	}
	
	public List<Item> getItems(JSONObject obj) {
		List<Item> res = new ArrayList<>();
		Iterator<String> keys = obj.keys();
		try {
			while(keys.hasNext()) {
				String time = keys.next();
				String[] timeInfo = AlphaVantageAPIUtil.parse(time);
				String date = timeInfo[0];
				int hour = Integer.parseInt(timeInfo[1]);
				int minute = Integer.parseInt(timeInfo[2]);
				JSONObject tempObject = obj.getJSONObject(time);
				ItemBuilder builder = new ItemBuilder();
				String open = tempObject.getString("1. open");
				builder.setOpen(Double.parseDouble(open));
				String high = tempObject.getString("2. high");
				builder.setHigh(Double.parseDouble(high));
				String low = tempObject.getString("3. low");
				builder.setLow(Double.parseDouble(low));
				String close = tempObject.getString("4. close");
				builder.setClose(Double.parseDouble(close));
				String volume = tempObject.getString("5. volume");
				builder.setVolume(Double.parseDouble(volume));
				//Time information
				builder.setDate(date);
				builder.setHour(hour);
				builder.setMinute(minute);
				Item item = new Item(builder);
				res.add(item);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		Collections.sort(res,new Comparator<Item>(){
			   @Override
			   public int compare(Item i1,Item i2) {
				   if (i1.getHour() != i2.getHour()) {
					   return (i1.getHour() - i2.getHour());
				   }
				   else {
					   return (i1.getMinute() - i2.getMinute());
				   }
			   }

		});
		return res;
	}
	
	
	public static void main(String[] args) {
		AlphaVantageAPI test = new AlphaVantageAPI();
		JSONObject dummy = test.getResponse("");
		List<Item> itemList = test.getItems(dummy);
		for (Item item: itemList) {
			item.print();
			System.out.println("===============");
		}
	}
}

