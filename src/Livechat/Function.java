package Livechat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Function {
	public static long timestamp(){
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}
	
	public static Connector mysql(){
		BufferedReader br = null;
		try{
			String host,db,un,pw;
			br = new BufferedReader(new FileReader("db.txt"));
			host=br.readLine();
			db=br.readLine();
			un=br.readLine();
			pw=br.readLine();
			return new Connector(host,db,un,pw);
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static int checkUsername(String user) {
		//4-24
		//a-zA-Z0-9_-
		return 0;
	}
	
	public static int checkPassword(String pass) {
		//8-24
		//ASCII 32-126
		//Mindst 1 lille bogstav, 1 stort bogstav & 1 tal
		return 0;
	}
}
