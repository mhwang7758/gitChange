package mhwang.com.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：记帐中选择记账类型的工具类
 * 作者：王明海
 * 创建时间：2016/4/19
 */
public class RecordTypeUtil {
    private Context mContext;

    private void showLog(String msg){
        Log.d("---RecordTypeUtil--->",msg);
    }

    /**
     *  记录的类型列表
     */
    private ArrayList<String> mOutcomeTypes;
    private ArrayList<String> mIncomeTypes;

    /**
     *  类型与子项所对应的map
     */
    private Map<String,ArrayList<String>> OutcomeTypeMaps;
    private Map<String,ArrayList<String>> ImcomeTypeMaps;

    /**
     *  对应记录类型的子项资源ID
     */
    private int[] typeChildSrcIds = {R.array.food_water,R.array.clothes,R.array.house,
                                    R.array.traffic,R.array.communicate,R.array.travel,
                                    R.array.study,R.array.human_feeling,R.array.medical_care,
                                    R.array.other};

    private int[] incomeTypeChildsSrcIds = {R.array.job_income,R.array.other_income};

    public RecordTypeUtil(Context context) {
        this.mContext = context;
        buildOutcomeTypeMap();
        buildIncomeTypeMap();
    }

    /** 获取支出记录类型
     * @return
     */
    public ArrayList<String> getOutcomeTypes(){
        return mOutcomeTypes;
    }

    /** 获取收入记录类型
     * @return
     */
    public ArrayList<String> getImcomeTypes(){
        return mIncomeTypes;
    }

    /** 获取支出记录类型的子类型
     * @param type
     * @return
     */
    public ArrayList<String> getOutcomeTypeChilds(String type){
        return OutcomeTypeMaps.get(type);
    }

    /** 获取收入记录类型的子类型
     * @param type
     * @return
     */
    public ArrayList<String> getIncomeTypeChilds(String type){
        return ImcomeTypeMaps.get(type);
    }



    /**
     *  建立支出选择项和选择子项的联系
     */
    private void buildOutcomeTypeMap(){
        String[] types = mContext.getResources().getStringArray(R.array.budget_type);
        mOutcomeTypes = getList(types);
        OutcomeTypeMaps = new HashMap<>();
        for(int i = 0; i < types.length; i++){
            String[] childs = mContext.getResources().getStringArray(typeChildSrcIds[i]);
            OutcomeTypeMaps.put(types[i], getList(childs));
//            showLog("type is "+types[i]+" children is "+childs[0]);
        }

    }

    /**
     *  建立收入选择项和选择子项的联系
     */
    private void buildIncomeTypeMap(){
        String[] types = mContext.getResources().getStringArray(R.array.income_type);
        mIncomeTypes = getList(types);
        ImcomeTypeMaps = new HashMap<>();
        for(int i = 0; i < types.length; i++){
            String[] childs = mContext.getResources().getStringArray(incomeTypeChildsSrcIds[i]);
            ImcomeTypeMaps.put(types[i], getList(childs));
//            showLog("type is "+types[i]+" children is "+childs[0]);
        }

    }


    /** 获取数组的列表
     * @param array
     * @return
     */
    private ArrayList<String> getList(String[] array){

        ArrayList<String> list = new ArrayList<>();
        for (String str : array){
            list.add(str);
        }
        return list;
    }


}
