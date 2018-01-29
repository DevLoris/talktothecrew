/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.http;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import loris.pinna.channelmessaging.listeners.OnDownloadListener;

public class HttpPostHandler extends AsyncTask<PostRequest, Long, JsonLoginResponse> {
    public ArrayList<OnDownloadListener> onDownloadListeners = new ArrayList<>();

    private Context myContext;
    public HttpPostHandler(Context myContext)
    {
        this.myContext = myContext;
    }


    public void addOnDownloadListeners(OnDownloadListener downloadListener) {
        onDownloadListeners.add(downloadListener);
    }

    @Override
    protected JsonLoginResponse doInBackground(PostRequest[] params) {
        JsonLoginResponse r = null;
        for(PostRequest re : params) {
           String s =   performPostCall(re.getUrl(), re.getParams());
            Gson gson = new Gson();
            r = gson.fromJson(s, JsonLoginResponse.class);
        }
        return r;
    }

    @Override
    protected void onPostExecute(JsonLoginResponse result) {
        for(OnDownloadListener onDownloadListener : this.onDownloadListeners) {
            if(result.getCode() == 500)
                onDownloadListener.onDownloadError(result);
            else
                onDownloadListener.onDownloadComplete(result);
        }
    }


    public String performPostCall(String requestURL, HashMap<String, String>
            postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private String getPostDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
