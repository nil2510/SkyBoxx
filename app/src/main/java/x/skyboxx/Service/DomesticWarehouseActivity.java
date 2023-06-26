package x.skyboxx.Service;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.skyboxx.Activity.MainActivity;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;

public class DomesticWarehouseActivity extends AppCompatActivity {

    EditText customer_name,customer_address,mobile,email,website,description,origin_port,total_warehouse_space,office_space,net_weight,
            open_space,covere_space,number_of_loading_bays_required,number_of_unloading_bays_required,date_of_possession,
            period_of_intended_lease,deadline_for_quote,customer_decision_date,special_istructions;

    TextView submit_enquery;

    Button Yes,No;

    FrameLayout progress;

    String cat_id;

    RadioGroup quotation_radiogroup;
    RadioButton radioButton;
    String quotation_type = "";
    String availability = "";

    String Deadline_for_quote = "";
    String Customer_decision_date = "";

    public static boolean isEmailValid(String usremail) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher( usremail );
        return matcher.matches();
    }

    public static boolean IsValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException ignored) {
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domestic_warehouse);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });


        customer_name = findViewById(R.id.customer_name);
        customer_address = findViewById(R.id.customer_address);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        website = findViewById(R.id.website);
        description = findViewById(R.id.description);
        origin_port = findViewById(R.id.origin_port);
        total_warehouse_space = findViewById(R.id.total_warehouse_space);
        office_space = findViewById(R.id.office_space);
        net_weight = findViewById(R.id.net_weight);
        open_space = findViewById(R.id.open_space);
        covere_space = findViewById(R.id.covere_space);
        number_of_loading_bays_required = findViewById(R.id.number_of_loading_bays_required);
        number_of_unloading_bays_required = findViewById(R.id.number_of_unloading_bays_required);
        date_of_possession = findViewById(R.id.date_of_possession);
        period_of_intended_lease = findViewById(R.id.period_of_intended_lease);
        deadline_for_quote = findViewById(R.id.deadline_for_quote);
        customer_decision_date = findViewById(R.id.customer_decision_date);
        special_istructions = findViewById(R.id.special_istructions);
        submit_enquery = findViewById(R.id.submit_enquery);

        progress = findViewById(R.id.progress);

        cat_id = getIntent().getStringExtra("cat_id");
        //    service_cat = getIntent().getStringExtra("service_cat");
        //    servicename.setText(service_cat);

        Yes = findViewById(R.id.yes);
        No = findViewById(R.id.no);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                availability = "yes";

                Toast.makeText(DomesticWarehouseActivity.this, availability, Toast.LENGTH_SHORT).show();

            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                availability = "no";

                Toast.makeText(DomesticWarehouseActivity.this, availability, Toast.LENGTH_SHORT).show();

            }
        });


        quotation_radiogroup = findViewById(R.id.quotation_radiogroup);

        quotation_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkidd) {

                if (checkidd == R.id.any_vendor_radio) {
                    quotation_type = "Any Vendor";
                } else if (checkidd == R.id.platinum_radio) {
                    quotation_type = "Platinum";
                } else if (checkidd == R.id.gold_radio) {
                    quotation_type = "Gold";
                } else if (checkidd == R.id.standard_radio) {
                    quotation_type = "Silver";
                } else {
                    quotation_type = "Standard";
                }
            }
        });

        deadline_for_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                Calendar today = Calendar.getInstance();
                Calendar oneDaysAgo = (Calendar) today.clone();
                oneDaysAgo.add(Calendar.DATE, 0);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DomesticWarehouseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //DonorDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                //dob = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                Calendar calendar = Calendar.getInstance();

                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate = format.format(calendar.getTime());
                                deadline_for_quote.setText(strDate);
                                Deadline_for_quote = (strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(oneDaysAgo.getTimeInMillis());
                datePickerDialog.show();

            }
        });
        customer_decision_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                Calendar today = Calendar.getInstance();
                Calendar oneDaysAgo = (Calendar) today.clone();
                oneDaysAgo.add(Calendar.DATE, 0);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DomesticWarehouseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //DonorDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                //dob = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                Calendar calendar = Calendar.getInstance();

                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate = format.format(calendar.getTime());
                                customer_decision_date.setText(strDate);
                                Customer_decision_date = (strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(oneDaysAgo.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        submit_enquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = quotation_radiogroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);

                String Customer_name = customer_name.getText().toString();
                String Customer_address = customer_address.getText().toString();
                String Mobile = mobile.getText().toString();
                String Email = email.getText().toString();
                String Website = website.getText().toString();
                String Description = description.getText().toString();
                String Origin_port = origin_port.getText().toString();
                String Total_warehouse_space = total_warehouse_space.getText().toString();
                String Office_space = office_space.getText().toString();
                String Covered_space = net_weight.getText().toString();
                String Open_space = open_space.getText().toString();
                String Covere_space = covere_space.getText().toString();
                String Number_of_loading_bays_required = number_of_loading_bays_required.getText().toString();
                String Number_of_unloading_bays_required = number_of_unloading_bays_required.getText().toString();
                String Date_of_possession = date_of_possession.getText().toString();
                String Period_of_intended_lease = period_of_intended_lease.getText().toString();
                Deadline_for_quote = deadline_for_quote.getText().toString();
                Customer_decision_date = customer_decision_date.getText().toString();
                String Special_istructions = special_istructions.getText().toString();


                if (quotation_type.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "select your qoute", Snackbar.LENGTH_SHORT).show();

                }else if (Customer_name.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your name", Snackbar.LENGTH_SHORT).show();

                }else if (Customer_address.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your address", Snackbar.LENGTH_SHORT).show();

                }else if (Mobile.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your mobile number", Snackbar.LENGTH_SHORT).show();

                }else if (Mobile.length() < 10){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter valid mobile number", Snackbar.LENGTH_SHORT).show();

                }else if (Email.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your email", Snackbar.LENGTH_SHORT).show();

                }else if (!isEmailValid(Email)){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter valid email", Snackbar.LENGTH_SHORT).show();

                }/*else if (Website.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your website", Snackbar.LENGTH_SHORT).show();

                }else if (!IsValidUrl(Website)){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter valid url", Snackbar.LENGTH_SHORT).show();

                }*/else if (Description.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your requirement", Snackbar.LENGTH_SHORT).show();

                }else if (Origin_port.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Origin port", Snackbar.LENGTH_SHORT).show();

                }else if (Total_warehouse_space.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Total warehouse space", Snackbar.LENGTH_SHORT).show();

                }else if (Office_space.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Office space", Snackbar.LENGTH_SHORT).show();

                }else if (Covered_space.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your covered space", Snackbar.LENGTH_SHORT).show();

                }else if (Open_space.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter number of Open space", Snackbar.LENGTH_SHORT).show();

                }else if (Covere_space.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter type of covered space", Snackbar.LENGTH_SHORT).show();

                }else if (Number_of_loading_bays_required.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Number of loading bays required", Snackbar.LENGTH_SHORT).show();

                }else if (Number_of_unloading_bays_required.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Number of unloading bays required", Snackbar.LENGTH_SHORT).show();

                }else if (Date_of_possession.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Date of possession", Snackbar.LENGTH_SHORT).show();

                }else if (Period_of_intended_lease.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Period of intended lease", Snackbar.LENGTH_SHORT).show();

                }else if (Deadline_for_quote.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your deadline qoute", Snackbar.LENGTH_SHORT).show();

                }else if (Customer_decision_date.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Customer decission date", Snackbar.LENGTH_SHORT).show();

                }else if (Special_istructions.equals("")){

                    Snackbar.make(DomesticWarehouseActivity.this.findViewById(android.R.id.content), "Enter your Special istructions", Snackbar.LENGTH_SHORT).show();

                }else {

                    //  Toast.makeText(DomesticWarehouseActivity.this, "success", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(submit_enquery.getWindowToken(), 0);

                    submitenquiry(cat_id,quotation_type,Customer_name,Customer_address,Mobile,Email,Website,Description,Origin_port,
                            Total_warehouse_space, Office_space,Covered_space,Open_space,Covere_space,Number_of_loading_bays_required,
                            Number_of_unloading_bays_required,Date_of_possession,Period_of_intended_lease, Deadline_for_quote,
                            Customer_decision_date,Special_istructions);

                }
            }
        });

    }

    private void submitenquiry(String cat_id, String quotation_type, String customer_name, String customer_address,
                               String mobile, String email, String website, String description, String origin_port,
                               String total_warehouse_space, String office_space, String covered_space, String open_space,
                               String covere_space, String number_of_loading_bays_required, String number_of_unloading_bays_required,
                               String date_of_possession, String period_of_intended_lease, String deadline_for_quote,
                               String customer_decision_date, String special_istructions) {

        progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.enquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 1) {

                        String msg = jsonObject.getString("msg");

                        //   Toast.makeText(DomesticWarehouseActivity.this, msg, Toast.LENGTH_SHORT).show();

                        opensuccessdialog();

                        progress.setVisibility(View.GONE);

                    } else {

                        String msg = jsonObject.getString("msg");

                        Toast.makeText(DomesticWarehouseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(DomesticWarehouseActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(DomesticWarehouseActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cat_id",cat_id);
                params.put("customer_name",customer_name);
                params.put("customer_address", customer_address);
                params.put("phone", mobile);
                params.put("email", email);
                params.put("website", website);
                params.put("goods_desc", description);
                params.put("origin_city", origin_port);
                params.put("warehouse_space", total_warehouse_space);
                params.put("office_space", office_space);
                params.put("covered_space", covered_space);
                params.put("open_space", open_space);
                params.put("covered_space_lwh",covere_space);
                params.put("loading_bays_cnt",number_of_loading_bays_required);
                params.put("unloading_bays_cnt",number_of_unloading_bays_required);
                params.put("possession_date",date_of_possession);
                params.put("lease_period",period_of_intended_lease);
                params.put("deadline_date",deadline_for_quote);
                params.put("decission_date",customer_decision_date);
                params.put("instruction",special_istructions);
                params.put("membership", quotation_type);
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

        RequestQueue requestQueue = Volley.newRequestQueue(DomesticWarehouseActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }

    private void opensuccessdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DomesticWarehouseActivity.this);
        builder.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertview = inflater.inflate(R.layout.enquiry_form_dialog, null);
        builder.setView(convertview);

        TextView go_home = convertview.findViewById(R.id.go_home);

        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DomesticWarehouseActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        final AlertDialog b = builder.show();

    }

}