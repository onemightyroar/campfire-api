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


/**
 * The Class MessageStream.
 */
public class MessageStream implements Runnable {

    /** The Constant DEFAULT_TIMEOUT. The starting value for the timeout */
    private static final int DEFAULT_TIMEOUT = 1000;
    
    /** The Constant STEP_TIMEOUT. The amount to increase the timeout each failure */
    private static final int STEP_TIMEOUT = 1000;
    
    /** The Constant MAX_TIMEOUT. The timeout will never get higher than this */
    private static final int MAX_TIMEOUT = 16000;
    
    /** The ping interval */
    private static final int PING_INTERVAL = 60 * 4 * 1000;
    
    /** The size to read in from the buffered reader */
    private static final int BUFFERED_READER_SIZE = 2 * 1024;

    /** The buffered reader */
    private BufferedReader mBufferedReader;
    
    /** The campfire room object */
    private Room mRoom;
    
    /** The request method */
    private final HttpRequestBase mRequestMethod;
    
    /** The message mHandler used for callbacks  */
    private MessageStreamHandler mHandler;
    
    /** The mClient */
    private HttpClient mClient;
    
    /** The campfire api */
    private CampfireApi mApi;

    /** If a stop is requested */
    private boolean mStopRequested;
    
    /** The current timeout */
    private int mTimeout = DEFAULT_TIMEOUT;
    
    /** The ping. */
    private Thread mPing = new Thread()
    {
        @Override
        public void run() {
            while (true) {
                sleepMilli(PING_INTERVAL);
                
                MessageStream.this.mApi.pingRoom(MessageStream.this.mRoom.getId());

                if (MessageStream.this.mStopRequested) {
                    break;
                }
            }
        }
    };

    /**
     * Start a stream with a given connection.
     *
     * @param request the request
     * @param handler The handler to send items to
     * @param api the api
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public MessageStream(final HttpRequestBase request, 
            final MessageStreamHandler handler, final CampfireApi api) throws IOException {
        this.mRequestMethod = request;
        this.mHandler = handler;
        this.mClient = new DefaultHttpClient();
        this.mApi = api;
    }

    /**
     * Start a stream with a given connection
     * 
     * <p>
     * This method takes a reference to a Room object as a parameter. 
     * If provided, this message stream will manage the present
     * users inside the room object.
     * </p>
     * 
     * @param request the request
     * @param handler The handler to send items to
     * @param api the api
     * @param room the room
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public MessageStream(final HttpRequestBase request, 
            final MessageStreamHandler handler, final CampfireApi api, final Room room) throws IOException {
        this.mRequestMethod = request;
        this.mHandler = handler;
        this.mClient = new DefaultHttpClient();
        this.mApi = api;
        this.mRoom = room;
    }

    /**
     * Set the handler (used if wanting to modify handler mid-stream).
     *
     * @param handler The handler to substitute
     */
    public void setHandler(final MessageStreamHandler handler) {
        this.mHandler = handler;
    }

    /**
     * Stop waiting for new messages.  Signals the close of loop structures
     * so outside threads may close
     */
    public void stop() {
        this.mStopRequested = true;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
    	startPing();
    	
        while (true) {
            try {
                //continuously read message and other things, passing them to the mHandler as appropriate
                HttpResponse response = this.mClient.execute(this.mRequestMethod);
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new IOException("No entity");
                }
                
                this.mBufferedReader = new BufferedReader(
                        new InputStreamReader(entity.getContent(), "UTF-8"), BUFFERED_READER_SIZE);
                
                this.readIncoming();
                //successful, reset timeout
                this.mTimeout = DEFAULT_TIMEOUT;
            } catch (final IOException ex) {
                //wait a certain amount of time
                sleepMilli(this.mTimeout);
                
                //up the timeout to the next appropriate value
                this.mTimeout += STEP_TIMEOUT;
                
                if (this.mTimeout > MAX_TIMEOUT) {
                    this.mTimeout = MAX_TIMEOUT;
                }
                
                this.mHandler.disconnect(this.mRoom);
            }
            if (this.mStopRequested) {
                break;
            }
        }
        
        this.mHandler.stop();
        this.mRequestMethod.abort();
        this.mClient.getConnectionManager().shutdown();
    }

    /**
     * Start ping.
     */
    private void startPing() {
    	this.mPing.start();
    }
    
    
    /**
     * Sleep for a certain number of milliseconds.
     *
     * @param timeout Number of milliseconds to sleep
     */
    private void sleepMilli(final int timeout) {
        try { Thread.sleep(timeout); } catch (final InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Read an incoming line and attempt to parse it.  Continue this until
     * asked to stop or a connection issue is encountered
     * @throws IOException A connection issue was encountered
     */
    private void readIncoming() throws IOException {
        String line;
        
        while ((line = this.mBufferedReader.readLine()) != null) {
            try {
                if (line.length() == 0) {
                    continue;
                } 
                parseIncoming(line);
            } catch (final JSONException ex) {
            	ex.printStackTrace();
            }
            if (this.mStopRequested) {
                break;
            }
        }
    }

    /**
     * Parse incoming lines, determine what type they are and act accordingly.
     *
     * @param line The line to parse (JSON)
     * @throws JSONException the jSON exception
     */
    private void parseIncoming(final String line) throws JSONException {
        if (line == null || this.mStopRequested) {
            return;
        }
        
        String typeField = "type";
        
        //parse with JSON
    	try {
	        JSONObject elem = new JSONObject(line);
	        if (elem.has(typeField)) {
	            switch(MessageType.fromString(elem.getString(typeField))) {
                    case ADVERTISEMENT:
                    case ALLOW_GUEST:
                    case DISALLOW_GUEST:
                    case IDLE:
                    case PASTE:
                    case SOUND:
                    case SYSTEM:
                    case TEXT:
                    case TIMESTAMP:
                    case TOPIC_CHANGE:
                    case TWEET:
                    case UNIDLE:
                    case UNLOCK:
                    case UPLOAD:
                        parseMessage(elem);
                        break;
                    case ENTER:
                        parseLeaveMessage(elem);
                        break;
                    case KICK:
                    case LEAVE:
                        parseEnterMessage(elem);
                        break;
                    default:
                        break;
	            
	            }
	        }

		} catch (final JSONException e) {
			throw e;
		}
    }

    /**
     * Parse message objects and give them to the mHandler.
     *
     * @param elem the elem
     */
    private void parseMessage(final JSONObject elem) {
    	ResourceFactory resourceFactory = new ResourceFactory(this.mApi);
		
    	Message message = null;
    	if (this.mRoom == null) {
	        message = resourceFactory.buildMessage(elem);
    	} else {
	        message = resourceFactory.buildMessage(elem, this.mRoom);
    	}

    	
    	if (message.getType() == MessageType.SOUND) {
            message.setPlayed(false);
        }
    	
        if (message != null) {
            this.mHandler.addMessage(message);
        }
    }

    /**
     * Parse enter message objects and give them to the mHandler.
     *
     * @param elem the elem
     */
    private void parseEnterMessage(final JSONObject elem) {
    	ResourceFactory resourceFactory = new ResourceFactory(this.mApi);
		
    	Message message = null;
    	if (this.mRoom == null) {
	        message = resourceFactory.buildMessage(elem);
    	} else {
	        message = resourceFactory.buildMessage(elem, this.mRoom);
	        if (message.getUser() != null) {
	            this.mRoom.addActiveUser(message.getUser());
	        	this.mHandler.enter(message.getUser(), this.mRoom);
	        }
    	}
    	
        if (message != null) {
        	this.mHandler.addMessage(message);
        }
    }

    /**
     * Parse leave message objects and give them to the mHandler.
     *
     * @param elem the elem
     */
    private void parseLeaveMessage(final JSONObject elem) {
    	ResourceFactory resourceFactory = new ResourceFactory(this.mApi);
		
    	Message message = null;
    	if (this.mRoom == null) {
	        message = resourceFactory.buildMessage(elem);
    	} else {
	        message = resourceFactory.buildMessage(elem, this.mRoom);
	        if (message.getUser() != null) {
	            this.mRoom.addInactiveUser(message.getUser());
	        	this.mHandler.leave(message.getUser(), this.mRoom);
	        }
    	}
        if (message != null) {
        	this.mHandler.addMessage(message);
        }
    }

}
