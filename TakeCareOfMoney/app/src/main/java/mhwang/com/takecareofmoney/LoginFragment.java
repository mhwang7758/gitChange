package mhwang.com.takecareofmoney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mhwang.com.bean.User;
import mhwang.com.database.DBUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/16
 */
public class LoginFragment extends Fragment {
    private View mView;
    private Button btn_login;
    private Button btn_register;
    private EditText et_user_name;
    private EditText et_password;
    private OnItemListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login,null);
        initComponent();
        Log.d("--LoginFragment-->", "onCreateView");
        return mView;
    }


    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     *  初始化控件
     */
    private void initComponent(){
        et_user_name = (EditText) mView.findViewById(R.id.et_user_name);
        et_password = (EditText) mView.findViewById(R.id.et_user_password);
        btn_login = (Button) mView.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = checkUserExits();
                if(user == null)
                    return;
                mListener.Login(user);
            }
        });
        btn_register = (Button) mView.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.Register();
            }
        });
    }

    /** 检查用户是否在数据库中
     * @return
     */
    private User checkUserExits(){
        String userName = et_user_name.getText().toString();
        String password = et_password.getText().toString();
        if (userName.isEmpty() || password.isEmpty()){
            showToast("用户不存在或密码不正确");
            return null;
        }

        User user = DBUtil.getInstance(getActivity()).readUsers(userName,password);
        return user;

    }

    /** 监听用户点击登陆事件
     * @param listener
     */
    public void setOnLoginOrRegisterListener(OnItemListener listener){
        mListener = listener;
    }

    /**
     *  点击登陆的接口
     */
    public interface OnItemListener{
        public void Login(User user);
        public void Register();
    }
}
