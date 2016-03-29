package Livechat;

public class Function {
	public static long timestamp(){
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}
}
