package app.creativestudio.dtu.truyenlalala.Model;

import android.util.Log;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class BookDetail extends Book {
    int likes,view,comment;
    User submitUser;
    ArrayList<Tag> tags;
    ArrayList<Category> categories;
    String description,dateUpdate;
    public BookDetail(JsonSnapshot jsonSnapshot) {
        super(jsonSnapshot.child("data/bookDetail"));
        JsonSnapshot data = jsonSnapshot.child("data/bookDetail");
        dateUpdate = data.child("bookDateUpdate").toString();
        comment = jsonSnapshot.child("data/comments").toInt() ;
        this.description = data.child("bookDescription").toString();
        this.likes = data.child("likes").toInt();
        this.view = data.child("bookView").toInt();
        int submitUserId = data.child("submitUserId").toInt();
        String submitUserEmail = data.child("submitUserEmail").toString();
        String submitUserName = data.child("submitUserName").toString();
        String submitUserAvatar = data.child("submitUserAvatar").toString();
        submitUser = new User(submitUserId,submitUserEmail,submitUserName,submitUserAvatar);
        this.dateUpdate = data.child("bookDatePublication").toString();
        addTags(jsonSnapshot);
        addCategories(jsonSnapshot);
    }
    private void addTags(JsonSnapshot jsonSnapshot){
        //
        this.tags = new ArrayList<>();
        JsonSnapshot tag = jsonSnapshot.child("data/tags");
        for (JsonSnapshot element:tag.getArrayOfChild()) {
            String name = element.child("name").toString();
            int id = element.child("id").toInt();
            tags.add(new Tag(id,name));
        }
    }
    private void addCategories(JsonSnapshot jsonSnapshot){
        JsonSnapshot category = jsonSnapshot.child("data/categories");
        this.categories = new ArrayList<>();
        for (JsonSnapshot element:category.getArrayOfChild()) {
            String name = element.child("name").toString();
            String id = element.child("id").toString();
            categories.add(new Category(id,name));
        }
    }
    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public User getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(User submitUser) {
        this.submitUser = submitUser;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Override
    public String toString() {
        return "BookDetail{" +
                "likes=" + likes +
                ", view=" + view +
                ", comment=" + comment +
                ", submitUser=" + submitUser +
                ", tags=" + tags +
                ", categories=" + categories +
                ", description='" + description + '\'' +
                ", dateUpdate='" + dateUpdate + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", author='" + author + '\'' +
                ", id=" + id +
                ", lastChapt=" + lastChapt +
                '}';
    }
}
