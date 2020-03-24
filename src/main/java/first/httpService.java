package first;

import java.net.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.*;




public class httpService {

	public String apiUrl;
	public String apiKey;

	public httpService(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
	}
/*
	public void testConnection() throws Exception {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		try {
			URL url = new URL(this.apiUrl + "test_connection/" + this.apiKey);
			System.out.println(url.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			Reader reader = new InputStreamReader(connection.getInputStream());
			while (true) {
				int ch = reader.read();
				if (ch == -1) {
					break;
				}
				System.out.print((char) ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error when trying to connect to API");
		}
	}
*/
	public JSONObject checkInFirst(String md5, String crc32) throws Exception  {
		
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		try {
			URL url = new URL(this.apiUrl + "sample/checkin/" + this.apiKey);
			Charset charset = StandardCharsets.UTF_8;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", charset.toString());
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset.toString());
			String body ="md5=" + md5 + "&crc32=" + crc32;
			try (OutputStream os = conn.getOutputStream()){
				os.write(body.getBytes());
			}

			Reader reader = new InputStreamReader(conn.getInputStream());
			String retour="";
			while (true) {
				int ch = reader.read();
				if (ch == -1) {
					break;
				}
				retour+=(char)ch;
			}
			JSONObject result = new JSONObject(retour);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}

	}

	/*
	public static void main(String[] args) throws Exception {
		httpService myService = new httpService("https://louishusson.com/api/", "BFBFC6FC-4C84-4299-B2F6-7335C479810D");
		JSONObject result = myService.checkInFirst("1b3105ada011ed1053739d9c6028b3cc","1272189608");
		if(result.getBoolean("failed")==false) {
			out.println("Request Success");
			out.println("checkin = " + result.get("checkin"));
		}
		else
			out.println("Request failed");
	}
	*/

}