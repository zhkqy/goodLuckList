package com.yilong.todolist.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by sun on 2018/3/23.
 */

public class FolderView extends FrameLayout {
    public FolderView(@NonNull Context context) {
        super(context);
    }

    public FolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
