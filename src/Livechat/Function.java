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
		char c;
		if(user.length() <4 || user.length() > 24) return 1;
		for(int i = 0; i<user.length(); i++){
			c = user.charAt(i);
			if(c!=45 && c!= 95 && (c<48 || c>57) && (c<65 || c>90) && (c<97 || c>122)) return 2;
		}
		return 0;
	}
	
	public static int checkPassword(String pass) {
		String num   = ".*[0-9].*";
		String bl = ".*[A-Z].*";
		String sl = ".*[a-z].*";
		char c;
		if(pass.length() <8 || pass.length() >24) return 1;
		if(!pass.matches(bl) && !pass.matches(num) && !pass.matches(sl)) return 2;
		for(int i = 0; i<pass.length(); i++){
			c = pass.charAt(i);
			if(c<32 && c>126) return 3;
		}
		//8-24
		//ASCII 32-126
		//Mindst 1 lille bogstav, 1 stort bogstav & 1 tal
		return 0;
	}
}
