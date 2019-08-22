package app.creativestudio.dtu.truyenlalala.PagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Fragment.ReadBookContentFragment;
import app.creativestudio.dtu.truyenlalala.Model.Chapter;

public class ReadBookPagerAdapter extends FragmentStatePagerAdapter {
    ReadBookContentFragment frag;
    ArrayList<Chapter> chapters;
    String bookName;
    String bookId;
    public ReadBookPagerAdapter(FragmentManager fragmentManager, String bookId ,String bookName , ArrayList<Chapter> chapters){
        super(fragmentManager);
        /*if (fragmentManager.getFragments() != null) {
            fragmentManager.getFragments().clear();
        }*/
        this.chapters = chapters;
        this.bookName = bookName;
        this.bookId = bookId;
    }
    @Override
    public Fragment getItem(int position) {
        frag = new ReadBookContentFragment();
        Bundle args = new Bundle();
        args.putString("bookId", bookId);
        args.putString("bookName", bookName);
        args.putInt("chapterStt", chapters.get(position).getStt());
        args.putString("chapterName", chapters.get(position).getName());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        return title;
    }
}