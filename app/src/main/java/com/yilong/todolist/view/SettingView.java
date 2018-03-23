package com.yilong.todolist.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.yilong.todolist.R;

/**
 * Created by sun on 2018/3/23.
 */

public class SettingView extends FrameLayout {

    private Context mContext;

    public SettingView(@NonNull Context context) {
        super(context);
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.view_setting, this);
    }
}
