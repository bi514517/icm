package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.R;

public class CategoryBookRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView categoryTextView;
    public LinearLayout categoryLayout;
    public CategoryBookRecyclerViewHolder(View itemView) {
        super(itemView);
        categoryTextView = itemView.findViewById(R.id.recycler_item_category_tv_item);
        categoryLayout = itemView.findViewById(R.id.recycler_item_category_layout_item);
    }
}