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
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.skyboxx.Activity.RegisterOtpActivity;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class SignUpFragment extends Fragment {
    TextInputEditText username, email, mobile, password, confirm_password;
    Button Proceed;
    ProgressDialog progressDialog;
    CheckBox checkBox;
    TextView termsandcondition;
    SharedPreferenceClass sharedPreferenceClass;
    String Terms = "unchecked";
    String user_id, booking, service_id, service_charge, service_cat, image;

    public static boolean isEmailValid(String usremail) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(usremail);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {

        String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        progressDialog = new ProgressDialog(getActivity());
        sharedPreferenceClass = new SharedPreferenceClass(getActivity());

        username = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        password = view.findViewById(R.id.password);
        confirm_password = view.findViewById(R.id.confirm_password);
        Proceed = view.findViewById(R.id.proceed);

        checkBox = view.findViewById(R.id.checkBox);
        termsandcondition = view.findViewById(R.id.termsandcondition);

        user_id = sharedPreferenceClass.getValue_string("user_id");

        booking = sharedPreferenceClass.getValue_string("booking");
        service_id = sharedPreferenceClass.getValue_string("service_id");
        service_charge = sharedPreferenceClass.getValue_string("service_charge");
        service_cat = sharedPreferenceClass.getValue_string("service_cat");
        image = sharedPreferenceClass.getValue_string("image");

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkBox.isChecked()) {
                Terms = "checked";
            } else {
                Terms = "unchecked";
            }
        });

        termsandcondition.setOnClickListener(v -> opendialog());

        Proceed.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                Terms = "checked";
            } else {
                Terms = "unchecked";
            }
            String Username = username.getText().toString();
            String Email = email.getText().toString();
            String Mobile = mobile.getText().toString();
            String Password = password.getText().toString();
            String Confirm_password = confirm_password.getText().toString();

            if (Username.equals("")) {
                username.setError("enter your username");
            } else if (Email.equals("")) {
                email.setError("enter your email");
            } else if (!isEmailValid(Email)) {
                email.setError("enter your valid email");
            } else if (Mobile.equals("")) {
                mobile.setError("enter your mobile number");
            } else if (Mobile.length() < 10) {
                mobile.setError("enter valid mobile number");
            } else if (Password.equals("")) {
                password.setError("enter your  password");
            } else if (!isValidPassword(Password)) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Password must be 6 digit with Upercase,lowercase,special character and number", Snackbar.LENGTH_LONG).show();
            } else if (!Confirm_password.matches(Password)) {
                confirm_password.setError("password did not matched");
            } else if (Terms.equals("unchecked")) {
                Toast.makeText(getActivity(), "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show();
            } else {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Proceed.getWindowToken(), 0);

                userregister(Username, Email, Mobile, Password, Confirm_password);
            }
        });
        return view;
    }

    private void opendialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.dialog_terms1, null);
        builder.setView(convertview);
        TextView terms_condition = convertview.findViewById(R.id.terms_condition);
        ProgressBar progress = convertview.findViewById(R.id.progress);
        setcontent(terms_condition);
        final AlertDialog b = builder.show();
    }

    private void setcontent(TextView terms_condition) {
        terms_condition.setText("By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages or make derivative versions. The app itself, and all the trademarks, copyright, database rights, and other intellectual property rights related to it, still belong to SkyBoxx.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"SkyBoxx is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"The SkyBoxx app stores and processes personal data that you have provided to us, to provide our Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the SkyBoxx app won’t work properly or at all.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"The app does use third-party services that declare their Terms and Conditions.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"Link to Terms and Conditions of third-party service providers used by the app\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"Google Play Services\\n\" +\n" +
                "                        \"You should be aware that there are certain things that SkyBoxx will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi or provided by your mobile network provider, but SkyBoxx cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"If you’re using the app outside of an area with Wi-Fi, you should remember that the terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third-party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"Along the same lines, SkyBoxx cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, SkyBoxx cannot accept responsibility.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"With respect to SkyBoxx’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavor to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. SkyBoxx accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"At some point, we may wish to update the app. The app is currently available on Android – the requirements for the system(and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. SkyBoxx does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"Changes to This Terms and Conditions\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"We may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Terms and Conditions on this page.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"These terms and conditions are effective as of 2023-04-20\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"Contact Us\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"If you have any questions or suggestions about our Terms and Conditions, do not hesitate to contact us at sp3319024@gmail.com.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"This Terms and Conditions page was generated by App Privacy Policy Generator");
    }


    private void userregister(String username, String email, String mobile, String password, String confirm_password) {
        progressDialog.show();
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.verifyEmail, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    progressDialog.hide();
                    String otp = jsonObject.getString("otp");
                    Toast.makeText(getActivity(), "Otp sent to your registered email", Toast.LENGTH_SHORT).show();

                    Intent in = new Intent(getActivity(), RegisterOtpActivity.class);
                    in.putExtra("username", username);
                    in.putExtra("email", email);
                    in.putExtra("mobile", mobile);
                    in.putExtra("password", password);
                    in.putExtra("confirm_password", confirm_password);
                    in.putExtra("otp", otp);
                    startActivity(in);
                } else if (status == 2) {
                    progressDialog.hide();
                    Toast.makeText(getActivity(), "Already have an account ! please sign in", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                Toast.makeText(getActivity(), "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }, error -> {
            if (error.getMessage() == null) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "something went wrong!try again later", Toast.LENGTH_SHORT).show();
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
                params.put("type", "reg");
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
}