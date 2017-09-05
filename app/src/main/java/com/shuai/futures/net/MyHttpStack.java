package com.shuai.futures.net;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * 测试时信任测试环境
 * https://developer.android.com/training/articles/security-ssl.html#CommonHostnameProbs
 */
public class MyHttpStack extends HurlStack {
	
	HostnameVerifier hostnameVerifier = new HostnameVerifier() {
	    @Override
	    public boolean verify(String hostname, SSLSession session) {
	        HostnameVerifier hv =
	            HttpsURLConnection.getDefaultHostnameVerifier();
	        //return hv.verify("example.com", session);
	        return true;
	    }
	};
	
	protected HttpURLConnection createConnection(URL url) throws IOException {
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		if ("https".equals(url.getProtocol())){
			((HttpsURLConnection)conn).setHostnameVerifier(hostnameVerifier);
			
			try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[]{new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {}
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}}}, new SecureRandom());
			((HttpsURLConnection)conn).setSSLSocketFactory(
					context.getSocketFactory());
			} catch (Exception e) { // should never happen
				e.printStackTrace();
			}
		}
		return conn;
    }

}
