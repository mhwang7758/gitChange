package mhwang.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import mhwang.com.bean.BudgetType;
import mhwang.com.takecareofmoney.R;

/**
 * 项目名称：
 * 类描述：预算种类列表适配器
 * 作者：王明海
 * 创建时间：2016/4/12
 */
public class BudgetTypeAdapter extends BaseAdapter{
    private List<BudgetType> budgets;
    private LayoutInflater inflater;
    private int[] imgIds = {R.drawable.ic_wator,R.drawable.ic_clothes,
                            R.drawable.ic_house,R.drawable.ic_traffic,
                            R.drawable.ic_phone,R.drawable.ic_traver,
                            R.drawable.ic_study,R.drawable.ic_gifts,
                            R.drawable.ic_doctor,R.drawable.ic_others};
    private String budgetTypes[];

    public BudgetTypeAdapter(Context context,List<BudgetType> budgets) {
        this.budgets = budgets;
        inflater = LayoutInflater.from(context);
        budgetTypes = context.getResources().getStringArray(R.array.budget_type);
    }

    @Override
    public int getCount() {
        return budgets.size();
    }

    @Override
    public Object getItem(int position) {
        return budgets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BudgetType budget = budgets.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_budget,null);
            holder = new ViewHolder();
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_item_budget_photo);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_item_budget_type);
            holder.tv_budget = (TextView) convertView.findViewById(R.id.tv_item_budget_budget);
            holder.tv_surplus = (TextView) convertView.findViewById(R.id.tv_item_budget_suplus);
            holder.pb_value = (ProgressBar) convertView.findViewById(R.id.pb_item_budget_probar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_photo.setImageResource(imgIds[position]);
        holder.tv_type.setText(budget.getType());
        holder.tv_surplus.setText("剩余" + budget.getSurplus());
        holder.tv_budget.setText("预算" + budget.getBudget());
        holder.pb_value.setProgress((int) budget.getValue());
        return convertView;
    }

    private class ViewHolder{
        ImageView iv_photo;
        TextView tv_type;
        TextView tv_surplus;
        TextView tv_budget;
        ProgressBar pb_value;
    }
}
