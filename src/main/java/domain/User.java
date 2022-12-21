package domain;

/**
 * @author yhw
 * @version 1.0
 **/
public class User {
    private String username="";
    private String password="";
    private String phoneNum="";

    public User() {
    }

    public User(String username, String password, String phoneNum) {
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
