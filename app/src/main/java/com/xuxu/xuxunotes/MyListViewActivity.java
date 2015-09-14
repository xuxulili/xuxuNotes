package com.xuxu.xuxunotes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.xuxu.view.CornerListView;
import com.xuxu.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/14.
 */
public class MyListViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", i);
            list.add(map);
        }

        CornerListView myListView = (CornerListView) findViewById(R.id.myListView);
        CornerListView myListView_two = (CornerListView) findViewById(R.id.myListView_two);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.item_my,
               new String[]{"title"}, new int[]{R.id.title});
        myListView.setAdapter(simpleAdapter);
        SimpleAdapter simpleAdapter_two = new SimpleAdapter(this, list, R.layout.item_my,
                new String[]{"title"}, new int[]{R.id.title});
        myListView_two.setAdapter(simpleAdapter_two);

    }
}
