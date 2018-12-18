package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection; 
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import jdk.internal.org.objectweb.asm.commons.StaticInitMerger;

//import entity.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import entity.Item.ItemBuilder;
public class AlphaVantageAPI {
	private static final String URL = "https://www.alphavantage.co/query?";
	private static final String API_KEY =  "IJ02XXB3R3J8DSDZ";
	private static final String INTRA_DAY = "TIME_SERIES_INTRADAY";
	private static final String INTERVAL = "1min";
	private static final String COMPACT_MODE = "compact";
	private static final String FULL_MODE = "full";
	private static final String DAILY_ADJUSTED = "TIME_SERIES_DAILY_DAJUSTED";
	//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&outputsize=full&apikey=demo
	public String constructQuery(String asset) {
		return URL + "function=" + INTRA_DAY + "&symbol=" + asset + "&interval=" + INTERVAL + "&outputsize=" +COMPACT_MODE+ "&apikey=" + API_KEY;
	}
	
//	public JSONArray getJSON(String asset) {
//		
//	}
	public static void main(String[] args) {
		AlphaVantageAPI test = new AlphaVantageAPI();
		System.out.println(test.constructQuery("MSFT"));
	}
}
