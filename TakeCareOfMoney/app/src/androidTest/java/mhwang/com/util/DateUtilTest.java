package mhwang.com.util;

import android.util.Log;

import junit.framework.TestCase;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/21
 */
public class DateUtilTest extends TestCase {
    private DateUtil util;
    private void showLog(String msg){
        Log.e("---DateUtilTest--->",msg);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        util = DateUtil.getInstance();
    }

    public void testGetYear() throws Exception {
        showLog(Integer.toString(util.getYear()));
        assertEquals(2016,util.getYear());
    }

    public void testGetMonth() throws Exception {
        showLog(Integer.toString(util.getMonth()));
        assertEquals(04,util.getMonth());
    }

    public void testGetDay() throws Exception {
        showLog(Integer.toString(util.getDay()));
        assertEquals(21,util.getDay());
    }
}