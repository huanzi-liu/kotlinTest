package com.example.testdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        AppCompatImageView iv = findViewById(R.id.iv);
//        iv.setAnimation(getRotateAnimation());

    }
//    public static RotateAnimation getRotateAnimation() {
//        //旋转动画
//        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
//                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setInterpolator(new LinearInterpolator());//匀速
//        rotateAnimation.setRepeatCount(-1);//重复
//        rotateAnimation.setDuration(10000);
//        return rotateAnimation;
//    }
}
