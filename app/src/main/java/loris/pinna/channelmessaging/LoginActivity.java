/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;

import loris.pinna.channelmessaging.http.HttpPostHandler;
import loris.pinna.channelmessaging.http.JsonLoginResponse;
import loris.pinna.channelmessaging.http.PostRequest;
import loris.pinna.channelmessaging.listeners.OnDownloadListener;

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button login;
    private TextView input_blase;
    private TextView input_password;

    private static final String PREFS_NAME = "access_token";
    private static final String PREFS_NAME2 = "notif_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        input_blase = (TextView) findViewById(R.id.input_blase);
        input_password = (TextView) findViewById(R.id.input_password);

        final Handler mHandlerTada = new Handler(); // android.os.handler
        final int mShortDelay = 4000; //milliseconds

        mHandlerTada.postDelayed(new Runnable(){
            public void run(){
                // Your code here
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(1)
                        .playOn(findViewById(R.id.logo));
                mHandlerTada.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);
    }

    @Override
    public void onClick(final View v) {


        final SharedPreferences settings =
                getSharedPreferences(PREFS_NAME, 0);
        final String token = settings.getString("notif", "");

        final PostRequest postRequest = new PostRequest(
                "http://www.raphaelbischof.fr/messaging/?function=connect",
                new HashMap<String, String>() {{
                    put("username", input_blase.getText().toString());
                    put("password", input_password.getText().toString());
                    put("registrationid", token);
                }}
        );

        final HttpPostHandler handler = new HttpPostHandler(this);
        handler.addOnDownloadListeners(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(JsonLoginResponse downloadedContent) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("access", downloadedContent.getAccesstoken());
                editor.putString("login", input_blase.getText().toString());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ChannelListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, findViewById(R.id.logo), "logo").toBundle());

            }

            @Override
            public void onDownloadError(JsonLoginResponse error) {
                v.clearAnimation();

                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.llBackground),
                        error.getResponse(), Snackbar.LENGTH_SHORT);
                mySnackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        input_password.setText("");
                    }
                });
                mySnackbar.show();

               //Toast.makeText(getApplicationContext(), error.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });


        Animation animSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        v.startAnimation(animSlideLeft);
        final Handler mHandlerTada = new Handler(); // android.os.handler
        mHandlerTada.postDelayed(new Runnable() {
            public void run() {
                handler.execute(postRequest);

            }
        }, 500);
    }
}
