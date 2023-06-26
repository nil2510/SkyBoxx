package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class RegisterOtpActivity extends AppCompatActivity {

    EditText otpOne, otpTwo, otpThree, otpFour;
    String username, email, Mobile, password, confirm_password;
    String finalotp, otpone, otptwo, otpthree, otpfour;
    TextView verify, resend;
    String otp;
    SharedPreferenceClass sharedPrefClass;
    ProgressDialog progressDialog;
    String user_id, booking, service_id, service_charge, service_cat, image;
    FrameLayout progress;
    private TextWatcher textwatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() == 1) {

                if (otpOne.length() == 1) {

                    otpOne.setBackground(getResources().getDrawable(R.drawable.otp_text_bg_fill));
                    otpOne.setTextColor(getResources().getColor(R.color.white));
                    otpTwo.requestFocus();
                }

                if (otpTwo.length() == 1) {

                    otpTwo.setBackground(getResources().getDrawable(R.drawable.otp_text_bg_fill));
                    otpTwo.setTextColor(getResources().getColor(R.color.white));
                    otpThree.requestFocus();

                }
                if (otpThree.length() == 1) {

                    otpThree.setBackground(getResources().getDrawable(R.drawable.otp_text_bg_fill));
                    otpThree.setTextColor(getResources().getColor(R.color.white));
                    otpFour.requestFocus();
                }

                if (otpFour.length() == 1) {

                    otpFour.setBackground(getResources().getDrawable(R.drawable.otp_text_bg_fill));
                    otpFour.setTextColor(getResources().getColor(R.color.white));
                }

            } else if (editable.length() == 0) {
                if (otpFour.length() == 0) {
                    otpFour.setBackground(getResources().getDrawable(R.drawable.otp_text_bg));
                    otpFour.setTextColor(getResources().getColor(R.color.colorPrimary));
                    otpThree.requestFocus();
                }
                if (otpThree.length() == 0) {
                    otpThree.setBackground(getResources().getDrawable(R.drawable.otp_text_bg));
                    otpThree.setTextColor(getResources().getColor(R.color.colorPrimary));
                    otpTwo.requestFocus();
                }
                if (otpTwo.length() == 0) {
                    otpTwo.setBackground(getResources().getDrawable(R.drawable.otp_text_bg));
                    otpTwo.setTextColor(getResources().getColor(R.color.colorPrimary));
                    otpOne.requestFocus();
                }
                if (otpOne.length() == 0) {
                    otpOne.setBackground(getResources().getDrawable(R.drawable.otp_text_bg));
                    otpOne.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);


        otpOne = findViewById(R.id.editText1);
        otpTwo = findViewById(R.id.editText2);
        otpThree = findViewById(R.id.editText3);
        otpFour = findViewById(R.id.editText4);
        verify = findViewById(R.id.verify);
        resend = findViewById(R.id.resend);
        progress = findViewById(R.id.progress);

        progressDialog = new ProgressDialog(RegisterOtpActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent get = getIntent();


        otp = get.getStringExtra("otp");

        username = get.getStringExtra("username");
        email = get.getStringExtra("email");
        Mobile = get.getStringExtra("mobile");
        password = get.getStringExtra("password");
        confirm_password = get.getStringExtra("confirm_password");

        Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();

        sharedPrefClass = new SharedPreferenceClass(RegisterOtpActivity.this);


        booking = sharedPrefClass.getValue_string("booking");
        service_id = sharedPrefClass.getValue_string("service_id");
        service_charge = sharedPrefClass.getValue_string("service_charge");
        service_cat = sharedPrefClass.getValue_string("service_cat");
        image = sharedPrefClass.getValue_string("image");

        //    Toast.makeText(this, booking, Toast.LENGTH_SHORT).show();

        otpOne.addTextChangedListener(textwatcher1);
        otpTwo.addTextChangedListener(textwatcher1);
        otpThree.addTextChangedListener(textwatcher1);
        otpFour.addTextChangedListener(textwatcher1);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otpone = otpOne.getText().toString();
                otptwo = otpTwo.getText().toString();
                otpthree = otpThree.getText().toString();
                otpfour = otpFour.getText().toString();

                finalotp = otpone + otptwo + otpthree + otpfour;

                if (finalotp.length() == 0) {

                    Toast.makeText(RegisterOtpActivity.this, "OTP field is empty", Toast.LENGTH_SHORT).show();

                } else if (!finalotp.equals(otp)) {

                    Toast.makeText(RegisterOtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();

                } else if (finalotp.equals(otp)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(otpOne.getWindowToken(), 0);

                    registerUser(username, email, Mobile, password, confirm_password);

                }
            }
        });

    }

    private void registerUser(String username, String email, String mobile, String password, String confirm_password) {

        progressDialog.show();
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 1) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");


                        String user_id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        String email = jsonObject1.getString("email");
                        String mobile = jsonObject1.getString("mobile");
                        String profile_pic = jsonObject1.getString("profile_pic");

                        progressDialog.hide();
                        Toast.makeText(RegisterOtpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        sharedPrefClass.setValue_string("user_id", user_id);
                        sharedPrefClass.setValue_string("name", name);
                        sharedPrefClass.setValue_string("email", email);
                        sharedPrefClass.setValue_string("mobile", mobile);
                        sharedPrefClass.setValue_string("profile_pic", profile_pic);

                        if (booking.equals("login")) {

                            Intent intent = new Intent(RegisterOtpActivity.this, ServiceDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("service_id", service_id);
                            intent.putExtra("service_charge", service_charge);
                            intent.putExtra("service_cat", service_cat);
                            intent.putExtra("image", image);
                            startActivity(intent);

                        } else {

                            InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm1.hideSoftInputFromWindow(otpOne.getWindowToken(), 0);

                            startActivity(new Intent(RegisterOtpActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }

                    } else {
                        progressDialog.hide();
                        verify.setClickable(true);
                        Toast.makeText(RegisterOtpActivity.this, "username already exist", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterOtpActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    resend.setClickable(true);

                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        resend.setClickable(true);
                        Toast.makeText(RegisterOtpActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
                        if (error.getMessage() == null) {
                            error.printStackTrace();
                            int i = 0;
                            if (i < 3) {
                                i++;
                                // ResendOtp( mobile );
                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Auth", ServerLinks.Authkey);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterOtpActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }
}