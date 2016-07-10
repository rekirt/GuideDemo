package com.example.lc.guidedemo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import butterknife.InjectView;
import butterknife.OnItemClick;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.lv_demo_list)
    ListView lv_demo_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);
        adapter.add("Guide1");
        adapter.add("Guide2");
        lv_demo_list.setAdapter(adapter);
    }

    @OnItemClick(R.id.lv_demo_list)
    public void itemClick(int position){
        switch (position){
            case 0:
                openActivity(GuideActivity1.class);
                break;
            case 1:

                break;
        }
    }


}
