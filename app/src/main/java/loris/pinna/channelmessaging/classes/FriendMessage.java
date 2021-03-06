/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.classes;

/**
 * Created by pinnal on 22/01/2018.
 */
public class FriendMessage {

    private int userID;
    private int sendbyme;
    private String username;
    private String message;
    private String date;
    private String imageUrl;
    private int everRead;

    public FriendMessage(int userID, int sendbyme, String username, String message, String date, String imageUrl, int everRead) {
        this.userID = userID;
        this.sendbyme = sendbyme;
        this.username = username;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.everRead = everRead;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSendbyme() {
        return sendbyme;
    }

    public void setSendbyme(int sendbyme) {
        this.sendbyme = sendbyme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getEverRead() {
        return everRead;
    }

    public void setEverRead(int everRead) {
        this.everRead = everRead;
    }
}
