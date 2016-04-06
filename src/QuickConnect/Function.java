package QuickConnect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import QuickConnect_GUI.startFrame;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;

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

	public static String md5(String str) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
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
		if(!pass.matches(bl) || !pass.matches(num) || !pass.matches(sl)) return 2;
		for(int i = 0; i<pass.length(); i++){
			c = pass.charAt(i);
			if(c<32 || c>126) return 3;
		}
		return 0;
	}

	public static boolean checkEmail(String email) {
		boolean b = email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"); 
		if (b == false) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Ugyldig e-mail adresse!");
			alert.setHeaderText(null);
			alert.setContentText("Indtast en gyldig e-mail adresse, for at kunne registrere dig.");
			alert.showAndWait();

		}	
		return true;
	}
}
