package mhwang.com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import mhwang.com.bean.DateRecord;
import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：首页下方日期项适配器
 * 作者：王明海
 * 创建时间：2016/4/9
 */
public class DateAdapter extends BaseAdapter {

    private Context mContext;
    private List<DateRecord> dateRecords;
    private LayoutInflater inflater;

    private void showLog(String msg){
        Log.d("---DateAdapter--->",msg);
    }

    public DateAdapter(Context context,List<DateRecord> dateRecords){
        mContext = context;
        this.dateRecords = dateRecords;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return dateRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return dateRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DateRecord record = (DateRecord) dateRecords.get(position);
        showLog("the record info is " + record.getName()
                + " status is " + record.getStatus());
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_date,null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_item_status);
            holder.tv_income = (TextView) convertView.findViewById(R.id.tv_item_income);
            holder.tv_output = (TextView) convertView.findViewById(R.id.tv_item_output);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tv_name.setText(record.getName());
        holder.tv_status.setText(record.getStatus());
        DecimalFormat df = new DecimalFormat(".##");
        holder.tv_income.setText(df.format(record.getIncome()));
        holder.tv_output.setText(df.format(record.getOutput()));
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_status;
        TextView tv_income;
        TextView tv_output;
    }
}
