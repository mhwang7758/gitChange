package mhwang.com.takecareofmoney;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mhwang.com.activity.RecordMoneyActivity;
import mhwang.com.bean.Record;
import mhwang.com.bean.Request;
import mhwang.com.database.DBUtil;
import mhwang.com.dialog.SelectAccountDialog;
import mhwang.com.dialog.SelectDateDialog;
import mhwang.com.dialog.SelectTypeDialog;
import mhwang.com.util.NumberFormat;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/5/5
 */
public class RecordDetailFragment extends Fragment {
    public static final String KEY_RECORD_ID = "recordId";
    public static final String KEY_RECORD_MODIFY_STATUS = "modify";
    public static final int EDIT = 0;
    public static final int DELETE = 1;

    private View mView;
    private ImageButton ib_back;
    private ImageButton ib_finish;
    private Button btn_edit;
    private Button btn_delete;
    private Button btn_status;
    private EditText et_money;
    private EditText et_notes;
    private ImageView iv_photo;
    private TextView tv_type;
    private TextView tv_account;
    private TextView tv_date;
    private LinearLayout ll_date;



    private Record record;
    /**
     *  是否处于编辑状态
     */
    private boolean isEdit = false;
    /**
     *  是否是收入状态
     */
    private boolean isIn = false;

    /**
     *  选择的日期
     */
    private String date = null;
    private String selectType;
    private String selectTypeChild;
    private String selectAccount;

    /**
     *  修改记录的状态-1表示未修改，0表示编辑，1表示删除
     */
    private int modifyStatus = -1;


    private void showLog(String msg){
        Log.d("---RecordDetailFragment-->", msg);
    }

    /** 获取实例对象
     * @param recordId
     * @return
     */
    public static RecordDetailFragment newInstance(int recordId){
        Bundle args = new Bundle();
        args.putInt(KEY_RECORD_ID, recordId);
        RecordDetailFragment fragment = new RecordDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recordId = getActivity().getIntent().getIntExtra(KEY_RECORD_ID,-1);
        record = DBUtil.getInstance(getActivity()).readRecordById(recordId);
        selectType = record.getType();
        selectTypeChild = record.getTypeChild();
        selectAccount = record.getAccount();
        String status = record.getStatus();
        if (status.equals("收入")){
            isIn = true;
        }
        showLog("get the record id " + recordId);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record_detail, null);
        initComponent();
        initEvent();
        showData();
        return mView;
    }



    /**
     *  初始化控件
     */
    private void initComponent(){
        ib_back = (ImageButton) mView.findViewById(R.id.ib_record_detail_back);
        ib_finish = (ImageButton) mView.findViewById(R.id.ib_record_detail_finish);
        btn_edit = (Button) mView.findViewById(R.id.btn_detail_edit);
        btn_delete = (Button) mView.findViewById(R.id.btn_detail_delete);
        btn_status = (Button) mView.findViewById(R.id.btn_detail_in_out);
        et_money = (EditText) mView.findViewById(R.id.et_detail_money);
        et_notes = (EditText) mView.findViewById(R.id.et_detail_note);
        iv_photo = (ImageView) mView.findViewById(R.id.iv_detail_photo);
        tv_type = (TextView) mView.findViewById(R.id.tv_detail_type);
        tv_account = (TextView) mView.findViewById(R.id.tv_detail_account);
        tv_date = (TextView) mView.findViewById(R.id.tv_detail_date);
        ll_date = (LinearLayout) mView.findViewById(R.id.ll_detail_date);
    }

    /**
     *  显示数据
     */
    private void showData(){
        if (record == null){
            return;
        }
        et_money.setText(NumberFormat.format(record.getMoney()));
        et_notes.setText(record.getNote());
        btn_status.setText(record.getStatus());
        tv_account.setText(record.getAccount());
        String type = record.getType()+"->"+record.getTypeChild();
        tv_type.setText(type);
        String sDate = record.getYear()+"年"+record.getMonth()+"月"+record.getDay()
                +"日   "+record.getTime();
        tv_date.setText(sDate);
        setAllUIStatus(false, false, false, false, false);
    }

    /** 设置组件是否可编辑或点击
     * @param status
     * @param money
     * @param account
     * @param type
     * @param note
     */
    private void setAllUIStatus(boolean status,boolean money,
                                boolean account,boolean type,boolean note){
        btn_status.setClickable(status);
        et_money.setEnabled(money);
        tv_account.setClickable(account);
        tv_type.setClickable(type);
        et_notes.setEnabled(note);
    }

    /**
     *  弹出选择对话框
     */
    private void showSelectTypeDialog() {
        Intent intent = new Intent(getActivity(), SelectTypeDialog.class);
        intent.putExtra(Request.KEY_WHICH_DATA_TYPE, isIn);
        startActivityForResult(intent, Request.SELECT_TYPE);
    }

    /**
     *  弹出账户选择对话框
     */
    private void showSelectAccountDialog(){
        Intent intent = new Intent(getActivity(), SelectAccountDialog.class);
        startActivityForResult(intent, Request.SELECT_ACCOUNT);

    }

    /**
     *  删除时弹出的提示对话框
     */
    private void showWarmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否删除该条记录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBUtil.getInstance(getActivity()).deleteRecord(record.getId());
                Intent data = new Intent();
                modifyStatus = DELETE;
                data.putExtra(KEY_RECORD_MODIFY_STATUS, modifyStatus);
                getActivity().setResult(getActivity().RESULT_OK, data);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     *  打开日期选择框
     */
    private void showSelectDateDialog(){
        Intent intent = new Intent(getActivity(), SelectDateDialog.class);
        startActivityForResult(intent, Request.SELECT_DATE);
    }

    /**
     *  更新数据库数据
     */
    private void updateDBData(){
        record.setStatus(isIn ? "收入" : "支出");
        record.setMoney(Double.parseDouble(et_money.getText().toString()));
        record.setType(selectType);
        record.setTypeChild(selectTypeChild);
        record.setAccount(selectAccount);
        record.setNote(et_notes.getText().toString());
        if (date != null){
            String[] dates = date.split("-");
            record.setYear(Integer.parseInt(dates[0]));
            record.setMonth(Integer.parseInt(dates[1]));
            record.setDay(Integer.parseInt(dates[2]));
        }
        DBUtil.getInstance(getActivity()).updateRecord(record);
        showLog("update a record id is " + record.getId());
        Intent data = new Intent();
        modifyStatus = EDIT;
        data.putExtra(KEY_RECORD_MODIFY_STATUS,modifyStatus);
        getActivity().setResult(getActivity().RESULT_OK,data);
    }

    /**
     *  初始化事件
     */
    private void initEvent(){
        // 编辑
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUIStatus(true,true,true,true,true);
                isEdit = true;
            }
        });
        // 删除
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarmDialog();
            }
        });
        // 返回
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ib_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit){
                    updateDBData();
                }
                getActivity().finish();
            }
        });
        // 消费类型
        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    showSelectTypeDialog();
                }
            }
        });

        // 收入类型
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIn = (isIn == false) ? true: false;
                btn_status.setText((isIn == false)?"支出":"收入");
            }
        });

        //账户
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectAccountDialog();
            }
        });

        // 日期选择
        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    showSelectDateDialog();
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != getActivity().RESULT_OK){
            return;
        }

        switch (requestCode){
            case Request.SELECT_TYPE:
                selectType = data.getStringExtra(Request.KEY_SELECT_TYPE);
                selectTypeChild = data.getStringExtra(Request.KEY_SELECT_TYPE_CHILD);
                tv_type.setText(selectType+"->"+selectTypeChild);
                break;
            case Request.SELECT_ACCOUNT:
                selectAccount = data.getStringExtra(Request.KEY_SELECT_ACCOUNT);
                tv_account.setText(selectAccount);
                break;
            case  Request.SELECT_DATE:
                date = data.getStringExtra(SelectDateDialog.KEY_SELECT_DATE);
                String[] dates = date.split("-");
                String sdate = dates[0]+"年"+dates[1]+"月"+dates[2]+"日  "+record.getTime();
                tv_date.setText(sdate);
                break;
        }
    }
}
