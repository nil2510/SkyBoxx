package x.skyboxx.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import x.skyboxx.Adapter.RecentOrdersAdapter;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class OrderActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> order_list = new ArrayList<>();
    SharedPreferenceClass sharedPreferenceClass;
    String userId;
    ImageView noOrders;
    FrameLayout progressFrame;
    private RecyclerView rcntorder_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        sharedPreferenceClass = new SharedPreferenceClass(OrderActivity.this);
        userId = sharedPreferenceClass.getValue_string("user_id");

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());


        progressFrame = findViewById(R.id.progressframe);
        rcntorder_recyclerview = findViewById(R.id.recyclerview);
        noOrders = findViewById(R.id.imageView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderActivity.this);
        rcntorder_recyclerview.setLayoutManager(linearLayoutManager);

        if (!userId.equals("")) {
            getRecentOrders(userId);
        } else {
            openlogindialog();
        }
        getRecentOrders(userId);
    }

    private void openlogindialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) OrderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(convertview);

        TextView lets_go = convertview.findViewById(R.id.login);
        lets_go.setOnClickListener(view -> {
            Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        final AlertDialog b = builder.show();

    }

    private void getRecentOrders(String userId) {
        progressFrame.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.myBookings, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    order_list.clear();
                    noOrders.setVisibility(View.GONE);
                    progressFrame.setVisibility(View.GONE);
                    rcntorder_recyclerview.setVisibility(View.VISIBLE);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String booking_id = jsonObject1.optString("id");
                        String book_date = jsonObject1.optString("book_date");
                        String price = jsonObject1.optString("price");
                        String company_name = jsonObject1.optString("company_name");
                        String tag = jsonObject1.optString("tag");
                        String image = jsonObject1.optString("image");
                        String service_name = jsonObject1.optString("service_name");

                        hashMap.put("booking_id", booking_id);
                        hashMap.put("book_date", book_date);
                        hashMap.put("price", price);
                        hashMap.put("company_name", company_name);
                        hashMap.put("tag", tag);
                        hashMap.put("image", image);
                        hashMap.put("service_name", service_name);

                        order_list.add(hashMap);
                    }
                    RecentOrdersAdapter recentOrdersAdapter = new RecentOrdersAdapter(OrderActivity.this, order_list);
                    rcntorder_recyclerview.setAdapter(recentOrdersAdapter);
                    progressFrame.setVisibility(View.GONE);
                } else {
                    progressFrame.setVisibility(View.GONE);
                    rcntorder_recyclerview.setVisibility(View.GONE);
                    noOrders.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(OrderActivity.this, "something went wrong !try again later", Toast.LENGTH_SHORT).show();
                progressFrame.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(OrderActivity.this, "something went wrong !try again later", Toast.LENGTH_SHORT).show();
            if (error.getMessage() == null) {
                int i = 0;
                if (i < 3) {
                    i++;
                    getRecentOrders(userId);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
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
        RequestQueue requestQueue = Volley.newRequestQueue(OrderActivity.this);
        requestQueue.add(stringRequest);
        int socketTimeout = 6000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }
}