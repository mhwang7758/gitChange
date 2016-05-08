package mhwang.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mhwang.com.bean.Record;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.NumberFormat;

/**
 * 项目名称：
 * 类描述：显示每条记录适配器
 * 作者：王明海
 * 创建时间：2016/5/4
 */
public class RecordAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Record> records;
    private LayoutInflater inflater;

    public RecordAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** 重置数据
     * @param data
     */
    public void resetData(ArrayList<Record> data){
        records.clear();
        for(Record record : data){
            records.add(record);
        }
        notifyDataSetChanged();
    }

    /** 获取数据集
     * @return
     */
    public ArrayList<Record> getData(){
        return records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Record record = records.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_record,null);
            holder = new ViewHolder();
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_item_record_date);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_item_record_type);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_item_record_money);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String date = record.getDay()+"日"+record.getTime();
        String type = record.getType()+"->"+record.getTypeChild();
        String status = record.getStatus();
        holder.tv_date.setText(date);
        holder.tv_type.setText(type);
        holder.tv_money.setTextColor(status.equals("支出") ? Color.GREEN : Color.RED);
        holder.tv_money.setText(NumberFormat.format(record.getMoney()));
        return convertView;
    }

    private static class ViewHolder{
        TextView tv_date;
        TextView tv_type;
        TextView tv_money;
    }
}
