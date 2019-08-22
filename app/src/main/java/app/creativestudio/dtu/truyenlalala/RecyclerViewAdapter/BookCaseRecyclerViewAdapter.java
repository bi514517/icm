package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Book;
import app.creativestudio.dtu.truyenlalala.Model.Category;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.BookCaseRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.BookStoreRecyclerViewHolder;

public class BookCaseRecyclerViewAdapter extends RecyclerView.Adapter<BookCaseRecyclerViewHolder>{
    BookCaseRecyclerViewAdapterInterface bookCaseRecyclerViewAdapterInterface;
    private ArrayList<Book> books ;
    public BookCaseRecyclerViewAdapter(ArrayList<Book> books, BookCaseRecyclerViewAdapterInterface bookCaseRecyclerViewAdapterInterface) {
        this.books = books;
        this.bookCaseRecyclerViewAdapterInterface = bookCaseRecyclerViewAdapterInterface;
    }
    @Override
    public int getItemViewType(int position) {
        return position % 2 ;
    }
    @Override
    public BookCaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                return new BookCaseRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_book_item, parent, false));
            case 1:
                return new BookCaseRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_book_item_1, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BookCaseRecyclerViewHolder holder, int position) {
        bookCaseRecyclerViewAdapterInterface.onBindViewHolder(holder,books,position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
    public interface BookCaseRecyclerViewAdapterInterface {
        void onBindViewHolder(BookCaseRecyclerViewHolder holder, ArrayList<Book> books , int position);
    }
}