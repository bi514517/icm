package app.creativestudio.dtu.truyenlalala.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Activity.BookDetailActivity;
import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Model.Comment;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.CommentRecyclerViewCommunityFragmentAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.CommentRecyclerViewCommunityFragmentHolder;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class HomeCommunityFragment extends Fragment implements CommentRecyclerViewCommunityFragmentAdapter.CommentRecyclerViewAdapterInterface{
    public HomeCommunityFragment() {
    }
    Context context;
    // sqlite
    DatabaseQueries databaseQueries;
    //view
    RecyclerView recyclerView;
    CommentRecyclerViewCommunityFragmentAdapter adapter;
    //
    ArrayList<Comment> comments = new ArrayList<>();
    int page = 1,numberOfPage = 40;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            context = getActivity();
        }catch (NullPointerException e){}

        return inflater.inflate(R.layout.fragment_home_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        setupDatabase();
        viewSetup(view);
        getData();
    }

    void viewSetup(View view){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentRecyclerViewCommunityFragmentAdapter(comments,this);
        recyclerView.setAdapter(adapter);
    }
    void setupDatabase(){
        databaseQueries = new DatabaseQueries(context);
    }
    void getData(){
        GetJsonAPI.setUrl(AppConfig.SERVER + "/comment/"+page+"/"+numberOfPage).get(new ApiCall.AsyncApiCall() {
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
    public void onBindViewHolder(CommentRecyclerViewCommunityFragmentHolder holder, ArrayList<Comment> comments, int position) {
        final Comment comment = comments.get(position);
        holder.userNameTv.setText(comment.getUserName());
        holder.bookNameTv.setText(comment.getBookName());
        holder.bookNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToBookDetailActivity(comment.getBookId());
            }
        });
        holder.timeTv.setText(comment.getTimeUpload());
        holder.contentTv.setText(comment.getContent());
        Glide.with(context)
                .load(comment.getUserAvatar())
                .placeholder(R.drawable.app_icon)
                .into(holder.userAvatar);
        Glide.with(context)
                .load(comment.getBookAvatar())
                .placeholder(R.drawable.app_icon)
                .into(holder.bookAvatar);
    }
    boolean isCommentExits(Comment comment){
        for (Comment comment1:comments) {
            if(comment.getCommentId()==comment1.getCommentId())
                return false;
        }
        return true;
    }
    void sendUserToBookDetailActivity(String bookId){
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("bookId",bookId);
        startActivity(intent);
    }
}
