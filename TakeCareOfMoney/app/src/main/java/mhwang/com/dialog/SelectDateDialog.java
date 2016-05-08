package mhwang.com.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import mhwang.com.database.DBUtil;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.DateUtil;

/**
 * 项目名称：
 * 类描述：日期选择对话框
 * 作者：王明海
 * 创建时间：2016/5/3
 */
public class SelectDateDialog extends FragmentActivity {
    public static final String KEY_SELECT_DATE = "date";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_select);
        DateUtil dateUtil = DateUtil.getInstance();
        int year = dateUtil.getYear();
        int month = dateUtil.getMonth();
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, month);
        args.putInt(CaldroidFragment.YEAR, year);
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fl_date_select, caldroidFragment);
        t.commit();

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Intent data = new Intent();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectDate = sdf.format(date);
                data.putExtra(KEY_SELECT_DATE,selectDate);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }
}
