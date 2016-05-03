package Tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FacebookValidation {
	
	/* Facebooks guide: 
	 * https://developers.facebook.com/docs/facebook-login/for-devices#tech
	 * 
	 * Kilde til kode:
	 * http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
	 */
	
	public FacebookValidation() throws MalformedURLException, IOException {
	
	String url = "http://example.com";
	String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
	String param1 = "value1";
	String param2 = "value2";
	// ...

	String query = String.format("param1=%s&param2=%s", 
	     URLEncoder.encode(param1, charset), 
	     URLEncoder.encode(param2, charset));
	
	URLConnection connection = new URL(url).openConnection();
	connection.setDoOutput(true); // Triggers POST.
	connection.setRequestProperty("Accept-Charset", charset);
	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
	
	try (OutputStream output = connection.getOutputStream()) {
		output.write(query.getBytes(charset));
	}
	
	InputStream response = connection.getInputStream();
	
	}
	
	
}
