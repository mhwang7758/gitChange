package mhwang.com.takecareofmoney;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import mhwang.com.abstracts.PagerFragment;
import mhwang.com.adapter.AccountFragmentAdapter;
import mhwang.com.bean.Account;
import mhwang.com.bean.Record;
import mhwang.com.database.DBUtil;
import mhwang.com.util.NumberFormat;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/7
 */
public class AccountFragment extends PagerFragment{
    private View mView;
    private ArrayList<Account> accounts;
    private ListView mListView;

    private AccountFragmentAdapter adapter;
    private TextView tv_totalSurplusAsset;
    private TextView tv_totalNetAsset;
    private TextView tv_totalNegativeAsset;

    private double totalIncome = 0.00;
    private double totalOutcome = 0.00;
    private double totalSurplus = 0.00;
    private double negativeAsset = 0.00;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account,null);
        initComponent();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        mListView = (ListView)mView.findViewById(R.id.lv_account_list);
        tv_totalNegativeAsset = (TextView)mView.findViewById(R.id.tv_total_negative_asset);
        tv_totalNetAsset = (TextView)mView.findViewById(R.id.tv_account_net_asset);
        tv_totalSurplusAsset = (TextView)mView.findViewById(R.id.tv_account_total_surplus);
        adapter = new AccountFragmentAdapter(accounts,getActivity());
        mListView.setAdapter(adapter);
    }

    /**
     *  显示数据
     */
    private void showData(){
        tv_totalNegativeAsset.setText(NumberFormat.format(negativeAsset));
        tv_totalSurplusAsset.setText(NumberFormat.format(totalIncome));
        tv_totalNetAsset.setText(NumberFormat.format(totalSurplus));
    }

    /**
     *  初始化数据
     */
    private void initData() {
        DBUtil dbUtil = DBUtil.getInstance(getActivity());
        accounts = dbUtil.readAccounts();
        for(Account account : accounts){
            ArrayList<Record> records = dbUtil.readRecordsByAccount(account.getName());
            double outcome = 0.00;
            double income = 0.00;
            for (Record record : records){
                if (record.getStatus().equals("支出")){
                    outcome += record.getMoney();
                    if (record.getAccount().equals("信用卡")){
                        negativeAsset += record.getMoney();
                    }
                }else{
                    income += record.getMoney();
                }
            }
            totalIncome += income;
            totalOutcome += outcome;
            account.setMoney(outcome);
        }
        totalSurplus = totalIncome - totalOutcome;
    }





    @Override
    public void updateData() {

    }
}
