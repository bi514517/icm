package app.creativestudio.dtu.truyenlalala.Model;

public class User {
    int id;
    String email,name,avt;

    public User(int id, String email, String name, String avt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avt = avt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
