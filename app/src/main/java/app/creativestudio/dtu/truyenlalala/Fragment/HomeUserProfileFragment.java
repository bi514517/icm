package app.creativestudio.dtu.truyenlalala.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.creativestudio.dtu.truyenlalala.R;

public class HomeUserProfileFragment extends Fragment {
    public HomeUserProfileFragment() {
    }
    ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_user_profile, container, false);
    }

}
