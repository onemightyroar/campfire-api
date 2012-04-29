campfire-api
============

A java library for the Campfire API. This library was created primarily for use with Android. There may be dependencies that currently reflect this.

To see the official Campfire API documentation, visit http://developer.37signals.com/campfire

Usage
-----

To get started, if you do not know the user's api token, use:

```java
CampfireApi api = new CampfireApi(accountname); 
```

If you do know the user's api token, you can use:

```java
CampfireApi api = new CampfireApi(accountname,authtoken);
```

If you're using the first constructor, you can get the current user's api token for other calls by using:

```java
User me = api.getMe(username, password);
api.setAuthToken(me.getApiAuthToken());
```

Once you have set the user's api auth token you can make the remainder of the API calls.

###Basic Usage

```java
CampfireApi api = new CampfireApi(accountname); 
User me = api.getMe(username, password);
api.setAuthToken(me.getApiAuthToken());

List<Room> rooms = api.getRooms();
Room exampleRoom = rooms.get(0);

// Get the messages for a specific room. If you pass the room object it will
// Maintain the list of active and inactive users. (see below)
List<Message> messages = api.getRecentMessages(exampleRoom);

// The toom object contains lists of active and inactive users in the room.
// (an inactive user is one with messages in the transcript, but no current presence.
// This information isn't available until after you call getRecentMessages for a room.
Map<Long, User> activeUsers = exampleRoom.getActiveUsers();
Map<Long, User> inactiveUsers = exampleRoom.getInactiveUsers();

//Post a message
api.postMessage(exampleRoom, "TextMessage", "Hello world!");

```

###Streaming Usage

```java
//Message stream implements the Runnable interface
try {
  MessageStream stream = api.messages(exampleRoom, new MyMessageStreamHandler()));
} catch (IOException e) {
	e.printStackTrace();
}

Thread thread = new Thread(stream);
thread.start();

```

```java
class MyMessageStreamHandler implements MessageStreamHandler{

	@Override
	public void addMessage(final Message message) {
    //we have a new message
	}

	@Override
	public void stop() {
	  //the stream has been stopped
	}

	@Override
	public void enter(User user, Room room) {
    //A user has entered the room
	}

	@Override
	public void leave(final User user, final Room room) {
    //A user has left the room
	}
    	
}
```

Contact
-------

* brian@onemightyroar.com
* http://github.com/brianmuse
* http://twitter.com/briancmuse

License
-------

###MIT License

Copyright (c) 2012 One Mighty Roar (http://onemightyroar.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.