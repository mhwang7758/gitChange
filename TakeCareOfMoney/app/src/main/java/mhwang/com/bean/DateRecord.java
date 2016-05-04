package mhwang.com.bean;

/**
 * 项目名称：
 * 类描述：日期记录类，主要有今天，本周，本月，本年或者其他
 * 作者：王明海
 * 创建时间：2016/4/9
 */
public class DateRecord {
    private String name;
    private String status;
    private double income;
    private double output;
    public DateRecord(){
        name = "mhwang";
        status = "还没有记过帐";
        income = 0.00;
        output = 0.00;
    }
    public  DateRecord(String name,String status,double income,double output){
        this.name = name;
        this.status = status;
        this.income = income;
        this.output = output;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public double getIncome() {
        return income;
    }

    public double getOutput() {
        return output;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void setOutput(double output) {
        this.output = output;
    }
}
