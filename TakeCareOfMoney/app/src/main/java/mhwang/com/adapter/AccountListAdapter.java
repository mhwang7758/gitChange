package mhwang.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：账户选择列表适配器
 * 作者：王明海
 * 创建时间：2016/4/11
 */
public class AccountListAdapter extends BaseAdapter {

    /**
     *  账户列表
     */
    private Map<String,Double> accounts;
    private LayoutInflater inflater;

    public AccountListAdapter(Context context,Map<String, Double> accounts) {
        this.accounts = accounts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_account,null);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_item_account_name);
        TextView tv_status = (TextView) convertView.findViewById(R.id.tv_item_account_status);
//        tv_name.setText(accounts.get(position).);
        return null;
    }
}
