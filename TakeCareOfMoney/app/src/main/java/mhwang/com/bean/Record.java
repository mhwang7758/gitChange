package mhwang.com.bean;

/**
 * 项目名称：
 * 类描述：花销的记录
 * 作者：王明海
 * 创建时间：2016/4/10
 */
public class Record {
    public static final String INCOME = "收入";
    public static final String OUTCOME = "支出";
    private int id;
    private String time;         // 记录的时间
    private String name;         // 记录的名字
    private String status;       // 记录的状态，支出或收入
    private double money;       // 记录的钱数
    private String account;     // 记录的账户
    private String note;        // 记录的备注
    private String type;        // 记录的类型
    private String typeChild;   // 类型子项
    private int userId;         // 记录的用户ID
    private int year;           // 记录的年份
    private int month;          // 记录的月份
    private int day;            // 记录的日期（某月的某一日）
    private String photoPath;  // 图片路径

    public  Record(){
        time = "2016.04.09.12:22";
        name = "早餐";
        money = -22.0;
        status = "支出";
        account = "现金";
        note = "null";
        type = "食品酒水->早中午餐";
        year = 2016;
        month = 4;
        day = 20;
        photoPath = "";
    }

    public  Record(String time,String name,double money){
        this.time = time;
        this.name = name;
        this.money = money;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public int getId() {
        return id;
    }

    public String getTypeChild() {
        return typeChild;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccount() {
        return account;
    }

    public String getNote() {
        return note;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTypeChild(String typeChild) {
        this.typeChild = typeChild;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
