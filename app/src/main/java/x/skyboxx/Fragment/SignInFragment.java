package x.skyboxx.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.skyboxx.Activity.BookingSuccessActivity;
import x.skyboxx.Activity.MainActivity;
import x.skyboxx.Activity.ServiceDetailActivity;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class SignInFragment extends Fragment {

    public BottomSheetDialog mBottomSheetDialog;
    public TextView totalamount;
    TextView ForgotPswd;
    TextInputEditText username, Password;
    Button Proceed;
    ProgressDialog progressDialog;
    SharedPreferenceClass sharedPreferenceClass;
    FrameLayout progress;
    String name, email, mobile, service_id, service_charge, service_cat, image, user_id, booking;
    String payment_type;


    public static boolean isValidPassword(String password) {

        String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        username = view.findViewById(R.id.email);
        Password = view.findViewById(R.id.password);
        ForgotPswd = view.findViewById(R.id.forgot_pswd);
        Proceed = view.findViewById(R.id.proceed);
        progress = view.findViewById(R.id.progress);

        progressDialog = new ProgressDialog(getActivity());
        sharedPreferenceClass = new SharedPreferenceClass(getActivity());
        mBottomSheetDialog = new BottomSheetDialog(getActivity());

        email = sharedPreferenceClass.getValue_string("email");
        user_id = sharedPreferenceClass.getValue_string("user_id");
        name = sharedPreferenceClass.getValue_string("name");
        mobile = sharedPreferenceClass.getValue_string("mobile");

        booking = sharedPreferenceClass.getValue_string("booking");
        service_id = sharedPreferenceClass.getValue_string("service_id");
        service_charge = sharedPreferenceClass.getValue_string("service_charge");
        service_cat = sharedPreferenceClass.getValue_string("service_cat");
        image = sharedPreferenceClass.getValue_string("image");


        ForgotPswd.setOnClickListener(view1 -> forgot_password_dialog());
        Proceed.setOnClickListener(v -> {

            String Username = username.getText().toString();
            String password = Password.getText().toString();
            if (Username.equals("")) {
                username.setError("enter your username");
            } else if (password.equals("")) {
                Password.setError("enter your  password");
            } else {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Proceed.getWindowToken(), 0);
                loginuser(Username, password);
            }
        });
        return view;
    }

    private void forgot_password_dialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        dialogBuilder.setCancelable(false);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_forgot_password, null);

        final EditText email = dialogView.findViewById(R.id.email);
        final EditText password = dialogView.findViewById(R.id.otp_passcode);
        final EditText new_password = dialogView.findViewById(R.id.update_password);
        final TextView cancel_dialog = dialogView.findViewById(R.id.cancel_dialog);
        final TextView forgot_password_button = dialogView.findViewById(R.id.forgot_password_button);
        final ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        forgot_password_button.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cancel_dialog.getWindowToken(), 0);
            String Email = email.getText().toString();

            if (Email.equals("")) {
                Toast.makeText(getActivity(), "Please enter your valid email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else {

                verifyemail(Email, email, password, new_password, progressBar, forgot_password_button, dialogBuilder);
            }
        });

        cancel_dialog.setOnClickListener(view -> {
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void verifyemail(String Email, EditText email, EditText password, EditText new_password,
                             ProgressBar progressBar, TextView forgot_password_button, AlertDialog dialogBuilder) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.verifyEmail, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    progressBar.setVisibility(View.GONE);
                    String otp = jsonObject.getString("otp");
                    String user_id = jsonObject.getString("user_id");
                    Toast.makeText(getActivity(), "otp is " + otp, Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    email.setEnabled(false);
                    password.setVisibility(View.VISIBLE);

                    forgot_password_button.setOnClickListener(view -> {
                        progressBar.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(forgot_password_button.getWindowToken(), 0);
                        String PassWord = password.getText().toString();
                        if (PassWord.length() == 0) {
                            Toast.makeText(getActivity(), "OTP field is empty", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else if (!PassWord.equals(otp)) {
                            Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else if (PassWord.equals(otp)) {
                            password.setEnabled(false);
                            progressBar.setVisibility(View.GONE);
                            new_password.setVisibility(View.VISIBLE);
                            forgot_password_button.setOnClickListener(view1 -> {
                                String New_password = new_password.getText().toString();
                                if (New_password.equals("")) {
                                    Toast.makeText(getActivity(), "Please enter your new password", Toast.LENGTH_SHORT).show();
                                } else if (!isValidPassword(New_password)) {
                                    Toast.makeText(getActivity(), "Password must be 6 digit with Upercase,lowercase,special character and number", Toast.LENGTH_SHORT).show();
                                } else {
                                    resetpassword(user_id, Email, email, PassWord, password, new_password, New_password, progressBar, forgot_password_button, dialogBuilder);
                                }
                            });
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    email.setEnabled(true);
                    Toast.makeText(getActivity(), "Email not registered", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Please try after sometime", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", Email);
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
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loginuser(String Username, String password) {
        progressDialog.show();
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.login, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                String msg = jsonObject.getString("msg");
                if (status == 1) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    progressDialog.hide();
                    String user_id = jsonObject1.getString("id");
                    String Emaill = jsonObject1.getString("email");
                    String Name = jsonObject1.getString("name");
                    String phone_no = jsonObject1.getString("mobile");
                    String profile_pic = jsonObject1.getString("profile_pic");

                    sharedPreferenceClass.setValue_string("user_id", user_id);
                    sharedPreferenceClass.setValue_string("email", Emaill);
                    sharedPreferenceClass.setValue_string("name", Name);
                    sharedPreferenceClass.setValue_string("mobile", phone_no);
                    sharedPreferenceClass.setValue_string("profile_pic", profile_pic);

                    if (booking.equals("login")) {
                        Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("service_charge", service_charge);
                        intent.putExtra("service_cat", service_cat);
                        intent.putExtra("image", image);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else if (status == 2) {
                    Proceed.setClickable(true);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                } else {
                    Proceed.setClickable(true);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            } catch (JSONException jsn) {
                jsn.printStackTrace();
                Toast.makeText(getActivity(), "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }, error -> {
            if (error.getMessage() == null) {
                Toast.makeText(getActivity(), "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                int i = 0;
                if (i < 3) {
                    i++;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Auth", ServerLinks.Authkey);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void resetpassword(String user_id, String Email, EditText email, String PassWord, EditText password, EditText new_password, String New_password, ProgressBar progressBar, TextView forgot_password_button, AlertDialog dialogBuilder) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.resetPassword, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(getActivity(), "password updated successfully", Toast.LENGTH_SHORT).show();
                    new_password.setEnabled(false);
                    dialogBuilder.dismiss();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Please try after sometime", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please try after sometime", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("newpassword", New_password);
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
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void bookservice(String user_id, String service_id, String amount, String payment_type, String mobile, String email, String name) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.bookService, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    String msg = jsonObject.getString("msg");
                    String order_id = jsonObject.getString("order_id");
                    String trans_id = jsonObject.getString("trans_id");
                    float finalamount = Float.parseFloat(amount);
                    finalamount = finalamount * 100;
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                progress.setVisibility(View.GONE);

            }
        }, error -> {
            error.printStackTrace();
            progress.setVisibility(View.GONE);
            if (error.getMessage() == null) {
                int i = 0;
                if (i < 3) {
                    i++;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("service_id", service_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Auth", ServerLinks.Authkey);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        int socketTimeout = 6000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }
}