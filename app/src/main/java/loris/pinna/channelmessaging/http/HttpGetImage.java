/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import loris.pinna.channelmessaging.listeners.OnImageDownloadListener;

public class HttpGetImage extends AsyncTask<ImageRequest, Long, String> {
    private static final String TAG = HttpGetImage.class.getSimpleName();
    public ArrayList<OnImageDownloadListener> onImageDownloadListeners = new ArrayList<>();

    private Context myContext;
    public HttpGetImage(Context myContext)
    {
        this.myContext = myContext;
    }


    public void addOnImageDownload(OnImageDownloadListener downloadListener) {
        onImageDownloadListeners.add(downloadListener);
    }

    @Override
    protected String doInBackground(ImageRequest[] params) {
        String r = null;
        for(ImageRequest re : params) {
           r =   downloadFromUrl(re.getUrl(), re.getName());
        }
        return r;
    }

    @Override
    protected void onPostExecute(String result) {
        for(OnImageDownloadListener onDownloadListener : this.onImageDownloadListeners) {
            if(result == null)
                onDownloadListener.onDownloadError(result);
            else
                onDownloadListener.onDownloadComplete(result);
        }
    }


    public String downloadFromUrl(String fileURL, String fileName) {
        try {
            URL url = new URL( fileURL);
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            else
                return fileName;
 /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();
 /* Define InputStreams to read from the URLConnection.*/
            InputStream is = ucon.getInputStream();
 /* Read bytes to the Buffer until there is nothing more to
read(-1) and write on the fly in the file.*/
            FileOutputStream fos = new FileOutputStream(file);
            final int BUFFER_SIZE = 23 * 1024;
            BufferedInputStream bis = new BufferedInputStream(is,
                    BUFFER_SIZE);
            byte[] baf = new byte[BUFFER_SIZE];
            int actual = 0;
            while (actual != -1) {
                fos.write(baf, 0, actual);
                actual = bis.read(baf, 0, BUFFER_SIZE);
            }
            fos.close();
            return fileName;
        } catch (IOException e) {
            //TODO HANDLER
            Log.e(TAG, "downloadFromUrl: " , e );
            return null;
        }
    }

}
