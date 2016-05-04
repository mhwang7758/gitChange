package mhwang.com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


import java.util.ArrayList;

import mhwang.com.takecareofmoney.R;
import mhwang.com.util.LogUitl;
import mhwang.com.util.RecordTypeUtil;


public class TypePicker extends LinearLayout {
    private static final int REFRESH_VIEW = 0x001;

    private WheelView mTypePicker;
    private WheelView mTypeChildPicker;

    private RecordTypeUtil mUtil;
    private ArrayList<String> mOutcomeTypes;
    private ArrayList<String> mIncomeTypes;
    private Context mContext;
    
    private String selectedType;
    private String selectedTypeChild;

    private boolean isIncome = false;

    public TypePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mUtil = new RecordTypeUtil(context);
        mOutcomeTypes = mUtil.getOutcomeTypes();
        mIncomeTypes = mUtil.getImcomeTypes();
    }

    public TypePicker(Context context) {
        this(context, null);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.picker_type, this);

        mTypePicker = (WheelView) findViewById(R.id.wv_type);
        mTypeChildPicker = (WheelView) findViewById(R.id.wv_type_child);

        // 设置数据为支出类型的数据
        setOutcomeTypeData();

        mTypePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;

                selectedType = mTypePicker.getSelectedText();
                if (selectedType == null || selectedType.equals(""))
                    return;

                // 如果当前是支出类型数据，则设置支出类型的子项数据
                ArrayList<String> types;
                if (!isIncome) {
                    types = mUtil.getOutcomeTypeChilds(text);
                } else {
                    types = mUtil.getIncomeTypeChilds(text);
                }

                if (types.size() == 0) {
                    return;
                }

                mTypeChildPicker.setData(types);

                if (types.size() > 1) {
                    //if city is more than one,show start index == 1
                    mTypeChildPicker.setDefault(1);
                    selectedTypeChild = types.get(1);
                } else {
                    mTypeChildPicker.setDefault(0);
                    selectedTypeChild = types.get(0);
                }

            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mTypeChildPicker.setOnSelectListener(new WheelView.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                selectedTypeChild = mTypeChildPicker.getSelectedText();
                if (selectedTypeChild == null || selectedTypeChild.equals(""))
                    return;
                int lastIndex = Integer.valueOf(mTypeChildPicker.getListSize());
                if (id > lastIndex) {
                    mTypeChildPicker.setDefault(lastIndex - 1);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

    }

    /**
     *  更改数据为收入类型
     */
    public void changeDataType(){
        // 如果当前是收入类型数据，则设置支出类型数据
        if(isIncome){
            setOutcomeTypeData();
            isIncome = false;
        }else{
            setIncomeTypeData();
            isIncome = true;
        }

    }

    /**
     *  设置收入类型数据
     */
    private void setIncomeTypeData(){
        mTypePicker.resetData(mIncomeTypes);
        mTypePicker.setDefault(0);
        selectedType = mIncomeTypes.get(0);

        mTypeChildPicker.setData(mUtil.getIncomeTypeChilds(selectedType));
        mTypeChildPicker.setDefault(1);
        selectedTypeChild = mUtil.getIncomeTypeChilds(selectedType).get(0);

        isIncome = true;
    }

    /**
     *  设置支出类型数据
     */
    private void setOutcomeTypeData(){
        mTypePicker.resetData(mOutcomeTypes);
        mTypePicker.setDefault(0);
        selectedType = mOutcomeTypes.get(0);

        mTypeChildPicker.setData(mUtil.getOutcomeTypeChilds(selectedType));
        mTypeChildPicker.setDefault(1);
        selectedTypeChild = mUtil.getOutcomeTypeChilds(selectedType).get(0);

        isIncome = false;
    }

    /** 获取选择的类型
     * @return
     */
    public String getSelectedType(){
        LogUitl.showLog("TypePicker","the selectType is "+selectedType);
        return selectedType;
    }

    /** 获取选择的类型子项
     * @return
     */
    public String getSelectedTypeChild(){
        return selectedTypeChild;
    }

}
