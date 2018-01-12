package com.yilong.todolist.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.yilong.todolist.R;
import com.yilong.todolist.utils.KeyBoardUtils;


public class ContentDialog extends Dialog {

    private Context mContext;
    private EditText editText;

    private Listener listener;

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
        editText = contentView.findViewById(R.id.editText);
        setCanceledOnTouchOutside(true);
        setContentView(contentView);
    }

    private void setHeight() {
        Window window = getWindow();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = displayMetrics.widthPixels;
        attributes.gravity = Gravity.TOP;//设置显示在中间

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
        void resultContentSuccess(String content);

        void resultNoContentError();
    }

    @Override
    public void show() {
        super.show();
        setHeight();
        KeyBoardUtils.openKeyboard(editText);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (listener != null) {
            String str = editText.getText().toString();
            if (!TextUtils.isEmpty(str)) {
                listener.resultContentSuccess(str);
            } else {
                listener.resultNoContentError();
            }
        }

    }
}
