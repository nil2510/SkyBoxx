package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import x.skyboxx.Adapter.LoginViewPagerAdapter;
import x.skyboxx.R;
import x.skyboxx.Utils.SharedPreferenceClass;

public class LoginActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    LoginViewPagerAdapter loginViewPagerAdapter;
    SharedPreferenceClass sharedPreferenceClass;

    String userName, email, service_id,service_charge,service_cat,image,user_id,booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedPreferenceClass = new SharedPreferenceClass(LoginActivity.this);

        userName = sharedPreferenceClass.getValue_string("username");
        email = sharedPreferenceClass.getValue_string("email");
        user_id = sharedPreferenceClass.getValue_string("user_id");

        service_id = sharedPreferenceClass.getValue_string("service_id");
        service_charge = sharedPreferenceClass.getValue_string("service_charge");
        service_cat = sharedPreferenceClass.getValue_string("service_cat");
        image = sharedPreferenceClass.getValue_string("image");


        booking = sharedPreferenceClass.getValue_string("booking");

        //   Toast.makeText(this, booking, Toast.LENGTH_SHORT).show();

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        loginViewPagerAdapter = new LoginViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(loginViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}