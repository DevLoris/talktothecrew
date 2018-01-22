package loris.pinna.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ChannelListActivity extends Activity implements View.OnClickListener {

    private ListView lvMyListView;

    private static final String PREFS_NAME = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channellist);

        lvMyListView = (ListView)findViewById(R.id.channels);

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

                lvMyListView.setAdapter(new MySimpleArrayAdapter(getApplicationContext(), downloadedContent.getChannels()));
                lvMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Channel channel = downloadedContent.getChannels().get(position);

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("channel", channel.getChannelID());
                        editor.putString("channel_name", channel.getName());
                        editor.commit();


                        Intent intent = new Intent(getApplicationContext(), MessageListActivity.class);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);

    }

    @Override
    public void onClick(View v) {

    }
}
