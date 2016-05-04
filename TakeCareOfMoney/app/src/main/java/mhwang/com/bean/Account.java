package mhwang.com.bean;

/**
 * 项目名称：
 * 类描述：账户类
 * 作者：王明海
 * 创建时间：2016/4/30
 */
public class Account {
    private String name;
    private double money;
    public Account(){
        name = "";
        money = 0.00;
    }

    public Account(String name, double money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
