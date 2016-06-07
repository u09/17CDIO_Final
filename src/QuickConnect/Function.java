package QuickConnect;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import sun.misc.IOUtils;

public class Function {
	private Connector con;
	private User user;
	private long timestamp=0;

	public Function(User user) {
		this.user = user;
		con = mysql();
	}

	private Connector mysql() {
		BufferedReader br = null;
		try {
			String host, db, un, pw;
			br = new BufferedReader(new FileReader("db.txt"));
			host = br.readLine();
			db = br.readLine();
			un = br.readLine();
			pw = br.readLine();
			return new Connector(host, db, un, pw);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null)
					br.close();
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public int[] convertIntegers(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	public long timestamp() throws IOException {
		if(timestamp==0)
		{
			URL url = new URL("http://s8dev.org/timestamp.php");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
			    baos.write(buf, 0, len);
			}
			String body = new String(baos.toByteArray(), encoding);
			timestamp=Long.parseLong(body);
			System.out.println(timestamp);
			return timestamp;
		}
		return timestamp;
	}

	public String md5(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for(byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	public int checkUsername(String user) {
		char c;
		if(user.length() < 4 || user.length() > 24)
			return 1;
		for(int i = 0; i < user.length(); i++) {
			c = user.charAt(i);
			if(c != 45 && c != 95 && (c < 48 || c > 57) && (c < 65 || c > 90) && (c < 97 || c > 122))
				return 2;
		}
		return 0;
	}

	public int checkPassword(String pass) {
		String num = ".*[0-9].*";
		String bl = ".*[A-Z].*";
		String sl = ".*[a-z].*";
		char c;
		if(pass.length() < 8 || pass.length() > 24)
			return 1;
		if(!pass.matches(bl) || !pass.matches(num) || !pass.matches(sl))
			return 2;
		for(int i = 0; i < pass.length(); i++) {
			c = pass.charAt(i);
			if(c < 32 || c > 126)
				return 3;
		}
		return 0;
	}

	public boolean checkEmail(String email) {
		boolean b = email
		        .matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		if(b == false)
			return false;
		return true;
	}

	public int checkRegister(String user, String pass1, String pass2, String email) throws SQLException {
		boolean bool;
		int in;
		in = checkUsername(user);
		if(in == 1)
			return 1;
		else if(in == 2)
			return 2;
		if(!pass1.equals(pass2))
			return 3;
		in = checkPassword(pass1);
		if(in == 1)
			return 4;
		else if(in == 2)
			return 5;
		else if(in == 3)
			return 6;
		if(!checkEmail(email))
			return 7;
		bool = con().check("SELECT username FROM users WHERE UPPER(username) LIKE UPPER(?)", user);
		if(bool)
			return 8;
		else return 9;
	}

	public void updateUser(int id, String username, String email, String nickname, int age, int user_created) {
		user.setUserID(id);
		user.setUsername(username);
		user.setEmail(email);
		user.setNickname(nickname);
		user.setAge(age);
		user.setCreated(user_created);
	}

	public Connector con() {
		return con;
	}

	public User user() {
		return this.user;
	}

	public void printArrayListMulti(ArrayList<ArrayList<String>> msg) {
		if(msg != null) {
			if(msg.size()!=0) System.out.println("[");
			for(int i = 1; i <= msg.size(); i++) {
				System.out.print("\t[\n\t\t{");
				for(int t = 1; t <= msg.get(i - 1).size(); t++) {
					if(t == 1)
						System.out.print(msg.get(i - 1).get(t - 1));
					System.out.print(", " + msg.get(i - 1).get(t - 1));
				}
				System.out.print("}\n\t]\n");
			}
			if(msg.size()!=0) System.out.println("]");
		}
	}
	
	public void printArrayList(ArrayList<?> msg) {
		if(msg != null) {
			if(msg.size()!=0) System.out.print("[\n\t");
			for(int i = 1; i <= msg.size(); i++) {
				System.out.print(", " + msg.get(i-1));
			}
			if(msg.size()!=0) System.out.println("\n]");
		}
	}

	public void timestampInc() throws IOException {
		if(timestamp!=0) timestamp++;
		else timestamp();
	}
}
