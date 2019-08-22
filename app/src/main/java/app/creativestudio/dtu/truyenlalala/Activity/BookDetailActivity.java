package app.creativestudio.dtu.truyenlalala.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Model.BookDetail;
import app.creativestudio.dtu.truyenlalala.Model.Category;
import app.creativestudio.dtu.truyenlalala.Model.Tag;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.BlurTransformation;
import app.creativestudio.dtu.truyenlalala.Ref.CreateNotification;
import app.creativestudio.dtu.truyenlalala.Ref.DownloadBook;
import app.creativestudio.dtu.truyenlalala.Ref.GetChapterHTML;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;
import app.creativestudio.dtu.truyenlalala.Ref.SnackBar;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView bookName,authorName,latestChapt,dateUpdate,view,like,comment,description;
    CircleImageView avatar;
    ImageView backGround;
    Button readNowBtn;
    ImageButton reactionBtn,commentBtn,downloadBtn,sharebtn,reportBtn;
    FlowLayout categoriesLayout,tagsLayout;
    // id của sách
    BookDetail book;
    String bookId ;
    int currentChapt = 0;
    boolean isLike = false;
    // sqlite
    DatabaseQueries databaseQueries ;
    // download progress
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        bookId = getIntent().getStringExtra("bookId");
        setupDatabaseOffline();
        getCurrentChaptOffline();
        bookData();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getCurrentChaptOffline();
    }
    void setupDatabaseOffline(){
        databaseQueries = new DatabaseQueries(this);
    }
    private void viewSetup() {
        bookName = findViewById(R.id.book_detail_name);
        authorName = findViewById(R.id.book_detail_author_name);
        latestChapt = findViewById(R.id.book_detail_latest_chapt);
        dateUpdate = findViewById(R.id.book_detail_date_update);
        view = findViewById(R.id.book_detail_view);
        like = findViewById(R.id.book_detail_like);
        comment = findViewById(R.id.book_detail_comment);
        description = findViewById(R.id.book_detail_description);
        // image
        avatar = findViewById(R.id.book_detail_avatar);
        backGround = findViewById(R.id.book_detail_background);
        // button
        readNowBtn = findViewById(R.id.book_detail_read_btn);
        reactionBtn = findViewById(R.id.book_detail_reaction_btn);
        commentBtn = findViewById(R.id.book_detail_comment_btn);
        downloadBtn = findViewById(R.id.book_detail_download_btn);
        sharebtn = findViewById(R.id.book_detail_share_btn);
        reportBtn = findViewById(R.id.book_detail_report_btn);
        // layout
        categoriesLayout = findViewById(R.id.book_detail_categories_list);
        tagsLayout = findViewById(R.id.book_detail_tags_list);
        // onclick listener
        reactionBtn.setOnClickListener(this);
        readNowBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
    }
    void bookData(){
        String url = UserInfo.isLogged ? AppConfig.SERVER + "/book/detail/" + bookId + "/" + UserInfo.id : AppConfig.SERVER + "/book/detail/" + bookId ;
        GetJsonAPI.setUrl(url).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                // sau khi lấy thành công dữ liệu mới setupview kẻo mấy thằng ngu bấm tầm bậy
                viewSetup();
                //
                if(resultJson.child("status").toBoolean()){
                    book = new BookDetail(resultJson);
                    // current chapt
                    if(resultJson.child("data/currentUser").toString().length()>0){
                        currentChapt = resultJson.child("data/currentUser/chapt").toInt();
                        isLike = resultJson.child("data/currentUser/reactionName").toString() != "null";
                        likeBtnUpdate(book.getLikes());
                    }
                    // text
                    bookName.setText(book.getName());
                    authorName.setText(book.getAuthor());
                    dateUpdate.setText(book.getDateUpdate());
                    latestChapt.setText(book.getLastChapt().toString());
                    view.setText(book.getView() + "");
                    comment.setText(book.getComment() + " Bình luận");
                    description.setText(GetChapterHTML.displayHtml(book.getDescription()));
                    //img
                    Glide.with(getApplicationContext())
                            .load(book.getAvatar())
                            .placeholder(R.drawable.app_icon)
                            .into(avatar);

                    Glide.with(getApplicationContext())
                            .load(book.getAvatar())
                            .transform(new BlurTransformation(BookDetailActivity.this))
                            .into(backGround);
                    //layout
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(5, 5, 5, 5);
                    for (Category element:book.getCategories()) {
                        categoriesLayout.addView(createCategoriesItem(element),params);
                    }

                    params.setMargins(5, 5, 5, 5);
                    for (Tag element:book.getTags()) {
                        tagsLayout.addView(createTagsItem(element),params);
                    }
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }
    LinearLayout createCategoriesItem(Category category){
        LinearLayout root = new LinearLayout(this);
        root.setBackgroundResource(R.drawable.categories_tag_background);
        TextView categoryTv = new TextView(this);
        categoryTv.setText(category.getName());
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        LinearLayout wrapLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 3, 5, 3);
        wrapLayout.setLayoutParams(params);
        wrapLayout.addView(categoryTv);
        root.addView(wrapLayout);
        return root;
    }
    LinearLayout createTagsItem(Tag tag){
        LinearLayout root = new LinearLayout(this);
        root.setBackgroundResource(R.drawable.tags_tag_background);
        TextView tagTv = new TextView(this);
        tagTv.setText(tag.getName());
        tagTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        LinearLayout wrapLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 3, 5, 3);
        wrapLayout.setLayoutParams(params);
        wrapLayout.addView(tagTv);
        root.addView(wrapLayout);
        return root;
    }
    void sendUserToReadBookActivity(String bookName,int chapt){
        Intent intent = new Intent(this,ReadBookActivity.class);
        intent.putExtra("bookId",bookId);
        intent.putExtra("bookName",bookName);
        intent.putExtra("chapt",chapt);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.book_detail_read_btn:
                sendUserToReadBookActivity(bookName.getText().toString(),currentChapt);
                break;
            case R.id.book_detail_download_btn:
                downloadBookConfirmDialog();
                break;
            case R.id.book_detail_comment_btn:
                sendUserToCommentActivity();
                break;
            case R.id.book_detail_reaction_btn:
                sendLike();
                break;
        }
    }
    void getCurrentChaptOffline(){
        if(databaseQueries.checkExistsUser_Book(UserInfo.id,bookId)){
            currentChapt = databaseQueries.getCurrentChapt(UserInfo.id,bookId);
        }
    }
    void downloadBookConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận tải : ");
        builder.setMessage("Bạn có muốn tải truyện \"" + book.getName() + "\" ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Tải ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadBookSetup();
                new DownloadBook(book.getId(),book.getName(),book.getAuthor(),book.getAvatar(),BookDetailActivity.this).GetChapter(new DownloadBook.DownloadBookProcessInterface() {
                    @Override
                    public void onProcess(int stt, long count) {
                        downloadBookUpdate(stt);
                    }

                    @Override
                    public void onComplete() {
                        downloadBookDone();
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    void downloadBookSetup(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang tải truyện");
        progressDialog.setMessage("Vui lòng đợi ... ");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(book.getLastChapt().getStt());
        progressDialog.show();
    }
    void downloadBookUpdate(int status){
        progressDialog.setProgress(status);
    }
    void downloadBookDone(){
        progressDialog.dismiss();
        SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"Đã tải thành công vào thư viện , nhanh vào thử xem :D");
        CreateNotification.doNotification(book.getName() ,"bạn đã tải thành công , nhanh vào thư viện nào ! " ,653,this);
    }
    void sendUserToCommentActivity(){
        Intent intent = new Intent(this,CommentActivity.class);
        intent.putExtra("bookId",bookId);
        startActivity(intent);
    }


    void sendLike(){
        if(!UserInfo.isLogged)
            SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"Bạn cần đăng nhập để thực hiện tính năng này !");
        else if(isLike)
            SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"Bạn đã thích truyện này rồi !");
        else{
            // trước tiên cứ cập nhật trước đã , nó bấm nhiều lần đéo được tốn request
            isLike = true;
            JSONObject body = new JSONObject();
            try {
                body.put("userId",UserInfo.id);
                body.put("bookId",bookId);
                body.put("reaction",1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetJsonAPI.setUrl(AppConfig.SERVER + "/book/reaction/").post(new ApiCall.AsyncApiCall() {
                @Override
                public void onSuccess(long resTime, JsonSnapshot resultJson) {
                    if(resultJson.child("status").toBoolean()){
                        likeBtnUpdate(resultJson.child("data").toInt());
                    }
                }

                @Override
                public void onFail(int responeCode, String mess) {

                }
            },new JsonSnapshot(body));
        }
    }
    void likeBtnUpdate(int likes){
        like.setText(likes +" Thích");
        if(isLike)
            reactionBtn.setImageResource(R.drawable.like);
        else
            reactionBtn.setImageResource(R.drawable.dislike);
    }
}
