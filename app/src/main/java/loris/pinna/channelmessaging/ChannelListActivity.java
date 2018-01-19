package loris.pinna.channelmessaging;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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



        /*
        Récup du token
         */
        SharedPreferences settings =
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
            public void onDownloadComplete(JsonLoginResponse downloadedContent) {
                Toast.makeText(getApplicationContext(), downloadedContent.getChannels().toString() ,Toast.LENGTH_SHORT).show();

                lvMyListView = (ListView)findViewById(R.id.channels);
                lvMyListView.setAdapter(new MySimpleArrayAdapter(getApplicationContext(), downloadedContent.getChannels()));
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                //Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);

    }

    @Override
    public void onClick(View v) {

    }
}
