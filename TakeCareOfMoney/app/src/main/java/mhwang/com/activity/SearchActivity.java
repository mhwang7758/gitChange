package mhwang.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import mhwang.com.adapter.RecordAdapter;
import mhwang.com.adapter.TextAdapter;
import mhwang.com.bean.Record;
import mhwang.com.bean.Request;
import mhwang.com.database.DBUtil;
import mhwang.com.dialog.SelectDateDialog;
import mhwang.com.takecareofmoney.R;
import mhwang.com.takecareofmoney.RecordDetailFragment;
import mhwang.com.util.ChineseToPinYinUtil;
import mhwang.com.util.DateUtil;
import mhwang.com.util.NumberFormat;
import mhwang.com.util.RecordTypeToDBWordUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/5/3
 */
public class SearchActivity extends Activity {
    public static final int REQUEST_SELECT_DATE = 10010;
    private Button btn_search;
    private EditText et_search;
    private ListView lv_showSearch;
    private ListView lv_showSearchResult;
    private Spinner sp_year;
    private Spinner sp_month;
    private Spinner sp_day;

    private ArrayList<String> years;
    private ArrayList<String> months;
    private ArrayList<String> days;
    private String sYear = "无";
    private String sMonth = "无";
    private String sDay = "无";
    private String searchKey = "";

    private RecordAdapter adapter;
    private ArrayList<Record> resultRecords;
    private TextAdapter hintAdapter;


    /**
     *  搜索的提示词集
     */
    private ArrayList<String> searchHints;

    /**
     *  打开的详细记录的id
     */
    private int detailRecordId;
    /**
     *  打开的详细记录在数据集中的位置
     */
    private int detailRecordPos;


    private void showLog(String msg){
        Log.e("--SearchActivity--->", msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        initData();
        initComponent();
        initEvent();
    }

    /**
     *  添加事件
     */
    private void initEvent() {
        // 设置提示适配器
        ArrayList<String> emptys = new ArrayList<>();
        hintAdapter = new TextAdapter(this,emptys);
        lv_showSearch.setAdapter(hintAdapter);
        // 设置结果适配器
        adapter = new RecordAdapter(this,resultRecords);
        lv_showSearchResult.setAdapter(adapter);
        lv_showSearchResult.setEmptyView(findViewById(R.id.tv_item_record_emptyView));
        lv_showSearchResult.setVisibility(View.GONE);
        // 年份选择数据
        years = new ArrayList<>();
        int curYear = DateUtil.getInstance().getYear();
        years.add("无");
        for(int i = 2010; i <= curYear; i++){
            years.add(Integer.toString(i));
        }
        // 月份选择数据
        months = new ArrayList<>();
        months.add("无");
        for(int i = 1; i <= 12; i++){
            months.add(Integer.toString(i));
        }
        // 日期选择数据
        days = new ArrayList<>();
        days.add("无");
        for(int i = 1; i < 31; i++){
            days.add(Integer.toString(i));
        }

        TextAdapter yearAdapter = new TextAdapter(this,years);
        TextAdapter monthAdapter = new TextAdapter(this,months);
        TextAdapter dayAdapter = new TextAdapter(this, days);

        sp_year.setAdapter(yearAdapter);
        sp_month.setAdapter(monthAdapter);
        sp_day.setAdapter(dayAdapter);
        sp_month.setEnabled(false);
        sp_day.setEnabled(false);

        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sYear = years.get(position);
                if (sYear.equals("无")) {
                    sp_month.setSelection(0);
                    sp_month.setEnabled(false);
                } else {
                    sp_month.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sMonth = months.get(position);
                if (sMonth.equals("无")){
                    sp_day.setSelection(0);
                    sp_day.setEnabled(false);
                }else{
                    sp_day.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDay = days.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_showSearchResult.getVisibility() == View.GONE) {
                    lv_showSearchResult.setVisibility(View.VISIBLE);
                    lv_showSearch.setVisibility(View.GONE);
                }
                ArrayList<Record> records = searchResults();
                adapter.resetData(records);
            }
        });

        // 添加输入框文本监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(lv_showSearch.getVisibility() != View.VISIBLE){
                    lv_showSearchResult.setVisibility(View.GONE);
                    lv_showSearch.setVisibility(View.VISIBLE);
                }
                ArrayList<String> results = searchHintResults(s.toString());
                hintAdapter.resetData(results);
            }
        });

        // 关键词选择监听
        lv_showSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectKey = hintAdapter.getData().get(position);
                et_search.setText(selectKey);
                et_search.setSelection(selectKey.length());
                hintAdapter.clear();
            }
        });

        lv_showSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record = (Record) adapter.getItem(position);
                detailRecordId = record.getId();
                detailRecordPos = position;
                Intent intent = new Intent(getApplicationContext(),RecordDetailActivity.class);
                intent.putExtra(RecordDetailFragment.KEY_RECORD_ID,detailRecordId);
                startActivityForResult(intent, Request.RECORD_DETAIL);
            }
        });

    }

    /** 查找结果
     * @return
     */
    private ArrayList<Record> searchResults(){
        String key = et_search.getText().toString();
        DBUtil dbUtil = DBUtil.getInstance(this);
        ArrayList<Record> records;
        String date = sYear + "-" + sMonth + "-" + sDay;
        // 如果没有选择关键词，则按日期搜索
        if (key.isEmpty()) {
            records = dbUtil.readRecordsByDate(date);
        }else{
            if (sYear.equals("无")){
                records = dbUtil.readRecordsByKey(key);
            }else {
                records = dbUtil.readRecordsByDateAndKey(date,key);
            }
        }
        return  records;
    }

    /** 查找提示字符串结果
     * @param key
     * @return
     */
    private ArrayList<String> searchHintResults(String key){
        ArrayList<String> results = new ArrayList<>();
        if (key.isEmpty()){
            return results;
        }
        String keyPinYin = ChineseToPinYinUtil.getPingYin(key);
        showLog("search key pinyin is "+keyPinYin);
        for(String str : searchHints){
            String pingyin = ChineseToPinYinUtil.getPingYin(str);
            if (-1 != pingyin.indexOf(keyPinYin)){
                showLog("get the search result "+str);
                results.add(str);
            }
        }
        return results;
    }

    /**
     *  初始化数据
     */
    private void initData(){
        resultRecords = new ArrayList<>();

        resultRecords.add(new Record());
        searchHints = new ArrayList<>();
        RecordTypeToDBWordUtil rttdbwu = RecordTypeToDBWordUtil.getInstance(this);
        searchHints.add("收入");
        searchHints.add("支出");
        // 账户
        ArrayList<String> accounts = rttdbwu.getAccounts();
        for(String str : accounts){
            searchHints.add(str);
        }
        // 类型选择
        ArrayList<String> types = rttdbwu.getTypes();
        for(String str : types){
            searchHints.add(str);
        }
        // 类型子项
        ArrayList<String> typeChilds = rttdbwu.getTypeChilds();
        for(String str : typeChilds){
            searchHints.add(str);
        }


        showLog("line 260 init Data searchHints size is "+searchHints.size());
    }

    /**
     *  初始化控件
     */
    private void initComponent(){
        lv_showSearch = (ListView) findViewById(R.id.lv_search_showsearch);
        lv_showSearchResult = (ListView) findViewById(R.id.lv_search_result);
        btn_search = (Button) findViewById(R.id.btn_search_search);
        sp_year = (Spinner) findViewById(R.id.sp_year);
        sp_month = (Spinner) findViewById(R.id.sp_month);
        sp_day = (Spinner) findViewById(R.id.sp_day);
        et_search = (EditText) findViewById(R.id.et_search_key);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        int status = data.getIntExtra(RecordDetailFragment.KEY_RECORD_MODIFY_STATUS,-1);
        showLog("onActivityResult status is "+status);
        if (status == RecordDetailFragment.DELETE){
            // 删除数据集中该记录
            adapter.getData().remove(detailRecordPos);
        }else{
            // 获取该记录修改后的详细信息
            Record modifyRecord  = DBUtil.getInstance(getApplicationContext()).
                    readRecordById(detailRecordId);
            Record detailRecord = adapter.getData().get(detailRecordPos);
            // 查看时间是否相同，如果不同，则直接将该记录从数据集中删除,否则修改该数据
            String modifyTime = ""+modifyRecord.getYear()+modifyRecord.getMonth()
                    +modifyRecord.getDay()+modifyRecord.getTime();
            String detailTime = ""+detailRecord.getYear()+detailRecord.getMonth()
                    +detailRecord.getDay()+detailRecord.getTime();
            if (modifyTime.equals(detailTime)){
                detailRecord.setMoney(modifyRecord.getMoney());
                detailRecord.setStatus(modifyRecord.getStatus());
                detailRecord.setAccount(modifyRecord.getAccount());
                detailRecord.setNote(modifyRecord.getNote());
                detailRecord.setType(modifyRecord.getType());
                detailRecord.setTypeChild(modifyRecord.getTypeChild());
            }else {
                adapter.getData().remove(detailRecordPos);
            }
        }
        // 刷新列表
        adapter.notifyDataSetChanged();
    }

}
