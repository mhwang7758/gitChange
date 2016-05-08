package mhwang.com.bean;

/**
 * 项目名称：
 * 类描述：请求码类
 * 作者：王明海
 * 创建时间：2016/5/5
 */
public class Request {
    public static final int SELECT_TYPE = 1000;
    public static final int SELECT_ACCOUNT = 1001;
    public static final int SELECT_DATE = 1002;
    public static final int RECORD_DETAIL = 1003;
    public static final int RECORD_CAMERA = 1004;

    public static final String KEY_SELECT_TYPE = "selectType";
    public static final String KEY_SELECT_TYPE_CHILD = "selectTypeChild";
    public static final String KEY_SELECT_ACCOUNT = "selectAccount";
    public static final String KEY_WHICH_DATA_TYPE = "which";
    public static final String KEY_PHOTO_PATH = "path";
}
