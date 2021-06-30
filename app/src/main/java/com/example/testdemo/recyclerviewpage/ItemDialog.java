package com.example.testdemo.recyclerviewpage;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.testdemo.R;

/**
 * @author : huanzi
 * @date : 2020/1/16
 * desc :
 */
public class ItemDialog extends Dialog {

    public static final String TYPE_TOP = "top";
    public static final String TYPE_CENTER = "center";
    public static final String TYPE_BOTTOM = "bottom";

    Context context;
    String type;
    PageActivity.Bean bean;

    public ItemDialog(@NonNull Context context, String type, PageActivity.Bean bean) {
        super(context);
        this.context = context;
        this.type = type;
        this.bean = bean;
        init();

    }

    private void init() {

        View view;
        if (TYPE_TOP.equals(type)) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_popup_task_top, null);
        } else if (TYPE_CENTER.equals(type)) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_popup_task, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_popup_task_bottom, null);
        }
        setContentView(view);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        WindowManager manager = window.getWindowManager();
        Display display = manager.getDefaultDisplay();

        params.y = 605;
        params.height = 580;
        params.width = display.getWidth() - 80;

        window.setAttributes(params);
        window.setGravity(Gravity.TOP);
    }
}
