package com.example.com.example;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.com.componentadapter.ComponentAdapter;


/**
 * Created by 默默 on 17/9/22.
 */

public class ComponentType0 implements ComponentAdapter.IItemComponent {
    private static final String TAG = "ComponentType0";
    private TextView textView;
    @Override
    public int getItemComponentType() {
        return 0;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_type0;
    }

    @Override
    public void onViewCreated(View view) {
        textView=view.findViewById(R.id.textView);
    }

    @Override
    public void bindView(RecyclerView.Adapter adapter, ComponentAdapter.IDataComponent t, int position) {
        textView.setText("我是第"+position+"个");
    }
}
