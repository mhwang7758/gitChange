package com.haiming.android_listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by haiming on 2015/8/29.
 */
public class FruitAdapter extends ArrayAdapter<fruit> {
    private int recId;
public FruitAdapter(Context context,int recId,List<fruit> objeccts){
    super(context,recId,objeccts);
    this.recId = recId;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        fruit f = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(recId, null);
        ImageView fruitimg = (ImageView)view.findViewById(R.id.fruit_img);
        TextView fruittv = (TextView)view.findViewById(R.id.fruit_tv);
        fruitimg.setImageResource(f.getSourceId());
        fruittv.setText(f.getName());
        return view;
    }
}
