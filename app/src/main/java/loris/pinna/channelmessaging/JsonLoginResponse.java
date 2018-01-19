package loris.pinna.channelmessaging;

import java.util.ArrayList;
import java.util.List;

public class JsonLoginResponse {
    private String response;
    private int code;
    private String accesstoken;


    private ArrayList<Channel> channels;


    public JsonLoginResponse(String response, int code, String accesstoken) {
        this.response = response;
        this.code = code;
        this.accesstoken = accesstoken;
    }

    public JsonLoginResponse(ArrayList<Channel> channels) {
        this.channels = channels;
    }

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
}
