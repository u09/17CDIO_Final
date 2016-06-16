package QuickConnect;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Function {
	private Connector connector;
	private User user;
	private long timestamp = 0;

	public Function(User user) {
		this.user = user;
		connector = mysql();
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

	/**
	 * Checks whether or not the <code>username</code> is valid.
	 * 
	 * @param username
	 * @return 0 : valid <br>
	 *         1 : too short or too long <br>
	 *         2 : found invalid entries
	 */
	public int checkUsername(String username) {
		char c;
		if(username.length() < 4 || username.length() > 24)
			return 1;
		for(int i = 0; i < username.length(); i++) {
			c = username.charAt(i);
			if(c != 45 && c != 95 && (c < 48 || c > 57) && (c < 65 || c > 90) && (c < 97 || c > 122))
				return 2;
		}
		return 0;
	}

	/**
	 * Checks whether or not the <code>password</code> is valid.
	 * 
	 * @param pass
	 * @return 0 : valid <br>
	 *         1 : too short or too long <br>
	 *         2 : doesn't contain numbers, uppercase letters or lowercase
	 *         letters <br>
	 *         3 : found invalid entries
	 */
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

	/**
	 * Checks whether or not the <code>email</code> has the correct structure
	 * 
	 * @param email
	 * @return <code>true</code> if it has and <code>false</code> if it hasn't.
	 */
	public boolean checkEmail(String email) {
		boolean b = email
		        .matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		if(b == false)
			return false;
		return true;
	}

	/**
	 * Determines whether or not the user is approved to be registered by using
	 * <code>{@link #checkUsername(String)}</code>,
	 * <code>{@link #checkPassword(String)}</code>,
	 * <code>{@link #checkEmail(String)}</code> and also the age (using the
	 * <code>date</code>) and whether the <code>username</code> already exists.
	 * 
	 * @param user
	 * @param pass1
	 * @param pass2
	 * @param email
	 * @param date
	 * @return 1-9 : dependent upon the cause of the disapproval <br>
	 *         10 : approved for registration
	 * @throws SQLException
	 */
	public int checkRegister(String user, String pass1, String pass2, String email, LocalDate date)
	        throws SQLException {
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
		if(ChronoUnit.YEARS.between(date, LocalDate.now()) < 13)
			return 8;
		bool = getConnector().check("SELECT username FROM users WHERE UPPER(username) LIKE UPPER(?)", user);
		if(bool)
			return 9;
		else return 10;
	}

	/**
	 * Updates all fields in the <code>user</code> object from the
	 * <code>{@link #User}</code> class.
	 * 
	 * @param id
	 * @param username
	 * @param email
	 * @param nickname
	 * @param age
	 * @param user_created
	 */
	public void updateUser(int id, String username, String email, String nickname, int age, int user_created) {
		user.setUserID(id);
		user.setUsername(username);
		user.setEmail(email);
		user.setNickname(nickname);
		user.setAge(age);
		user.setCreated(user_created);
	}

	/**
	 * Method to digest a <code>String</code> such as a password or message
	 * using the Message Digest algorithm 5.
	 * 
	 * @param string
	 * @return encrypted <code>String</code>
	 * @throws NoSuchAlgorithmException
	 */
	public String md5(String string) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(string.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for(byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	public void printArrayList(ArrayList<?> msg) {
		if(msg != null) {
			if(msg.size() != 0)
				System.out.print("[\n\t");
			for(int i = 1; i <= msg.size(); i++) {
				System.out.print(", " + msg.get(i - 1));
			}
			if(msg.size() != 0)
				System.out.println("\n]");
		}
	}

	public void printArrayListMulti(ArrayList<ArrayList<String>> msg) {
		if(msg != null) {
			if(msg.size() != 0)
				System.out.println("[");
			for(int i = 1; i <= msg.size(); i++) {
				System.out.print("\t[\n\t\t{");
				for(int t = 1; t <= msg.get(i - 1).size(); t++) {
					if(t == 1)
						System.out.print(msg.get(i - 1).get(t - 1));
					System.out.print(", " + msg.get(i - 1).get(t - 1));
				}
				System.out.print("}\n\t]\n");
			}
			if(msg.size() != 0)
				System.out.println("]");
		}
	}

	/**
	 * Returns the current <code>timestamp</code> in <code>unix</code> time.
	 * 
	 * @return <code>timestamp</code> in type <code>long</code>
	 * @throws IOException
	 */
	public long timestamp() throws IOException {
		if(timestamp == 0) {
			URL url = new URL("http://s8dev.org/timestamp.php");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int len = 0;
			while((len = in.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			String body = new String(baos.toByteArray(), encoding);
			timestamp = Long.parseLong(body);
			System.out.println(timestamp);
			return timestamp;
		}
		return timestamp;
	}

	/**
	 * Method for increasing the <code>timestamp</code> by 1.
	 * 
	 * @throws IOException
	 */
	public void timestampInc() throws IOException {
		if(timestamp != 0)
			timestamp++;
		else timestamp();
	}

	/**
	 * Hvad gør denne her metode leow?
	 * 
	 * @param arr
	 * @param seperator
	 * @return
	 */
	public String implode(String[] arr, String seperator) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if(i != arr.length - 1)
				sb.append(seperator);
		}
		String joined = sb.toString();
		return joined;
	}

	/**
	 * Converts an <code>Arraylist</code> of <code>Integers</code> to a
	 * <code>int[]</code>.
	 * 
	 * @param integers
	 * @return int[]
	 */
	public int[] convertIntegers(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for(int i = 0; i < ret.length; i++) ret[i] = integers.get(i).intValue();
		return ret;
	}
	
	public Connector getConnector() {
		return connector;
	}
	
	public User getUser() {
		return this.user;
	}
}
