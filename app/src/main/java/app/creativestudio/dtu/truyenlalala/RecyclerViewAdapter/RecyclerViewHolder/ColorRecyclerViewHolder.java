package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.R;

public class ColorRecyclerViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public ColorRecyclerViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.color_item);
    }
}