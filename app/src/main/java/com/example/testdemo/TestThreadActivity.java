package com.example.testdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.example.testdemo.databinding.ActivityTestThreadBinding;
import com.example.testdemo.utils.LastThread;

import java.util.concurrent.atomic.AtomicLong;

public class TestThreadActivity extends AppCompatActivity {

    ActivityTestThreadBinding binding;
    private static final String TAG = "TestThreadActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_thread);
        init();
    }

    private void init() {
        LastThread thread = new LastThread();
            thread.setListener(new LastThread.Listener() {
                @Override
                public void call(int flag, Object obj) {
                    if (flag == LastThread.START) {
                    Log.i(TAG, flag+"_____");
                    }
                }
            });
        binding.tv.setOnClickListener(v -> {
            thread.setSleep(1000);
            thread.call("");
        });
    }

}
