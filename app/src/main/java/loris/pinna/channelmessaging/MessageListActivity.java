package loris.pinna.channelmessaging;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MessageListActivity extends Activity {
    private Button send_message;
    private ListView list_messages;
    private TextView input_chatmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);

        list_messages = (ListView)findViewById(R.id.messages_list);
        send_message = (Button) findViewById(R.id.send_message);
        input_chatmessage = (TextView) findViewById(R.id.input_chatmessage);
    }
}
