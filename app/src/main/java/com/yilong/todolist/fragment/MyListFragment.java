package com.yilong.todolist.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilong.todolist.R;
import com.yilong.todolist.view.RefreshView;

import java.util.ArrayList;

/**
 * Created by sun on 2018/3/13.
 */

public class MyListFragment extends Fragment {


    private RefreshView progressbarRefresh;

    private static ArrayList<String> arrayList = new ArrayList<>();
    private static ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.fragment_list, null);

        arrayList.clear();
        for (int x = 0; x < 20; x++) {
            arrayList.add("测试" + (x + 1));
        }

        listAdapter = new ListAdapter();

        ListView listview = v.findViewById(R.id.listview);

        progressbarRefresh = v.findViewById(R.id.progressbar_refresh);
        progressbarRefresh.setSlidablyView(listview);
        listview.setAdapter(listAdapter);

        return v;
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
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
            View v = View.inflate(getActivity(), R.layout.item_list, null);
            ImageView img = v.findViewById(R.id.img);

            TextView content = v.findViewById(R.id.content);
            String str = arrayList.get(i);
            content.setText(str);

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
            int r = startColor[0] + redPadding * (i + 1);

            int greenPadding = (endColor[1] - startColor[1]) / getCount();
            if (greenPadding > 15) {
                greenPadding = 15;
            }
            int g = startColor[1] + greenPadding * (i + 1);

            int bluePadding = (endColor[2] - startColor[2]) / getCount();
            if (bluePadding > 15) {
                bluePadding = 15;
            }
            int b = startColor[2] + bluePadding * (i + 1);

            img.setBackgroundColor(Color.rgb(r, g, b));
            return v;

        }
    }

    public static void add(String str) {
        arrayList.add(0, str);
        listAdapter.notifyDataSetChanged();
    }
}
