package mhwang.com.takecareofmoney;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.sourceforge.pinyin4j.PinyinHelper;

import mhwang.com.abstracts.PagerFragment;
import mhwang.com.activity.SearchActivity;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/7
 */
public class MoreFragment extends PagerFragment {

    private View mView;
    private TextView tv_test;
    private String url = "http://ent.sina.cn/?vt=1&post=1";
    private Button btn_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_more,null);
        btn_search = (Button) mView.findViewById(R.id.btn_more_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        return mView;
    }

    @Override
    public void updateData() {
    }
}
