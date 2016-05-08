package mhwang.com.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import mhwang.com.activity.RecordMoneyActivity;
import mhwang.com.bean.Request;
import mhwang.com.takecareofmoney.R;
import mhwang.com.view.TypePicker;
import mhwang.com.view.WheelView;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/17
 */
public class SelectTypeDialog extends Activity{
    private Button btn_cancel;
    private Button btn_sure;
    private TypePicker tp_recordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_type);
        boolean isIn = getIntent().getBooleanExtra(Request.KEY_WHICH_DATA_TYPE,false);
        initComponent();
        if(isIn){
            tp_recordType.changeDataType();
        }
        initEvent();
    }

    /**
     *  添加事件
     */
    private void initEvent() {
        // 取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 确定
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String selectType = tp_recordType.getSelectedType();
                String selectTypeChild = tp_recordType.getSelectedTypeChild();
                intent.putExtra(Request.KEY_SELECT_TYPE,selectType);
                intent.putExtra(Request.KEY_SELECT_TYPE_CHILD,selectTypeChild);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        btn_cancel = (Button) findViewById(R.id.btn_select_type_cancel);
        btn_sure = (Button) findViewById(R.id.btn_select_type_sure);
        tp_recordType = (TypePicker) findViewById(R.id.tp_record_type);
    }

    private boolean mIsData1 = true;

    /**
     * 重置数据
     *
     * @param view
     */
    public void resetData(View view) {
        if (mIsData1) {
//            mWheelView.resetData(getData2());
//            mWheelView.setDefault(1);
        } else {
//            mWheelView.resetData(getData1());
        }
        mIsData1 = !mIsData1;
    }


}
