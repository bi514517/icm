package app.creativestudio.dtu.truyenlalala.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Fragment.ReadBookContentFragment;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;
import app.creativestudio.dtu.truyenlalala.PagerAdapter.ReadBookPagerAdapter;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;

public class ReadBookActivity extends AppCompatActivity implements ReadBookContentFragment.ReadBookContentFragmentCallBack, View.OnClickListener, ViewPager.OnPageChangeListener {
    //view
    LinearLayout topTab,bottomTab;
    ImageButton showChapterListBtn,backBtn,moveBtn,commentBtn,settingBtn;
    TextView bookNameTv;
    // view pager
    private ViewPager pager;
    ReadBookPagerAdapter adapter;
    //
    ArrayList<Chapter> chapters = new ArrayList<>();
    String bookName,bookId;
    int chapt;
    private boolean doubleBackToExitPressedOnce = false;
    boolean isBookDownloaded = false;
    // sqlite
    DatabaseQueries databaseQueries;
    private final int CHAPTER_REQUEST_CODE = 659;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        getIntentData();
        setupDatabaseOffline();
        checkBookDownloaded();
        viewSetup();
        getChaptList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ReadBookContentFragment) {
            ReadBookContentFragment headlinesFragment = (ReadBookContentFragment) fragment;
            headlinesFragment.setCallBack(this);
        }
    }
    void checkBookDownloaded(){
        isBookDownloaded = databaseQueries.isBookDownloaded(bookId);
    }

    void setupDatabaseOffline(){
        databaseQueries = new DatabaseQueries(this);
    }

    void getIntentData(){
        Intent intent = getIntent();
        chapt = intent.getIntExtra("chapt",0);
        bookName = intent.getStringExtra("bookName");
        bookId = intent.getStringExtra("bookId");
    }
    private void viewSetup() {
        //
        backBtn = findViewById(R.id.read_book_activity_back_btn);
        moveBtn = findViewById(R.id.read_book_activity_chapter_move_btn);
        commentBtn = findViewById(R.id.read_book_activity_write_comment_btn);
        settingBtn = findViewById(R.id.read_book_activity_setting_btn);
        topTab = findViewById(R.id.read_book_top_tab);
        bottomTab = findViewById(R.id.read_book_bottom_tab);
        showChapterListBtn = findViewById(R.id.read_book_activity_show_chapt_list_btn);
        // onclick
        backBtn.setOnClickListener(this);
        showChapterListBtn.setOnClickListener(this);
        moveBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        //pager
        pager = (ViewPager) findViewById(R.id.read_book_view_pager);
        pager.addOnPageChangeListener(this);
    }

    void setPager(){
        FragmentManager manager = getSupportFragmentManager();
        adapter = new ReadBookPagerAdapter(manager,bookId,bookName,chapters);
        pager.setAdapter(adapter);
    }
    void getChapterListOffline(){
        databaseQueries.getListChapters(chapters,bookId);
        adapter.notifyDataSetChanged();
    }

    void getChaptList(){
        setPager();
        // offline
        if(isBookDownloaded) {// nếu đã được tải thì xài trước dữ liệu offline
            getChapterListOffline();
            chapOfflineDirector();
        }
        GetJsonAPI.setUrl(AppConfig.SERVER + "/chapter/getlist/" + bookId ).get(new ApiCall.AsyncApiCall() {
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
                    adapter.notifyDataSetChanged();
                    // điều hướng đến trang đang đọc dở
                    chapOnlineDirector(resultJson);
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }

    void chapOfflineDirector(){
        // lướt đến chapt đang đọc ,nếu chapt chưa được tải thì lướt đến trang cuối
        if (chapt<chapters.size() && databaseQueries.isDownloadChapterExist(bookId, chapters.get(chapt).getStt()))
            pager.setCurrentItem(chapt,true);
        else pager.setCurrentItem(chapters.size() - 1,true);
    }

    void chapOnlineDirector(JsonSnapshot resultJson){
        // điều hướng đến trang đang đọc dở
        if( resultJson.getChildentsCount() > 0 ){
            if(isBookDownloaded)
                //check và nếu k ofline k đồng bộ thì hỏi xem có muốn đồng bộ hóa với dữ liệu online k
                showAlertDialog("Thông báo",
                        "Hệ thống ghi nhận tài khoản " + UserInfo.name + " đã đọc đến chap " + chapters.get(chapt).getStt() + " . bạn có muốn chuyển trang ? ");
                // nếu chưa tải thì khỏi hỏi
            else {
                pager.setCurrentItem(chapt, true);
            }
        }
    }

    void showAlertDialog(String title,String mess){
        if(pager.getCurrentItem()!=chapt) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(mess);
            builder.setCancelable(false);
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    pager.setCurrentItem(chapt,true);
                }
            });
            builder.setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        updateCurrentChapt();
    }
    @Override
    protected void onStop(){
        super.onStop();;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back thêm 1 lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    int updateCurrentChapt(){
        int currentChapt = pager.getCurrentItem();
        JSONObject body = new JSONObject();
        try {
            body.put("userId", UserInfo.id);
            body.put("bookId",bookId);
            body.put("chapt",currentChapt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetJsonAPI.setUrl(AppConfig.SERVER + "/book/currentchapt/").put(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {

            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        },new JsonSnapshot(body));
        // update sqlite
        addCurrentChaptToDatabaseOffline(currentChapt);
        return currentChapt;
    }
    void addCurrentChaptToDatabaseOffline(int currentChapt){
        databaseQueries.addUser_Book(UserInfo.id,bookId,currentChapt);
    }

    @Override
    public void onClickOrPull(boolean click) {
        showAndHideToolTab(click);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.read_book_activity_show_chapt_list_btn:
                sendUserToChapterListActivity();
                break;
            case R.id.read_book_activity_back_btn:
                finish();
                break;
            case R.id.read_book_activity_chapter_move_btn:
                moveToChapter();
                break;
            case R.id.read_book_activity_write_comment_btn:
                sendUserToCommentActivity();
                break;
            case R.id.read_book_activity_setting_btn:
                sendUserToSettingActivity();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHAPTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int chapter = data.getIntExtra("chapter",1);
                pager.setCurrentItem(chapter,true);
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        chapt = updateCurrentChapt();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("chapt",chapt);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        chapt = savedInstanceState.getInt("chapt");
    }

    void showAndHideToolTab(boolean click){
        if(topTab.getVisibility() == View.INVISIBLE && click) {
            bottomTabAppear();
            topTabAppear();
        }else{
            bottomTabDisappear();
            topTabDisappear();
        }
    }

    // slide the view from below itself to the current position
    public void bottomTabAppear(){
        if(bottomTab.getVisibility()!=View.VISIBLE){
            bottomTab.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    bottomTab.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(250);
            animate.setFillAfter(true);
            bottomTab.startAnimation(animate);
        }
    }

    // slide the view from its current position to below itself
    public void bottomTabDisappear(){
        if(bottomTab.getVisibility()!=View.INVISIBLE) {
            bottomTab.setVisibility(View.INVISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    bottomTab.getHeight()); // toYDelta
            animate.setDuration(250);
            animate.setFillAfter(true);
            bottomTab.startAnimation(animate);
        }
    }

    // slide the view from its current position to below itself
    public void topTabAppear(){
        if(topTab.getVisibility()!=View.VISIBLE) {
            topTab.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    -topTab.getHeight(),                 // fromYDelta
                    0); // toYDelta
            animate.setDuration(250);
            animate.setFillAfter(true);
            topTab.startAnimation(animate);
        }
    }

    // slide the view from below itself to the current position
    public void topTabDisappear(){
        if(topTab.getVisibility()!=View.INVISIBLE){
            topTab.setVisibility(View.INVISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,  // fromYDelta
                    -topTab.getHeight());                // toYDelta
            animate.setDuration(250);
            animate.setFillAfter(true);
            topTab.startAnimation(animate);
        }
    }
    void sendUserToChapterListActivity(){
        Intent intent = new Intent(this, ChapterListActivity.class);
        intent.putExtra("bookId",bookId);
        intent.putExtra("bookName",bookName);
        intent.putExtra("chapterStt",pager.getCurrentItem());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,CHAPTER_REQUEST_CODE);
    }
    void sendUserToCommentActivity(){
        Intent intent = new Intent(this,CommentActivity.class);
        intent.putExtra("bookId",bookId);
        startActivity(intent);
    }
    void sendUserToSettingActivity(){
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("bookId",bookId);
        startActivity(intent);
    }

    void moveToChapter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReadBookActivity.this);
        builder.setTitle(" nhảy đến chương (1 - " +  chapters.size() + ") :");
        final EditText tv_info= new EditText(ReadBookActivity.this);
        // xét cho nó chỉ nhập só
        tv_info.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(tv_info);
        //
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result = Integer.parseInt(tv_info.getText().toString()) - 1 ;
                if(result >= 0 && result < chapters.size() )
                    pager.setCurrentItem(result,true);
            }
        });
        builder.show();
    }


}
