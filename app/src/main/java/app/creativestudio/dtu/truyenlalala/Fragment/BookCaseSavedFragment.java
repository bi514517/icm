package app.creativestudio.dtu.truyenlalala.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Activity.ReadBookActivity;
import app.creativestudio.dtu.truyenlalala.Database.DatabaseQueries;
import app.creativestudio.dtu.truyenlalala.Model.Book;
import app.creativestudio.dtu.truyenlalala.PagerAdapter.BookCasePagerAdapter;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.BookCaseRecyclerViewAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.BookCaseRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;

public class BookCaseSavedFragment extends Fragment implements BookCaseRecyclerViewAdapter.BookCaseRecyclerViewAdapterInterface {
    ArrayList<Book> books = new ArrayList<>();
    //sqlite
    DatabaseQueries databaseQueries;
    //view
    private RecyclerView mRecyclerView;
    Context context;
    private BookCaseRecyclerViewAdapter bookCaseRecyclerViewAdapter;
    //
    int currentChapt = 0;

    public BookCaseSavedFragment() {
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
        dbSetup();
        getData();
    }
    void dbSetup(){
        databaseQueries = new DatabaseQueries(context);
    }
    void getData(){
        databaseQueries.getDownloadedListBooks(books);
        bookCaseRecyclerViewAdapter.notifyDataSetChanged();
    }
    void recyclerViewSetup(View container){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = container.findViewById(R.id.book_case_saved_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
        bookCaseRecyclerViewAdapter = new BookCaseRecyclerViewAdapter(books,this);
        mRecyclerView.setAdapter(bookCaseRecyclerViewAdapter);
    }
    @Override
    public void onBindViewHolder(BookCaseRecyclerViewHolder holder, ArrayList<Book> books, int position) {
        final Book book = books.get(position);
        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookChaptAmount.setText("");
        if(book.getLastChapt().getStt()!=-1)
            holder.bookChaptAmount.setText("Đã tải đến chương "+ book.getLastChapt().getStt() );;
        Glide.with(context)
                .load(databaseQueries.StringToBitMap(book.getAvatar()))
                .placeholder(R.drawable.app_icon)
                .into(holder.bookAvt);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentChaptOffline(book.getId());
                sendUserToReadBookActivity(book.getId(),book.getName(),currentChapt);
            }
        });
    }
    void sendUserToReadBookActivity(String bookId,String bookName,int chapt){
        Intent intent = new Intent(getActivity(), ReadBookActivity.class);
        intent.putExtra("bookId",bookId);
        intent.putExtra("bookName",bookName);
        intent.putExtra("chapt",chapt);
        startActivity(intent);
    }
    void getCurrentChaptOffline(String bookId){
        if(databaseQueries.checkExistsUser_Book(UserInfo.id,bookId)){
            currentChapt = databaseQueries.getCurrentChapt(UserInfo.id,bookId);
        }
    }
}
