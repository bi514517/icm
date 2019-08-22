package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.R;

public class BookStoreRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView bookName,bookAuthor,bookChaptAmount;
    public ImageView bookAvt;
    public LinearLayout layout;
    public BookStoreRecyclerViewHolder(View itemView) {
        super(itemView);
        bookName = itemView.findViewById(R.id.recycler_item_book_name);
        bookAuthor = itemView.findViewById(R.id.recycler_item_book_author);
        bookChaptAmount = itemView.findViewById(R.id.recycler_item_book_chapt_amount);
        bookAvt = itemView.findViewById(R.id.recycler_item_book_avt);
        layout = itemView.findViewById(R.id.recycler_item_book_full_layout);
    }
}