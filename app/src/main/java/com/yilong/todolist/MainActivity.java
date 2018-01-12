package com.yilong.todolist;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.yilong.todolist.view.RefreshView;


public class MainActivity extends Activity {

    private RefreshView progressbarRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = findViewById(R.id.listview);
        progressbarRefresh = findViewById(R.id.progressbar_refresh);
        progressbarRefresh.setSlidablyView(listview);
        listview.setAdapter(new ListAdapter());

    }

    class ListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 22;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(MainActivity.this, R.layout.item_list, null);
            ImageView img = v.findViewById(R.id.img);

//紫色
            int[] startColor = {81, 57, 157};
            int[] endColor = {189, 176, 227};

//            红色

//            int[] startColor = {240, 0, 0};
//            int[] endColor = {250, 254, 69};

            int redPadding = (endColor[0] - startColor[0]) / getCount();
            if (redPadding > 15) {
                redPadding = 15;
            }
            int r = startColor[0] + redPadding * i;


            int greenPadding = (endColor[1] - startColor[1]) / getCount();
            if (greenPadding > 15) {
                greenPadding = 15;
            }
            int g = startColor[1] + greenPadding * i;

            int bluePadding = (endColor[2] - startColor[2]) / getCount();
            if (bluePadding > 15) {
                bluePadding = 15;
            }
            int b = startColor[2] + bluePadding * i;

            img.setBackgroundColor(Color.rgb(r, g, b));
            return v;

        }
    }


}
