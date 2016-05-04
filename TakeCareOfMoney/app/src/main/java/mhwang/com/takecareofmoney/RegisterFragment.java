package mhwang.com.takecareofmoney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import mhwang.com.bean.User;
import mhwang.com.database.DBUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/16
 */
public class RegisterFragment extends Fragment {
    private View mView;
    private ImageButton ib_back;
    private ImageButton ib_finish;
    private OnItemClickListener mListener;

    private EditText et_user_name;
    private EditText et_user_password;
    private EditText et_user_password_again;
    private EditText et_word;

    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register,null);
        initComponent();
        return mView;
    }

    /**
     *  初始化控件
     */
    private void initComponent(){
        ib_back = (ImageButton) mView.findViewById(R.id.ib_regiter_back);
        ib_finish = (ImageButton) mView.findViewById(R.id.ib_register_finish);
        et_user_name = (EditText) mView.findViewById(R.id.et_register_user_name);
        et_user_password = (EditText) mView.findViewById(R.id.et_register_password);
        et_user_password_again = (EditText) mView.findViewById(R.id.et_register_password_again);
        et_word = (EditText) mView.findViewById(R.id.et_register_word);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick();
            }
        });

        ib_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!putRegisterDataToDB())
                    return;
                mListener.onItemClick();
            }
        });
    }

    /**
     *  将注册信息保存到数据库
     */
    private boolean putRegisterDataToDB() {
        String userName = et_user_name.getText().toString();
        String password = et_user_password.getText().toString();
        String passwordAgain = et_user_password_again.getText().toString();
        String word = et_word.getText().toString();

        // 判断用户填入的数据是否有效
        if (userName.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()){
            showToast("请填写完整");
            return false;
        }

        // 判断用户输入的密码是否一致
        if (!password.equals(passwordAgain)){
            showToast("密码不一致");
            return false;
        }

        // 将注册数据提交数据库
        User user = new User(userName,word,"44444",password);
        DBUtil.getInstance(getActivity()).insertUser(user);
        return true;
    }

    /** 监听按钮点击
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     *  按钮点击接口
     */
    public interface OnItemClickListener{
        public void onItemClick();
    }
}
