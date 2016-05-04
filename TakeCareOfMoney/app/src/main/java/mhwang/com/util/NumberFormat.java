package mhwang.com.util;

import java.text.DecimalFormat;

/**
 * 项目名称：
 * 类描述：将数字转化为两位小数
 * 作者：王明海
 * 创建时间：2016/4/30
 */
public class NumberFormat {
    public static String format(double num){
        DecimalFormat df = new DecimalFormat(".##");
        return df.format(num);
    }
}
