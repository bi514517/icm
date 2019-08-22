package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.BookStoreRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Model.Book;


public class BookStoreRecyclerViewAdapter extends RecyclerView.Adapter<BookStoreRecyclerViewHolder>{
    BookStoreRecyclerViewAdapterInterface bookStoreRecyclerViewAdapterInterface;
    private ArrayList<Book> books ;
    public BookStoreRecyclerViewAdapter(ArrayList<Book> books, BookStoreRecyclerViewAdapterInterface bookStoreRecyclerViewAdapterInterface) {
        this.books = books;
        this.bookStoreRecyclerViewAdapterInterface = bookStoreRecyclerViewAdapterInterface;
    }
    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0)
            return 0;
        else
            return 1;
    }
    @Override
    public BookStoreRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                return new BookStoreRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_book_item, parent, false));
            case 1:
                return new BookStoreRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_book_item_1, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BookStoreRecyclerViewHolder holder, int position) {
        bookStoreRecyclerViewAdapterInterface.onBindViewHolder(holder,books,position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
    public interface BookStoreRecyclerViewAdapterInterface {
        void onBindViewHolder(BookStoreRecyclerViewHolder holder,ArrayList<Book> books , int position);
    }
}
