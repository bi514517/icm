package app.creativestudio.dtu.truyenlalala.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.Ref.ApiCall;
import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;
import app.creativestudio.dtu.truyenlalala.Ref.GetJsonAPI;
import app.creativestudio.dtu.truyenlalala.Ref.JsonSnapshot;
import app.creativestudio.dtu.truyenlalala.Ref.UserInfo;

public class LoadingActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    boolean isSentToHome = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        authWithGoogle();
    }
    void authWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        login(account);
        setTimeOut();
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
                    loginSuccess(resultJson,account);
                }

                @Override
                public void onFail(int responeCode, String mess) {
                    loginFail();
                }
            },new JsonSnapshot(body));
        }
        else {
            sendUserToHomeActivity();
        }
    }
    void sendUserToHomeActivity(){
        isSentToHome = true;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    void loginSuccess(JsonSnapshot resultJson,GoogleSignInAccount account){
        if(!isSentToHome) {
            int id = resultJson.child("data").toInt();
            String email = account.getEmail();
            String name = UserInfo.name;
            String avatar = UserInfo.avatar;
            try {
                name = account.getDisplayName();
                avatar = account.getPhotoUrl().toString();
            } catch (NullPointerException e) {
            }
            UserInfo.saveUserInfo(true, id, email, name, avatar);
            sendUserToHomeActivity();
        }
    }
    void loginFail(){
        if(!isSentToHome) {
            UserInfo.saveUserInfo(false,
                    0,
                    UserInfo.EMAIL,
                    UserInfo.NAME,
                    UserInfo.AVATAR);
            sendUserToHomeActivity();
        }
    }
    void setTimeOut(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                loginFail();
            }
        }, 5000);
    }
}
