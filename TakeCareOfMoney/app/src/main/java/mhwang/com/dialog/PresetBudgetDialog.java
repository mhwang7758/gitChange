package mhwang.com.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import mhwang.com.activity.BudgetActivity;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.LogUitl;

/**
 * 项目名称：
 * 类描述：设置预算金额对话框
 * 作者：王明海
 * 创建时间：2016/4/26
 */
public class PresetBudgetDialog extends Activity{
    private EditText et_input;
    private Button btn_cancel;
    private Button btn_sure;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_preset_budget);
        LogUitl.showLog("PresetBudgetDialog","onCreate");
        initComponent();
        initEvent();
    }

    /**
     *  添加监听
     */
    private void initEvent() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = et_input.getText().toString();
                Intent data = new Intent();
                data.putExtra(BudgetActivity.KEY_PRESET_MONEY,money);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

    /**
     *  初始化组件
     */
    private void initComponent(){
        et_input = (EditText) findViewById(R.id.et_preset_budget);
        btn_cancel = (Button) findViewById(R.id.btn_budget_cancel);
        btn_sure = (Button) findViewById(R.id.btn_budget_sure);
    }
}
