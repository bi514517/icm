package app.creativestudio.dtu.truyenlalala.Model;

import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class Comment {
    int userId,commentId;
    String userName,bookName,timeUpload,content,bookAvatar,userAvatar,bookId;

    public Comment(JsonSnapshot data) {
        if(data.hasChild("bookId"))
            this.bookId = data.child("bookId").toString();
        if(data.hasChild("bookAvatar"))
            this.bookAvatar = data.child("bookAvatar").toString();
        if(data.hasChild("bookName"))
            this.bookName = data.child("bookName").toString();
        this.commentId = data.child("commentId").toInt();
        this.userName = data.child("userName").toString();
        this.userId = data.child("userId").toInt();
        this.timeUpload = data.child("commentTimeUpload").toString();
        this.content = data.child("commentContent").toString();

        this.userAvatar = data.child("userAvatar").toString();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getTimeUpload() {
        return timeUpload;
    }

    public void setTimeUpload(String timeUpload) {
        this.timeUpload = timeUpload;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBookAvatar() {
        return bookAvatar;
    }

    public void setBookAvatar(String bookAvatar) {
        this.bookAvatar = bookAvatar;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
