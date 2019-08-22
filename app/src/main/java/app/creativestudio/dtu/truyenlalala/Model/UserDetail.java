package app.creativestudio.dtu.truyenlalala.Model;

public class UserDetail extends User {
    String gmail,pass;
    boolean gender;

    public UserDetail(int id, String email, String name, String avt, String gmail, String pass, boolean gender) {
        super(id, email, name, avt);
        this.gmail = gmail;
        this.pass = pass;
        this.gender = gender;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
