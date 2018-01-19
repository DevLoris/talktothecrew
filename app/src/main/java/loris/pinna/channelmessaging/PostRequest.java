package loris.pinna.channelmessaging;

import java.util.HashMap;

/**
 * Created by pinnal on 19/01/2018.
 */
public class PostRequest {
    private String url;
    private HashMap<String, String> params;

    public PostRequest(String url, HashMap<String, String> params) {
        this.url = url;
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
