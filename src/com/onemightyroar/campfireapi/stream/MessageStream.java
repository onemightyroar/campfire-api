package com.onemightyroar.campfireapi.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.onemightyroar.campfireapi.CampfireApi;
import com.onemightyroar.campfireapi.factory.ResourceFactory;
import com.onemightyroar.campfireapi.models.Message;
import com.onemightyroar.campfireapi.models.MessageType;
import com.onemightyroar.campfireapi.models.Room;


public class MessageStream implements Runnable {

    private BufferedReader br;
    private Room room;
    
    private final HttpRequestBase requestMethod;
    private MessageStreamHandler handler;
    private HttpClient client;
    private CampfireApi mApi;

    private boolean stopRequested = false;

    private static final int DEFAULT_TIMEOUT = 1000, STEP_TIMEOUT = 1000, MAX_TIMEOUT = 16000;
    private int timeout = DEFAULT_TIMEOUT;
    
    private int pingInterval = 60*4*1000;

    /**
     * Start a stream with a given connection
     * @param url The url to connect to
     * @param postContents The post contents as a string (null on none)
     * @param handler The handler to send items to
     */
    public MessageStream(HttpRequestBase request, MessageStreamHandler handler, CampfireApi api) throws IOException {
        this.requestMethod = request;
        this.handler = handler;
        this.client = new DefaultHttpClient();
        this.mApi = api;
    }

    /**
     * Start a stream with a given connection
     * 
     * This method takes a reference to a Room object as a parameter. If provided, this message stream will manage the present
     * users inside the room object.
     * 
     * @param url The url to connect to
     * @param postContents The post contents as a string (null on none)
     * @param handler The handler to send items to
     */
    public MessageStream(HttpRequestBase request, MessageStreamHandler handler, CampfireApi api, Room room) throws IOException {
        this.requestMethod = request;
        this.handler = handler;
        this.client = new DefaultHttpClient();
        this.mApi = api;
        this.room = room;
    }

    /**
     * Set the handler (used if wanting to modify handler mid-stream)
     * @param handler The handler to substitute
     */
    public void setHandler(MessageStreamHandler handler) {
        this.handler = handler;
    }

    /**
     * Stop waiting for new messages.  Signals the close of loop structures
     * so outside threads may close
     */
    public void stop() {
        this.stopRequested = true;
    }

    @Override
    public void run() {
    	startPing();
    	
        while (true) {
            try {
                //continuously read message and other things, passing them to the handler as appropriate
                HttpResponse response = client.execute(requestMethod);
                HttpEntity entity = response.getEntity();
                if (entity == null) throw new IOException("No entity");
                br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 2*1024);
                this.readIncoming();
                //successful, reset timeout
                this.timeout = DEFAULT_TIMEOUT;
            } catch (IOException ex) {
                //wait a certain amount of time
                sleepMilli(this.timeout);
                //up the timeout to the next appropriate value
                timeout += STEP_TIMEOUT;
                if (timeout > MAX_TIMEOUT) timeout = MAX_TIMEOUT;
                
                this.handler.disconnect(room);
            }
            if (this.stopRequested) break;
        }
        
        handler.stop(); // Signal to the handler
        this.requestMethod.abort();
        this.client.getConnectionManager().shutdown();
    }
    
    private Thread ping = new Thread()
    {
        @Override
        public void run() {
            while(true) {
				sleepMilli(MessageStream.this.pingInterval);
				
				mApi.pingRoom(room.getId());

	            if (MessageStream.this.stopRequested) break;
			}
        }
    };

    private void startPing() {
    	ping.start();
    }
    
    
    /**
     * Sleep for a certain number of milliseconds
     * @param timeout Number of milliseconds to sleep
     */
    private void sleepMilli(int timeout) {
        try { Thread.sleep(timeout); } catch (InterruptedException ex) { }
    }

    /**
     * Read an incoming line and attempt to parse it.  Continue this until
     * asked to stop or a connection issue is encountered
     * @throws IOException A connection issue was encountered
     */
    private void readIncoming() throws IOException {
        String line;
        
        while((line = br.readLine()) != null) {
            try {
                if (line.length() == 0) continue; 
                parseIncoming(line);
            } catch (JSONException ex) {
            	ex.printStackTrace();
            }
            if (this.stopRequested) break;
        }
    }

    /**
     * Parse incoming lines, determine what type they are and act accordingly.
     * @param line The line to parse (JSON)
     */
    private void parseIncoming(String line) throws JSONException {
        if (line == null || this.stopRequested) return;
        //parse with JSON
    	try {
	        JSONObject elem = new JSONObject(line);
	        if(elem.has("type")) {
	        	if(elem.getString("type").equals("TextMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("PasteMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("SoundMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("AdvertisementMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("AllowGuestsMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("DisallowGuestsMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("IdleMessage")) parseMessage(elem);
				if(elem.getString("type").equals("KickMessage")) parseLeaveMessage(elem);
	        	if(elem.getString("type").equals("EnterMessage")) parseEnterMessage(elem);
	        	if(elem.getString("type").equals("LeaveMessage")) parseLeaveMessage(elem);
	        	if(elem.getString("type").equals("SystemMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("TimestampMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("TopicChangeMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("UnidleMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("UnlockMessage")) parseMessage(elem);
	        	if(elem.getString("type").equals("UploadMessage")) parseMessage(elem);
	        }else{
	        	// Got a bad line, API might be out of date.
	        }

		} catch (JSONException e) {
			throw e;
		}
    }

    /**
     * Parse message objects and give them to the handler
     * @param obj The object to parse
     */
    private void parseMessage(JSONObject elem) {
    	ResourceFactory resourceFactory = new ResourceFactory(mApi);
		
    	Message message = null;
    	if(room == null) {
	        message = resourceFactory.buildMessage(elem);
    	}else{
	        message = resourceFactory.buildMessage(elem, room);
    	}

    	
    	if(message.getType() == MessageType.SOUND)
    		message.setPlayed(false);
    	
        if (message != null) this.handler.addMessage(message);
    }

    /**
     * Parse enter message objects and give them to the handler
     * @param obj The object to parse
     */
    private void parseEnterMessage(JSONObject elem) {
    	ResourceFactory resourceFactory = new ResourceFactory(mApi);
		
    	Message message = null;
    	if(room == null) {
	        message = resourceFactory.buildMessage(elem);
    	}else{
	        message = resourceFactory.buildMessage(elem, room);
	        if(message.getUser() != null) {
		        room.addActiveUser(message.getUser());
	        	this.handler.enter(message.getUser(), room);
	        }
    	}
    	
        if (message != null) {
        	this.handler.addMessage(message);
        }
    }

    /**
     * Parse leave message objects and give them to the handler
     * @param obj The object to parse
     */
    private void parseLeaveMessage(JSONObject elem) {
    	ResourceFactory resourceFactory = new ResourceFactory(mApi);
		
    	Message message = null;
    	if(room == null) {
	        message = resourceFactory.buildMessage(elem);
    	}else{
	        message = resourceFactory.buildMessage(elem, room);
	        if(message.getUser() != null) {
		        room.addInactiveUser(message.getUser());
	        	this.handler.leave(message.getUser(), room);
	        }
    	}
        if (message != null) {
        	this.handler.addMessage(message);
        }
    }

}