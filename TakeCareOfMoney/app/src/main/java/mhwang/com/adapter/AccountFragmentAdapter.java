package mhwang.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mhwang.com.bean.Account;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.NumberFormat;

/**
 * 项目名称：
 * 类描述：账户界面适配器
 * 作者：王明海
 * 创建时间：2016/4/30
 */
public class AccountFragmentAdapter extends BaseAdapter {

    private ArrayList<Account> accounts;
    private Context context;
    private LayoutInflater inflater;

    public AccountFragmentAdapter(ArrayList<Account> accounts, Context context) {
        this.accounts = accounts;
        this.context = context;
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
        ViewHolder holder;
        Account account = accounts.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_account,null);
            holder = new ViewHolder();
            holder.tv_accountName = (TextView) convertView.findViewById(R.id.tv_item_account_name);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_item_account_status);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tv_accountName.setText(account.getName());
        holder.tv_money.setText(NumberFormat.format(account.getMoney()));
        return convertView;
    }

    private static class ViewHolder{
        TextView tv_accountName;
        TextView tv_money;
    }

}
