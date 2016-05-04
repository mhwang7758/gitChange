package mhwang.com.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：记录类型和数据库字段的对应关系
 * 作者：王明海
 * 创建时间：2016/5/4
 */
public class RecordTypeToDBWordUtil {
    public static final String DB_RECORD_TYPE = "type";
    public static final String DB_RECORD_TYPE_CHILD = "typeChild";
    public static final String DB_RECORD_ACCOUNT = "account";
    public static final String DB_RECORD_STATUS = "status";
    private ArrayList<String> accounts;
    private Map<String,ArrayList<String>> map;
    private ArrayList<String> types;
    private ArrayList<String> typeChilds;

    private static RecordTypeToDBWordUtil rttdbw = null;
    private RecordTypeToDBWordUtil(Context context){
        RecordTypeUtil rtu = new RecordTypeUtil(context);
        // 建立对应map
        map = new HashMap<>();
        // 类型项
        types = new ArrayList<>();
        // 记录类型子项
        typeChilds = new ArrayList<>();
        // 账户类型项
        accounts = new ArrayList<>();
        ArrayList<String> outcomeTypes = rtu.getOutcomeTypes();
        ArrayList<String> incomeTypes = rtu.getImcomeTypes();
        // 获取支出项和其子项，并加入到类型项和类型子项中
        for (String str : outcomeTypes){
            types.add(str);
            ArrayList<String> childs = rtu.getOutcomeTypeChilds(str);
            for(String child : childs){
                typeChilds.add(child);
            }
        }

        for (String str : incomeTypes){
            types.add(str);
            ArrayList<String> childs = rtu.getIncomeTypeChilds(str);
            for(String child : childs){
                typeChilds.add(child);
            }
        }

        // 获取账户
        String[] sAccounts = context.getResources().getStringArray(R.array.accounts);
        for(String str : sAccounts){
            accounts.add(str);
        }

        // 收支状态
        ArrayList<String> status = new ArrayList<>();
        status.add("收入");
        status.add("支出");

        // 建立对应关系
        map.put(DB_RECORD_TYPE,types);
        map.put(DB_RECORD_TYPE_CHILD,typeChilds);
        map.put(DB_RECORD_ACCOUNT,accounts);
        map.put(DB_RECORD_STATUS,status);


    }

    /** 获取实例对象
     * @param context
     * @return
     */
    public static RecordTypeToDBWordUtil getInstance(Context context){
        if (rttdbw == null){
            rttdbw = new RecordTypeToDBWordUtil(context);
        }
        return rttdbw;
    }

    /** 获取值在数据库中字段
     * @param value
     * @return
     */
    public String getDBWord(String value){
        Iterator i = map.entrySet().iterator();
        String word = "";
        while (i.hasNext()){
            Map.Entry e = (Map.Entry)i.next();
            ArrayList<String> lists = (ArrayList<String>) e.getValue();
            if (lists.contains(value)){
                word = (String)e.getKey();
                break;
            }
        }
        return word;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public ArrayList<String> getTypeChilds() {
        return typeChilds;
    }

    public ArrayList<String> getAccounts() {
        return accounts;
    }
}
