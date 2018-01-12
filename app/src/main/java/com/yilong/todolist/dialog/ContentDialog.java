package com.yilong.todolist.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yilong.todolist.R;
import com.yilong.todolist.utils.DisplayUtil;


public class ContentDialog extends Dialog {

    private Context mContext;

    private View delete;
    private View edit;
    Listener listener;

    public ContentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initView();
    }

    private void initView() {
        View contentView = View.inflate(mContext, R.layout.dialog_content, null);
        setContentView(contentView);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        setHeight();
    }

    private void setHeight() {
        Window window = getWindow();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.4)) {
            attributes.height = (int) (displayMetrics.heightPixels * 0.4);
        }
        attributes.width = displayMetrics.widthPixels - DisplayUtil.dipToPixels(getContext(), 80);

        window.setAttributes(attributes);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyShortcut(keyCode, event);
    }


    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public interface Listener {
        void onRefreshList();
    }

}
