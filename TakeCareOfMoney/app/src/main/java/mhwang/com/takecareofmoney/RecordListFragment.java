package mhwang.com.takecareofmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mhwang.com.activity.RecordDetailActivity;
import mhwang.com.adapter.RecordAdapter;
import mhwang.com.bean.Record;
import mhwang.com.bean.Request;
import mhwang.com.database.DBUtil;
import mhwang.com.util.DateUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/5/6
 */
public class RecordListFragment extends Fragment {
    public static final String KEY_WHICH_FRAGMENT = "which";
    public static final int TODAY = 0;
    public static final int THIS_WEEK = 1;
    public static final int THIS_MONTH = 2;
    public static final int THIS_YEAR = 3;

    private View mView;
    private ListView lv_list;
    private RecordAdapter adapter;

    private ArrayList<Record> records;

    private int recordPos;
    private int recordId;

    private void showLog(String msg){
        Log.d("--RecordListFragment-->",msg);
    }

    /** 获取实例对象
     * @param which 显示哪种数据，0表示今天的数据，1表示本周，2表示本月，3表示本年
     * @return
     */
    public static RecordListFragment newInstance(int which){
        Bundle args = new Bundle();
        args.putInt(KEY_WHICH_FRAGMENT, which);
        RecordListFragment fragment = new RecordListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record_list,null);
        initComponent();
        initEvent();
        return mView;
    }

    /**
     *  事件处理
     */
    private void initEvent() {
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record = (Record)adapter.getItem(position);
                recordPos = position;
                recordId = record.getId();
                Intent intent = new Intent(getActivity(), RecordDetailActivity.class);
                intent.putExtra(RecordDetailFragment.KEY_RECORD_ID,recordId);
                startActivityForResult(intent, Request.RECORD_DETAIL);
            }
        });
    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        lv_list = (ListView) mView.findViewById(R.id.lv_record_list);
        adapter = new RecordAdapter(getActivity(),records);
    }

    /**
     *  初始化数据
     */
    private void initData(){
        int fragmentId = getActivity().getIntent().getIntExtra(KEY_WHICH_FRAGMENT,-1);
        if (fragmentId == -1){
            return;
        }
        int year = DateUtil.getInstance().getYear();
        int month = DateUtil.getInstance().getMonth();
        int day = DateUtil.getInstance().getDay();
        DBUtil dbUtil = DBUtil.getInstance(getActivity());
        switch (fragmentId){
            case TODAY:
                records = dbUtil.readRecordsByDay(year,month,day);
                break;
            case THIS_WEEK:
                String[] weekDate = DateUtil.getInstance().getDateOfWeek();
                records = dbUtil.readRecordsByWeek(weekDate[0], weekDate[1]);
                break;
            case THIS_MONTH:
                records = dbUtil.readRecordsByMonth(year,month);
                break;
            case THIS_YEAR:
                records = dbUtil.readRecordsByYear(year);
                break;
        }
        showLog("read the fragmentId "+fragmentId+" data!");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != getActivity().RESULT_OK){
            return;
        }
        int status = data.getIntExtra(RecordDetailFragment.KEY_RECORD_MODIFY_STATUS,-1);
        showLog("onActivityResult status is "+status);
        if (status == RecordDetailFragment.DELETE){
            // 删除数据集中该记录
            adapter.getData().remove(recordPos);
        }else{
            // 获取该记录修改后的详细信息
            Record modifyRecord  = DBUtil.getInstance(getActivity()).
                    readRecordById(recordId);
            Record detailRecord = adapter.getData().get(recordPos);
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
                adapter.getData().remove(recordPos);
            }
        }
        // 刷新列表
        adapter.notifyDataSetChanged();
    }
}
