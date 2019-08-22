package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Chapter;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.ColorRecyclerViewHolder;

public class ColorRecyclerAdapter extends RecyclerView.Adapter<ColorRecyclerViewHolder>{
    ColorRecyclerViewAdapterInterface colorRecyclerViewAdapterInterface;
    private String[] color ;
    public ColorRecyclerAdapter(String[] color, ColorRecyclerViewAdapterInterface colorRecyclerViewAdapterInterface) {
        this.color = color;
        this.colorRecyclerViewAdapterInterface = colorRecyclerViewAdapterInterface;
    }
    @Override
    public ColorRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ColorRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_color_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ColorRecyclerViewHolder holder, int position) {
        colorRecyclerViewAdapterInterface.onBindViewHolder(holder,color,position);
    }

    @Override
    public int getItemCount() {
        return color.length;
    }
    public interface ColorRecyclerViewAdapterInterface {
        void onBindViewHolder(ColorRecyclerViewHolder holder, String[] color, int position);
    }
}