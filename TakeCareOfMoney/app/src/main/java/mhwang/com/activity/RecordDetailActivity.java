package mhwang.com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import mhwang.com.takecareofmoney.R;
import mhwang.com.takecareofmoney.RecordDetailFragment;

/**
 * 项目名称：
 * 类描述：记录详细界面
 * 作者：王明海
 * 创建时间：2016/5/5
 */
public class RecordDetailActivity extends FragmentActivity{
    /** 创建fragment
     * @return
     */
    private Fragment createFragment(){
        int recordId = getIntent().getIntExtra(RecordDetailFragment.KEY_RECORD_ID,-1);
        return RecordDetailFragment.newInstance(recordId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_fragment,createFragment());
        ft.commit();
    }
}
