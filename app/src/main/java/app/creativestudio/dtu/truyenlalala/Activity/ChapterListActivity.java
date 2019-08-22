package app.creativestudio.dtu.truyenlalala.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.ChapterListRecyclerAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.ChapterListRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class ChapterListActivity extends AppCompatActivity implements ChapterListRecyclerAdapter.ChapterListRecyclerViewAdapterInterface {
    String bookName;
    String bookId;
    int chapterStt;
    ArrayList<Chapter> chapters = new ArrayList<>();
    // sqlite
    DatabaseQueries databaseQueries;
    // view
    private RecyclerView mRecyclerView;

    private ChapterListRecyclerAdapter chapterListRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        try {
            bookName = getIntent().getStringExtra("bookName");
            bookId = getIntent().getStringExtra("bookId");
            chapterStt = getIntent().getIntExtra("chapterStt",0);
        }catch (NullPointerException e){

        }
        viewSetup();
        setupDatabase();
        getChaptList();
    }
    void viewSetup(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = findViewById(R.id.chapter_list_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
        chapterListRecyclerAdapter = new ChapterListRecyclerAdapter(chapters,this);
        mRecyclerView.setAdapter(chapterListRecyclerAdapter);
    }
    void setupDatabase(){
        databaseQueries = new DatabaseQueries(this);
    }
    void getChaptList(){
        databaseQueries.getListChapters(chapters,bookId);
        Log.d("getChaptList", chapters.toString());
        GetJsonAPI.setUrl(AppConfig.SERVER + "/chapter/getlist/" + bookId).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                if(resultJson.child("status").toBoolean()){
                    chapters.clear();
                    for(JsonSnapshot element:resultJson.child("data").getArrayOfChild()){
                        int stt = element.child("stt").toInt();
                        String name = element.child("name").toString();
                        Chapter chapter = new Chapter(stt,name);
                        chapters.add(chapter);
                    }
                    chapterListRecyclerAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(chapterStt);
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }

    @Override
    public void onBindViewHolder(ChapterListRecyclerViewHolder holder, final ArrayList<Chapter> chapters, final int position) {
        final Chapter chapter = chapters.get(position);
        if(databaseQueries.isDownloadChapterExist(bookId,chapter.getStt())){
            holder.chapterName.setText(chapter.getName().replaceAll("\n","").replaceAll((char)13+"","") + "( đã tải )");
        }
        else {
        holder.chapterName.setText(chapter.getName().replaceAll("\n","").replaceAll((char)13+"","") );
        }
        holder.chapterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserBack(position);
            }
        });
        holder.chapterStt.setText("Chương " + chapter.getStt() +" : ");
    }
    public void sendUserBack(int chapterStt){
        Intent data = new Intent();
        data.putExtra("chapter",chapterStt);
        setResult(RESULT_OK, data);
        //---close the activity---
        finish();
    }
}
