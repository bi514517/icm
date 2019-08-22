package app.creativestudio.dtu.truyenlalala.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;


import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class Book {
    String name,avatar,author;
    String id;
    Chapter lastChapt ;
    public Book(JsonSnapshot jsonSnapshot) {
        this.name = jsonSnapshot.child("bookName").toString().replaceAll("\n","").replaceAll((char)13+"","");
        this.avatar = jsonSnapshot.child("bookAvatar").toString();
        this.id = jsonSnapshot.child("bookId").toString();
        this.author = jsonSnapshot.child("authorName").toString().replaceAll("\n","").replaceAll((char)13+"","");
        int chapterStt = jsonSnapshot.child("chapterStt").toInt();
        String chapterName = jsonSnapshot.child("chapterName").toString().replaceAll("\n","").replaceAll((char)13+"","");
        this.lastChapt = new Chapter(chapterStt,chapterName);
    }

    public Book(String name, String avatar, String author, String id, Chapter lastChapt) {
        this.name = name;
        this.avatar = avatar;
        this.author = author;
        this.id = id;
        this.lastChapt = lastChapt;
    }

    public interface GetChaptInterface{
        void onComplete();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public Chapter getLastChapt() {
        return lastChapt;
    }

    public void setLastChapt(Chapter lastChapt) {
        this.lastChapt = lastChapt;
    }
}
