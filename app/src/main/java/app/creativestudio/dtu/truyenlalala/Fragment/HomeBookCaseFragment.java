package app.creativestudio.dtu.truyenlalala.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.creativestudio.dtu.truyenlalala.PagerAdapter.BookCasePagerAdapter;
import app.creativestudio.dtu.truyenlalala.R;

public class HomeBookCaseFragment extends Fragment {
    public HomeBookCaseFragment() {
        // Required empty public constructor
    }
    TabLayout tabLayout;
    ViewPager pager;
    private FragmentActivity context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_book_case, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        addControl(view);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            this.context=(FragmentActivity) context;
        }
    }
    private void addControl(View view) {
        pager = (ViewPager) view.findViewById(R.id.book_case_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.book_case_tablayout);
        FragmentManager manager = context.getSupportFragmentManager();
        BookCasePagerAdapter adapter = new BookCasePagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }
}
