package x.skyboxx.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import x.skyboxx.Adapter.JobListAdapter;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;


public class JobsFragment extends Fragment {
    public JobsFragment activity;
    NestedScrollView nested_parent;
    ConstraintLayout dept_list, constraintLayout3;
    RecyclerView job_list_recycle;
    SharedPreferenceClass sharedPreferenceClass;
    ArrayList<HashMap<String, String>> job_list = new ArrayList<>();
    Spinner location, education, experience, dept;
    String SelectedCountry = "", Countryid = "";
    String Selectededucation = "", educationid = "";
    String Experiencename = "", Experienceid = "";
    String Dept_name = "", Deptid = "";
    String user_id;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ArrayList<String> country_name = new ArrayList<String>();
    private ArrayList<String> country_id = new ArrayList<String>();
    private ArrayList<String> education_name = new ArrayList<String>();
    private ArrayList<String> education_id = new ArrayList<String>();
    private ArrayList<String> experience_name = new ArrayList<String>();
    private ArrayList<String> experience_id = new ArrayList<String>();
    private ArrayList<String> dept_name = new ArrayList<String>();
    private ArrayList<String> dept_id = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        sharedPreferenceClass = new SharedPreferenceClass(getActivity());
        user_id = sharedPreferenceClass.getValue_string("user_id");

        location = view.findViewById(R.id.location);
        education = view.findViewById(R.id.education);
        experience = view.findViewById(R.id.experience);
        dept = view.findViewById(R.id.dept);
        dept_list = view.findViewById(R.id.constraintLayout);
        constraintLayout3 = view.findViewById(R.id.constraintLayout3);

        sharedPreferenceClass = new SharedPreferenceClass(getActivity());

        nested_parent = view.findViewById(R.id.nested_parent);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();

        job_list_recycle = view.findViewById(R.id.job_list_recycle);

        country_name.add("Location Prefer");
        country_id.add("0");
        education_name.add("Education");
        education_id.add("0");
        experience_name.add("Experience");
        experience_id.add("0");
        dept_name.add("Designation");
        dept_id.add("0");

        getcountry();
        getdept();
        getjobEducation();
        getexperience();

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                SelectedCountry = parent.getItemAtPosition(position).toString();
                Countryid = country_id.get(position);
                if (SelectedCountry.equals("Location Prefer")) {
                } else {
                    job_list_recycle.setVisibility(View.GONE);
                    getjoblist(Countryid, Deptid, Experienceid, educationid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Dept_name = parent.getItemAtPosition(position).toString();
                Deptid = dept_id.get(position);
                if (Dept_name.equals("Designation")) {

                } else {
                    job_list_recycle.setVisibility(View.GONE);
                    getjoblist(Countryid, Deptid, Experienceid, educationid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Experiencename = parent.getItemAtPosition(position).toString();
                Experienceid = experience_id.get(position);
                if (Experiencename.equals("Experience")) {
                } else {
                    job_list_recycle.setVisibility(View.GONE);
                    getjoblist(Countryid, Deptid, Experienceid, educationid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Selectededucation = parent.getItemAtPosition(position).toString();
                educationid = education_id.get(position);
                if (Selectededucation.equals("Education")) {
                } else {
                    job_list_recycle.setVisibility(View.GONE);
                    getjoblist(Countryid, Deptid, Experienceid, educationid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        job_list_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        getjoblist(Countryid, Deptid, Experienceid, educationid);

        return view;
    }

    private void getcountry() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.country, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String countryid = jsonObj.getString("id");
                        String countryname = jsonObj.getString("name");
                        country_name.add(countryname);
                        country_id.add(countryid);
                    }

                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, country_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    location.setAdapter(stateadapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        },
                error -> {
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getdept() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.jobDesignations, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String designation_id = jsonObj.getString("designation_id");
                        String designation_name = jsonObj.getString("designation_name");
                        dept_name.add(designation_name);
                        dept_id.add(designation_id);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dept_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dept.setAdapter(stateadapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },
                error -> {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getexperience() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.experience, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String exp_id = jsonObj.getString("id");
                        String exp = jsonObj.getString("jobExperience");
                        experience_name.add(exp);
                        experience_id.add(exp_id);
                    }

                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, experience_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    experience.setAdapter(stateadapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        },
                error -> {
                    error.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getjobEducation() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.jobEducation, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String education_idd = jsonObj.getString("education_id");
                        String education_namee = jsonObj.getString("education_name");
                        education_name.add(education_namee);
                        education_id.add(education_idd);
                    }
                    ArrayAdapter<String> stateadapter;
                    stateadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, education_name);
                    stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    education.setAdapter(stateadapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void getjoblist(String countryid, String deptid, String experienceid, String educationid) {
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.jobs, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                String image_path = jsonObject.getString("image_path");
                if (status == 1) {
                    job_list.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jOb = jsonArray.getJSONObject(i);

                        String job_id = jOb.getString("id");
                        String job_name = jOb.getString("title");
                        String job_image = image_path + jOb.getString("image_name");
                        String job_company_name = jOb.getString("company_name");
                        String job_experience = jOb.getString("experience");
                        String job_country = jOb.getString("country");
                        String job_city = jOb.getString("city");
                        String salary = jOb.getString("salary");
                        String job_designation = jOb.getString("designation");
                        String job_educations = jOb.getString("educations");
                        String job_posted_date = jOb.getString("posted_date");
                        String description = jOb.getString("description");

                        hashMap.put("job_id", job_id);
                        hashMap.put("job_name", job_name);
                        hashMap.put("job_image", job_image);
                        hashMap.put("job_company_name", job_company_name);
                        hashMap.put("job_experience", job_experience);
                        hashMap.put("job_country", job_country);
                        hashMap.put("job_city", job_city);
                        hashMap.put("salary", salary);
                        hashMap.put("job_designation", job_designation);
                        hashMap.put("job_educations", job_educations);
                        hashMap.put("job_posted_date", job_posted_date);
                        hashMap.put("description", description);

                        job_list.add(hashMap);
                    }
                    job_list_recycle.setVisibility(View.VISIBLE);

                    JobListAdapter jobListAdapter = new JobListAdapter(getActivity(), job_list);
                    job_list_recycle.setAdapter(jobListAdapter);

                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    nested_parent.setVisibility(View.VISIBLE);
                    dept_list.setVisibility(View.VISIBLE);
                    constraintLayout3.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Sorry no data found", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    nested_parent.setVisibility(View.VISIBLE);
                    dept_list.setVisibility(View.VISIBLE);
                    constraintLayout3.setVisibility(View.VISIBLE);
                }
            } catch (JSONException jsn) {
                jsn.printStackTrace();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                nested_parent.setVisibility(View.VISIBLE);
                dept_list.setVisibility(View.VISIBLE);
                constraintLayout3.setVisibility(View.VISIBLE);

            }
        }, error -> {
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("designation_id", deptid);
                params.put("country_id", countryid);
                params.put("exp_id", experienceid);
                params.put("education_id", educationid);
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
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }
}