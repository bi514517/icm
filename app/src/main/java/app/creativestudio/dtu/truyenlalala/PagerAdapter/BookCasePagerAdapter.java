package app.creativestudio.dtu.truyenlalala.PagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Fragment.BookCaseReadingFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.BookCaseSavedFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.ReadBookContentFragment;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;

public class BookCasePagerAdapter extends FragmentStatePagerAdapter {
    public BookCasePagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BookCaseReadingFragment();
            case 1:
                return new BookCaseSavedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Đang đọc";
                break;
            case 1:
                title = "Tủ sách";
                break;
        }
        return title;
    }
}