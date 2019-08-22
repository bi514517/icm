package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.R;

public class CommentActivityRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView userNameTv,timeTv,contentTv;
    public ImageView userAvatar;
    public CommentActivityRecyclerViewHolder(View itemView) {
        super(itemView);
        userNameTv = itemView.findViewById(R.id.comment_item_user_name);
        timeTv = itemView.findViewById(R.id.comment_item_time);
        contentTv = itemView.findViewById(R.id.comment_item_content);
        userAvatar = itemView.findViewById(R.id.comment_item_user_avatar);
    }
}