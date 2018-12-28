package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection; 
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;

import java.util.*;
import external.AlphaVantageAPIUtil;
import entity.Item.ItemBuilder;

public class AlphaVantageAPI {
	private static final String URL = "https://www.alphavantage.co/query?";
	private static final String API_KEY =  "8OK2OQMFJ7ATJYOF";
	private static final String INTRA_DAY = "TIME_SERIES_INTRADAY";
	private static final String SYMBOL_SEARCH = "SYMBOL_SEARCH";
	private static final String INTERVAL = "1min";
	private static final String COMPACT_MODE = "compact";
//https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=xiaomi&apikey=demo
	public String constructAssetQuery(String asset) {
		if (asset == null || asset.length() == 0) {
			asset = "MSFT";
		}
		return URL + "function=" + INTRA_DAY + "&symbol=" + asset + "&interval=" + INTERVAL + "&outputsize=" +COMPACT_MODE+ "&apikey=" + API_KEY;
	}
	
	public String constructSymbolsQuery(String fragment) {
		return URL + "function=" + SYMBOL_SEARCH + "&keywords=" + fragment + "&apikey=" + API_KEY;
	}
	public JSONObject getResponse(String query) {
		System.out.println(query);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(query).openConnection();
			connection.setRequestMethod("GET");
			
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
			return obj;
		}
		catch (Exception e) {
			System.out.println("Something is Wrong with the getResponse Method");
		}
		return new JSONObject();
	}
	
	public List<Item> getItems(JSONObject object) {
		System.out.println(object);
		String metaKey = "Time Series (" + INTERVAL + ")";
		List<Item> res = new ArrayList<>();
		
		try {
			JSONObject obj = object.getJSONObject(metaKey);
			Iterator<?> keys = obj.keys();
			while(keys.hasNext()) {
				String time = (String)keys.next();
//				String[] timeInfo = AlphaVantageAPIUtil.parse(time);
//				String date = timeInfo[0];
//				int hour = Integer.parseInt(timeInfo[1]);
//				int minute = Integer.parseInt(timeInfo[2]);
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
				builder.setTime(time);
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
				   String t1 = i1.getTime();
				   String t2 = i2.getTime();
				   String[] timeInfo1 = AlphaVantageAPIUtil.parse(t1);
				   String[] timeInfo2 = AlphaVantageAPIUtil.parse(t2);
				   int hour1 = Integer.parseInt(timeInfo1[1]);
				   int minute1 = Integer.parseInt(timeInfo1[2]);
				   int hour2 = Integer.parseInt(timeInfo2[1]);
				   int minute2 = Integer.parseInt(timeInfo2[2]);
				   if (hour1 != hour2) {
					   return hour1 - hour2;
				   }
				   else {
					   return minute1-minute2;
				   }
//				   if (i1.getHour() != i2.getHour()) {
//					   return (i1.getHour() - i2.getHour());
//				   }
//				   else {
//					   return (i1.getMinute() - i2.getMinute());
//				   }
			   }

		});
		return res;
	}
	
	public HashMap<String, String> getAllSymbols(List<String> permutations) {
		HashMap<String, String> symbolInfo = new HashMap<>();
		String metaKey = "bestMatches";
		try {
			int count = 0;
			for (String fragment: permutations) {
				count ++;
				String query = constructSymbolsQuery(fragment);
				JSONObject result= getResponse(query);
				JSONArray array = result.getJSONArray(metaKey);
				for (int i=0 ; i < array.length(); i++) {
					JSONObject info = array.getJSONObject(i);
					String symbol = info.getString("1. symbol");
					String name = info.getString("2. name");
					symbolInfo.put(symbol, name);
				}
				if (count % 25 == 0) {
					System.out.println("Sleeping for 1 minute wait for API...");
					TimeUnit.MINUTES.sleep(1);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return symbolInfo;
	}
	
	public static void main(String[] args) {
		AlphaVantageAPI test = new AlphaVantageAPI();
		for (int i=0; i < 10; i++) {
			System.out.println(i);
			if (i % 4 == 0) {
				try {
					TimeUnit.SECONDS.sleep(10);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}

