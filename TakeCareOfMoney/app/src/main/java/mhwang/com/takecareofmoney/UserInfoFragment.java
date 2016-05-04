package mhwang.com.takecareofmoney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mhwang.com.bean.User;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/16
 */
public class UserInfoFragment extends Fragment {
    private View mView;
    /**
     *  登陆的用户
     */
    private User user = null;

    private TextView tv_name;
    private TextView tv_word;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_userinfo,null);
        initComponent();
        return mView;
    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        tv_name = (TextView) mView.findViewById(R.id.tv_userinfo_name);
        tv_word = (TextView) mView.findViewById(R.id.tv_userinfo_word);
        tv_name.setText(user.getName());
        tv_word.setText(user.getWord());
    }

    /** 设置显示信息的用户
     * @param user
     */
    public void setUser(User user){
        this.user = user;
    }
}
