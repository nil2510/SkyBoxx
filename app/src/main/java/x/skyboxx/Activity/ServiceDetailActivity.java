package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import x.skyboxx.Adapter.Detailsadapter;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class ServiceDetailActivity extends AppCompatActivity {

    public BottomSheetDialog mBottomSheetDialog;
    public TextView totalamount;
    TextView book_now;
    SharedPreferenceClass sharedPreferenceClass;
    String email, name, mobile, user_id;
    String service_id, service_type_id, service_cat, service_charge, imagee;
    RecyclerView details_recycle;
    ArrayList<HashMap<String, Object>> service_details_list = new ArrayList<>();
    FrameLayout progress;
    TextView location, company_name, vendor_type, service_name, service_descp, offer_validity;
    ImageView job_img;
    ConstraintLayout parent;
    String payment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        sharedPreferenceClass = new SharedPreferenceClass(ServiceDetailActivity.this);
        mBottomSheetDialog = new BottomSheetDialog(ServiceDetailActivity.this);

        email = sharedPreferenceClass.getValue_string("email");
        user_id = sharedPreferenceClass.getValue_string("user_id");
        name = sharedPreferenceClass.getValue_string("name");
        mobile = sharedPreferenceClass.getValue_string("mobile");

        service_type_id = getIntent().getStringExtra("service_type_id");
        service_id = getIntent().getStringExtra("service_id");
        service_cat = getIntent().getStringExtra("service_cat");
        service_charge = getIntent().getStringExtra("service_charge");
        imagee = getIntent().getStringExtra("image");

        parent = findViewById(R.id.parent);
        location = findViewById(R.id.location);
        company_name = findViewById(R.id.company_name);
        vendor_type = findViewById(R.id.vendor_type);
        service_name = findViewById(R.id.service_name);
        details_recycle = findViewById(R.id.details_recycle);
        service_descp = findViewById(R.id.service_descp);
        offer_validity = findViewById(R.id.offer_validity);
        book_now = findViewById(R.id.book_now);
        progress = findViewById(R.id.progress);
        job_img = findViewById(R.id.job_img);

        final LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ServiceDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        details_recycle.setLayoutManager(linearLayoutManager1);
        details_recycle.setHasFixedSize(true);
        details_recycle.setNestedScrollingEnabled(false);

        if (!user_id.equals("")) {
            goHome();
        } else {
            goLogin();
        }
        getservicedetails(service_id);
    }

    private void getservicedetails(String service_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.serviceDetail, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    parent.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    String image_path = jsonObject.getString("image_path");
                    JSONObject dataobject = jsonObject.getJSONObject("data");

                    String vendor_name = dataobject.getString("vendor_name");
                    String vendor_typee = dataobject.getString("vendor_type");
                    String service_cat = dataobject.getString("service_cat");
                    String description = dataobject.getString("description");
                    String valid_till = dataobject.getString("valid_till");
                    String origin_city_name = dataobject.getString("origin_city_name");
                    String destination_city_name = dataobject.getString("destination_city_name");
                    String image = image_path + dataobject.getString("image");

                    if (service_type_id.equals("10")) {
                        Picasso.get().load(image).placeholder(R.drawable.logosquatrans).into(job_img);
                        location.setText("Origin city : " + origin_city_name);
                        company_name.setText("Vendor Name : " + vendor_name);
                        vendor_type.setText("Vendor Type : " + vendor_typee);
                        service_name.setText("Service Name : " + service_cat);
                    } else {
                        Picasso.get().load(image).placeholder(R.drawable.logosquatrans).into(job_img);
                        location.setText("Origin city : " + origin_city_name + " to " + destination_city_name);
                        company_name.setText("Vendor Name : " + vendor_name);
                        vendor_type.setText("Vendor Type : " + vendor_typee);
                        service_name.setText("Service Name : " + service_cat);
                        service_descp.setText(description);
                        offer_validity.setText(valid_till);
                    }
                    JSONArray jsonArray = dataobject.getJSONArray("detail");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        JSONObject jOb = jsonArray.getJSONObject(i);
                        Iterator<String> iter = jOb.keys();
                        while (iter.hasNext()) {
                            try {
                                String key = iter.next();
                                hashMap.put("key", key);
                                Object value = jOb.get(key);
                                hashMap.put("value", value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ServiceDetailActivity.this, "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                        service_details_list.add(hashMap);
                    }
                    Detailsadapter detailsadapter = new Detailsadapter(ServiceDetailActivity.this, service_details_list);
                    details_recycle.setAdapter(detailsadapter);
                    book_now.setVisibility(View.VISIBLE);
                } else {
                    parent.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    book_now.setVisibility(View.GONE);
                    Toast.makeText(ServiceDetailActivity.this, "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                parent.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                book_now.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            parent.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            book_now.setVisibility(View.GONE);
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
                params.put("service_id", service_id);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceDetailActivity.this);
        requestQueue.add(stringRequest);
        int socketTimeout = 6000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }

    private void goLogin() {
        book_now.setOnClickListener(view -> {
            Intent intent = new Intent(ServiceDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            sharedPreferenceClass.setValue_string("booking", "login");
            sharedPreferenceClass.setValue_string("service_id", service_id);
            sharedPreferenceClass.setValue_string("service_cat", service_cat);
            sharedPreferenceClass.setValue_string("service_charge", service_charge);
            sharedPreferenceClass.setValue_string("image", imagee);
        });
    }

    private void goHome() {
        book_now.setOnClickListener(view -> AskPaymentInfo(user_id, service_id, service_charge));
    }

    private void AskPaymentInfo(String user_id, String service_id, String final_price) {
        mBottomSheetDialog = new BottomSheetDialog(ServiceDetailActivity.this);

        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.payment_dialog, null);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);

        final RadioButton Pay_Online = bottomSheetLayout.findViewById(R.id.online);
        final RadioButton cod = bottomSheetLayout.findViewById(R.id.cod);
        final RadioButton check = bottomSheetLayout.findViewById(R.id.check);

        totalamount = bottomSheetLayout.findViewById(R.id.amount);
        totalamount.setText("Total Amount : " + "₹ " + final_price);
        (bottomSheetLayout.findViewById(R.id.ok)).setOnClickListener(v -> {
            if (Pay_Online.isChecked()) {
                payment_type = "eBanking";
                mBottomSheetDialog.dismiss();
                bookservice(user_id, service_id, final_price, payment_type, mobile, email, name);
            } else if (cod.isChecked()) {
                Toast.makeText(ServiceDetailActivity.this, "Not Available Right Now", Toast.LENGTH_SHORT).show();
            } else if (check.isChecked()) {
                Toast.makeText(ServiceDetailActivity.this, "Not Available Right Now", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ServiceDetailActivity.this, "Select payment type", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookservice(String user_id, String service_id, String amount, String payment_type, String mobile, String email, String name) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.bookService, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    float finalamount = Float.parseFloat(amount);
                    finalamount = finalamount * 100;
                    Intent intent = new Intent(ServiceDetailActivity.this, BookingSuccessActivity.class);
                    startActivity(intent);
                    Toast.makeText(ServiceDetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ServiceDetailActivity.this, "something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(ServiceDetailActivity.this, "Time out error", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceDetailActivity.this);
        requestQueue.add(stringRequest);
        int socketTimeout = 60000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }
}