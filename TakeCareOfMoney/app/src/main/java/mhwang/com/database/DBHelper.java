package mhwang.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import mhwang.com.bean.Record;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.DateUtil;
import mhwang.com.util.LogUitl;
import mhwang.com.util.RecordTypeUtil;

/**
 * 项目名称：
 * 类描述：数据库及表创建类

 * 作者：王明海
 * 创建时间：2016/4/16
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TCOF.db";
    private static final int version = 1;
    private Context mContext;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
        mContext = context;
    }

    /**
     *  创建用户表
     */
    private static final String CREATE_USER = "CREATE TABLE users ("
            +"userId integer primary key autoincrement, "
            +"name text, "
            +"password text, "
            +"word text, "                 // 名言
            +"info text)";                 // 个人信息

    /**
     *  创建每笔收支的记录表
     */
    private static final String CREATE_RECORD = "CREATE TABLE records ("
            +"id integer primary key autoincrement, "   // 记录的Id
            +"money real, "                               // 记录的钱
            +"status text, "                             // 记录的状态(支出或收入)
            +"picture text, "                            // 记录的图片路径
            +"type text, "                               // 记录的消费类别
            +"typeChild text, "                          // 记录的消费子类别
            +"account text, "                            // 记录消费的帐户
            +"note text, "                               // 记录的备注
            +"year integer, "                            // 记录的年月日
            +"month integer, "
            +"day integer, "
            +"time text, "
            +"userId integer)";                          // 记录的用户id

    /**
     *  预算表
     */
    private static final String CREATE_BUDGET = "CREATE TABLE budget ("
            +"id integer primary key autoincrement, "
            +"type text, "                 // 预算类型
            +"budget real, "              // 预算金额
            +"surplus real, "
            +"userId integer)";            // 用户Id

    /**
     *  账户表
     */
    private static final String CREATE_ACCOUNT = "CREATE TABLE account ("
            +"id integer primary key autoincrement, "
            +"name text, "                // 账户名称
            +"money real, "              // 金额
            +"userId integer)";          // 用户id

    // 为账户表插入预设账户
    String[] accounts;
    String[] budgetTypes;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_BUDGET);
        db.execSQL(CREATE_ACCOUNT);
        LogUitl.showLog("DBHelper","onCreate");
        // 为预算表插入预算类型
        budgetTypes = mContext.getResources().getStringArray(R.array.budget_type);
        for (int i = 0; i < budgetTypes.length; i++){
            ContentValues values = new ContentValues();
            values.put("type",budgetTypes[i]);
            values.put("budget",0.00);
            values.put("surplus",0.00);
            values.put("userId",0);
            db.insert("budget",null,values);
        }

        // 为账户表插入预设账户
        accounts = mContext.getResources().getStringArray(R.array.accounts);
        for(int i = 0; i < accounts.length; i++){
            ContentValues values = new ContentValues();
            values.put("name",accounts[i]);
            values.put("money",0.00);
            values.put("userId",0);
            db.insert("account",null,values);
        }

        // 插入随机数据
        insertTestData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *  插入模拟数据
     */
    private void insertTestData(SQLiteDatabase db){
        LogUitl.showLog("DBHelper","insertTestData");
        DateUtil util = DateUtil.getInstance();
        int curYear = util.getYear();
        int curMonth = util.getMonth();
        int curDay = util.getDay();
        // 从2010年的数据开始插入
        int earlyYear = 2010;
        for (int i = earlyYear; i <= curYear; i++) {
            // 月份
            for (int j = 1; j <= 12; j++) {
                // 日
                for (int h = 1; h < 30; h++) {
                    int recordCount = 10;
                    for (int c = 0; c <= recordCount; c++) {
                        Record record = createRecord(i, j, h);
                        insertRecord(record, db);
                    }
                }
            }
        }

    }

    private void showLog(String str){
        Log.d("---DBHelper--->",str);
    }

    /** 生成一条记录
     * @param year
     * @param month
     * @param day
     * @return
     */
    private Record createRecord(int year,int month,int day){
//        showLog("insert record year = "+year+" month = "+month+" day = "+day);
        Record record = new Record();
//        record.setName(type);
        // 生成随机账户
        int accontIndex = (int)Math.random() * 5;
        record.setAccount(accounts[accontIndex]);
        // 生成随机金额
        double money = Math.random() * 200;
        record.setMoney(money);
        // 生成随机状态
        String status = "";
        if(money > 50 && money < 150) {
            status = "收入";
        }else{
            status = "支出";
        }
        record.setStatus(status);
        // 设置备注
        record.setNote("account " + accounts[accontIndex] + " money " + money);

        RecordTypeUtil util = new RecordTypeUtil(mContext);
        String type = "";
        String typeChild = "";
        if (status.equals("支出")){
            ArrayList<String> types = util.getOutcomeTypes();
            int typeIndex = (int)Math.random() * (types.size() - 1) ;
            type = types.get(typeIndex);
            ArrayList<String> typeChilds = util.getOutcomeTypeChilds(type);
            int typeChildIndex = (int)Math.random() * (typeChilds.size() - 1);
            typeChild = typeChilds.get(typeChildIndex);
        }else{
            ArrayList<String> types = util.getImcomeTypes();
            int typeIndex = (int)Math.random() * (types.size() - 1) ;
            type = types.get(typeIndex);
            ArrayList<String> typeChilds = util.getIncomeTypeChilds(type);
            int typeChildIndex = (int)Math.random() * (typeChilds.size() - 1);
            typeChild = typeChilds.get(typeChildIndex);
        }
        record.setType(type);
        record.setTypeChild(typeChild);
        record.setYear(year);
        record.setMonth(month);
        record.setDay(day);
        record.setTime(DateUtil.getInstance().getCurrentTime());
        return record;
    }




    /** 插入一条记录
     * @param record
     */
    public void insertRecord(Record record,SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("money", record.getMoney());
        values.put("status",record.getStatus());
        values.put("type",record.getType());
        values.put("typeChild",record.getTypeChild());
        values.put("account",record.getAccount());
        values.put("note",record.getNote());
        values.put("userId",record.getUserId());
        values.put("year", record.getYear());
        values.put("month", record.getMonth());
        values.put("day", record.getDay());
        values.put("time", record.getTime());



        db.insert("records", null, values);
    }

}
