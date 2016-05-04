package mhwang.com.bean;

/**
 * 项目名称：
 * 类描述：预算种类类
 * 作者：王明海
 * 创建时间：2016/4/12
 */
public class BudgetType {
    public static final int FOOD_WARTOR = 0;
    public static final int CLOTHES = 1;
    public static final int HOSET_TOOLS = 2;
    public static final int TRAFFIC = 3;
    public static final int COMMUNICATE = 4;
    public static final int HAVE_FUN = 5;
    public static final int STUDY = 6;
    public static final int HUMAN_FEELING = 7;
    public static final int HEALTH_CARE = 8;
    public static final int OTHERS = 9;


    /**
     *  预算种类
     */
    private String type;
    /**
     *  预算余额进度条的值
     */
    private double value;
    /**
     *  预算余额
     */
    private double surplus;
    /**
     *  预算金额
     */
    private double budget;

    /**
     *  消费的金额
     */
    private double outcome;

    /**
     *  预算是否更改过
     */
    private boolean modified;

    public BudgetType(){
        type = "null";
        value = 0.0;
        surplus = 0.0;
        budget = 0.0;
        outcome = 0.0;
        modified = false;
    }

    public BudgetType(String type, double surplus, double budget) {
        this.type = type;
        this.surplus = surplus;
        this.budget = budget;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setOutcome(double outcome) {
        this.outcome = outcome;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setSurplus(double surplus) {
        this.surplus = surplus;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public double getSurplus() {
        return surplus;
    }

    public double getBudget() {
        return budget;
    }

    public double getOutcome() {
        return outcome;
    }

    public boolean isModified() {
        return modified;
    }
}
