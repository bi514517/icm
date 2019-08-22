package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Comment;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.CommentActivityRecyclerViewHolder;

public class CommentActivityRecyclerViewAdapter extends RecyclerView.Adapter<CommentActivityRecyclerViewHolder>{
    CommentRecyclerViewAdapterInterface commentRecyclerViewAdapterInterface;
    private ArrayList<Comment> comments ;
    public CommentActivityRecyclerViewAdapter(ArrayList<Comment> comments, CommentRecyclerViewAdapterInterface commentRecyclerViewAdapterInterface) {
        this.comments = comments;
        this.commentRecyclerViewAdapterInterface = commentRecyclerViewAdapterInterface;
    }
    @Override
    public CommentActivityRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CommentActivityRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_comment_activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentActivityRecyclerViewHolder holder, int position) {
        commentRecyclerViewAdapterInterface.onBindViewHolder(holder,comments,position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public interface CommentRecyclerViewAdapterInterface {
        void onBindViewHolder(CommentActivityRecyclerViewHolder holder, ArrayList<Comment> comments, int position);
    }
}