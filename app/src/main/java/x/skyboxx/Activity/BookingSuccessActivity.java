package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import x.skyboxx.R;
import x.skyboxx.Utils.SharedPreferenceClass;

public class BookingSuccessActivity extends AppCompatActivity {
    TextView go_home;
    String booking;
    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        sharedPreferenceClass = new SharedPreferenceClass(BookingSuccessActivity.this);
        booking = sharedPreferenceClass.getValue_string("booking");

        go_home = findViewById(R.id.go_home);
        go_home.setOnClickListener(v -> {
            Intent intent = new Intent(BookingSuccessActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingSuccessActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}