package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Book;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.ChapterListRecyclerViewHolder;

public class ChapterListRecyclerAdapter extends RecyclerView.Adapter<ChapterListRecyclerViewHolder>{
    ChapterListRecyclerViewAdapterInterface chapterListRecyclerViewAdapterInterface;
    private ArrayList<Chapter> chapters ;
    public ChapterListRecyclerAdapter(ArrayList<Chapter> chapters, ChapterListRecyclerViewAdapterInterface chapterListRecyclerViewAdapterInterface) {
        this.chapters = chapters;
        this.chapterListRecyclerViewAdapterInterface = chapterListRecyclerViewAdapterInterface;
    }
    @Override
    public ChapterListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ChapterListRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_chapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChapterListRecyclerViewHolder holder, int position) {
        chapterListRecyclerViewAdapterInterface.onBindViewHolder(holder,chapters,position);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }
    public interface ChapterListRecyclerViewAdapterInterface {
        void onBindViewHolder(ChapterListRecyclerViewHolder holder, ArrayList<Chapter> chapters , int position);
    }
}