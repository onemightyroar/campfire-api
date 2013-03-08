package com.onemightyroar.campfireapi.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.onemightyroar.campfireapi.ApiException;
import com.onemightyroar.campfireapi.utils.Base64Coder;
import com.onemightyroar.campfireapi.xml.DOMUtils;

public class HttpConnection  {
	
	private final String credentials;

	public HttpConnection(String username, String password) {
		this.credentials = Base64Coder.encodeString(username + ":" + password);
	}
	
	public String getCredentials(){
		return credentials;
	}

	public String doGet(URL url) {
		return doMethod(url, "GET", null);
	}

	public String doPost(URL url, String request) {
		return doMethod(url, "POST", request);
	}

	public void doPut(URL url, String request) {
		doMethod(url, "PUT", request);
	}
	
	public void doDelete(URL url) {
		doMethod(url, "DELETE", null);
	}
	

	public String doMethod(URL url, String method, String messageBody) {
			
//			HttpParams params = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(params, 10000);
//			HttpConnectionParams.setSoTimeout(params, 10000); 
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
			String result = "";
			Log.i("Lantern",url.toString());
			try {
				
				if(method.equalsIgnoreCase("POST")) {
					HttpPost httpPost = new HttpPost(url.toString());
					
					StringEntity s = new StringEntity(messageBody.toString(), "UTF-8");
					s.setContentType("application/json");
					httpPost.setEntity(s);
				    httpPost.setHeader("Authorization", "Basic " + credentials);
				    httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
				    httpPost.setHeader("Accept", "application/json");
	
				    HttpResponse httpResponse = httpClient.execute(httpPost);
				    int responseCode = httpResponse.getStatusLine().getStatusCode();
				    HttpEntity httpEntity = httpResponse.getEntity();

				    if (httpEntity != null) {
				    	result = DOMUtils.convertStreamToString(httpEntity.getContent());
				    }
				    
				    httpClient.close();
				    
				    if (!successful(responseCode)) {
				    	throw new ApiException(responseCode, "Campfire API error");
				    }
				    
				    return result;
					
				}else if(method.equalsIgnoreCase("GET")) {
					HttpGet httpGet = new HttpGet(url.toString());
	
					httpGet.setHeader("Authorization", "Basic " + credentials);
					httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
				    httpGet.setHeader("Accept", "application/json");
				    				    
				    HttpResponse httpResponse = httpClient.execute(httpGet);
				    int responseCode = httpResponse.getStatusLine().getStatusCode();
				    HttpEntity httpEntity = httpResponse.getEntity();

				    if (httpEntity != null) {
				    	result = DOMUtils.convertStreamToString(httpEntity.getContent());
				    }
				    
				    httpClient.close();
				    
				    if (!successful(responseCode)) {
				    	throw new ApiException(responseCode, "Campfire API error at: " + url.toString());
				    }
				    
				    return result;
					    
				} else if(method.equalsIgnoreCase("PUT")) {
					HttpPut httpPut = new HttpPut(url.toString());
	
					httpPut.setHeader("Authorization", "Basic " + credentials);
					httpPut.setHeader("Content-Type", "application/json; charset=utf-8");
					httpPut.setHeader("Accept", "application/json");
	
				    HttpResponse httpResponse = httpClient.execute(httpPut);
				    int responseCode = httpResponse.getStatusLine().getStatusCode();
				    
				    httpClient.close();
				    
				    if (!successful(responseCode)) {
				    	throw new ApiException(responseCode, "Campfire API error");
				    }
				    
				    return null;
					    
				} else if(method.equalsIgnoreCase("DELETE")) {
					
					HttpDelete httpDelete = new HttpDelete(url.toString());
	
					httpDelete.setHeader("Authorization", "Basic " + credentials);
					httpDelete.setHeader("Content-Type", "application/json; charset=utf-8");
					httpDelete.setHeader("Accept", "application/json");
	
				    HttpResponse httpResponse = httpClient.execute(httpDelete);
				    int responseCode = httpResponse.getStatusLine().getStatusCode();
				    
				    httpClient.close();
				    
				    if (!successful(responseCode)) {
				    	throw new ApiException(responseCode, "Campfire API error");
				    }
				    
				    return null;
				    
				}
		    
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {}
			
			return null;
			
	}
	

	public String doPostUpload(URL url, InputStream stream, String filename, String mimeType) {
		try { 
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "---------------------------XXX";

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Basic " + credentials);
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			con.setUseCaches(false);
	         
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"upload\"; filename=\"" + filename + "\"" + lineEnd);
            dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            dos.writeBytes("Content-Type: " + mimeType + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = stream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = stream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = stream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = stream.read(buffer, 0, bufferSize);
            }
            
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            stream.close();
            dos.flush();
            dos.close();
            
			int responseCode = con.getResponseCode();
			
			if (!successful(responseCode)) {

				throw new ApiException(responseCode, "Campfire API error");

			} else {
		    	String result = DOMUtils.convertStreamToString(con.getInputStream());
				return result;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean successful(int responseCode) {
		return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
	}
	
    public HttpRequestBase buildConnection(String base, HttpParams params) {

            HttpGet method = new HttpGet(base);
            method.setParams(params);
            method.addHeader("Authorization", "Basic "+ credentials);
            method.addHeader("Content-Type", "application/json; charset=utf-8");  
            method.addHeader("Accept", "application/json");
            return method;

    }	
}