package com.billy.plane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.billy.plane.Adapter.SettingAdapter;
import com.billy.plane.Entity.Item_settting;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private ListView list;
    private SettingAdapter adapter;
    private List<Item_settting> items;
    private ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        items = new ArrayList<Item_settting>();
        initItems();
        list = (ListView)findViewById(R.id.list);
        back_btn = (ImageView) findViewById(R.id.back);
        adapter = new SettingAdapter(this,R.layout.list_settings,items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void initItems()
    {
        items.add(new Item_settting(R.drawable.photo,"相册"));
        items.add(new Item_settting(R.drawable.wifi,"WIFI管理"));
        items.add(new Item_settting(R.drawable.height,"高度限定"));
        items.add(new Item_settting(R.drawable.hori,"水平校准"));
        items.add(new Item_settting(R.drawable.control,"遥感灵敏度"));
        items.add(new Item_settting(R.drawable.help,"帮助"));
    }
}
