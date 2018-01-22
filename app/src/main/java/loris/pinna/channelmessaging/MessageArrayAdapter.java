package loris.pinna.channelmessaging;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final ArrayList<Message> values;
    private final String username;
    public MessageArrayAdapter(Context context, ArrayList<Message> values, String username) {
        super(context,  R.layout.view_message, values);
        this.context = context;
        this.values = values;
        this.username = username;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Message item = values.get(position);

        View rowView = inflater.inflate(  R.layout.view_message, parent, false);


        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.message_layout);


        TextView textView = (TextView) rowView.findViewById(R.id.user_message);
        textView.setText(item.getUsername());
        textView.setTextColor(Color.BLACK);

        TextView textView2 = (TextView) rowView.findViewById(R.id.message_message);
        textView2.setText(item.getMessage());
        textView2.setTextColor(Color.BLACK);

        final ImageView img = (ImageView) rowView.findViewById(R.id.image_user);

        ImageRequest request = new ImageRequest(item.getImageUrl(), item.getUsername() + ".jpg");
        HttpGetImage image = new HttpGetImage(this.context);
        image.addOnImageDownload(new OnImageDownloadListener() {
            @Override
                public void onDownloadComplete(String downloadedContent) {
                Toast.makeText(context, downloadedContent, Toast.LENGTH_LONG).show();
                img.setImageBitmap(BitmapFactory.decodeFile(downloadedContent));
            }

            @Override
            public void onDownloadError(String error) {

            }
        });
        image.execute(request);

        if(this.username.equalsIgnoreCase(item.getUsername())) {
            layout.setBackgroundResource(R.drawable.shape_my);
            textView2.setTextColor(Color.WHITE);
        }

        return rowView;
    }
}