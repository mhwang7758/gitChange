package mhwang.com.takecareofmoney;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.ProcessingInstruction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import mhwang.com.abstracts.PagerFragment;
import mhwang.com.activity.BudgetActivity;
import mhwang.com.activity.RecordListActivity;
import mhwang.com.activity.RecordMoneyActivity;
import mhwang.com.adapter.DateAdapter;
import mhwang.com.bean.BudgetType;
import mhwang.com.bean.DateRecord;
import mhwang.com.bean.Record;
import mhwang.com.database.DBUtil;
import mhwang.com.util.DateUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/7
 */
public class HomeFragment extends PagerFragment {
    public static final String MONEY_IN = "收入";
    public static final String MONEY_OUT = "支出";
    public static final String KEY_BUDGET = "budget";
    public static final String KEY_SURPLUS = "surplus";
    public static final String KEY_OUT = "out";
    public static final int REQUEST_CODE_PRESET_BUDGET = 10086;
    public static final int REQUEST_CODE_RECORD = 10087;
    public static final double DEFAULT_MONEY = 0.00;

    private void showLog(String msg){
        Log.e("----HomeFragment--->", msg);
    }
    private View mView;
    private Button mBtnBudget;
    private Button mBtnRecord;
    private TextView tv_month;
    private TextView tv_year;
    private TextView tv_thisMonthIn;
    private TextView tv_thisMonthOut;
    private TextView tv_budgetSurplus;
    private DateAdapter adapter;


    private double totalIn;
    private double totalOut;
    private double totalBudgetSurplus;
    private double totalBudget;
    private int month;
    private int year;
    private DateUtil dateUtil;
    private DBUtil dbUtil;

    /**
     *  是否有新记录
     */
    private boolean hasNewRecord = false;

    /**
     *  日期列表项
     */
    private List<DateRecord> dateRecords;


    /**
     *  日期列表
     */
    private ListView lv_date_item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLog("onCreate");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,null);
        showLog("onCreateView");
        initComponent();

        return mView;
    }

    private void initEvent() {
        adapter = new DateAdapter(getActivity(),dateRecords);
        lv_date_item.setAdapter(adapter);
        tv_month.setText("" + month);
        tv_year.setText("/" + year);
        lv_date_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecordListActivity.class);
                intent.putExtra(RecordListFragment.KEY_WHICH_FRAGMENT,position);
                startActivity(intent);
            }
        });
    }

    /**
     *  显示数据
     */
    private void showData(){
        totalBudgetSurplus = totalBudget - totalOut;
        DecimalFormat df = new DecimalFormat(".##");
        tv_thisMonthOut.setText(df.format(totalOut));
        tv_thisMonthIn.setText(df.format(totalIn));
        tv_budgetSurplus.setText(df.format(totalBudgetSurplus));
        if (hasNewRecord){
            showLog("update date Data");
            adapter.notifyDataSetChanged();
            hasNewRecord = false;
        }
    }

    /**
     *  更新预算余额
     */
//    private void updataBudgetSurplus(){
//        DecimalFormat df = new DecimalFormat(".##");
//        showLog("the budgetsurplus is "+totalBudgetSurplus);
//        tv_budgetSurplus.setText(df.format(totalBudgetSurplus));
//    }

    /**
     *  初始化组件
     */
    private void initComponent() {
        lv_date_item = (ListView)mView.findViewById(R.id.lv_date_item);
        mBtnBudget = (Button) mView.findViewById(R.id.btn_home_budget);
        mBtnRecord = (Button) mView.findViewById(R.id.btn_record);
        tv_month = (TextView) mView.findViewById(R.id.tv_home_month);
        tv_year = (TextView) mView.findViewById(R.id.tv_home_year);
        tv_budgetSurplus = (TextView) mView.findViewById(R.id.tv_home_budget_surplus);
        tv_thisMonthIn = (TextView) mView.findViewById(R.id.tv_this_money_in);
        tv_thisMonthOut = (TextView) mView.findViewById(R.id.tv_this_money_out);


        mBtnBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToBudgetActivity();
            }
        });
        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToRecordActivity();
            }
        });
    }

    /**
     *  跳转到预算界面
     */
    private void jumpToBudgetActivity() {
        Intent intent = new Intent(getActivity(), BudgetActivity.class);
        intent.putExtra(KEY_BUDGET,totalBudget);
        intent.putExtra(KEY_OUT, totalOut);
        intent.putExtra(KEY_SURPLUS, totalBudgetSurplus);
        startActivityForResult(intent, REQUEST_CODE_PRESET_BUDGET);
    }

    /**
     *  跳转到记帐界面
     */
    private void jumpToRecordActivity(){
        Intent intent = new Intent(getActivity(), RecordMoneyActivity.class);
        startActivityForResult(intent,REQUEST_CODE_RECORD);
    }

    /**
     *  初始化数据
     */
    private void initData() {
        dateRecords = new ArrayList<>();
        dateUtil = DateUtil.getInstance();
        dbUtil = DBUtil.getInstance(getActivity());
        DateRecord today = getTodayData();
        DateRecord thisWeek = getThisWeekData();
        DateRecord thisMonth = getThisMonthData();
        DateRecord thisYear = getThisYearData();
        dateRecords.add(today);
        dateRecords.add(thisWeek);
        dateRecords.add(thisMonth);
        dateRecords.add(thisYear);
        showLog("the dateRecord size is " + dateRecords.size());

        // 获取总收入
        setTotalData();

    }

    /**
     *  设置总额
     */
    private void setTotalData(){
        totalIn = 0.00;
        totalOut = 0.00;
        totalBudgetSurplus = 0.00;
        month = dateUtil.getMonth();
        year = dateUtil.getYear();
        ArrayList<Record> records = dbUtil.readRecordsByMonth(year,month);
        ArrayList<BudgetType> budgetTypes = dbUtil.readBudgetTypes();
        for (Record record : records){
            if (record.getStatus().equals(MONEY_IN)){
                totalIn += record.getMoney();
            }else{
                totalOut += record.getMoney();
            }
        }
        for (BudgetType budgetType : budgetTypes){
            totalBudget += budgetType.getBudget();
        }

    }

    /** 获取今天的记录花费总数
     * @return
     */
    private DateRecord getTodayData(){
        DateRecord today;
        double income = 0.00;
        double outcome = 0.00;
        int day = DateUtil.getInstance().getDay();
        int month = dateUtil.getMonth();
        int year = dateUtil.getYear();
        ArrayList<Record> records = dbUtil.readRecordsByDay(year,month,day);
        if (records.isEmpty()){
            today = new DateRecord("今天","还没有记录",0.00,0.00);
            return today;
        }
        for (Record record : records){
            if (record.getStatus().equals(Record.INCOME)){
                income += record.getMoney();
            }else{
                outcome += record.getMoney();
            }
        }
        String date = month+"月"+day+"日";
        today = new DateRecord("今天",date,income,outcome);
        return today;
    }

    /** 获取本周数据
     * @return
     */
    private DateRecord getThisWeekData(){
        // 获取本周日期
        String[] dateOfWeek = dateUtil.getDateOfWeek();
        String[] firstDates = dateOfWeek[0].split("-");
        String firstDate = firstDates[1]+"月"+firstDates[2]+"日";
        String[] lastDates = dateOfWeek[1].split("-");
        String lastDate = lastDates[1]+"月"+lastDates[2]+"日";
        DateRecord thisWeek;

        ArrayList<Record> records = dbUtil.
                readRecordsByWeek(dateOfWeek[0], dateOfWeek[1]);
        if (records.isEmpty()){
            thisWeek = new DateRecord("本周","还没有记录",0.00,0.00);
            return thisWeek;
        }
        double income = 0.00;
        double outcome = 0.00;
        for (Record record : records){
            if (record.getStatus().equals(Record.INCOME)){
                income += record.getMoney();
            }else{
                outcome += record.getMoney();
            }
        }
        thisWeek = new DateRecord("本周",firstDate+"-"+lastDate,income,outcome);
        return thisWeek;
    }

    /** 获取本月数据总数
     * @return
     */
    private DateRecord getThisMonthData(){
        int month = dateUtil.getMonth();
        int year = dateUtil.getYear();
        ArrayList<Record> records = dbUtil.readRecordsByMonth(year,month);
        DateRecord thisMonth;
        if (records.isEmpty()){
            thisMonth = new DateRecord("本月","还没有记录",0.00,0.00);
            return thisMonth;
        }

        // 获取本月时间
        int days = dateUtil.getDaysOfMonth();
        String date = month + "月1日-"+ month+"月"+days+"日";

        double income = 0.00;
        double outcome = 0.00;
        for (Record record : records){
            if (record.getStatus().equals(Record.INCOME)){
                income += record.getMoney();
            }else{
                outcome += record.getMoney();
            }
        }

        thisMonth = new DateRecord("本月",date,income,outcome);
        return thisMonth;

    }

    /** 获取今年数据总数
     * @return
     */
    private DateRecord getThisYearData(){
        int year = dateUtil.getYear();
        ArrayList<Record> records = dbUtil.readRecordsByYear(year);
        DateRecord thisYear;
        if (records.isEmpty()){
            thisYear = new DateRecord("本年","还没有记录",0.00,0.00);
            return thisYear;
        }

        double income = 0.00;
        double outcome = 0.00;
        for (Record record : records){
            if (record.getStatus().equals(Record.INCOME)){
                income += record.getMoney();
            }else{
                outcome += record.getMoney();
            }
        }
        thisYear = new DateRecord("本年","01月1日-12月31日",income,outcome);
        return thisYear;
    }

    /** 更新今天，本周，本月，本年的数据
     * @param recordStatus
     * @param money
     */
    private void updateDateRecords(String recordStatus, double money){
        if (recordStatus.equals(MONEY_IN)){
            totalIn += money;
            for (DateRecord record : dateRecords){
                record.setIncome(record.getIncome()+money);
            }
        }else {
            totalOut += money;
            for (DateRecord record : dateRecords){
                record.setOutput(record.getOutput() + money);
            }
        }
        hasNewRecord = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        showLog("onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        initEvent();
        showData();
        showLog("onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showLog("onDestroy");
    }

    @Override
    public void updateData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK){
            return;
        }
        switch (requestCode){
            case REQUEST_CODE_PRESET_BUDGET:
                totalBudget = data.getDoubleExtra(BudgetActivity.KEY_NEW_TOTAL_BUDGET,DEFAULT_MONEY);
                break;
            case REQUEST_CODE_RECORD:
                showLog("record money");
                double recordMoney = data.getDoubleExtra(RecordMoneyActivity.KEY_RECORD_MONEY
                ,DEFAULT_MONEY);
                String recordStatus = data.getStringExtra(RecordMoneyActivity.KEY_RECORD_STATUS);
                updateDateRecords(recordStatus,recordMoney);
                break;
        }


        showLog("onActivityResult,the totalBudgetSurplus is "+totalBudgetSurplus);
    }
}
