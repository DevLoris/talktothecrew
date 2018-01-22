package loris.pinna.channelmessaging;

/**
 * Created by pinnal on 19/01/2018.
 */
public interface OnImageDownloadListener {
    public void onDownloadComplete(String downloadedContent);

    public void onDownloadError(String error);
}
