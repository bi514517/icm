package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.R;

public class CommentRecyclerViewCommunityFragmentHolder extends RecyclerView.ViewHolder {
    public TextView userNameTv,bookNameTv,timeTv,contentTv;
    public ImageView userAvatar,bookAvatar;
    public CommentRecyclerViewCommunityFragmentHolder(View itemView) {
        super(itemView);
        userNameTv = itemView.findViewById(R.id.comment_item_user_name);
        bookNameTv = itemView.findViewById(R.id.comment_item_book_name);
        timeTv = itemView.findViewById(R.id.comment_item_time);
        contentTv = itemView.findViewById(R.id.comment_item_content);
        userAvatar = itemView.findViewById(R.id.comment_item_user_avatar);
        bookAvatar = itemView.findViewById(R.id.comment_item_book_avatar);
    }
}