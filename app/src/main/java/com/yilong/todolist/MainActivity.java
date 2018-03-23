package com.yilong.todolist;

import android.content.Context;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yilong.todolist.activity.BaseActivity;
import com.yilong.todolist.view.FolderView;
import com.yilong.todolist.view.MyListView;
import com.yilong.todolist.view.SettingView;
import com.yilong.todolist.view.ZhkqyScrollview;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.scrollview)
    ZhkqyScrollview scrollview;

    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();

        SettingView settingView = new SettingView(this);
        LinearLayout.LayoutParams settingparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        settingView.setLayoutParams(settingparams);
        settingView.setBackgroundResource(R.color.red_btn_bg_color);
        container.addView(settingView);

        FolderView folderView = new FolderView(this);
        LinearLayout.LayoutParams folderparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        folderView.setBackgroundResource(R.color.yellow);
        folderView.setLayoutParams(folderparams);
        container.addView(folderView);

        MyListView myListView = new MyListView(this);
        LinearLayout.LayoutParams listparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        myListView.setBackgroundResource(R.color.task_center_patrol_major);
        myListView.setLayoutParams(listparams);
        container.addView(myListView);

    }
}
