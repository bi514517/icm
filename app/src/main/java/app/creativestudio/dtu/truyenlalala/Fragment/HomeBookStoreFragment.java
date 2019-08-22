package app.creativestudio.dtu.truyenlalala.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;


import com.bumptech.glide.Glide;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Activity.BookDetailActivity;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.BookStoreRecyclerViewAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.BookStoreRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Model.Book;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class HomeBookStoreFragment extends Fragment implements
        BookStoreRecyclerViewAdapter.BookStoreRecyclerViewAdapterInterface,
        ApiCall.AsyncApiCall,
        View.OnClickListener,
        View.OnKeyListener, SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout = null;
    RecyclerView mRecyclerView;
    EditText searchEditText;
    ImageButton sortOptions;
    BookStoreRecyclerViewAdapter bookStoreRecyclerViewAdapter;
    ArrayList<Book> books = new ArrayList<>();
    Context context;
    GetJsonAPI getBookApi =null;
    // queries
    static final int SORTBYDATE = 0;
    static final int SORTBYVIEW = 1;
    static final int SORTBYLIKE = 2;
    static final String DESC  = "DESC";
    static final String ASC = "ASC";
    int sortBy = 0;
    String sortType = "DESC";
    String searchKeywords = "";
    String category = null;
    int numberOfPage = 20;
    int page = 1;
    //
    public HomeBookStoreFragment() {
        if( books.size() == 0 ){
            getBooks();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arg = getArguments();
        if(arg != null)
        {
            category = arg.getString("category");
            if( books.size() == 0 ){
                getBooks();
            }
        }
        return inflater.inflate(R.layout.fragment_home_book_store, container, false);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        viewSetup(view);
    }
    @Override
    public void onBindViewHolder(BookStoreRecyclerViewHolder holder,ArrayList<Book> books, int position) {
        final Book book = books.get(position);
        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookChaptAmount.setText("");
        holder.bookChaptAmount.setText("Chương :" + book.getLastChapt().toString());
        Glide.with(context)
                .load(book.getAvatar())
                .placeholder(R.drawable.app_icon)
                .into(holder.bookAvt);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToBookDetailActivity(book.getId());
            }
        });
    }
    void viewSetup(View container){
        //get UI
        getUI(container);
        // lisstener
        setListener(container);
        //
        recyclerViewSetup(container);
    }
    void setListener(View container){
        swipeRefreshLayout.setOnRefreshListener(this);
        searchEditText.setOnKeyListener(this);
        sortOptions.setOnClickListener(this);
    }
    void getUI(View container){
        swipeRefreshLayout = container.findViewById(R.id.book_store_swipe_container);
        searchEditText = container.findViewById(R.id.book_store_search_edit_text);
        sortOptions = container.findViewById(R.id.book_store_sort_option);
    }
    void recyclerViewSetup(View container){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = container.findViewById(R.id.book_store_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
        bookStoreRecyclerViewAdapter = new BookStoreRecyclerViewAdapter(books,this);
        mRecyclerView.setAdapter(bookStoreRecyclerViewAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("onScrollStateChanged", "onScrollStateChanged: ");
                if (!recyclerView.canScrollVertically(1)&&books.size()>page*numberOfPage && !getBookApi.isRunning()) {
                    page++;
                    getBooks();
                }
            }
        });
    }
    void showAlertDialog(String title,String mess){
        if(context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(mess);
            builder.setCancelable(false);
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    void displayOption(){
        if(context != null) {
        String[] colors = {"Mới nhất","Xem nhiều","Yêu thích"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("sắp xếp theo : ");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    // 0
                    case SORTBYDATE:
                        sortBy = SORTBYDATE;
                        books.clear();
                        getBooks();
                        page = 1;
                        break;
                    // 1
                    case SORTBYVIEW:
                        sortBy = SORTBYVIEW;
                        books.clear();
                        getBooks();
                        page = 1;
                        break;
                     // 2
                    case SORTBYLIKE:
                        sortBy = SORTBYLIKE;
                        books.clear();
                        getBooks();
                        page = 1;
                        break;
                }
            }
        });
        builder.show();
        }
    }

    void getBooks(){
        JSONObject body = new JSONObject();
        try {
            body.put("sortBy",sortBy);
            body.put("sortType",sortType);
            body.put("page",page);
            body.put("numberOfPage",numberOfPage);
            if( category != null ){
                body.put("category",category);
            }
            if( searchKeywords.replaceAll(" ","").length() > 0){
                body.put("searchKeywords",searchKeywords);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("home ", body.toString());
        if(getBookApi != null)
            getBookApi.interruptApiCall();
        getBookApi = GetJsonAPI.setUrl(AppConfig.SERVER + "/book/");
        getBookApi.post(this,new JsonSnapshot(body));
    }
    public int getBookIndex(String id){
        for(int i = 0;i<books.size();i++){
            if(id.equals(books.get(i).getId())){
                return i;
            }
        }
        return -1;
    }
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
        if(bookStoreRecyclerViewAdapter!=null){
            bookStoreRecyclerViewAdapter.notifyDataSetChanged();
        }
        hideRefreshLayout();
    }

    @Override
    public void onFail(int responeCode, String mess) {
        showAlertDialog("error : " +responeCode,mess);
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.book_store_sort_option:
                displayOption();
                break;
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (view.getId()){
            case R.id.book_store_search_edit_text:
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    searchKeywords = searchEditText.getText().toString();
                    books.clear();
                    getBooks();
                    searchKeywords = "";
                    // hide keyboard
                    hideKeyboard();
                    return true;
                }
        }
        return false;
    }
    void sendUserToBookDetailActivity(String bookId){
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("bookId",bookId);
        startActivity(intent);
    }
    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }
    void refreshData(){
        books.clear();
        bookStoreRecyclerViewAdapter.notifyDataSetChanged();
        getBooks();
    }
    void hideRefreshLayout(){
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
    }
}
