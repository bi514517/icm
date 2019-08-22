package app.creativestudio.dtu.truyenlalala.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Book;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;
import app.creativestudio.dtu.truyenlalala.Model.Comment;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;

public class DatabaseQueries {
    DatabaseSetup databaseSqlite ;
    public DatabaseQueries(Context context){
        databaseSqlite = new DatabaseSetup(context, AppConfig.DATA_BASE_NAME,null,1);
        // create database
        try {
            databaseSqlite.excuteQuery(BOOK);
            databaseSqlite.excuteQuery(CATEGORY);
            databaseSqlite.excuteQuery(CHAPTER);
            databaseSqlite.excuteQuery(USER_BOOK);
            databaseSqlite.excuteQuery(REACTION);
        }catch (Exception exp){
            Log.d("DatabaseQueries", exp.getMessage());
        }
    }
    public boolean checkExistsUser_Book(int userId,String bookId){
        Cursor result = databaseSqlite.getData("SELECT EXISTS(SELECT * FROM user_book WHERE bookId='" + bookId +"' and userId=" + userId + ") as isexists");
        while (result.moveToNext())
            return result.getInt(result.getColumnIndex("isexists"))!=0;
        return false;
    }
    public void addUser_Book(int userId,String bookId,int chapt) {
        if (checkExistsUser_Book(userId,bookId)) {
            databaseSqlite.excuteQuery("UPDATE user_book " +
                    "SET chapt = " + chapt +
                    " WHERE bookId = '" + bookId + "' and userId = "  +userId + ";");
        } else {
            databaseSqlite.excuteQuery("INSERT INTO user_book(bookId,userId,chapt) VALUES ('" + bookId + "'," + userId + "," + chapt + ");");
        }
    }
    public void getDownloadedListBooks(ArrayList<Book> books) {
        books.clear();
        Cursor result = databaseSqlite.getData("SELECT * FROM (SELECT book.name as bookName ,book.id as bookId , book.avatar as bookAvatar ,book.authorName as authorName, chapter.stt as chapterStt,chapter.name as chapterName FROM book LEFT JOIN chapter on book.id = chapter.bookId ORDER by chapter.stt ASC) GROUP by bookId" );
        while(result.moveToNext()){
            String bookName = result.getString(result.getColumnIndex("bookName"));
            Log.d("getDownloadedListBooks", "getDownloadedListBooks: " + bookName);
            String bookAvatar = result.getString(result.getColumnIndex("bookAvatar"));
            String authorName = result.getString(result.getColumnIndex("authorName"));
            String bookId = result.getString(result.getColumnIndex("bookId"));
            int chapterStt = result.getInt(result.getColumnIndex("chapterStt"));
            String chaptName = result.getString(result.getColumnIndex("chapterName"));
            Chapter lastChapt = new Chapter(chapterStt,chaptName);
            books.add(new Book(bookName, bookAvatar,authorName, bookId, lastChapt));
        }
    }
    public boolean isBookDownloaded(String bookId){
        Cursor result = databaseSqlite.getData("SELECT EXISTS(" +
                " SELECT * FROM (" +
                    "SELECT book.name as bookName ,book.id as bookId , book.avatar as bookAvatar ,book.authorName as authorName, chapter.stt as chapterStt,chapter.name as chapterName " +
                    "FROM book LEFT JOIN chapter on book.id = chapter.bookId ORDER by chapter.stt ASC" +
                ") WHERE bookId = '"+bookId+"' GROUP by bookId) as isexists");
        while (result.moveToNext())
            return result.getInt(result.getColumnIndex("isexists"))!=0;
        return false;
    }
    public void getListChapters(ArrayList<Chapter> chapters,String bookId) {
        chapters.clear();
        Cursor result = databaseSqlite.getData("select * from chapter where bookId = '" + bookId + "'");
        while(result.moveToNext()){
            String name = result.getString(result.getColumnIndex("name"));
            int stt = result.getInt(result.getColumnIndex("stt"));
            chapters.add(new Chapter(stt,name));
        }
    }
    public int getCurrentChapt(int userId,String bookId) {
        Cursor result = databaseSqlite.getData("select user_book.chapt from user_book where user_book.userId = "+ userId + " and user_book.bookId = '" + bookId + "'");
        while(result.moveToNext())
            return result.getInt(result.getColumnIndex("chapt"));
        return 0;
    }
    public boolean isDownloadChapterExist(String bookId,int stt){
        Cursor result = databaseSqlite.getData("SELECT EXISTS(SELECT * FROM chapter WHERE bookId='" + bookId +"' and stt=" + stt + ") as isexists");
        while (result.moveToNext())
            return result.getInt(result.getColumnIndex("isexists"))!=0;
        return false;
    }
    public void addBook(String bookId, String bookName, String authorName, Bitmap avatar){
        bookName = handleIllegalText(bookName);
        authorName = handleIllegalText(authorName);
        ContentValues cv = new  ContentValues();
        cv.put("id",bookId);
        cv.put("name",bookName);
        cv.put("authorName",authorName);
        cv.put("avatar",BitMapToString(avatar));
        databaseSqlite.getSQLiteDatabase().insert("book",null,cv);
    }
    public void addChapter(String bookId,int stt,String name,String content) {
        name = handleIllegalText(name);
        content = handleIllegalText(content);
        databaseSqlite.excuteQuery("INSERT INTO chapter(bookId,stt,name,content) " +
                "VALUES ('" + bookId + "'," + stt + ",'"+  name + "','"+  content + "');");
    }
    public String getChaptContent(String bookId,int stt) {
        Cursor result = databaseSqlite.getData("SELECT chapter.content FROM chapter WHERE chapter.bookId = '" + bookId + "' AND chapter.stt = "+stt+" ;" );
        while(result.moveToNext())
            return result.getString(result.getColumnIndex("content"));
        return null;
    }
    
    String handleIllegalText(String text){
        return text.replaceAll("'","`");
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    final String BOOK = "CREATE TABLE IF NOT EXISTS \"book\" (\n" +
            "\t\"id\"\tTEXT,\n" +
            "\t\"name\"\tTEXT,\n" +
            "\t\"authorName\"\tTEXT,\n" +
            "\t\"avatar\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"id\")\n" +
            ")";
    final String CATEGORY = "CREATE TABLE IF NOT EXISTS \"category\" (\n" +
            "\t\"id\"\tTEXT,\n" +
            "\t\"name\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"id\")\n" +
            ");";
    final String CHAPTER = "CREATE TABLE IF NOT EXISTS \"chapter\" (\n" +
            "\t\"bookId\"\tINTEGER,\n" +
            "\t\"stt\"\tINTEGER,\n" +
            "\t\"name\"\tTEXT,\n" +
            "\t\"content\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"bookId\",\"stt\")\n" +
            ");";
    final String USER_BOOK = "CREATE TABLE IF NOT EXISTS \"user_book\" (\n" +
            "\t\"bookId\"\tTEXT,\n" +
            "\t\"userId\"\tINTEGER,\n" +
            "\t\"chapt\"\tINTEGER,\n" +
            "\tPRIMARY KEY(\"bookId\",\"userId\")\n" +
            ");";
    final String REACTION = "CREATE TABLE IF NOT EXISTS \"reaction\" (\n" +
            "\t\"id\"\tINTEGER,\n" +
            "\t\"name\"\tTEXT,\n" +
            "\t\"icon\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"id\")\n" +
            ");";
}
