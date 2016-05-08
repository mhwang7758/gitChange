package mhwang.com.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import mhwang.com.activity.RecordMoneyActivity;
import mhwang.com.bean.Request;
import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/20
 */
public class SelectAccountDialog  extends Activity {
    private ListView lv_selectAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_account);
        initData();
        initComponent();
        initEvent();
    }

    /**
     *  添加事件处理
     */
    private void initEvent() {
        final String[] accounts = getResources().getStringArray(R.array.accounts);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_singlechoice,accounts);
        lv_selectAccount.setAdapter(adapter);

        lv_selectAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String account = accounts[position];
                Intent data = new Intent();
                data.putExtra(Request.KEY_SELECT_ACCOUNT,account);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void initData() {

    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        lv_selectAccount = (ListView) findViewById(R.id.lv_select_account);
    }


}
