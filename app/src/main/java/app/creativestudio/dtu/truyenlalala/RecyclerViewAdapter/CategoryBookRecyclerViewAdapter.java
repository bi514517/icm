package app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.CategoryBookRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Model.Category;


public class CategoryBookRecyclerViewAdapter extends RecyclerView.Adapter<CategoryBookRecyclerViewHolder>{
    CategoryBookRecyclerViewAdapterInterface CategoryBookRecyclerViewAdapterInterface;
    private ArrayList<Category> categories ;
    public CategoryBookRecyclerViewAdapter(ArrayList<Category> categories, CategoryBookRecyclerViewAdapterInterface CategoryBookRecyclerViewAdapterInterface) {
        this.categories = categories;
        this.CategoryBookRecyclerViewAdapterInterface = CategoryBookRecyclerViewAdapterInterface;
    }
    @Override
    public int getItemViewType(int position) {
        return position % 3 ;
    }
    @Override
    public CategoryBookRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                return new CategoryBookRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_category_item, parent, false));
            case 1:
                return new CategoryBookRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_category_item_1, parent, false));
            case 2:
                return new CategoryBookRecyclerViewHolder(inflater.inflate(R.layout.recycler_list_category_item_2, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(CategoryBookRecyclerViewHolder holder, int position) {
        CategoryBookRecyclerViewAdapterInterface.onBindViewHolder(holder,categories,position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    public interface CategoryBookRecyclerViewAdapterInterface {
        void onBindViewHolder(CategoryBookRecyclerViewHolder holder,ArrayList<Category> categories , int position);
    }
}
