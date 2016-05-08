package mhwang.com.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import mhwang.com.bean.Request;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.PictureUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/5/6
 */
public class ShowBigPhotoActivity extends Activity {
    private ImageView iv_showBigPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_big_photo);
        iv_showBigPhoto = (ImageView) findViewById(R.id.iv_show_big_photo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String path = getIntent().getStringExtra(Request.KEY_PHOTO_PATH);
        Bitmap bitmap = PictureUtil.getBitmap(path);
        iv_showBigPhoto.setImageBitmap(bitmap);
    }
}
