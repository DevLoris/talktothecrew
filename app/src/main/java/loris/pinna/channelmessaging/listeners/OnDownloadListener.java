/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.listeners;

import loris.pinna.channelmessaging.http.JsonLoginResponse;

/**
 * Created by pinnal on 19/01/2018.
 */
public interface OnDownloadListener {
    public void onDownloadComplete(JsonLoginResponse downloadedContent);

    public void onDownloadError(JsonLoginResponse error);
}
