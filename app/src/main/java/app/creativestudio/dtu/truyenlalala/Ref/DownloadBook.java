package app.creativestudio.dtu.truyenlalala.Ref;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spanned;


import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;

public class DownloadBook {
    String bookId ;
    String bookName;
    String bookAuthorName;
    String bookAvatar;
    DatabaseQueries databaseQueries;
    DownloadBookProcessInterface downloadBookProcessInterface;
    // interface
    public interface DownloadBookProcessInterface{
        void onProcess(int stt,long count);
        void onComplete();
    }
    public DownloadBook(String bookId, String bookName, String bookAuthorName, String bookAvatar, Context context) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
        this.bookAvatar = bookAvatar;
        addBook(context);
    }
    void addBook(Context context){
        databaseQueries = new DatabaseQueries(context);
        new DownloadImageFromUrl().startDownload(bookAvatar, new DownloadImageFromUrl.DownloadImageFromUrlInterface() {
            @Override
            public void onPostExecute(Bitmap result) {
                //byte[] imageArray = getBitmapAsByteArray(result);
                databaseQueries.addBook(bookId,bookName,bookAuthorName,result);
            }
        });
    }

    public void GetChapter(DownloadBookProcessInterface downloadBookProcessInterface1){
        downloadBookProcessInterface = downloadBookProcessInterface1;
        GetJsonAPI.setUrl(AppConfig.SERVER + "/chapter/getlist/" + bookId).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                if(resultJson.child("status").toBoolean()){
                    long count = resultJson.getChildentsCount();
                    ArrayList<Chapter> chapters = new ArrayList<Chapter>();
                    for(JsonSnapshot element:resultJson.child("data").getArrayOfChild()){
                        int stt = element.child("stt").toInt();
                        String name = element.child("name").toString();
                        Chapter chapter = new Chapter(stt,name);
                        chapters.add(chapter);
                    }
                    downloadAllChapter(chapters,0,count);
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }
    void downloadAllChapter(final ArrayList<Chapter> chapter, final int currentChapt, final long count){
        if(currentChapt==chapter.size())
        {
            downloadBookProcessInterface.onComplete();
        }
        else if(!databaseQueries.isDownloadChapterExist(bookId,chapter.get(currentChapt).getStt())){
            new GetChapterHTML().bookStorageGetData(bookId, chapter.get(currentChapt).getStt(), new GetChapterHTML.GetChapterHTMLInterface() {
                @Override
                public void onComplete(Spanned result, String html) {
                    downloadBookProcessInterface.onProcess(currentChapt + 1, count);
                    databaseQueries.addChapter(bookId, chapter.get(currentChapt).getStt(), chapter.get(currentChapt).getName(), html);
                    downloadAllChapter(chapter, currentChapt + 1, count);
                }
            },true);
        }
        else {
            downloadAllChapter(chapter, currentChapt + 1, count);
        }
    }
   /* public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }*/
}
