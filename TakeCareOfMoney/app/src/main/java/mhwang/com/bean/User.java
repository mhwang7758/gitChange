package mhwang.com.bean;

/**
 * 项目名称：
 * 类描述：用户类
 * 作者：王明海
 * 创建时间：2016/4/16
 */
public class User {
    private int userId;
    private String name;
    private String word;
    private String password;
    private String info;

    public User(){
        userId = 1;
        name = "王明海";
        word = "书山有路勤为径，学海无涯苦作舟！";
        info = "桂林电子科技大学";
        password = "123456";
    }

    public User(String name,String word,String info,String password){
        this.name = name;
        this.word = word;
        this.info = info;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getWord() {
        return word;
    }

    public String getPassword() {
        return password;
    }

    public String getInfo() {
        return info;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}


