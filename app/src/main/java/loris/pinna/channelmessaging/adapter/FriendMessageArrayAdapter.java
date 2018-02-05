/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import loris.pinna.channelmessaging.R;
import loris.pinna.channelmessaging.classes.FriendMessage;
import loris.pinna.channelmessaging.classes.Message;
import loris.pinna.channelmessaging.http.HttpGetImage;
import loris.pinna.channelmessaging.http.ImageRequest;
import loris.pinna.channelmessaging.listeners.OnImageDownloadListener;

public class FriendMessageArrayAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final ArrayList<Message> values;
    private final String username;
    private final File path;
    public FriendMessageArrayAdapter(Context context, ArrayList<Message> values, String username, File path) {
        super(context,  R.layout.view_friendmessage, values);
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

        View rowView = inflater.inflate(  R.layout.view_friendmessage, parent, false);


        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.message_layout);


        TextView textView = (TextView) rowView.findViewById(R.id.user_message);
        textView.setText(item.getUsername());
        textView.setTextColor(Color.BLACK);

        TextView textView2 = (TextView) rowView.findViewById(R.id.message_message);
        textView2.setText(item.getMessage());
        textView2.setTextColor(Color.BLACK);

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

        if(item.getEverRead() == 0)
            textView2.setTypeface(null, Typeface.BOLD);

        if(this.username.equalsIgnoreCase(item.getUsername())) {
            layout.setBackgroundResource(R.drawable.shape_my);
            textView2.setTextColor(Color.WHITE);
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