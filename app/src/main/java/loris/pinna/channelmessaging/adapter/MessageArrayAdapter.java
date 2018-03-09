/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import loris.pinna.channelmessaging.FullScreenActivity;
import loris.pinna.channelmessaging.MessageListActivity;
import loris.pinna.channelmessaging.http.HttpGetImage;
import loris.pinna.channelmessaging.http.ImageRequest;
import loris.pinna.channelmessaging.classes.Message;
import loris.pinna.channelmessaging.R;
import loris.pinna.channelmessaging.listeners.OnImageDownloadListener;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final ArrayList<Message> values;
    private final String username;
    private final File path;
    public MessageArrayAdapter(Context context, ArrayList<Message> values, String username, File path) {
        super(context,  R.layout.view_message, values);
        this.context = context;
        this.values = values;
        this.path = path;
        this.username = username;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Message item = values.get(position);
        View rowView;
        if(item.getMessageImageUrl() != "")
            rowView = inflater.inflate(  R.layout.view_message_pics, parent, false);
        else if(item.getSoundUrl() != "")
            rowView = inflater.inflate(  R.layout.view_message_pics, parent, false);
        else
            rowView = inflater.inflate(  R.layout.view_message, parent, false);


        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.message_layout);


        TextView textView = (TextView) rowView.findViewById(R.id.user_message);
        textView.setText(item.getUsername());
        textView.setTextColor(Color.BLACK);

        final ImageView img = (ImageView) rowView.findViewById(R.id.image_user);

        ImageRequest request = new ImageRequest(item.getImageUrl(), this.path + item.getUsername() + ".jpg");
        HttpGetImage image = new HttpGetImage(this.context);
        image.addOnImageDownload(new OnImageDownloadListener() {
            @Override
                public void onDownloadComplete(String downloadedContent) {
                    img.setImageBitmap( getRoundedCornerBitmap(BitmapFactory.decodeFile(downloadedContent)));
            }

            @Override
            public void onDownloadError(String error) {
            }
        });
        image.execute(request);

        if(item.getMessageImageUrl() != "") {
            final ImageView img_message = (ImageView) rowView.findViewById(R.id.nude);

            ImageRequest request_message = new ImageRequest(item.getMessageImageUrl(), this.path +  item.getDate().replace(" ", "").replace("-", "").replace(":","") + ".jpg");
            HttpGetImage image_message = new HttpGetImage(this.context);
            image_message.addOnImageDownload(new OnImageDownloadListener() {
                @Override
                public void onDownloadComplete(String downloadedContent) {
                    img_message.setImageBitmap( getRoundedCornerBitmap(BitmapFactory.decodeFile(downloadedContent)));
                }

                @Override
                public void onDownloadError(String error) {
                }
            });
            image_message.execute(request_message);
            img_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullScreenActivity.class);

                    img_message.buildDrawingCache();
                    Bitmap image= img_message.getDrawingCache();

                    Bundle extras = new Bundle();
                    extras.putParcelable("imagebitmap", image);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }
        else {
            TextView textView2 = (TextView) rowView.findViewById(R.id.message_message);
            textView2.setText(item.getMessage());
            textView2.setTextColor(Color.BLACK);

            if(this.username.equalsIgnoreCase(item.getUsername())) {
                layout.setBackgroundResource(R.drawable.shape_my);
                textView2.setTextColor(Color.WHITE);
            }
        }

        return rowView;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        if(bitmap == null)
            return bitmap;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}