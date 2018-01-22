package loris.pinna.channelmessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MessageListActivity extends Activity {
    private Button send_message;
    private ListView list_messages;
    private EditText input_chatmessage;
    private TextView conversations_name;

    private static final String PREFS_NAME = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);


        final SharedPreferences settings =
                getSharedPreferences(PREFS_NAME, 0);
        final String token =  settings.getString("access", "");
        final int channel =  settings.getInt("channel", 1);
        final String channel_name =  settings.getString("channel_name", "???");


        list_messages = (ListView)findViewById(R.id.messages_list);
        send_message = (Button) findViewById(R.id.send_message);
        input_chatmessage = (EditText) findViewById(R.id.input_chatmessage);
        conversations_name = (TextView) findViewById(R.id.conversation_name);
        conversations_name.setText("Conv' : " + channel_name);

       refreshMessage(token, channel);

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
                list_messages.setAdapter(new MessageArrayAdapter(getApplicationContext(), downloadedContent.getMessages()));

                hideKeyboard();
            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
    }
}
