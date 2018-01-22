package loris.pinna.channelmessaging;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final ArrayList<Message> values;
    public MessageArrayAdapter(Context context, ArrayList<Message> values) {
        super(context,  R.layout.view_message, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Message item = values.get(position);

        View rowView = inflater.inflate(  R.layout.view_message, parent, false);


        TextView textView = (TextView) rowView.findViewById(R.id.user_message);
        textView.setText(item.getUsername());
        textView.setTextColor(Color.BLACK);

        TextView textView2 = (TextView) rowView.findViewById(R.id.message_message);
        textView2.setText(item.getMessage());
        textView2.setTextColor(Color.BLACK);



        return rowView;
    }
}