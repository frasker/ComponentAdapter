package com.example.com.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.com.componentadapter.ComponentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ComponentAdapter componentAdapter;
    List<Data> data = new ArrayList<>(10);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData(15);
        componentAdapter = new ComponentAdapter(this)
                .registerItemComponent(new ComponentType0())
                .registerItemComponent(new ComponentType1())
                .setData(data);
        recyclerView.setAdapter(componentAdapter);
    }


    private void initData(int num) {
        for (int i = 0; i < num; i++) {
            data.add(new Data(i));
        }
    }
}