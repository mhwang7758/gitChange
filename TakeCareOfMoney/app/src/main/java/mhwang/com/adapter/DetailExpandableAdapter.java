package mhwang.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mhwang.com.bean.DateRecord;
import mhwang.com.bean.Record;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.NumberFormat;

/**
 * 项目名称：
 * 类描述：详细界面ExpandableListView适配器
 * 作者：王明海
 * 创建时间：2016/4/10
 */
public class DetailExpandableAdapter extends BaseExpandableListAdapter{
    private List<DateRecord> groups;
    private List<ArrayList<Record>> childs;
    private LayoutInflater inflater;
    private Context mContext;

    public DetailExpandableAdapter(Context context,List<DateRecord> groups,List<ArrayList<Record>> childs){
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.groups = groups;
        this.childs = childs;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_detail_group,null);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_detail_group_name);
        TextView tv_income = (TextView) convertView.findViewById(R.id.tv_detail_group_income);
        TextView tv_output = (TextView) convertView.findViewById(R.id.tv_detail_group_output);
        DateRecord record = groups.get(groupPosition);
        tv_name.setText(record.getName());
        tv_income.setText(NumberFormat.format(record.getIncome()));
        tv_output.setText(NumberFormat.format(record.getOutput()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_detail_child,null);
        Record record = childs.get(groupPosition).get(childPosition);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_detail_child_time);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_detail_child_name);
        TextView tv_status = (TextView) convertView.findViewById(R.id.tv_detail_child_menoy_status);
        tv_time.setText(record.getDay()+"日 "+record.getTime());
        tv_name.setText(record.getType()+"->"+record.getTypeChild());
        String status = record.getStatus();
        if (status.equals("支出")){
            tv_status.setTextColor(Color.GREEN);
        }else{
            tv_status.setTextColor(Color.RED);
        }
        tv_status.setText(NumberFormat.format(record.getMoney()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
