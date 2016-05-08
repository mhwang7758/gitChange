package mhwang.com.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import mhwang.com.bean.Record;
import mhwang.com.bean.Request;
import mhwang.com.database.DBUtil;
import mhwang.com.dialog.SelectAccountDialog;
import mhwang.com.dialog.SelectTypeDialog;
import mhwang.com.dialog.ShowBigPhotoActivity;
import mhwang.com.dialog.TakePhotoDialog;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.DateUtil;
import mhwang.com.util.PictureUtil;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/12
 */
public class RecordMoneyActivity extends Activity {
    public static final String KEY_RECORD_STATUS = "moneystatus";
    public static final String KEY_RECORD_MONEY = "money";
    public static final String DEFALUT_OUTCOME_TYPE = "食品酒水->早午晚餐";
    public static final String DEFALUT_INCOME_TYPE = "职业收入->工资收入";
    public static final String PHOTO_FILE_NAME = "TCOYMPhotos";

    public static final int INCOME = 1;
    public static final int OUTCOME = 0;

    private LinearLayout ll_selectType;
    private RelativeLayout rl_selectAccount;
    private Button btn_recordStatus;
    private TextView tv_recordType;
    private TextView tv_accountType;
    private EditText et_recordMoney;
    private EditText et_note;
    private ImageButton ib_back;
    private ImageButton ib_finish;
    private RelativeLayout rl_takePhoto;
    private ImageView iv_showPhoto;

    private String selectType = "食品酒水";
    private String selectTypeChild = "早午晚餐";
    private String moneyStatus = "支出";
    private double money = 0.00;
    private String photoPath = "";
    private String PHOTO_FILE_PATH;

    TakePhotoDialog photoDialog;


    private void showLog(String msg){
        Log.d("---RecordMoneyActivity--->", msg);
    }

    /**
     *  记录的状态
     */
    private boolean isIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record_money);
        initData();
        initComponent();
        initEvent();
    }

    /**
     *  初始化数据
     */
    private void initData(){
        PHOTO_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator+PHOTO_FILE_NAME+File.separator;
        File file = new File(PHOTO_FILE_PATH);
        if (!file.exists()){
            file.mkdir();
        }
    }

    /**
     *  添加事件处理
     */
    private void initEvent() {
        // 选择账户事件
        rl_selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectAccountDialog();
            }
        });
        // 记录按钮状态事件
        btn_recordStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRecordStatus();
            }
        });

        // 输入框焦点监听
        et_recordMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_recordMoney.setSelection(et_recordMoney.getText().length());
                }
            }
        });

        // 开启相机
        rl_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoSelectDialog();
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                photoPath = PHOTO_FILE_PATH + DateUtil.getInstance().getCurrentTime() + ".png";
//                showLog("we will take the photo to " + photoPath);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
//                startActivityForResult(intent, Request.RECORD_CAMERA);
            }
        });

        // 关闭页面
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 保存数据到数据库
        ib_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDb();
                Intent data = new Intent();
                data.putExtra(KEY_RECORD_STATUS,moneyStatus);
                data.putExtra(KEY_RECORD_MONEY,money);
                setResult(RESULT_OK,data);
                finish();
            }
        });

    }

    /**
     *  显示对图片进行操作的对话框
     */
    private void showPhotoSelectDialog(){
        photoDialog = new TakePhotoDialog(this);
        photoDialog.setOnWhichClickListener(new TakePhotoDialog.OnWhichClickListener() {
            @Override
            public void whichClick(int which) {
                if (which == TakePhotoDialog.TAKE_PHOTO) {
                    openCamera();
                } else {
                    showPhoto();
                }
            }
        });
        photoDialog.show();
    }

    /**
     *  打开相机
     */
    private void openCamera() {
        photoDialog.dismiss();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        photoPath = PHOTO_FILE_PATH + DateUtil.getInstance().getCurrentTime() + ".png";
        showLog("we will take the photo to " + photoPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
        startActivityForResult(intent, Request.RECORD_CAMERA);
    }

    /**
     *  显示图片
     */
    private void showPhoto(){
        if (photoPath.isEmpty()){
            photoDialog.dismiss();
            return;
        }
        Intent intent = new Intent(this,ShowBigPhotoActivity.class);
        intent.putExtra(Request.KEY_PHOTO_PATH,photoPath);
        startActivity(intent);
        photoDialog.dismiss();
    }

    /**
     *  保存数据进数据库
     */
    private void saveDataToDb() {
        moneyStatus = btn_recordStatus.getText().toString();
        money = Double.parseDouble(et_recordMoney.getText().toString());
        String account = tv_accountType.getText().toString();
        String note = et_note.getText().toString();

        // 判断该记录是否有效
        if (money == 0){
            showLog("the record is invalidate");
            return;
        }

        Record record = new Record();
//        record.setName(type);
        record.setAccount(account);
        record.setMoney(money);
        record.setStatus(moneyStatus);
        record.setNote(note);
        record.setType(selectType);
        record.setTypeChild(selectTypeChild);
        DateUtil util = DateUtil.getInstance();
        record.setYear(util.getYear());
        record.setMonth(util.getMonth());
        record.setDay(util.getDay());
        record.setTime(util.getCurrentTime());
        record.setPhotoPath(photoPath);

        DBUtil.getInstance(this).insertRecord(record);
    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        ll_selectType = (LinearLayout) findViewById(R.id.ll_select_type);
        rl_selectAccount = (RelativeLayout) findViewById(R.id.rl_select_account);
        tv_recordType = (TextView) findViewById(R.id.tv_record_type);
        tv_accountType = (TextView) findViewById(R.id.tv_account_type);
        btn_recordStatus = (Button) findViewById(R.id.btn_in_out);
        et_recordMoney = (EditText) findViewById(R.id.et_record_money);
        ib_back = (ImageButton) findViewById(R.id.ib_record_money_back);
        ib_finish = (ImageButton) findViewById(R.id.ib_record_money_finish);
        et_note = (EditText) findViewById(R.id.et_note);
        rl_takePhoto = (RelativeLayout) findViewById(R.id.rl_take_photo);
        iv_showPhoto = (ImageView) findViewById(R.id.iv_show_photo);

        ll_selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectTypeDialog();
            }
        });
    }

    /**
     *  改变记录状态
     */
    private void changeRecordStatus(){
        btn_recordStatus.setText(isIn ? R.string.output:R.string.income);
        tv_recordType.setText(isIn ? DEFALUT_OUTCOME_TYPE:DEFALUT_INCOME_TYPE);
        isIn = isIn ? false : true;
    }

    /**
     *  弹出选择对话框
     */
    private void showSelectTypeDialog() {
        Intent intent = new Intent(this, SelectTypeDialog.class);
        intent.putExtra(Request.KEY_WHICH_DATA_TYPE,isIn);
        startActivityForResult(intent, Request.SELECT_TYPE);
    }

    /**
     *  弹出账户选择对话框
     */
    private void showSelectAccountDialog(){
        Intent intent = new Intent(this, SelectAccountDialog.class);
        startActivityForResult(intent, Request.SELECT_ACCOUNT);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case Request.SELECT_TYPE:
                selectType = data.getStringExtra(Request.KEY_SELECT_TYPE);
                selectTypeChild = data.getStringExtra(Request.KEY_SELECT_TYPE_CHILD);
                tv_recordType.setText(selectType+"->"+selectTypeChild);
                break;
            case Request.SELECT_ACCOUNT:
                String account = data.getStringExtra(Request.KEY_SELECT_ACCOUNT);
                tv_accountType.setText(account);
                break;
            case Request.RECORD_CAMERA:
                showLog("take the photo path is "+photoPath);
                Bitmap bitmap = PictureUtil.getBitmap(photoPath);
                Bitmap newBitmap = PictureUtil.resizeBitmap(bitmap, rl_takePhoto.getWidth(),
                        rl_takePhoto.getHeight());
                iv_showPhoto.setImageBitmap(newBitmap);
                break;
        }

    }
}
