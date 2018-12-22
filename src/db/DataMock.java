package db;
import java.util.*;


public class DataMock {
	public static final String[] MOCK_USER_NAMES = new String[] 
			{"shangtingli","yuyingwang","weidongan","xuanyanchen","ziyueyan","sijiezhao"};
	public static final String[] MOCK_PASSWORDS = new String[]
			{"shangtingli","yuyingwang","weidongan","xuanyanchen","ziyueyan","sijiezhao"};
	public static final String[] MOCK_FIRST_NAMES = new String[]
			{"Shangting","Yuying","Weidong","Xuanyan","Ziyue","Sijie"};
	public static final String[] MOCK_LAST_NAMES = new String[]
			{"Li","Wang","An","Chen","Yan","Zhao"};
	
	public static HashMap<String,List<String>> GET_HOLDINGS(){
		HashMap<String,List<String>> HOLDINGS = new HashMap<>();
		HOLDINGS.put("shangtingli",
				new ArrayList<String>(Arrays.asList("AAPL","MSFT","TSLA","GPRO")));
		HOLDINGS.put("yuyingwang",
				new ArrayList<String>());
		HOLDINGS.put("weidongan",
				new ArrayList<String>(Arrays.asList("AMAZ","DIS","FB")));
		HOLDINGS.put("xuanyanchen",
				new ArrayList<String>(Arrays.asList("ABMD","TWTR","FB")));
		HOLDINGS.put("ziyueyan",
				new ArrayList<String>(Arrays.asList("BABA")));
		HOLDINGS.put("ziyueyan",
				new ArrayList<String>(Arrays.asList("NFLX","SIRI")));
		
		return HOLDINGS;
	}


}
