package com.xuxu.xuxunotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.logging.Log;

/**
 * Created by Administrator on 2015/6/20.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;//每行的集合
    private LayoutInflater inflater;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        System.out.println("getView " + i + " " + view);
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.mContent = (TextView) view.findViewById(R.id.list_text);
            viewHolder.mTime = (TextView) view.findViewById(R.id.list_time);
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.list_img);
            viewHolder.mVideo = (ImageView) view.findViewById(R.id.list_video);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
            System.out.println("view不是空的");
        }
        cursor.moveToPosition(i);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        android.util.Log.e("content", content);
        String time = cursor.getString(cursor.getColumnIndex("time"));
        android.util.Log.e("time", time);
//        if (cursor.getString(cursor.getColumnIndex("path"))!=null ){

        String url = cursor.getString(cursor.getColumnIndex("path"));
        android.util.Log.e("file1", url);//多次安装后出现数据库匹配出错情况
        viewHolder.mImageView.setImageBitmap(getImageThumbnail(url, 200, 200));
        String urlVideo = cursor.getString(cursor.getColumnIndex("video"));
        android.util.Log.e("file2", urlVideo);
        viewHolder.mVideo.setImageBitmap(getVideoThumbnail(urlVideo, 200, 200,
                MediaStore.Images.Thumbnails.MICRO_KIND));
//        }else {
//            viewHolder.mImageView.setImageBitmap(null);
//        }
        viewHolder.mContent.setText(content);
        viewHolder.mTime.setText(time);

        return view;
    }

    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap getVideoThumbnail(String uri, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }

    class ViewHolder {
        public TextView mContent;
        public TextView mTime;
        public ImageView mVideo;
        public ImageView mImageView;
    }
}
