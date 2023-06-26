package x.skyboxx.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;

public class CommonEnquiryActivity extends AppCompatActivity {
    TextInputEditText email, name, mobile, query;
    FrameLayout progress;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_enquiry);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        query = findViewById(R.id.query);
        submit = findViewById(R.id.submit_data);
        progress = findViewById(R.id.progress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        submit.setOnClickListener(v -> {
            String Email = email.getText().toString();
            String Name = name.getText().toString();
            String Mobile = mobile.getText().toString();
            String Query = query.getText().toString();

            if (Email.equals("")) {
                Toast.makeText(CommonEnquiryActivity.this, "enter your email", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                Toast.makeText(CommonEnquiryActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
            } else if (Name.equals("")) {
                Toast.makeText(CommonEnquiryActivity.this, "enter your name", Toast.LENGTH_SHORT).show();
            } else if (Mobile.equals("")) {
                Toast.makeText(CommonEnquiryActivity.this, "enter your mobile number", Toast.LENGTH_SHORT).show();
            } else if (Mobile.length() < 10) {
                Toast.makeText(CommonEnquiryActivity.this, "invalid mobile number", Toast.LENGTH_SHORT).show();
            } else if (Query.equals("")) {
                Toast.makeText(CommonEnquiryActivity.this, "enter your query", Toast.LENGTH_SHORT).show();
            } else {
                InputMethodManager imm = (InputMethodManager) CommonEnquiryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submit.getWindowToken(), 0);

                submitenquiry(Email, Name, Mobile, Query);
            }
        });
    }

    private void submitenquiry(String email, String name, String mobile, String query) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.commonEnquiry, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    progress.setVisibility(View.GONE);
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(CommonEnquiryActivity.this, msg, Toast.LENGTH_SHORT).show();
                    opensuccessdialog();
                } else if (status == 2) {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(CommonEnquiryActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                Toast.makeText(CommonEnquiryActivity.this, "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        }, error -> {
            if (error.getMessage() == null) {
                error.printStackTrace();
                Toast.makeText(CommonEnquiryActivity.this, "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                int i = 0;
                if (i < 3) {
                    i++;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("message", query);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Auth", ServerLinks.Authkey);
                return params;
            }
        };
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(CommonEnquiryActivity.this);
        requestQueue.add(stringRequest);
    }

    private void opensuccessdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonEnquiryActivity.this);
        builder.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.common_enquiry_dialog, null);
        builder.setView(convertview);

        TextView go_home = convertview.findViewById(R.id.go_home);

        go_home.setOnClickListener(v -> {
            Intent intent = new Intent(CommonEnquiryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        final AlertDialog b = builder.show();
    }
}