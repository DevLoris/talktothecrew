package loris.pinna.channelmessaging.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import loris.pinna.channelmessaging.R;
import loris.pinna.channelmessaging.adapter.MessageArrayAdapter;
import loris.pinna.channelmessaging.classes.Message;
import loris.pinna.channelmessaging.dialog.FriendsDialog;
import loris.pinna.channelmessaging.http.HttpPostHandler;
import loris.pinna.channelmessaging.http.JsonLoginResponse;
import loris.pinna.channelmessaging.http.PostRequest;
import loris.pinna.channelmessaging.listeners.OnDownloadListener;

public class MessageFragment extends Fragment {
    private Button send_message;
    private ImageButton photo;
    private ListView list_messages;
    private EditText input_chatmessage;
    private TextView conversations_name;
    public int chan;
    public String token;
    private File file;
    private ArrayList<Message> messages = new ArrayList<>();

    private static final String PREFS_NAME = "access_token";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messagelist,container);
        list_messages = (ListView)v.findViewById(R.id.messages_list);
        send_message = (Button) v.findViewById(R.id.send_message);
        photo = (ImageButton) v.findViewById(R.id.photo_button);
        input_chatmessage = (EditText) v.findViewById(R.id.input_chatmessage);
        conversations_name = (TextView) v.findViewById(R.id.conversation_name);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setContent(String name, ArrayList<Message> messages, String token, File file) {
        conversations_name.setText("Conv' : " + name);
        this.file = file;
        this.token = token;
        list_messages.setAdapter(new MessageArrayAdapter(getContext(), messages, token , file));
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

        HttpPostHandler handler = new HttpPostHandler(getContext());
        handler.addOnDownloadListeners(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(final JsonLoginResponse downloadedContent) {
                Toast.makeText(getContext(), downloadedContent.getResponse() ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
    }




}
