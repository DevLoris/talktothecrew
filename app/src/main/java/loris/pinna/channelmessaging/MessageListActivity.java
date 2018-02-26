/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import loris.pinna.channelmessaging.adapter.MessageArrayAdapter;
import loris.pinna.channelmessaging.classes.Message;
import loris.pinna.channelmessaging.dialog.FriendsDialog;
import loris.pinna.channelmessaging.http.HttpPostHandler;
import loris.pinna.channelmessaging.http.JsonLoginResponse;
import loris.pinna.channelmessaging.http.PostRequest;
import loris.pinna.channelmessaging.http.UploadFileToServer;
import loris.pinna.channelmessaging.listeners.OnDownloadListener;
import loris.pinna.channelmessaging.utils.ResizeImage;

public class MessageListActivity extends Activity {
    private Button send_message;
    private ImageButton photo;
    private ListView list_messages;
    private EditText input_chatmessage;
    private TextView conversations_name;
    private ArrayList<Message> messages = new ArrayList<>();

    private static final String PREFS_NAME = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);

        list_messages = (ListView)findViewById(R.id.messages_list);
        send_message = (Button) findViewById(R.id.send_message);
        photo = (ImageButton) findViewById(R.id.photo_button);
        input_chatmessage = (EditText) findViewById(R.id.input_chatmessage);
        conversations_name = (TextView) findViewById(R.id.conversation_name);

        setup();
    }

    public void setup() {
        final SharedPreferences settings =
                getSharedPreferences(PREFS_NAME, 0);
        final String token =  settings.getString("access", "");
        final int channel =  settings.getInt("channel", 1);
        final String channel_name =  settings.getString("channel_name", "???");



        refreshMessage(token, channel);

        list_messages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FriendsDialog dialog = new FriendsDialog();
                Message message = messages.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", message.getUserID());
                bundle.putString("name", message.getUsername());
                bundle.putString("url", message.getImageUrl());
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "friend");
                return true;
            }
        });
        list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = input_chatmessage.getText().toString();
                sendMessage(token, channel, message);
                input_chatmessage.setText("");
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(new File( getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/uploads/" + (new Random().nextInt(200000)) + ".jpg"), token,   channel);
            }
        });

    }


    public void sendMessage(final String token,final  int channel, final String message) {
        PostRequest postRequest = new PostRequest(
                "http://www.raphaelbischof.fr/messaging/?function=sendmessage",
                new HashMap<String, String>(){{
                    put("accesstoken", token);
                    put("message", message);
                    put("channelid", Integer.toString(channel));
                }}
        );

        HttpPostHandler handler = new HttpPostHandler(this);
        handler.addOnDownloadListeners(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(final JsonLoginResponse downloadedContent) {
                Toast.makeText(getApplicationContext(), downloadedContent.getResponse() ,Toast.LENGTH_SHORT).show();
                refreshMessage(token, channel);
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void refreshMessage(final String token,final  int channel) {
        PostRequest postRequest = new PostRequest(
                "http://www.raphaelbischof.fr/messaging/?function=getmessages",
                new HashMap<String, String>(){{
                    put("accesstoken", token);
                    put("channelid", Integer.toString(channel));
                }}
        );

        HttpPostHandler handler = new HttpPostHandler(this);
        handler.addOnDownloadListeners(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(final JsonLoginResponse downloadedContent) {

                final SharedPreferences settings =
                        getSharedPreferences(PREFS_NAME, 0);
                final String login =  settings.getString("login", "");

                messages = downloadedContent.getMessages();
                list_messages.setAdapter(new MessageArrayAdapter(getApplicationContext(), downloadedContent.getMessages(), login, getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)));

                hideKeyboard();
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
    }


    /*
    GESTION DES PHOTOS
     */


    private static final int INTENT_PHOTO = 0;
    private File _photoFile;
    private void takePhoto(File _photoFile, String token, int channel){
        this._photoFile = _photoFile;
        Uri _fileUri = Uri.fromFile(_photoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE
        );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, _fileUri);
        intent.putExtra("token", token);
        intent.putExtra("channel", channel);
        startActivityForResult(intent, INTENT_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent   intent) {
        switch (requestCode) {
            case INTENT_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try {
                        new ResizeImage().resizeFile(_photoFile, getApplicationContext());
                        String[] projection = { MediaStore.Images.ImageColumns.SIZE,
                                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                                MediaStore.Images.ImageColumns.DATA, BaseColumns._ID, };
                        Cursor c = null;
                        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                        UploadFileToServer uploadFileToServer = new UploadFileToServer(
                                getApplicationContext(),
                                _photoFile.getPath(),
                                new HashMap<String, String>() {{
                                    put("accesstoken", intent.getStringExtra("token"));
                                    put("channelid", intent.getStringExtra("channel"));
                                }},
                                new UploadFileToServer.OnUploadFileListener() {
                                    @Override
                                    public void onResponse(String result) {
                                        refreshMessage( intent.getStringExtra("token"),  Integer.parseInt(intent.getStringExtra("channel")));
                                    }

                                    @Override
                                    public void onFailed(IOException error) {

                                    }
                                }
                        );

                            try {
                            if (u != null) {
                                c = managedQuery(u, projection, null, null, null);
                            }
                            if ((c != null) && (c.moveToLast())) {
                                ContentResolver cr = getContentResolver();
                                cr.delete(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        BaseColumns._ID + "=" + c.getString(3), null);
                            }
                        } finally {
                            if (c != null) {
                                c.close();
                            }
                        }
                    }catch (IOException e) {

                    }

                }
                break;
        }
    }
}
