package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import x.skyboxx.Fragment.HomeFragment;
import x.skyboxx.Fragment.JobsFragment;
import x.skyboxx.Fragment.ProfileFragment;
import x.skyboxx.R;
import x.skyboxx.Utils.SharedPreferenceClass;

public class MainActivity extends AppCompatActivity {
    SharedPreferenceClass sharedPreferenceClass;
    String booking, user_id;
    BottomNavigationView navbar;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isConnected = isNetworkAvailable();
        if (!isConnected) {
            startActivity(new Intent(MainActivity.this, NoInternetActivity.class));
            finish();
        }

        sharedPreferenceClass = new SharedPreferenceClass(this);
        booking = sharedPreferenceClass.getValue_string("booking");
        user_id = sharedPreferenceClass.getValue_string("user_id");

        navbar = findViewById(R.id.navigationbar);

        HomeFragment h = new HomeFragment();
        ProfileFragment p = new ProfileFragment();
        JobsFragment j = new JobsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, h).commit();
        navbar.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, h).commit();
                return true;
            }
            else if(item.getItemId()==R.id.profile){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, p).commit();
                return true;
            }
            else if(item.getItemId()==R.id.jobs){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, j).commit();
                return true;
            }
            return false;
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}