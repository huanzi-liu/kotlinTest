package com.example.testdemo.recyclerviewpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.testdemo.R;
import com.example.testdemo.databinding.ActivityPageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : huanzi
 * @date : 2020/1/16
 * desc :
 */
public class PageActivity extends AppCompatActivity {

    private ActivityPageBinding binding;
    private List<Bean> beans = new ArrayList<>();
    private ItemDialog dialog;
    private static final String TAG = "PageActivity";
    int index = 0;
    int rows = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_page);
        initData();
        init();
    }

    private void initData() {
        for (int i = 0; i < 17; i++) {
            Bean bean = new Bean();
            bean.setName("0000+" + i);
            bean.setType(i % rows + "");
            bean.setStatus(i % 4 == 0);
            beans.add(bean);
        }

        if (beans.size() > rows) {
            index = rows;
        } else {
            index = beans.size();
        }
        for (int i = 0; i < index; i++) {
            getBean(beans.get(i),i);

        }
    }

    private void init() {
        ReAdapter adapter = new ReAdapter(R.layout.item_page, beans);
        PagingScrollHelper helper = new PagingScrollHelper();
        PageLayoutManager manager = new PageLayoutManager(rows, 1, LinearLayout.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };

        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        helper.setUpRecycleView(binding.recyclerView);
        helper.updateLayoutManger();
        helper.scrollToPosition(0);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Log.i(TAG, helper.getPageCount() + "");
//                Log.i(TAG, helper.getPageIndex() + "");
//                Log.i(TAG, helper.getStartPageIndex() + "");
//                Log.i(TAG, newState + "");

                if (beans.size() <= rows ) {
                    return;
                }
                if (newState == 0) {
                    if (beans.size() > rows * (helper.getPageIndex() + 1)) {
                        for (int i = rows * helper.getPageIndex(); i < rows * (helper.getPageIndex() + 1); i++) {
                            Log.i(TAG, i + "-");
                            getBean(beans.get(i),i);
                        }
                    } else {
                        for (int i = rows * helper.getPageIndex(); i < beans.size(); i++) {
                            Log.i(TAG, i + "+");
                            getBean(beans.get(i),i);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        adapter.setOnItemClickListener((a,v,p)->{
                Log.i(TAG, p + "%%%");
            if (beans.get(p).isStatus()) {
                Log.i(TAG, p + "***");
            }
        });


    }

    private void getBean(Bean bean,int index) {
//        if (bean.isStatus() && dialog == null) {
//            if (index % rows == 0) {
//                showDialog(ItemDialog.TYPE_TOP);
//            } else if (index % rows == 1) {
//                showDialog(ItemDialog.TYPE_CENTER);
//            } else {
//                showDialog(ItemDialog.TYPE_BOTTOM);
//            }
//        }
    }

    private void showDialog(String type) {
        dialog = new ItemDialog(this, type, beans.get(0));
        dialog.show();
        dialog.setOnDismissListener(dialog1 -> {
            dialog = null;
        });
    }

    class ReAdapter extends BaseQuickAdapter<Bean, BaseViewHolder> {

        public ReAdapter(int layoutResId, @Nullable List<Bean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Bean item) {
            helper.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_status,item.isStatus()+"")
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
