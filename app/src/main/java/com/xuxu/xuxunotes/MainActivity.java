package com.xuxu.xuxunotes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xuxu.view.AutoScrollTextView;


public class MainActivity extends Activity implements View.OnClickListener {
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private Button textBtn, imgBtn, videoBtn;
    private ListView list;
    private Intent intent;
    private MyAdapter mAdapter;
    private AutoScrollTextView autoScrollTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        String str = "江西";
//        String province = HanziToPinyin.hanziToPinyin(str);
        Log.e("之前", str);
//        Log.e("江西拼音",province);
        textBtn = (Button) findViewById(R.id.btn_text);
        imgBtn = (Button) findViewById(R.id.btn_img);
        videoBtn = (Button) findViewById(R.id.btn_video);
        list = (ListView) findViewById(R.id.note_list);
        textBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        list.setAdapter(selectDB());
        autoScrollTextView = (AutoScrollTextView)findViewById(R.id.TextViewNotice);
        autoScrollTextView.init(getWindowManager());
        autoScrollTextView.startScroll();

//        selectDB();
//        addDB();
    }


    private MyAdapter selectDB(){
        Cursor cursor=dbReader.query(NotesDB.TABLE_NAME,null,null,
                null,null,null,null);
        Log.e("cursor", "查询数据，更新至listview");
        mAdapter = new MyAdapter(this, cursor);
        return mAdapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
        list.setAdapter(mAdapter);//心碎错误 一天，主布局文件没有指定oritation导致无法显示！！！！！！！！
    }

    @Override
    public void onClick(View view) {
        intent = new Intent(this,contentActivity.class);
        switch (view.getId()) {
            case R.id.btn_text:
                intent.putExtra("flag", "1");
                startActivity(intent);
                break;
            case R.id.btn_img:
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case R.id.btn_video:
                Intent intent_list = new Intent(this,MyListViewActivity.class);
//                intent.putExtra("flag", "3");
                startActivity(intent_list);
                break;
        }
    }
}
