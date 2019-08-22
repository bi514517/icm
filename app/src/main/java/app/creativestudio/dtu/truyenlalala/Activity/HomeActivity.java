package app.creativestudio.dtu.truyenlalala.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import app.creativestudio.dtu.truyenlalala.Fragment.HomeBookCaseFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.HomeBookStoreFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.HomeCategoryBookFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.HomeCommunityFragment;
import app.creativestudio.dtu.truyenlalala.Fragment.HomeUserProfileFragment;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;
import app.creativestudio.dtu.truyenlalala.Ref.SnackBar;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;
import app.creativestudio.dtu.truyenlalala.Service.RemindService;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    final int BOOK_STORE = 0;
    final int CATEGORY = 1;
    final int BOOK_CASE = 2;
    final int COMMUNITY = 3;
    final int USER_PROFILE = 4;
    //
    int currentPagerFragment = 0;
    Fragment homeBookStoreFragment = null ,homeCategoryFragment = null,homeBookCaseFragment =null,homeCommunityFragment =null,homeUserProfileFragment=null;
    private boolean doubleBackToExitPressedOnce = false;
    // user
    CircleImageView userAvatar;
    TextView userName,userEmail;
    // view
    TabLayout tabLayout;
    // google login
    private static final int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private MenuItem  loginMenuItem ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService();
        setupUI();
        googleLoginSetup();
        getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //tabLayout.setScrollPosition(currentPagerFragment,0f,true);
        //tabLayout.getTabAt(currentPagerFragment).select();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case  R.id.nav_home:
                tabLayout.getTabAt(BOOK_STORE).select();
                break;
            case  R.id.nav_community:
                tabLayout.getTabAt(COMMUNITY).select();
                break;
            case  R.id.nav_bookcase:
                tabLayout.getTabAt(BOOK_CASE).select();
                break;
            case  R.id.nav_login:
                if(UserInfo.isLogged){
                    logOut();
                }else {
                    signIn();
                }
                break;
            case  R.id.nav_share:
                break;
            case  R.id.nav_app_vote:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            login(account);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPagerFragment", currentPagerFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPagerFragment = savedInstanceState.getInt("currentPagerFragment");
    }

    void setupUI(){
        // navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View mNavigationView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        // menu nav
        Menu navMenu = navigationView.getMenu();
        loginMenuItem = navMenu.findItem(R.id.nav_login);
        //user
        userAvatar = mNavigationView.findViewById(R.id.navigation_user_avatar_imageView);
        userName = mNavigationView.findViewById(R.id.navigation_user_name);
        userEmail = mNavigationView.findViewById(R.id.navigation_user_email);
        //
        tabLayout = findViewById(R.id.home_tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //fragment
        homeBookStoreFragment = new HomeBookStoreFragment();
        homeCategoryFragment = new HomeCategoryBookFragment();
        homeBookCaseFragment = new HomeBookCaseFragment();
        homeCommunityFragment = new HomeCommunityFragment();
        homeUserProfileFragment = new HomeUserProfileFragment();
        // defaur
        selectTab(currentPagerFragment);
        //tabLayout.getTabAt(currentPagerFragment).select();
    }
    void selectTab(int tab){
        currentPagerFragment = tab;
        switch (currentPagerFragment){
            case BOOK_STORE:
                loadFragment(homeBookStoreFragment);
                break;
            case CATEGORY:
                loadFragment(homeCategoryFragment);
                break;
            case BOOK_CASE:
                homeBookCaseFragment = new HomeBookCaseFragment();
                loadFragment(homeBookCaseFragment);
                break;
            case COMMUNITY:
                homeCommunityFragment = new HomeCommunityFragment();
                loadFragment(homeCommunityFragment);
                break;
            case USER_PROFILE:
                loadFragment(homeUserProfileFragment);
                break;
        }
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back thêm 1 lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    void getUserInfo(){
        Glide.with(this)
                .load(UserInfo.avatar)
                .placeholder(R.drawable.app_icon)
                .into(userAvatar);
        userName.setText(UserInfo.name);
        userEmail.setText(UserInfo.email);
        // menu
        if(UserInfo.isLogged)
            loginMenuItem.setTitle("Logout");
        else  loginMenuItem.setTitle("Login");
    }
    void googleLoginSetup(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount account = null;
        try {
            account = completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    void login(final GoogleSignInAccount account){
        if(account != null){
            final JSONObject body = new JSONObject();
            try {
                body.put("email",account.getEmail());
                body.put("name",account.getDisplayName());
                body.put("avatar",account.getPhotoUrl());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetJsonAPI.setUrl(AppConfig.SERVER+ "/user/login").post(new ApiCall.AsyncApiCall() {
                @Override
                public void onSuccess(long resTime, JsonSnapshot resultJson) {
                    int id = resultJson.child("data").toInt();
                    String email = account.getEmail();
                    String name =  UserInfo.name;
                    String avatar =  UserInfo.avatar;
                    try{
                        name = account.getDisplayName();
                        avatar = account.getPhotoUrl().toString();
                    }catch (NullPointerException e){}
                    UserInfo.saveUserInfo(true,id,email,name,avatar);
                    getUserInfo();
                    SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"Xin chào "+ name);
                }

                @Override
                public void onFail(int responeCode, String mess) {
                    SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"kết nối với máy chủ thất bại");
                    UserInfo.saveUserInfo(false,
                            0,
                            UserInfo.EMAIL,
                            UserInfo.NAME,
                            UserInfo.AVATAR);
                    getUserInfo();
                }
            },new JsonSnapshot(body));
        }
        else {
            getUserInfo();
        }
    }
    void logOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        UserInfo.saveUserInfo(false,
                                0,
                                UserInfo.EMAIL,
                                UserInfo.NAME,
                                UserInfo.AVATAR);
                        getUserInfo();
                        SnackBar.createSnackBar(getWindow().getDecorView().getRootView(),"Đã đăng xuất");
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }

    void startService() {
        if(!isMyServiceRunning(RemindService.class))
            startService(new Intent(getBaseContext(), RemindService.class));
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
