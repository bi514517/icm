package app.creativestudio.dtu.truyenlalala.Model;

public class Chapter {
    int stt ;
    String name;
    String content;
    public Chapter() {
        stt = -1;
        name ="";
    }

    public Chapter(int stt, String name) {
        this.stt = stt;
        this.name = name;
    }

    public Chapter(int stt, String name, String content) {
        this.stt = stt;
        this.name = name;
        this.content = content;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  stt +" " + name ;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
