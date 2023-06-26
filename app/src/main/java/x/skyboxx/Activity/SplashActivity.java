package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import x.skyboxx.R;
import x.skyboxx.Utils.SharedPreferenceClass;

public class SplashActivity extends AppCompatActivity {
    ImageView realogist;
    SharedPreferenceClass sharedPreferenceClass;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferenceClass = new SharedPreferenceClass(SplashActivity.this);
        user_id = sharedPreferenceClass.getValue_string("user_id");
        realogist = findViewById(R.id.logo);

        sharedPreferenceClass.setValue_string("login_now", "no");

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }, 500);
    }
}