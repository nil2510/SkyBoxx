package x.skyboxx.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import x.skyboxx.Adapter.ServiceListAdapter;
import x.skyboxx.R;
import x.skyboxx.Service.AirFreightDoorActivity;
import x.skyboxx.Service.AirFreightPortActivity;
import x.skyboxx.Service.DomesticServiceListActivity;
import x.skyboxx.Service.DomesticWarehouseActivity;
import x.skyboxx.Service.InterCityActivity;
import x.skyboxx.Service.InterNationalActivity;
import x.skyboxx.Service.OceanFreightFclDoorActivity;
import x.skyboxx.Service.OceanFreightFclPortActivity;
import x.skyboxx.Service.OceanFreightLclDoorActivity;
import x.skyboxx.Service.OceanFreightLclPortActivity;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class ServiceListActivity extends AppCompatActivity {

    NestedScrollView nested_parent;
    ConstraintLayout service_list, constraintLayout3, constraintLayout4;
    RecyclerView service_list_recycle;
    SharedPreferenceClass sharedPreferenceClass;
    ArrayList<HashMap<String, String>> cat_service_list = new ArrayList<>();
    Spinner origin_country, origin_city, service;
    Spinner destination_country, destination_city;
    String SelectedOriginCountry, OriginCountryid;
    String SelectedDestinationCountry, DestinationCountryid;
    String OriginSelectedcity, Origincityid = "";
    String DestinationSelectedcity, Destinationcityid = "";
    String Service_name = "", Serviceid = "";
    String catname, user_id, service_cat;
    TextView service_Name;
    ExtendedFloatingActionButton enquire_now;
    Boolean isOpen = false;
    private Toolbar HomeToolbar;
    private FrameLayout mShimmerViewContainer;
    private ArrayList<String> origin_country_name = new ArrayList<String>();
    private ArrayList<String> origin_country_id = new ArrayList<String>();
    private ArrayList<String> destination_country_name = new ArrayList<String>();
    private ArrayList<String> destination_country_id = new ArrayList<String>();
    private ArrayList<String> origin_city_name = new ArrayList<String>();
    private ArrayList<String> origin_city_id = new ArrayList<String>();
    private ArrayList<String> destination_city_name = new ArrayList<String>();
    private ArrayList<String> destination_city_id = new ArrayList<String>();
    private ArrayList<String> service_name = new ArrayList<String>();
    private ArrayList<String> service_id = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        sharedPreferenceClass = new SharedPreferenceClass(ServiceListActivity.this);
        user_id = sharedPreferenceClass.getValue_string("user_id");

        catname = getIntent().getStringExtra("catname");
        Serviceid = getIntent().getStringExtra("cat_id");
        service_Name = findViewById(R.id.servicename);

        service_Name.setText(catname);

        origin_country = findViewById(R.id.country);
        origin_city = findViewById(R.id.city);
        service = findViewById(R.id.service);
        service_list = findViewById(R.id.service_list);
        constraintLayout3 = findViewById(R.id.constraintLayout3);
        constraintLayout4 = findViewById(R.id.constraintLayout4);
        destination_country = findViewById(R.id.desti_country);
        destination_city = findViewById(R.id.desti_city);
        nested_parent = findViewById(R.id.nested_parent);

        mShimmerViewContainer = findViewById(R.id.progress);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        service_list_recycle = findViewById(R.id.service_list_recycle);
        enquire_now = findViewById(R.id.enquire_now);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        nested_parent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                if (isOpen) {
                    isOpen = false;
                }
                enquire_now.shrink();
            } else {
                enquire_now.extend();
            }
        });
        enquire_now.setOnClickListener(v -> {
            if (isOpen) {
                isOpen = false;
            } else {
                isOpen = true;
            }
        });

        origin_country_name.add("ORIGIN COUNTRY");
        origin_country_id.add("0");

        destination_country_name.add("DESTINATION COUNTRY");
        destination_country_id.add("0");

        origin_city_name.add("ORIGIN CITY");
        origin_city_id.add("0");

        destination_city_name.add("DESTINATION CITY");
        destination_city_id.add("0");

        service_name.add("SELECT SERVICE");
        service_id.add("0");

        getorigincountry();
        getservice();

        origin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                SelectedOriginCountry = parent.getItemAtPosition(position).toString();
                OriginCountryid = origin_country_id.get(position);
                origin_city_name.clear();
                origin_city_id.clear();
                if (!SelectedOriginCountry.equals("ORIGIN COUNTRY")) {
                    getorigincity(OriginCountryid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        origin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                OriginSelectedcity = parent.getItemAtPosition(position).toString();
                Origincityid = origin_city_id.get(position);

                destination_country_name.clear();
                destination_country_id.clear();
                if (!OriginSelectedcity.equals("ORIGIN CITY")) {
                    getdestinationcountry(Serviceid, OriginCountryid);
                    constraintLayout4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        destination_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                SelectedDestinationCountry = parent.getItemAtPosition(position).toString();
                DestinationCountryid = destination_country_id.get(position);
                destination_city_name.clear();
                destination_city_id.clear();
                if (!SelectedDestinationCountry.equals("DESTINATION COUNTRY")) {
                    getdestinationcity(DestinationCountryid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        destination_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                DestinationSelectedcity = parent.getItemAtPosition(position).toString();
                Destinationcityid = destination_city_id.get(position);
                if (!DestinationSelectedcity.equals("DESTINATION CITY")) {
                    getservicelist(user_id, Serviceid, Origincityid, Destinationcityid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                Service_name = parent.getItemAtPosition(position).toString();
                Serviceid = service_id.get(position);
                if (Service_name.equals("SELECT SERVICE")) {
                } else if (Serviceid.equals("10")) {
                    constraintLayout3.setVisibility(View.GONE);
                    constraintLayout4.setVisibility(View.GONE);
                    service_list_recycle.setVisibility(View.GONE);
                    getservicelist(user_id, Serviceid, Origincityid, Destinationcityid);
                    service_Name.setText(Service_name);
                } else {
                    service_list_recycle.setVisibility(View.GONE);
                    getservicelist(user_id, Serviceid, Origincityid, Destinationcityid);
                    constraintLayout3.setVisibility(View.VISIBLE);
                    constraintLayout4.setVisibility(View.VISIBLE);
                    service_Name.setText(Service_name);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        service_list_recycle.setLayoutManager(new LinearLayoutManager(ServiceListActivity.this, RecyclerView.VERTICAL, false));
        service_list_recycle.setHasFixedSize(true);
        service_list_recycle.setNestedScrollingEnabled(false);
        getservicelist(user_id, Serviceid, Origincityid, Destinationcityid);
    }

    private void getorigincountry() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.country, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String countryid = jsonObj.getString("id");
                        String countryname = jsonObj.getString("name");
                        origin_country_name.add(countryname);
                        origin_country_id.add(countryid);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(ServiceListActivity.this, android.R.layout.simple_spinner_item, origin_country_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    origin_country.setAdapter(stateadapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ServiceListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceListActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getdestinationcountry(String serviceid, String originCountryid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.country, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String countryid = jsonObj.getString("id");
                        String countryname = jsonObj.getString("name");
                        destination_country_name.add(countryname);
                        destination_country_id.add(countryid);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(ServiceListActivity.this, android.R.layout.simple_spinner_item, destination_country_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    destination_country.setAdapter(stateadapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ServiceListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
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
                params.put("service_id", serviceid);
                params.put("country_id", originCountryid);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceListActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getorigincity(String OriginCountryid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.city, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    origin_city_name.add("ORIGIN CITY");
                    origin_city_id.add("0");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String cityid = jsonObj.getString("id");
                        String cityname = jsonObj.getString("name");
                        origin_city_name.add(cityname);
                        origin_city_id.add(cityid);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(ServiceListActivity.this, android.R.layout.simple_spinner_item, origin_city_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    origin_city.setAdapter(stateadapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ServiceListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
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
                params.put("country_id", OriginCountryid);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceListActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getdestinationcity(String DestinationCountryid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.city, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    destination_city_name.add("DESTINATION CITY");
                    destination_city_id.add("0");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String cityid = jsonObj.getString("id");
                        String cityname = jsonObj.getString("name");
                        destination_city_name.add(cityname);
                        destination_city_id.add(cityid);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(ServiceListActivity.this, android.R.layout.simple_spinner_item, destination_city_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    destination_city.setAdapter(stateadapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ServiceListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
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
                params.put("country_id", DestinationCountryid);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceListActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getservice() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.services, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    String img_path = jsonObject.getString("img_path");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jOb = jsonArray.getJSONObject(i);
                        String catid = jOb.getString("id");
                        String catname = jOb.getString("name");
                        String service_type = jOb.getString("service_type");
                        String catimage = img_path + jOb.getString("image");
                        service_name.add(catname);
                        service_id.add(catid);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(ServiceListActivity.this, android.R.layout.simple_spinner_item, service_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    service.setAdapter(stateadapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ServiceListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceListActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getservicelist(String user_id, String Serviceid, String origincityid, String destinationcityid) {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.serviceList, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    String image_path = jsonObject.getString("image_path");
                    cat_service_list.clear();
                    service_list_recycle.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jOb = jsonArray.getJSONObject(i);

                        String service_id = jOb.getString("id");
                        String vendor_id = jOb.getString("vendor_id");
                        String service_type_id = jOb.getString("service_type_id");
                        String origin_country = jOb.getString("origin_country");
                        String origin_city = jOb.getString("origin_city");
                        String destination_country = jOb.getString("destination_country");
                        String destination_city = jOb.getString("destination_city");
                        String truck_type = jOb.getString("truck_type");
                        String payload_capacity = jOb.getString("payload_capacity");
                        String dimension = jOb.getString("dimension");
                        String rate_one_way = jOb.getString("rate_one_way");
                        String other_charges = jOb.getString("other_charges");
                        String transition_time = jOb.getString("transition_time");
                        String image = image_path + jOb.getString("image");
                        String vendor_name = jOb.getString("vendor_name");
                        String vendor_type = jOb.getString("vendor_type");
                        String service_charge = jOb.getString("service_charge");
                        service_cat = jOb.getString("service_cat");
                        String origin_city_name = jOb.getString("origin_city_name");
                        String destination_city_name = jOb.getString("destination_city_name");
                        String location_name = jOb.getString("location_name");
                        String buildup_area = jOb.getString("buildup_area");
                        String rental_sqft_month = jOb.getString("rental_sqft_month");

                        hashMap.put("service_id", service_id);
                        hashMap.put("vendor_id", vendor_id);
                        hashMap.put("service_type_id", service_type_id);
                        hashMap.put("origin_country", origin_country);
                        hashMap.put("origin_city", origin_city);
                        hashMap.put("destination_country", destination_country);
                        hashMap.put("destination_city", destination_city);
                        hashMap.put("truck_type", truck_type);
                        hashMap.put("payload_capacity", payload_capacity);
                        hashMap.put("dimension", dimension);
                        hashMap.put("rate_one_way", rate_one_way);
                        hashMap.put("other_charges", other_charges);
                        hashMap.put("transition_time", transition_time);
                        hashMap.put("image", image);
                        hashMap.put("vendor_name", vendor_name);
                        hashMap.put("vendor_type", vendor_type);
                        hashMap.put("service_cat", service_cat);
                        hashMap.put("service_charge", service_charge);
                        hashMap.put("origin_city_name", origin_city_name);
                        hashMap.put("destination_city_name", destination_city_name);
                        hashMap.put("location_name", location_name);
                        hashMap.put("buildup_area", buildup_area);
                        hashMap.put("rental_sqft_month", rental_sqft_month);

                        cat_service_list.add(hashMap);
                    }
                    ServiceListAdapter serviceListAdapter = new ServiceListAdapter(ServiceListActivity.this, cat_service_list, Serviceid);
                    service_list_recycle.setAdapter(serviceListAdapter);

                    mShimmerViewContainer.setVisibility(View.GONE);
                    nested_parent.setVisibility(View.VISIBLE);
                    service_list.setVisibility(View.VISIBLE);

                    if (Serviceid.equals("1")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, InterNationalActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("2")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, DomesticServiceListActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("3")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, InterCityActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("4")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, AirFreightDoorActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("5")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, AirFreightPortActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("6")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightLclPortActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("7")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightLclDoorActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("8")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightFclPortActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("9")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightFclDoorActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("10")) {
                        constraintLayout3.setVisibility(View.GONE);
                        constraintLayout4.setVisibility(View.GONE);
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, DomesticWarehouseActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else {
                        enquire_now.setVisibility(View.GONE);
                        enquire_now.setOnClickListener(view -> Toast.makeText(ServiceListActivity.this, "Service not available", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Toast.makeText(ServiceListActivity.this, "Sorry no data found", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    nested_parent.setVisibility(View.VISIBLE);
                    service_list.setVisibility(View.VISIBLE);
                    service_list_recycle.setVisibility(View.GONE);
                    constraintLayout3.setVisibility(View.VISIBLE);
                    if (Serviceid.equals("1")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, InterNationalActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("2")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, DomesticServiceListActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("3")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, InterCityActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("4")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, AirFreightDoorActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("5")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, AirFreightPortActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("6")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightLclPortActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("7")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightLclDoorActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("8")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightFclPortActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("9")) {
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, OceanFreightFclDoorActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else if (Serviceid.equals("10")) {
                        constraintLayout3.setVisibility(View.GONE);
                        constraintLayout4.setVisibility(View.GONE);
                        enquire_now.setOnClickListener(view -> {
                            Intent intent = new Intent(ServiceListActivity.this, DomesticWarehouseActivity.class);
                            intent.putExtra("cat_id", Serviceid);
                            intent.putExtra("service_cat", service_cat);
                            startActivity(intent);
                        });
                    } else {
                        enquire_now.setVisibility(View.GONE);
                        enquire_now.setOnClickListener(view -> Toast.makeText(ServiceListActivity.this, "Service not available", Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (JSONException jsn) {
                jsn.printStackTrace();
                Toast.makeText(ServiceListActivity.this, "something went wrong!try again later", Toast.LENGTH_SHORT).show();

                enquire_now.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.GONE);
                nested_parent.setVisibility(View.VISIBLE);
                service_list.setVisibility(View.VISIBLE);
                service_list_recycle.setVisibility(View.GONE);
                constraintLayout3.setVisibility(View.VISIBLE);
            }
        }, error -> {
            error.printStackTrace();
            enquire_now.setVisibility(View.GONE);
            mShimmerViewContainer.setVisibility(View.GONE);
            nested_parent.setVisibility(View.VISIBLE);
            service_list.setVisibility(View.VISIBLE);
            constraintLayout3.setVisibility(View.VISIBLE);
            service_list_recycle.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_type_id", Serviceid);
                params.put("origin_city", origincityid);
                params.put("destination_city", destinationcityid);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceListActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}