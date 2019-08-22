package app.creativestudio.dtu.truyenlalala.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Activity.CategoryListActivity;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.CategoryBookRecyclerViewAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.CategoryBookRecyclerViewHolder;
import app.creativestudio.dtu.truyenlalala.Model.Category;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;

public class HomeCategoryBookFragment extends Fragment implements CategoryBookRecyclerViewAdapter.CategoryBookRecyclerViewAdapterInterface, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout = null;
    CategoryBookRecyclerViewAdapter categoryBookRecyclerViewAdapter;
    Context context;
    ArrayList<Category> categories = new ArrayList<>();

    public HomeCategoryBookFragment() {
        getCategoriesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try{
            context = getActivity();
        }catch (NullPointerException e){}

        return inflater.inflate(R.layout.fragment_home_category_book, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        viewSetup(view);
    }
    void getCategoriesList(){
        GetJsonAPI.setUrl(AppConfig.SERVER+"/categories/").get(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime, JsonSnapshot resultJson) {
                hideRefreshLayout();
                for (JsonSnapshot json : resultJson.child("data").getArrayOfChild()) {
                    String name = json.child("name").toString();
                    String id = json.child("id").toString();
                    Category category = new Category(id,name);
                    categories.add(category);
                }
                if(categoryBookRecyclerViewAdapter!=null){
                    categoryBookRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int responeCode, String mess) {

            }
        });
    }
    void viewSetup(View container){
        swipeRefreshLayoutSetup(container);
        recyclerViewSetup(container);
    }
    void swipeRefreshLayoutSetup(View container){
        swipeRefreshLayout = container.findViewById(R.id.book_category_swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    void recyclerViewSetup(View container){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = container.findViewById(R.id.home_category_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
        categoryBookRecyclerViewAdapter = new CategoryBookRecyclerViewAdapter(categories,this);
        mRecyclerView.setAdapter(categoryBookRecyclerViewAdapter);
    }
    @Override
    public void onBindViewHolder(CategoryBookRecyclerViewHolder holder, final ArrayList<Category> categories, final int position) {
        final Category category =  categories.get(position);
        holder.categoryTextView.setText(category.getName());
        holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToCategoryListActivity(category.getId(),category.getName());
            }
        });
    }
    void sendUserToCategoryListActivity(String categoryId,String Name){
        Intent intent = new Intent(getActivity(), CategoryListActivity.class);
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("categoryName",Name);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }
    void refreshData(){
        categories.clear();
        categoryBookRecyclerViewAdapter.notifyDataSetChanged();
        getCategoriesList();
    }
    void hideRefreshLayout(){
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
    }
}