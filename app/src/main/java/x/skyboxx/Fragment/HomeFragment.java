package x.skyboxx.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import x.skyboxx.Activity.CommonEnquiryActivity;
import x.skyboxx.Activity.MainActivity;
import x.skyboxx.Adapter.BannerAdapter;
import x.skyboxx.Adapter.NavigationAdapter;
import x.skyboxx.Adapter.ServiceAdapter;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;


public class HomeFragment extends Fragment {
    DrawerLayout HomeDrawerLayout;
    RecyclerView nav_recyclerView;
    RecyclerView.LayoutManager mLayoutManager1;
    SharedPreferenceClass sharedPreferenceClass;
    String[] title = {"Home", "Change Password", "About Us", "Share App", "Logout"};
    int[] icon = {R.drawable.baseline_home_24, R.drawable.baseline_lock_24, R.drawable.baseline_info_24, R.drawable.baseline_share_24, R.drawable.baseline_logout_24};
    TextView service_text;
    RecyclerView banner_recycle, service_recycle;
    ArrayList<HashMap<String, String>> banner_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> service_list = new ArrayList<>();
    String username, email, password, confirm_password, user_id, booking;
    ConstraintLayout nested_parent;
    NestedScrollView nested_parent1;
    ExtendedFloatingActionButton enquire_now;
    Boolean isOpen = false;
    private ActionBarDrawerToggle toggle;
    private NavigationView HomeNavigationView;
    private Toolbar HomeToolbar;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferenceClass = new SharedPreferenceClass(getActivity());
        username = sharedPreferenceClass.getValue_string("Username");
        email = sharedPreferenceClass.getValue_string("Email");
        password = sharedPreferenceClass.getValue_string("Password");
        confirm_password = sharedPreferenceClass.getValue_string("Confirm_password");
        user_id = sharedPreferenceClass.getValue_string("user_id");
        booking = sharedPreferenceClass.getValue_string("booking");


        HomeToolbar = view.findViewById(R.id.home_toolbar);
        HomeDrawerLayout = view.findViewById(R.id.drawer_layout);
        HomeNavigationView = view.findViewById(R.id.navigation_drawer);
        nav_recyclerView = view.findViewById(R.id.nav_recycler_view);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();

        service_text = view.findViewById(R.id.service_text);
        banner_recycle = view.findViewById(R.id.banner_recycle);
        service_recycle = view.findViewById(R.id.service_recycle);
        nested_parent = view.findViewById(R.id.parent);
        nested_parent1 = view.findViewById(R.id.nested_parent);

        enquire_now = view.findViewById(R.id.enquire_now);

        toggle = new ActionBarDrawerToggle(getActivity(), HomeDrawerLayout, HomeToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        HomeDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        HomeNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            /*if (id == R.id.x) {

            } else if (id == R.id.y) {
                Intent intent = new Intent(Homepage.this, RegistrationActivity.class);
                startActivity(intent);
            } else if (id == R.id.z) {
                Intent intent = new Intent(Homepage.this, login_page.class);
                startActivity(intent);
            } else if (id == R.id.a) {
                Intent intent = new Intent(Homepage.this, login_page.class);
                startActivity(intent);
            } else if (id == R.id.b) {

            } else if (id == R.id.recycle) {
                Intent intent = new Intent(Homepage.this, RecyclerViewActivity.class);
                startActivity(intent);
            } else if (id == R.id.date) {
                Intent intent = new Intent(Homepage.this, DateRange.class);
                startActivity(intent);
            } else if (id == R.id.age) {
                Intent intent = new Intent(Homepage.this, AgeCalculator.class);
                startActivity(intent);
            }*/
            HomeDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        HomeToolbar.setNavigationOnClickListener(v -> {
            HomeDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
            if (HomeDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                HomeDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                HomeDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        nested_parent1.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                enquire_now.shrink();
            } else {
                enquire_now.extend();
            }
        });


        mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        nav_recyclerView.setLayoutManager(mLayoutManager1);
        NavigationAdapter navigationAdapter = new NavigationAdapter(getActivity(), title, icon, HomeDrawerLayout);
        nav_recyclerView.setAdapter(navigationAdapter);

        banner_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        service_recycle.setLayoutManager(gridLayoutManager);

        enquire_now.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CommonEnquiryActivity.class);
            startActivity(intent);
        });

        getbanner();
        getservices();

        setUserNavigationDrawer();

        return view;
    }

    private void getbanner() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.banners, response -> {
            try {
                banner_list.clear();
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                String img_path = jsonObject.getString("img_path");
                if (status == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    HashMap<String, String> hashmap1 = new HashMap<>();
                    hashmap1.put("image", "https://img.freepik.com/free-vector/global-transport-logistics-delivery-company_107791-651.jpg?w=996&t=st=1682452640~exp=1682453240~hmac=22d364db5776babfbe8a8b716ce7ed270e5af95699bfc8008c256612d62d4d18");
                    banner_list.add(hashmap1);
                    HashMap<String, String> hashmap2 = new HashMap<>();
                    hashmap2.put("image", "https://img.freepik.com/free-vector/global-postal-mail-delivery-illustration-isometric-thin-line-design_33099-765.jpg?w=1380&t=st=1682452784~exp=1682453384~hmac=461d35e62ebb34a6c3f32ec8f1333d7e92cfc683e01d75daa67247151451e6d7");
                    banner_list.add(hashmap2);
                    HashMap<String, String> hashmap3 = new HashMap<>();
                    hashmap3.put("image", "https://img.freepik.com/free-vector/logistics-company-supply-chain-isometric-webpage_33099-1808.jpg?w=1380&t=st=1682452805~exp=1682453405~hmac=56c2d7e02f4d848e915255e602124166b4c86a8721b163940e7550d3c31fcf58");
                    banner_list.add(hashmap3);
                    HashMap<String, String> hashmap4 = new HashMap<>();
                    hashmap4.put("image", "https://img.freepik.com/free-vector/business-logistics-service-isometric-vector-web-banner_33099-1363.jpg?w=1380&t=st=1682452845~exp=1682453445~hmac=d16994e0937f41f857ee314d66de907e7ce48927fac7f9b25de7c771f1f7864d");
                    banner_list.add(hashmap4);
                    HashMap<String, String> hashmap5 = new HashMap<>();
                    hashmap5.put("image", "https://img.freepik.com/free-vector/international-export-goods-isometric-web-banner_33099-1095.jpg?w=1380&t=st=1682452869~exp=1682453469~hmac=c67f4cb310f4eb5adf3a04b9f3e2feda358691c79a1e920450031b853d71b502");
                    banner_list.add(hashmap5);
                    HashMap<String, String> hashmap6 = new HashMap<>();
                    hashmap6.put("image", "https://img.freepik.com/free-vector/isometric-logistics-horizontal-composition-flowchart-with-view-human-characters-various-vehicles-connected-with-arrows-vector-illustration_1284-30919.jpg?w=996&t=st=1682452880~exp=1682453480~hmac=6cbcda4c337502a148f3f0f6554e4013735328914ebf2a1d2275f5ca7d665529");
                    banner_list.add(hashmap6);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jOb = jsonArray.getJSONObject(i);

                        String id = jOb.getString("id");
                        String title = jOb.getString("title");
                        String image = img_path + jOb.getString("image");

                        hashMap.put("id", id);
                        hashMap.put("image", image);
                        hashMap.put("title", title);

//                        banner_list.add(hashMap);

                    }
                    BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), banner_list);
                    banner_recycle.setAdapter(bannerAdapter);
                    setSpeedScrollTop(banner_list.size(), banner_recycle);
                } else {
                    Toast.makeText(getActivity(), "Sorry no data found", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            } catch (JSONException jsn) {
                jsn.printStackTrace();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getservices() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.services, response -> {
            try {
                service_list.clear();
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                String img_path = jsonObject.getString("img_path");
                if (status == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jOb = jsonArray.getJSONObject(i);

                        String catid = jOb.getString("id");
                        String catname = jOb.getString("name");
                        String service_type = jOb.getString("service_type");
                        String catimage = img_path + jOb.getString("image");
                        String service_charge = jOb.getString("service_charge");

                        hashMap.put("cat_id", catid);
                        hashMap.put("catname", catname);
                        hashMap.put("service_type", service_type);
                        hashMap.put("catimage", catimage);
                        hashMap.put("service_charge", service_charge);

                        service_list.add(hashMap);
                    }
                    ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity(), service_list);
                    service_recycle.setAdapter(serviceAdapter);

                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    nested_parent.setVisibility(View.VISIBLE);
                    service_text.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Sorry no data found", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    nested_parent.setVisibility(View.VISIBLE);
                    service_text.setVisibility(View.VISIBLE);
                }
            } catch (JSONException jsn) {
                jsn.printStackTrace();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                nested_parent.setVisibility(View.VISIBLE);
                service_text.setVisibility(View.VISIBLE);
            }
        }, error -> {

            error.printStackTrace();
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void setSpeedScrollTop(int length, RecyclerView banner_recycle) {
        final int speedScroll = 3000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;
            @Override
            public void run() {
                if (count < length) {
                    if (count == length - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;
                    banner_recycle.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    private void setUserNavigationDrawer() {
        HomeNavigationView.setNavigationItemSelectedListener(item -> {
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserNavigationDrawer();
    }
}