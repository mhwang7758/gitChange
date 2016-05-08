package mhwang.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import mhwang.com.adapter.BudgetTypeAdapter;
import mhwang.com.bean.BudgetType;
import mhwang.com.bean.Record;
import mhwang.com.database.DBUtil;
import mhwang.com.dialog.PresetBudgetDialog;
import mhwang.com.takecareofmoney.HomeFragment;
import mhwang.com.takecareofmoney.R;
import mhwang.com.util.LogUitl;

/**
 * 项目名称：
 * 类描述：预算界面
 * 作者：王明海
 * 创建时间：2016/4/12
 */
public class BudgetActivity extends Activity {
    public static final String KEY_PRESET_MONEY = "money";
    public static final double DEFAULT_MONEY = 0.00;
    public static final String KEY_NEW_TOTAL_BUDGET = "newtotalbudget";
    private ArrayList<BudgetType> budgets;
    private ListView lv_list;
    private BudgetTypeAdapter adapter;
    private TextView tv_total_budget;
    private TextView tv_total_outcome;
    private TextView tv_total_surplus;
    private String budgetTypes[];

    private double useableMoney;
    private double usedMoney;
    private double totalBudget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_budget);
        initData();
        initComponent();
        initEvent();
    }

    /**
     *  添加事件
     */
    private void initEvent() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPresetBudgetDialog(position);
            }
        });

        updateTotalUI();

    }

    /**
     *  更新预算UI
     */
    private void updateTotalUI(){
        // 保留余额两位小数
        DecimalFormat df = new DecimalFormat(".##");
        tv_total_budget.setText(df.format(totalBudget));
        tv_total_outcome.setText(df.format(usedMoney));
        tv_total_surplus.setText(df.format(useableMoney));
    }

    /** 弹出选择对话框
     * @param position
     */
    private void showPresetBudgetDialog(int position){
        LogUitl.showLog("BudgetActivity", "showPresetBudgetDialog");
        Intent intent = new Intent(this, PresetBudgetDialog.class);
        startActivityForResult(intent, position);
    }

    /**
     *  初始化控件
     */
    private void initComponent() {
        lv_list = (ListView) findViewById(R.id.lv_budget_list);
        adapter = new BudgetTypeAdapter(this,budgets);
        lv_list.setAdapter(adapter);
        tv_total_budget = (TextView) findViewById(R.id.tv_total_budget);
        tv_total_outcome = (TextView) findViewById(R.id.tv_budget_total_outcome);
        tv_total_surplus = (TextView) findViewById(R.id.tv_budget_total_surplus);

    }

    /**
     *  初始化数据
     */
    private void initData() {
//        budgets = readBudgets();
        budgets = DBUtil.getInstance(this).readBudgetTypes();
        for (BudgetType budgetType : budgets){
            ArrayList<Record> records = DBUtil.getInstance(this).readRecordsByType(budgetType.getType());
            if (records == null){
                continue;
            }
            double outcome = 0.00;
            for (Record record : records){
                outcome += record.getMoney();
                LogUitl.showLog("BudgetActivity","find the record money is "+record.getMoney());
            }
            budgetType.setOutcome(outcome);
            budgetType.setSurplus(budgetType.getBudget() - outcome);
        }

        Intent data = getIntent();
        useableMoney = data.getDoubleExtra(HomeFragment.KEY_SURPLUS,DEFAULT_MONEY);
        usedMoney = data.getDoubleExtra(HomeFragment.KEY_OUT,DEFAULT_MONEY);
        totalBudget = data.getDoubleExtra(HomeFragment.KEY_BUDGET, DEFAULT_MONEY);

    }

    /**
     *  设置预算总额
     */
    private void setTotalBudget(){
        totalBudget = DEFAULT_MONEY;
        useableMoney = DEFAULT_MONEY;
        usedMoney = DEFAULT_MONEY;
        // 计算总预算，已用金额，可用金额
        for (BudgetType budgetType : budgets){
            totalBudget += budgetType.getBudget();
            usedMoney += budgetType.getOutcome();
            useableMoney += budgetType.getSurplus();
        }

    }


    /**
     *  读取预算
     */
    private List<BudgetType> readBudgets(){
        budgetTypes = getResources().getStringArray(R.array.budget_type);
        List<BudgetType> wBudgets = new ArrayList<>();
        for(int i = 0; i < budgetTypes.length; i++){
            BudgetType budget = new BudgetType(budgetTypes[i],i+22.0,i+11.22);
            budget.setValue(i*5);
            wBudgets.add(budget);
        }
        return wBudgets;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        // 接收返回值
        double presetMoney = Double.parseDouble(data.getStringExtra(KEY_PRESET_MONEY));
        BudgetType budget = budgets.get(requestCode);
        // 如果预算改了,重新设置该项预算
        if (budget.getBudget() != presetMoney) {
            budget.setBudget(presetMoney);
            budget.setModified(true);
        }
        double surplus = presetMoney - budget.getOutcome();
        budget.setSurplus(surplus);
        setTotalBudget();
        updateTotalUI();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUitl.showLog("BudgetActivity", "onPause");
//        Intent data = new Intent();
//        data.putExtra(KEY_NEW_SURPLUS,useableMoney);
//        LogUitl.showLog("BudgetActivity", "onPause money is "+useableMoney);
//        setResult(RESULT_OK, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent data = new Intent();
            data.putExtra(KEY_NEW_TOTAL_BUDGET,totalBudget);
            LogUitl.showLog("BudgetActivity", "onKeyDown money is "+useableMoney);
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUitl.showLog("BudgetActivity","onDestroy");
        // 保存更改后的预算
        for (BudgetType type : budgets){
            if (type.isModified()){
                DBUtil.getInstance(this).updateBudge(type);
            }
        }
    }
}
