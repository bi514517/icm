package app.creativestudio.dtu.truyenlalala.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Comment;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.CommentActivityRecyclerViewAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.CommentActivityRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;
import app.creativestudio.dtu.truyenlalala.Ref.SnackBar;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;

public class CommentActivity extends AppCompatActivity  implements
        CommentActivityRecyclerViewAdapter.CommentRecyclerViewAdapterInterface ,
        View.OnClickListener {
    //view
    RecyclerView recyclerView;
    CommentActivityRecyclerViewAdapter adapter;
    EditText editComment;
    ImageButton sendComment;
    //
    ArrayList<Comment> comments = new ArrayList<>();
    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        try{
            bookId = getIntent().getStringExtra("bookId");
        }catch (NullPointerException e){}
        viewSetup();
        getData();
    }
    void viewSetup(){
        sendComment = findViewById(R.id.comment_send_btn);
        editComment = findViewById(R.id.comment_edit_content_text);
        // recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentActivityRecyclerViewAdapter(comments,this);
        recyclerView.setAdapter(adapter);
        // onclick
        sendComment.setOnClickListener(this);
    }
    void getData(){
        GetJsonAPI.setUrl(AppConfig.SERVER + "/comment/getCommentByBookId/" + bookId).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                if(resultJson.child("status").toBoolean()){
                    for (JsonSnapshot data:resultJson.child("data").getArrayOfChild()) {
                        Comment comment = new Comment(data);
                        if(isCommentExits(comment))
                            comments.add(comment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_send_btn:
                sendComment();
                break;
        }
    }
    void sendComment(){
        hideKeyboard();
        if(UserInfo.isLogged){
            if(editComment.getText().toString().length()>0){
                JSONObject body = new JSONObject();
                try {
                    body.put("userId", UserInfo.id);
                    body.put("bookId", bookId);
                    body.put("content", editComment.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // nhập xong thì clear thôi
                editComment.setText("");
                GetJsonAPI.setUrl(AppConfig.SERVER + "/comment/").post(new ApiCall.AsyncApiCall() {
                    @Override
                    public void onSuccess(long resTime, JsonSnapshot resultJson) {
                        if(resultJson.child("status").toBoolean()){
                            for (JsonSnapshot data:resultJson.child("data").getArrayOfChild()) {
                                Comment comment = new Comment(data);
                                if(isCommentExits(comment))
                                    comments.add(0,comment);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFail(int responeCode, String mess) {

                    }
                },new JsonSnapshot(body));
            }
        }else {
            SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"bạn cần đăng nhập để có thể bình luận");
        }
    }
    boolean isCommentExits(Comment comment){
        for (Comment comment1:comments) {
            if(comment.getCommentId()==comment1.getCommentId())
                return false;
        }
        return true;
    }
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBindViewHolder(CommentActivityRecyclerViewHolder holder, ArrayList<Comment> comments, int position) {
        Comment comment = comments.get(position);
        holder.userNameTv.setText(comment.getUserName());
        holder.timeTv.setText(comment.getTimeUpload());
        holder.contentTv.setText(comment.getContent());
        Glide.with(this)
                .load(comment.getUserAvatar())
                .placeholder(R.drawable.app_icon)
                .into(holder.userAvatar);
    }
}
