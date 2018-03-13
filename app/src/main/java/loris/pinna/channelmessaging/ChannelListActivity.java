/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging;

import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import loris.pinna.channelmessaging.adapter.MessageArrayAdapter;
import loris.pinna.channelmessaging.adapter.MySimpleArrayAdapter;
import loris.pinna.channelmessaging.classes.Channel;
import loris.pinna.channelmessaging.classes.Message;
import loris.pinna.channelmessaging.fragments.ChannelListFragment;
import loris.pinna.channelmessaging.fragments.MessageFragment;
import loris.pinna.channelmessaging.http.HttpPostHandler;
import loris.pinna.channelmessaging.http.JsonLoginResponse;
import loris.pinna.channelmessaging.http.PostRequest;
import loris.pinna.channelmessaging.listeners.OnDownloadListener;

public class ChannelListActivity extends ActionBarActivity implements View.OnClickListener , AdapterView.OnItemClickListener {

    private ListView lvMyListView;
    private Button friends;
    private Button channels;
    public static ArrayList<Channel> channels_list;
    private ArrayList<Message> messages = new ArrayList<>();

    private static final String PREFS_NAME = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channellist);

        lvMyListView = (ListView)findViewById(R.id.channels);
        friends = (Button) findViewById(R.id.friends_button);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendListActivity.class);
                startActivity(intent);
            }
        });

        channels = (Button) findViewById(R.id.crews_button);
        channels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChannelListActivity.class);
                startActivity(intent);
            }
        });

        requestChannels();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChannelListFragment fragA = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentA_ID);
        MessageFragment fragB = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentB_ID);

        if(fragB == null|| !fragB.isInLayout()){
            Intent intent = new Intent(getApplicationContext(), MessageListActivity.class);
            startActivity(intent);
        } else {

            final SharedPreferences settings =
                    getSharedPreferences(PREFS_NAME, 0);
            final String login =  settings.getString("access", "");
            Channel c = channels_list.get(position);
            refreshMessage(login, c.getChannelID());
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
                MessageFragment fragB = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentB_ID);
                fragB.setContent("Test", downloadedContent.getMessages(), login, getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));

            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
    }

    public ArrayList<Channel> requestChannels() {
         /*
        Récup du token
         */
        final SharedPreferences settings =
                getSharedPreferences(PREFS_NAME, 0);
        final String token =  settings.getString("access", "");
         /*
        Chargemennts des channels
         */
        PostRequest postRequest = new PostRequest(
                "http://www.raphaelbischof.fr/messaging/?function=getchannels",
                new HashMap<String, String>(){{
                    put("accesstoken", token);
                }}
        );

        HttpPostHandler handler = new HttpPostHandler(this);
        handler.addOnDownloadListeners(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(final JsonLoginResponse downloadedContent) {
                channels_list = downloadedContent.getChannels();

                ChannelListFragment fragA = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentA_ID);
                fragA.fillList(channels_list);
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
        return channels_list;
    }

}
