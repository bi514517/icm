package app.creativestudio.dtu.truyenlalala.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.UnsupportedEncodingException;

import app.creativestudio.dtu.truyenlalala.Activity.ChapterListActivity;
import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Model.Setting;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.Ref.GetChapterHTML;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;

public class ReadBookContentFragment extends Fragment implements View.OnClickListener {
    public ReadBookContentFragment() {
    }
    ReadBookContentFragmentCallBack callBack = null;
    TextView chapterNametv,content;
    ScrollView scrollView;
    Context context;

    // data
    String bookName,chapterName,bookId;
    int chapterStt;
    DatabaseQueries databaseQueries;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            context = getActivity();
        }catch (NullPointerException e){}

        Bundle arg = getArguments();
        if(arg != null)
        {
            bookName = arg.getString("bookName");
            chapterStt = arg.getInt("chapterStt");
            chapterName = arg.getString("chapterName");
            bookId = arg.getString("bookId");
        }
        return inflater.inflate(R.layout.fragment_read_book_content, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        viewSetup(view);
        // setup database
        databaseQueries = new DatabaseQueries(context);
        // kiểm trả trong thư viện offline trước , nếu nhận được kết quả thì khỏi tải về
        String offlineContent = databaseQueries.getChaptContent(bookId,chapterStt);
        if(offlineContent==null){
            new GetChapterHTML().bookStorageGetData(bookId, chapterStt, new GetChapterHTML.GetChapterHTMLInterface() {
                @Override
                public void onComplete(Spanned result,String html) {
                    content.setText(result);
                }
            },false);
        }else {
            content.setText(GetChapterHTML.displayHtml(offlineContent));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSetting();
    }

    void viewSetup(View view){
        scrollView = view.findViewById(R.id.read_book_scroll_view);
        chapterNametv =view.findViewById(R.id.read_book_chapter_name_fragment);
        content = view.findViewById(R.id.read_book_content_fragment);

        // onlcik
        content.setOnClickListener(this);
        // name
        chapterNametv.setText("Chương " + chapterStt + ": " + chapterName);
        //
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(callBack != null)
                    callBack.onClickOrPull(false);
            }
        });
        getSetting();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_book_content_fragment:
                if(callBack != null)
                    callBack.onClickOrPull(true);
                break;
        }
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    public void setCallBack(ReadBookContentFragmentCallBack callBack) {
        this.callBack = callBack;
        Log.d("fragmentcallback", callBack.toString());
    }

    public ReadBookContentFragmentCallBack getCallBack() {
        return callBack;
    }

    public interface ReadBookContentFragmentCallBack{
        void onClickOrPull(boolean click);
    }

    void getSetting(){
        Setting setting = new Setting(context);
        scrollView.setBackgroundColor(setting.getBackgroundColor());
        content.setTextSize(setting.getTextSize());
        content.setTextColor(setting.getTextColor());
        chapterNametv.setTextSize(setting.getTextSize()*1.33f);
    }
}
