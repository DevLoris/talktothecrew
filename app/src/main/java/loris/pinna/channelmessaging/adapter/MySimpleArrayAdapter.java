/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import loris.pinna.channelmessaging.classes.Channel;
import loris.pinna.channelmessaging.R;

public class MySimpleArrayAdapter extends ArrayAdapter<Channel> {
    private final Context context;
    private final ArrayList<Channel> values;
    public MySimpleArrayAdapter(Context context, ArrayList<Channel> values) {
        super(context,  R.layout.view_channel, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Channel item = values.get(position);

        View rowView = inflater.inflate(  R.layout.view_channel, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.channel_name);
        textView.setText(item.getName());
        textView.setTextColor(Color.BLACK);

        TextView textView2 = (TextView) rowView.findViewById(R.id.online);
        textView2.setText(item.getConnectedusers() + " bros connectés");
        textView2.setTextColor(Color.BLACK);



        return rowView;
    }
}