package com.theherdonline.ui.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.theherdonline.R;

public class MapMarkerBitmap  {
    public Bitmap mBitmap;

    public MapMarkerBitmap(Context context, int number) {
        int w = 180, h = 180;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        mBitmap = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        Canvas canvas = new Canvas(mBitmap);
        Paint paint = new Paint();
        //ColorFilter filter = new LightingColorFilter(ContextCompat.getColor(context, R.color.colorLightGreen), 1);
       // paint.setColorFilter(filter);

        Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_map_marker_big);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(rawBitmap, w, h, true);
        canvas.drawBitmap(scaledBitmap,new Matrix(), paint);


        /*TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(10 * context.getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawBitmap(scaledBitmap,new Matrix(), null);
        String str = number > 100 ? "..." : String.valueOf(number);
        canvas.drawText(str, w/2,h/2, textPaint);*/
    }

    public Bitmap asBitmap()
    {
        return mBitmap;
    }
}
