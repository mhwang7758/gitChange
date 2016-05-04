package mhwang.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：文本适配器
 * 作者：王明海
 * 创建时间：2016/5/4
 */
public class TextAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> arrays;
    private LayoutInflater inflater;

    public TextAdapter(Context context,ArrayList<String> arrays) {
        this.arrays = arrays;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrays.size();
    }

    @Override
    public Object getItem(int position) {
        return arrays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** 更新数据
     * @param data
     */
    public void resetData(ArrayList<String> data){
        arrays.clear();
        for(String str : data){
            arrays.add(str);
        }
        notifyDataSetChanged();
    }

    /** 获取内容列表
     * @return
     */
    public ArrayList<String> getData(){
        return arrays;
    }

    /**
     *  清除数据
     */
    public void clear(){
        arrays.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String text = arrays.get(position);
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_text,null);
            holder = new ViewHolder();
            holder.tv_text = (TextView) convertView.findViewById(R.id.tv_item_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tv_text.setText(text);
        return convertView;
    }

    private class ViewHolder{
        TextView tv_text;
    }
}
