package com.yilong.todolist;

import android.support.v4.app.Fragment;

import com.yilong.todolist.activity.BaseActivity;
import com.yilong.todolist.adapter.ViewPageAdapter;
import com.yilong.todolist.fragment.FolderFragment;
import com.yilong.todolist.fragment.MyListFragment;
import com.yilong.todolist.fragment.SettingFragment;
import com.yilong.todolist.view.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {


    @BindView(R.id.viewpager)
    VerticalViewPager verticalViewPager;

    ViewPageAdapter pageAdapter;

    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initUI() {

        pageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        fragmentList.clear();
        fragmentList.add(new SettingFragment());
        fragmentList.add(new FolderFragment());
        fragmentList.add(new MyListFragment());

        pageAdapter.setFragments(fragmentList);

        verticalViewPager.setAdapter(pageAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }


}
