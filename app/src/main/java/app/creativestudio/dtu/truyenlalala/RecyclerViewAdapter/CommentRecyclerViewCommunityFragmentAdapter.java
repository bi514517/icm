package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Comment;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.CommentRecyclerViewCommunityFragmentHolder;

public class CommentRecyclerViewCommunityFragmentAdapter extends RecyclerView.Adapter<CommentRecyclerViewCommunityFragmentHolder>{
    CommentRecyclerViewAdapterInterface commentRecyclerViewAdapterInterface;
    private ArrayList<Comment> comments ;
    public CommentRecyclerViewCommunityFragmentAdapter(ArrayList<Comment> comments, CommentRecyclerViewAdapterInterface commentRecyclerViewAdapterInterface) {
        this.comments = comments;
        this.commentRecyclerViewAdapterInterface = commentRecyclerViewAdapterInterface;
    }
    @Override
    public CommentRecyclerViewCommunityFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CommentRecyclerViewCommunityFragmentHolder(inflater.inflate(R.layout.recycler_list_comment_community_fragment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentRecyclerViewCommunityFragmentHolder holder, int position) {
        commentRecyclerViewAdapterInterface.onBindViewHolder(holder,comments,position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public interface CommentRecyclerViewAdapterInterface {
        void onBindViewHolder(CommentRecyclerViewCommunityFragmentHolder holder, ArrayList<Comment> comments , int position);
    }
}