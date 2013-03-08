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
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;

import android.net.http.AndroidHttpClient;

import com.onemightyroar.campfireapi.ApiException;
import com.onemightyroar.campfireapi.utils.Base64Coder;
import com.onemightyroar.campfireapi.utils.StreamUtils;

/**
 * HttpConnection
 * 
 * Class for handling HTTP requests.
 */
public class JsonHttpConnection  {
    
    /** Success */
    public static final int SUCCESS = 200;
    
    /** unauthorized */
    public static final int UNAUTHORIZED = 401;
    
    /** Forbidden */
    public static final int FORBIDDEN = 403;
    
    /** Not found */
    public static final int NOT_FOUND = 404;
    
    /** get request */
    public static final String GET = "GET";
    
    /** post request */
    public static final String POST = "POST";
    
    /** put request */
    public static final String PUT = "PUT";
    
    /** delete request */
    public static final String DELETE = "DELETE";
    
    /** json request header */
    public static final String JSON_CONTENT_TYPE = "application/json";
    
    /** Upload max buffer size */
    private static final int UPLOAD_MAX_BUFFER_SIZE = 1024;
    
	/** The credentials. */
	private final String mCredentials;
	
	/** The request headers */
	private BasicHeader[] mHeaders = {
            new BasicHeader("Authorization", "Basic " + this.mCredentials),
            new BasicHeader("Content-Type", "application/json; charset=utf-8"),
            new BasicHeader("Accept", "application/json"),
    };

	/**
	 * Instantiates a new http connection.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public JsonHttpConnection(final String username, final String password) {
		this.mCredentials = Base64Coder.encodeString(username + ":" + password);
	}
	
	/**
	 * Gets the credentials.
	 *
	 * @return the credentials
	 */
	public String getCredentials() {
		return this.mCredentials;
	}

	/**
	 * Do get.
	 *
	 * @param url the url
	 * @return the string
	 */
	public String doGet(final URL url) {
		return doMethod(url, JsonHttpConnection.GET, null);
	}

	/**
	 * Do post.
	 *
	 * @param url the url
	 * @param request the request
	 * @return the string
	 */
	public String doPost(final URL url, final String request) {
		return doMethod(url, JsonHttpConnection.POST, request);
	}

	/**
	 * Do put.
	 *
	 * @param url the url
	 * @param request the request
	 */
	public void doPut(final URL url, final String request) {
		doMethod(url, JsonHttpConnection.PUT, request);
	}
	
	/**
	 * Do delete.
	 *
	 * @param url the url
	 */
	public void doDelete(final URL url) {
		doMethod(url, JsonHttpConnection.DELETE, null);
	}
	
	/**
	 * Do method.
	 *
	 * @param url the url
	 * @param method the method
	 * @param messageBody the message body
	 * @return the string
	 */
	public String doMethod(final URL url, final String method, final String messageBody) {
	    
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		
		String result = null;
		int responseCode = -1;
        HttpResponse httpResponse = null;
        HttpUriRequest httpRequest;
        
		try {
			if (JsonHttpConnection.POST.equalsIgnoreCase(method)) {
				StringEntity s = new StringEntity(messageBody.toString(), "UTF-8");
				s.setContentType(JsonHttpConnection.JSON_CONTENT_TYPE);
                httpRequest = new HttpPost(url.toString());
				((HttpPost) httpRequest).setEntity(s);
			} else if (JsonHttpConnection.GET.equalsIgnoreCase(method)) {
				httpRequest = new HttpGet(url.toString());
            } else if (JsonHttpConnection.PUT.equalsIgnoreCase(method)) {
                httpRequest = new HttpPut(url.toString());
            } else if (JsonHttpConnection.DELETE.equalsIgnoreCase(method)) {
                httpRequest = new HttpDelete(url.toString());
			} else {
                httpRequest = new HttpHead();
			}
			
			httpRequest.setHeaders(this.mHeaders);
            httpResponse = httpClient.execute(httpRequest);

            responseCode = httpResponse.getStatusLine().getStatusCode();
            
			if (JsonHttpConnection.GET.equalsIgnoreCase(method) 
			        || JsonHttpConnection.POST.equalsIgnoreCase(method)) {

                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null) {
                    result = StreamUtils.convertStreamToString(httpEntity.getContent());
                }
			}

            httpClient.close();
            
            if (!successful(responseCode)) {
                throw new ApiException(responseCode, "Campfire " + method + " API error at: " + url.toString());
            }
	    
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		return result;
			
	}

	/**
	 * Do post upload.
	 *
	 * @param url the url
	 * @param stream the stream
	 * @param filename the filename
	 * @param mimeType the mime type
	 * @return the string
	 */
	public String doPostUpload(final URL url, final InputStream stream, final String filename, final String mimeType) {
		try { 
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "---------------------------XXX";

            HttpURLConnection con = this.getUploadConnection(url);
			con.setRequestProperty(this.mHeaders[0].getName(), this.mHeaders[0].getValue());
			con.setRequestProperty(this.mHeaders[1].getName(), "multipart/form-data; boundary=" + boundary);
	         
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"upload\"; filename=\"" + filename + "\"" + lineEnd);
            dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            dos.writeBytes("Content-Type: " + mimeType + lineEnd + lineEnd);

            int bytesAvailable = stream.available();
            int bufferSize = Math.min(bytesAvailable, UPLOAD_MAX_BUFFER_SIZE);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = stream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = stream.available();
                bufferSize = Math.min(bytesAvailable, UPLOAD_MAX_BUFFER_SIZE);
                bytesRead = stream.read(buffer, 0, bufferSize);
            }
            
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            stream.close();
            dos.flush();
            dos.close();
            
			int responseCode = con.getResponseCode();
			
			if (!successful(responseCode)) {
				throw new ApiException(responseCode, "Campfire API error while posting upload");
			} else {
			    return StreamUtils.convertStreamToString(con.getInputStream());
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * getUploadConnection
	 * @param url The url
	 * @return The connection object
	 */
	private HttpURLConnection getUploadConnection(final URL url) {
        HttpURLConnection con = null;
        
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod(JsonHttpConnection.POST);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        
        return con;
	}
	
	/**
	 * Successful.
	 *
	 * @param responseCode the response code
	 * @return true, if successful
	 */
	public boolean successful(final int responseCode) {
		return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
	}
	
    /**
     * Builds the connection.
     *
     * @param base the base
     * @param params the params
     * @return the http request base
     */
    public HttpRequestBase buildConnection(final String base, final HttpParams params) {

            HttpGet method = new HttpGet(base);
            method.setParams(params);
            method.addHeader("Authorization", "Basic " + this.mCredentials);
            method.addHeader("Content-Type", "application/json; charset=utf-8");  
            method.addHeader("Accept", "application/json");
            return method;

    }
}
