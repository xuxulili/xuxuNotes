package com.xuxu.xuxunotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/19.
 */
public class contentActivity extends Activity implements View.OnClickListener {
    private String val;
    private ImageView c_image;
    private VideoView c_video;
    private Button save, cancel;
    private EditText editText;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    private File imageFile, videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val = getIntent().getStringExtra("flag");

        c_image = (ImageView) findViewById(R.id.c_image);
        c_video = (VideoView) findViewById(R.id.c_video);
        save = (Button) findViewById(R.id.c_btn_save);
        cancel = (Button) findViewById(R.id.c_btn_cancel);
        editText = (EditText) findViewById(R.id.c_edit);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        notesDB = new NotesDB(this);
        Log.e("notesDB", "数据库建立成功");
        dbWriter = notesDB.getWritableDatabase();

        initView();
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, editText.getText().toString());
        Log.e("输入", editText.getText().toString());
        cv.put(NotesDB.TIME, getTime());//此处为notesDB还是NotesDB???
        cv.put(NotesDB.PATH, imageFile + "");
        cv.put(NotesDB.VIDEO, videoFile + "");
//        cv.put(NotesDB.PATH,imageFile+"");
//        Log.e("图片途径",imageFile+"");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    private void initView() {
        if (val.equals("1")) {
            c_image.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }
        if (val.equals("2")) {
            c_image.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent iImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            iImage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            File dir = new File(Environment.getExternalStorageDirectory() + "/note" );
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            imageFile = new File(dir,getTime() + ".jpg");
//            Log.e("file",imageFile+"");
//            iImage.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, Uri.fromFile(imageFile));
            startActivityForResult(iImage, 1);
        }
        if (val.equals("3")) {
            c_image.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);
            Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            videoFile = new File(Environment.getExternalStorageDirectory()
//                    .getAbsoluteFile() + "/note" +"/"+ getTime() + ".mp4");
//
//            Log.e("存储路径",videoFile+"");
//            video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(video, 2);
        }
    }

    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DCIM), albumName);
        if (!file.mkdirs()) {
            Log.e("LOG_TAG", "Directory not created");
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 1) {

            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File file = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/note");
            }

            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = getTime() + ".jpg";
            imageFile = new File(file, fileName);

            try {
                b = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            c_image.setImageBitmap(bitmap);
        }
        if (requestCode == 2) {
            Uri uriVideo = data.getData();
            Cursor cursor = this.getContentResolver().query(uriVideo, null, null, null, null);
            if (cursor.moveToNext()) {
                                         /* _data：文件的绝对路径 ，_display_name：文件名 */
                String strVideoPath = cursor.getString(cursor.getColumnIndex("_data"));
                String videoName = cursor.getString(cursor.getColumnIndex("_display_name"));
//            Uri uri = Uri.fromFile(videoFile);
                Log.e("file", strVideoPath + "");
                videoFile = new File(strVideoPath);
                FileInputStream inputStream = null;
                FileOutputStream outputStream = null;

//                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File f = new File(Environment.getExternalStorageDirectory() + "/note/" );
                if (!f.exists()) {
                    f.mkdir();
                }
                File file =new File(f,videoName);
                    Log.e("file4", file + "");
                    try {
                        inputStream = new FileInputStream(videoFile);
                        outputStream = new FileOutputStream(file);
                        byte buffer[] = new byte[4 * 1024];
                        try {
                            while ((inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer);
                                Log.e("outputStream", outputStream + "");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    c_video.setVideoPath(strVideoPath);
                    c_video.setMediaController(new MediaController(this));
                    c_video.requestFocus();
//            Toast toast=Toast.makeText(this, "视频已保存在:"+path, Toast.LENGTH_LONG);
                    c_video.start();
                }
            }
        }



/**
 * 将一个InputStream里面的数据写入到SD卡中
 */

public File write2SDFromInput(String path,String fileName,InputStream input){
        File file=null;
        OutputStream output=null;
        try{

        output=new FileOutputStream(file);
        byte buffer[]=new byte[4*1024];
        while((input.read(buffer))!=-1){
        output.write(buffer);
        }
        output.flush();
        }catch(Exception e){
        e.printStackTrace();
        }finally{
        try{
        output.close();
        }catch(Exception e){
        e.printStackTrace();
        }
        }
        return file;
        }

private String getTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        String str=format.format(curDate);
        return str;
        }

@Override
public void onClick(View view){
        switch(view.getId()){
        case R.id.c_btn_save:
        addDB();
        Log.e("addDB","退出编辑页面，保存数据");
        finish();
        break;
        case R.id.c_btn_cancel:
        finish();
        break;
        }
        }
        }
