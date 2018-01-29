/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.classes;

/**
 * Created by pinnal on 22/01/2018.
 */
public class Message {
    /*
    "userID": 1,
			"message": "Ceci est un test",
			"date": "2015-01-22 21:12:17",
			"imageUrl": "http://www.joomlaworks.net/images/demos/galleries/abstract/7.jpg"
     */

    private int userID;
    private String username;
    private String message;
    private String date;
    private String imageUrl;

    public Message(int userID, String username, String message, String date, String imageUrl) {
        this.userID = userID;
        this.username = username;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
