package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.R;

public class ChapterListRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView chapterStt,chapterName;
    public ChapterListRecyclerViewHolder(View itemView) {
        super(itemView);
        chapterStt = itemView.findViewById(R.id.chapter_list_stt);
        chapterName = itemView.findViewById(R.id.chapter_list_name);
    }
}