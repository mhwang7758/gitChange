package mhwang.com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import mhwang.com.takecareofmoney.R;
import mhwang.com.takecareofmoney.RecordDetailFragment;
import mhwang.com.takecareofmoney.RecordListFragment;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/5/6
 */
public class RecordListActivity extends FragmentActivity {
    /** 创建fragment
     * @return
     */
    private Fragment createFragment(){
        int fragmentId = getIntent().getIntExtra(RecordListFragment.KEY_WHICH_FRAGMENT,-1);
        return RecordListFragment.newInstance(fragmentId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_fragment,createFragment());
        ft.commit();
    }
}
