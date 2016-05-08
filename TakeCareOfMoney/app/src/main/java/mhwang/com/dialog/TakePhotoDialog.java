package mhwang.com.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/5/6
 */
public class TakePhotoDialog extends Dialog{
    public static final int TAKE_PHOTO = 0;
    public static final int LOOK_PHOTO = 1;
    private Context mContext;
    private TextView tv_takePhoto;
    private TextView tv_lookPhoto;
    private OnWhichClickListener listener = null;

    public TakePhotoDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_take_photo);
        initComponent();
        initEvent();
    }

    private void initEvent() {
        tv_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.whichClick(TAKE_PHOTO);
                }
            }
        });
        tv_lookPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.whichClick(LOOK_PHOTO);
            }
        });
    }

    /**
     *  对点击监听
     */
    public void setOnWhichClickListener(OnWhichClickListener listener){
        this.listener = listener;
    }

    private void initComponent(){
        tv_takePhoto = (TextView) findViewById(R.id.tv_take_photo);
        tv_lookPhoto = (TextView) findViewById(R.id.tv_look_photo);
    }

    public interface OnWhichClickListener{
        public void whichClick(int which);
    }

}
