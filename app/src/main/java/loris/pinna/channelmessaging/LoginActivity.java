package loris.pinna.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button login;
    private TextView input_blase;
    private TextView input_password;

    private static final String PREFS_NAME = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        input_blase = (TextView) findViewById(R.id.input_blase);
        input_password = (TextView) findViewById(R.id.input_password);
    }

    @Override
    public void onClick(View v) {
        PostRequest postRequest = new PostRequest(
                "http://www.raphaelbischof.fr/messaging/?function=connect",
                new HashMap<String, String>(){{
                    put("username", input_blase.getText().toString());
                    put("password", input_password.getText().toString());
                }}
        );

        HttpPostHandler handler = new HttpPostHandler(this);
        handler.addOnDownloadListeners(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(JsonLoginResponse downloadedContent) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("access", downloadedContent.getAccesstoken());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ChannelListActivity.class);
                startActivity(intent);

            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                Toast.makeText(getApplicationContext(), error.getResponse() ,Toast.LENGTH_SHORT).show();
            }
        });
        handler.execute(postRequest);
    }
}
