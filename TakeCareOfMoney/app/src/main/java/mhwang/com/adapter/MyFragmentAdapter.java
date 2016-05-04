package mhwang.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mhwang.com.util.LogUitl;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/9
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    public MyFragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        LogUitl.showLog("MyFragmentAdapter","fragment"+position);
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
