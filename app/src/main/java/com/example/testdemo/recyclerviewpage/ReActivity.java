package com.example.testdemo.recyclerviewpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.testdemo.R;
import com.example.testdemo.databinding.ActivityPageBinding;
import com.example.testdemo.databinding.ActivityReBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : huanzi
 * @date : 2020/1/18
 * desc :
 */
public class ReActivity extends AppCompatActivity {

    private ActivityReBinding binding;
    private List<Bean> beans = new ArrayList<>();
    private static final String TAG = "ReActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_re);
        initData();
        init();

    }

    private void init() {
        ReAdapter adapter = new ReAdapter(R.layout.item_page, beans);
        binding.recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewTask.setAdapter(adapter);
        adapter.setOnItemClickListener((a, v, p) -> {
            if (beans.get(p).isStatus()) {
                Log.i(TAG, p + "______");
            }
            View view = v;
            ImageView iv = view.findViewById(R.id.iv_jb);
            addView(v);
        });

    }

    private void addView(View view) {

        ConstraintLayout layout = view.findViewById(R.id.layout_jb);
        int laRight = layout.getRight();
        int laTop = layout.getTop();
        int laWidth = layout.getWidth();

        ImageView iv = layout.findViewById(R.id.iv_jb);
        int ivRight = iv.getRight();
        int ivTop = iv.getTop();
        int ivWidth = iv.getWidth();

        int top = view.getTop();
        int left = view.getLeft();
        int bottom = view.getBottom();
        int right = view.getRight();
        int width = view.getWidth();

        int reTop = binding.recyclerViewTask.getTop();
        int reRight = binding.recyclerViewTask.getRight();
        int reWidth = binding.recyclerViewTask.getWidth();

        int buTop = binding.ivTaskBubble.getTop();
        int buRight = binding.ivTaskBubble.getRight();
        int buWidth = binding.ivTaskBubble.getWidth();

        int suTop = binding.ivTaskSubject.getTop();
        int suRight = binding.ivTaskSubject.getRight();
        int suWidth = binding.ivTaskSubject.getWidth();

        Log.i(TAG, laTop + "-" + ivTop + "-" + top + "-" + right + "-" + bottom + "-" + reRight + "-" + reTop + "-" + buTop + "-" + buRight + "-" + suTop + "-" + suRight);
        Log.i(TAG, laRight + "-" + ivRight + "-" + right + "-" + reRight + "-" + buRight + "-" + suRight);
        Log.i(TAG, laWidth + "-" + ivWidth + "-" + width + "-" + reWidth + "-" + buWidth + "-" + suWidth);
        Log.i(TAG, (laRight - laWidth) + "-" + (ivRight - ivWidth) + "-" + (right - width) + "-" + (reRight - reWidth) + "-" + (buRight - buWidth) + "-" + (suRight - suWidth));

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.icon_primary_reward_jb_checked);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(64, 64);
        params.topToTop = binding.constraint.getId();
        params.endToEnd = binding.constraint.getId();
        params.width = 64;
        params.height = 64;
        params.topMargin = laTop + ivTop + top + reTop + buTop;
//        params.rightMargin = right + reRight + buRight + suRight + 100;
//        params.rightMargin = (right-width) + (reRight-reWidth) + (buRight-buWidth) ;
        params.rightMargin = laWidth + (reRight - reWidth) + (buRight - buWidth) + (right - laRight - ivRight);

        binding.constraint.addView(imageView, params);

    }

    private void initData() {
        for (int i = 0; i < 17; i++) {
            Bean bean = new Bean();
            bean.setName("0000+" + i);
            bean.setType(i % 3 + "");
            bean.setStatus(i % 4 == 0);
            beans.add(bean);
        }

    }

    class ReAdapter extends BaseQuickAdapter<Bean, BaseViewHolder> {

        public ReAdapter(int layoutResId, @Nullable List<Bean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Bean item) {
            helper.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_status, item.isStatus() + "")
                    .setText(R.id.tv_type, item.getType());
        }
    }

    class Bean {
        String type;
        String name;
        boolean status;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
