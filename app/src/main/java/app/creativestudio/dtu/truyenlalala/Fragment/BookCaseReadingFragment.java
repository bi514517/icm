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
import app.creativestudio.dtu.truyenlalala.Model.Book;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.BookCaseRecyclerViewAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.BookCaseRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;

public class BookCaseReadingFragment extends Fragment implements BookCaseRecyclerViewAdapter.BookCaseRecyclerViewAdapterInterface{
    ArrayList<Book> books = new ArrayList<>();
    //view
    private RecyclerView mRecyclerView;
    Context context;
    private BookCaseRecyclerViewAdapter bookCaseRecyclerViewAdapter;
    //
    DatabaseQueries databaseQueries ;
    public BookCaseReadingFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            context = getActivity();
        }catch (NullPointerException e){}

        return inflater.inflate(R.layout.fragment_book_case_saved, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        recyclerViewSetup(view);
        setupDatabase();
        getData();
    }
    void setupDatabase(){
        databaseQueries = new DatabaseQueries(context);
    }
    @Override
    public void onBindViewHolder(BookCaseRecyclerViewHolder holder, ArrayList<Book> books, int position) {
        final Book book = books.get(position);
        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookChaptAmount.setText("Chương trước : "+ book.getLastChapt().getStt() );
        if(UserInfo.isLogged) {
            Glide.with(context)
                    .load(book.getAvatar())
                    .placeholder(R.drawable.app_icon)
                    .into(holder.bookAvt);
        }else {
            Glide.with(context)
                    .load(databaseQueries.StringToBitMap(book.getAvatar()))
                    .placeholder(R.drawable.app_icon)
                    .into(holder.bookAvt);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToBookDetailActivity(book.getId());
            }
        });
    }
    void getData(){
        GetJsonAPI.setUrl(AppConfig.SERVER + "/book/reading/" + UserInfo.id).get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                ArrayList<JsonSnapshot> jsonSnapshotArrayList = resultJson.child("data").getArrayOfChild();
                for(int i = 0 ;i < jsonSnapshotArrayList.size();i++){
                    Book book = new Book(jsonSnapshotArrayList.get(i));
                    int bookIndex = getBookIndex(book.getId());
                    if(bookIndex == -1){
                        books.add(book);
                    }
                }
                if(bookCaseRecyclerViewAdapter!=null){
                    bookCaseRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }
    public int getBookIndex(String id){
        for(int i = 0;i<books.size();i++){
            if(id.equals(books.get(i).getId())){
                return i;
            }
        }
        return -1;
    }
    void recyclerViewSetup(View container){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = container.findViewById(R.id.book_case_saved_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
        bookCaseRecyclerViewAdapter = new BookCaseRecyclerViewAdapter(books,this);
        mRecyclerView.setAdapter(bookCaseRecyclerViewAdapter);
    }
    void sendUserToBookDetailActivity(String bookId){
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("bookId",bookId);
        startActivity(intent);
    }
}
