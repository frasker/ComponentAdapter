package com.example.com.example;


import com.example.com.componentadapter.ComponentAdapter;

/**
 * Created by 默默 on 17/9/22.
 */

public class Data implements ComponentAdapter.IDataComponent {
    int num;

    public Data(int num) {
        this.num = num;
    }

    @Override
    public int getViewType() {
        return num % 2;
    }
}
