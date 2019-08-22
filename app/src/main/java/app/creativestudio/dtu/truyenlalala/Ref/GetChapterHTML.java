package app.creativestudio.dtu.truyenlalala.Ref;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class GetChapterHTML {
    GetChapterHTMLInterface getChapterHTMLInterface;
    public interface GetChapterHTMLInterface{
        void onComplete(Spanned result,String html);
    }
    public void bookStorageGetData(String bookId,int chapterStt ,GetChapterHTMLInterface getChapterHTMLInterface1,boolean isDownload){
        String url = AppConfig.SERVER + "/chapter/getchapt/"+bookId+"/"+chapterStt;
        if(isDownload)
            url = AppConfig.SERVER + "/chapter/download/"+bookId+"/"+chapterStt;
        this.getChapterHTMLInterface = getChapterHTMLInterface1;
        GetJsonAPI.setUrl(url).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                if(resultJson.child("status").toBoolean()){
                    String str = resultJson.child("data").toString();
                    getChapterHTMLInterface.onComplete(displayHtml(str),str);
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }

    public void bookStorageGetData(String bookId,int chapterStt ,GetChapterHTMLInterface getChapterHTMLInterface1){
        this.getChapterHTMLInterface = getChapterHTMLInterface1;
        GetJsonAPI.setUrl(AppConfig.SERVER + "/chapter/getchapt/"+bookId+"/"+chapterStt).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                if(resultJson.child("status").toBoolean()){
                    String str = resultJson.child("data").toString();
                    getChapterHTMLInterface.onComplete(displayHtml(str),str);
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }
    public static Spanned displayHtml(String source){
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
            return spanned;
        } else {
            spanned = Html.fromHtml(source);
            return  spanned;
        }
    }
}
