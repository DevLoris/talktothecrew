/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;

import loris.pinna.channelmessaging.adapter.FriendArrayAdapter;
import loris.pinna.channelmessaging.classes.Channel;
import loris.pinna.channelmessaging.db.User;
import loris.pinna.channelmessaging.db.UserDataSource;

public class FriendListActivity extends Activity implements View.OnClickListener {

    private ListView lvMyListView;
    private Button friends;
    private Button channels;

    private static final String PREFS_NAME = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        lvMyListView = (ListView)findViewById(R.id.friends_list);
        friends = (Button) findViewById(R.id.friends_button);

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
        /*
        Récup du token
         */
        final SharedPreferences settings =
                getSharedPreferences(PREFS_NAME, 0);
        final String token =  settings.getString("access", "");

        /*
        Récupératino dans la db
         */
        UserDataSource source = new UserDataSource(getApplicationContext());
        source.open();
        final List<User> users = source.getAllUsers();

        lvMyListView.setAdapter(new FriendArrayAdapter(getApplicationContext(), users, getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)));
        lvMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = users.get(position);

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("userid", user.getUserID());
                editor.putString("username", user.getUsername());
                editor.commit();


                Intent intent = new Intent(getApplicationContext(), FriendMessageListActivity.class);
                startActivity(intent);

            }
        });
        source.close();

    }

    @Override
    public void onClick(View v) {

    }
}
