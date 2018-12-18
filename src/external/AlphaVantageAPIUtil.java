package external;

public class AlphaVantageAPIUtil {
	
	public static String[] parse(String time) {
		String[] res = new String[3];
		String[] dateAndTime = time.split(" ");
		String[] hourAndMinute = dateAndTime[1].split(":");
		res[0] = dateAndTime[0];
		res[1] = hourAndMinute[0];
		res[2] = hourAndMinute[1];
		return res;

	}
}
