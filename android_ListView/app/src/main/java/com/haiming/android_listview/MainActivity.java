package com.haiming.android_listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<fruit> fruits = new ArrayList<fruit>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //建立适配器
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        FruitAdapter adapter = new FruitAdapter(MainActivity.this,R.layout.fruit_item,
                fruits);
        ListView listView =(ListView)findViewById(R.id.mylist);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void Init(){
        fruit apple = new fruit("苹果",R.drawable.anple_pic);
        fruits.add(apple);
        fruit pears = new fruit("梨",R.drawable.pear_pic);
        fruits.add(pears);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
