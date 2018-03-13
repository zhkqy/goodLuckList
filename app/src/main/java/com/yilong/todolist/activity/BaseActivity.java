package com.yilong.todolist.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;

/**
 * Created by chenlei on 2017/6/9.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView();
        ButterKnife.bind(this);
        initPresenter();
        initUI();
        initListener();
        initData();
    }

    protected abstract void setContentView();

    protected abstract void initPresenter();

    protected abstract void initUI();

    protected abstract void initListener();

    protected abstract void initData();

}
