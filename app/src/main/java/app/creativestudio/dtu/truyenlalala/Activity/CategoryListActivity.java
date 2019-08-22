package app.creativestudio.dtu.truyenlalala.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import app.creativestudio.dtu.truyenlalala.Fragment.HomeBookStoreFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.HomeUserProfileFragment;
import app.creativestudio.dtu.truyenlalala.R;

public class CategoryListActivity extends AppCompatActivity {
    HomeBookStoreFragment homeBookStoreFragment;
    TextView bookCategoryNameTextview;
    String categoryId;
    String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);
        Intent intent = getIntent();
        if(intent.hasExtra("categoryId")&&intent.hasExtra("categoryName")){
            categoryId = intent.getStringExtra("categoryId");
            categoryName = intent.getStringExtra("categoryName");
            setupView();
        }
    }
    void setupView(){
        fragmentSetup();
        //
        bookCategoryNameTextview = findViewById(R.id.book_category_name_tv);
        bookCategoryNameTextview.setText("Thể Loại : " + categoryName);
    }
    void fragmentSetup(){
        homeBookStoreFragment = new HomeBookStoreFragment();
        Bundle args = new Bundle();
        args.putString("category", categoryId);
        homeBookStoreFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.book_category_frame_container, homeBookStoreFragment);
        transaction.commit();
    }
}
