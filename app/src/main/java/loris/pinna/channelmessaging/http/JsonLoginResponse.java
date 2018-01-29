/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.http;

import java.util.ArrayList;

import loris.pinna.channelmessaging.classes.Channel;
import loris.pinna.channelmessaging.classes.Message;

public class JsonLoginResponse {
    private String response;
    private int code;
    private String accesstoken;


    private ArrayList<Channel> channels;
    private ArrayList<Message> messages;


    public JsonLoginResponse(String response, int code, String accesstoken) {
        this.response = response;
        this.code = code;
        this.accesstoken = accesstoken;
    }

  /*  public JsonLoginResponse(ArrayList<Object> channels) {
        if(channels.get(0) instanceof Channel)
            this.channels = (ArrayList<Channel>) channels;
    } */

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
