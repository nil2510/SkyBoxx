package x.skyboxx.Service;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.skyboxx.Activity.MainActivity;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;

public class InterNationalActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int REQUEST_CODE_DOC = 1;
    EditText customer_name, customer_address, mobile, email, website, description, origin_address, destination_address,
            shipment_readyness_date, deadline_for_quote, customer_decision_date, special_istructions;
    TextView submit_enquery;
    FrameLayout progress;
    EditText box_number1, length_cm1, width_cm1, height_cm1, gross_weight_cm1, no_of_box1;
    String Box_number1, Length_cm1, Width_cm1, Height_cm1, Gross_weight_cm1, No_of_box1;
    EditText box_number2, length_cm2, width_cm2, height_cm2, gross_weight_cm2, no_of_box2;
    String Box_number2, Length_cm2, Width_cm2, Height_cm2, Gross_weight_cm2, No_of_box2;
    EditText box_number3, length_cm3, width_cm3, height_cm3, gross_weight_cm3, no_of_box3;
    String Box_number3, Length_cm3, Width_cm3, Height_cm3, Gross_weight_cm3, No_of_box3;
    EditText box_number4, length_cm4, width_cm4, height_cm4, gross_weight_cm4, no_of_box4;
    String Box_number4, Length_cm4, Width_cm4, Height_cm4, Gross_weight_cm4, No_of_box4;
    LinearLayout parent2, parent3, parent4;
    TextView add_more;
    String filePath1 = "";
    int file_size2;
    TextView file_name, upload_box;
    String cat_id;
    RadioGroup quotation_radiogroup;
    RadioButton radioButton;
    String quotation_type = "";
    String Shipment_readyness_date = "";
    String Deadline_for_quote = "";
    String Customer_decision_date = "";

    public static boolean isEmailValid(String usremail) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(usremail);
        return matcher.matches();
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getExternalFilesDir("") + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
//            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_national);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        customer_name = findViewById(R.id.customer_name);
        customer_address = findViewById(R.id.customer_address);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        website = findViewById(R.id.website);
        description = findViewById(R.id.description);
        origin_address = findViewById(R.id.origin_address);
        destination_address = findViewById(R.id.destination_address);
        shipment_readyness_date = findViewById(R.id.shipment_readyness_date);
        deadline_for_quote = findViewById(R.id.deadline_for_quote);
        customer_decision_date = findViewById(R.id.customer_decision_date);
        special_istructions = findViewById(R.id.special_istructions);
        submit_enquery = findViewById(R.id.submit_enquery);

        progress = findViewById(R.id.progress);

        box_number1 = findViewById(R.id.box_number1);
        length_cm1 = findViewById(R.id.length_cm1);
        width_cm1 = findViewById(R.id.width_cm1);
        height_cm1 = findViewById(R.id.height_cm1);
        gross_weight_cm1 = findViewById(R.id.gross_weight_cm1);
        no_of_box1 = findViewById(R.id.no_of_box1);

        box_number2 = findViewById(R.id.box_number2);
        length_cm2 = findViewById(R.id.length_cm2);
        width_cm2 = findViewById(R.id.width_cm2);
        height_cm2 = findViewById(R.id.height_cm2);
        gross_weight_cm2 = findViewById(R.id.gross_weight_cm2);
        no_of_box2 = findViewById(R.id.no_of_box2);

        box_number3 = findViewById(R.id.box_number3);
        length_cm3 = findViewById(R.id.length_cm3);
        width_cm3 = findViewById(R.id.width_cm3);
        height_cm3 = findViewById(R.id.height_cm3);
        gross_weight_cm3 = findViewById(R.id.gross_weight_cm3);
        no_of_box3 = findViewById(R.id.no_of_box3);

        box_number4 = findViewById(R.id.box_number4);
        length_cm4 = findViewById(R.id.length_cm4);
        width_cm4 = findViewById(R.id.width_cm4);
        height_cm4 = findViewById(R.id.height_cm4);
        gross_weight_cm4 = findViewById(R.id.gross_weight_cm4);
        no_of_box4 = findViewById(R.id.no_of_box4);

        parent2 = findViewById(R.id.parent2);
        parent3 = findViewById(R.id.parent3);
        parent4 = findViewById(R.id.parent4);

        add_more = findViewById(R.id.add_more);

        upload_box = findViewById(R.id.upload_box);
        file_name = findViewById(R.id.file_name);

        cat_id = getIntent().getStringExtra("cat_id");

        TextView servicename = findViewById(R.id.servicename);
        String service_cat = getIntent().getStringExtra("service_cat");
        servicename.setText(service_cat);

        quotation_radiogroup = findViewById(R.id.quotation_radiogroup);

        quotation_radiogroup.setOnCheckedChangeListener((radioGroup, checkidd) -> {

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
        });

        requestStoragePermission();
        requestWritePermission();

        upload_box.setOnClickListener(v -> {

            try {
                Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
                intentPDF.setType("application/pdf");
                intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intentPDF, "Select Picture"), REQUEST_CODE_DOC);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        });

        progress = findViewById(R.id.progress);

        add_more.setOnClickListener(view -> {

            parent2.setVisibility(View.VISIBLE);

            add_more.setOnClickListener(view12 -> {

                parent3.setVisibility(View.VISIBLE);

                add_more.setOnClickListener(view1 -> {

                    parent4.setVisibility(View.VISIBLE);
                    add_more.setVisibility(View.GONE);
                });

            });

        });

        shipment_readyness_date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            Calendar today = Calendar.getInstance();
            Calendar oneDaysAgo = (Calendar) today.clone();
            oneDaysAgo.add(Calendar.DATE, 0);
            DatePickerDialog datePickerDialog = new DatePickerDialog(InterNationalActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(calendar.getTime());
                        shipment_readyness_date.setText(strDate);
                        Shipment_readyness_date = (strDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(oneDaysAgo.getTimeInMillis());
            datePickerDialog.show();

        });
        deadline_for_quote.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            Calendar today = Calendar.getInstance();
            Calendar oneDaysAgo = (Calendar) today.clone();
            oneDaysAgo.add(Calendar.DATE, 0);
            DatePickerDialog datePickerDialog = new DatePickerDialog(InterNationalActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(calendar.getTime());
                        deadline_for_quote.setText(strDate);
                        Deadline_for_quote = (strDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(oneDaysAgo.getTimeInMillis());
            datePickerDialog.show();
        });
        customer_decision_date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            Calendar today = Calendar.getInstance();
            Calendar oneDaysAgo = (Calendar) today.clone();
            oneDaysAgo.add(Calendar.DATE, 0);
            DatePickerDialog datePickerDialog = new DatePickerDialog(InterNationalActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(calendar.getTime());
                        customer_decision_date.setText(strDate);
                        Customer_decision_date = (strDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(oneDaysAgo.getTimeInMillis());
            datePickerDialog.show();

        });

        submit_enquery.setOnClickListener(view -> {

            int selectedId = quotation_radiogroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedId);

            String Customer_name = customer_name.getText().toString();
            String Customer_address = customer_address.getText().toString();
            String Mobile = mobile.getText().toString();
            String Email = email.getText().toString();
            String Website = website.getText().toString();
            String Description = description.getText().toString();
            String Origin_address = origin_address.getText().toString();
            String Destination_address = destination_address.getText().toString();
            Shipment_readyness_date = shipment_readyness_date.getText().toString();
            Deadline_for_quote = deadline_for_quote.getText().toString();
            Customer_decision_date = customer_decision_date.getText().toString();
            String Special_istructions = special_istructions.getText().toString();

            Box_number1 = box_number1.getText().toString();
            Box_number2 = box_number2.getText().toString();
            Box_number3 = box_number3.getText().toString();
            Box_number4 = box_number4.getText().toString();

            Length_cm1 = length_cm1.getText().toString();
            Length_cm2 = length_cm2.getText().toString();
            Length_cm3 = length_cm3.getText().toString();
            Length_cm4 = length_cm4.getText().toString();

            Width_cm1 = width_cm1.getText().toString();
            Width_cm2 = width_cm2.getText().toString();
            Width_cm3 = width_cm3.getText().toString();
            Width_cm4 = width_cm4.getText().toString();

            Height_cm1 = height_cm1.getText().toString();
            Height_cm2 = height_cm2.getText().toString();
            Height_cm3 = height_cm3.getText().toString();
            Height_cm4 = height_cm4.getText().toString();

            Gross_weight_cm1 = gross_weight_cm1.getText().toString();
            Gross_weight_cm2 = gross_weight_cm2.getText().toString();
            Gross_weight_cm3 = gross_weight_cm3.getText().toString();
            Gross_weight_cm4 = gross_weight_cm4.getText().toString();

            No_of_box1 = no_of_box1.getText().toString();
            No_of_box2 = no_of_box2.getText().toString();
            No_of_box3 = no_of_box3.getText().toString();
            No_of_box4 = no_of_box4.getText().toString();

            JSONArray array = new JSONArray();
            JSONObject jsonParam1 = new JSONObject();
            JSONObject jsonParam2 = new JSONObject();
            JSONObject jsonParam3 = new JSONObject();
            JSONObject jsonParam4 = new JSONObject();
            try {
                jsonParam1.put("box_number", Box_number1);
                jsonParam1.put("box_length", Length_cm1);
                jsonParam1.put("box_width", Width_cm1);
                jsonParam1.put("box_height", Height_cm1);
                jsonParam1.put("box_weight", Gross_weight_cm1);
                jsonParam1.put("no_of_box", No_of_box1);

                jsonParam2.put("box_number", Box_number2);
                jsonParam2.put("box_length", Length_cm2);
                jsonParam2.put("box_width", Width_cm2);
                jsonParam2.put("box_height", Height_cm2);
                jsonParam2.put("box_weight", Gross_weight_cm2);
                jsonParam2.put("no_of_box", No_of_box2);

                jsonParam3.put("box_number", Box_number3);
                jsonParam3.put("box_length", Length_cm3);
                jsonParam3.put("box_width", Width_cm3);
                jsonParam3.put("box_height", Height_cm3);
                jsonParam3.put("box_weight", Gross_weight_cm3);
                jsonParam3.put("no_of_box", No_of_box3);

                jsonParam4.put("box_number", Box_number4);
                jsonParam4.put("box_length", Length_cm4);
                jsonParam4.put("box_width", Width_cm4);
                jsonParam4.put("box_height", Height_cm4);
                jsonParam4.put("box_weight", Gross_weight_cm4);
                jsonParam4.put("no_of_box", No_of_box4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(jsonParam1);
            array.put(jsonParam2);
            array.put(jsonParam3);
            array.put(jsonParam4);

            if (quotation_type.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "select your qoute", Snackbar.LENGTH_SHORT).show();
            } else if (Customer_name.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your name", Snackbar.LENGTH_SHORT).show();
            } else if (Customer_address.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your address", Snackbar.LENGTH_SHORT).show();
            } else if (Mobile.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your mobile number", Snackbar.LENGTH_SHORT).show();
            } else if (Mobile.length() < 10) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter valid mobile number", Snackbar.LENGTH_SHORT).show();
            } else if (Email.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your email", Snackbar.LENGTH_SHORT).show();
            } else if (!isEmailValid(Email)) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter valid email", Snackbar.LENGTH_SHORT).show();
            } else if (Description.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your requirement", Snackbar.LENGTH_SHORT).show();
            } else if (Origin_address.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your origin address", Snackbar.LENGTH_SHORT).show();
            } else if (Destination_address.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your destination address", Snackbar.LENGTH_SHORT).show();
            } else if (Shipment_readyness_date.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your shipment date ", Snackbar.LENGTH_SHORT).show();
            } else if (Deadline_for_quote.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your deadline qoute", Snackbar.LENGTH_SHORT).show();
            } else if (Customer_decision_date.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your Customer decission date", Snackbar.LENGTH_SHORT).show();
            } else if (Special_istructions.equals("")) {
                Snackbar.make(InterNationalActivity.this.findViewById(android.R.id.content), "Enter your Special istructions", Snackbar.LENGTH_SHORT).show();
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submit_enquery.getWindowToken(), 0);
                submitenquiry(cat_id, quotation_type, Customer_name, Customer_address, Mobile, Email, Website, Description, Origin_address, Destination_address,
                        Shipment_readyness_date, Deadline_for_quote, Customer_decision_date, Special_istructions, array, filePath1);
            }
        });
    }

    private void submitenquiry(String cat_id, String quotation_type, String customer_name, String customer_address, String mobile, String email, String website,
                               String description, String origin_address, String destination_address, String shipment_readyness_date,
                               String deadline_for_quote, String customer_decision_date, String special_istructions,
                               JSONArray jsonObject1, String filePath1) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.enquiry, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    String msg = jsonObject.getString("msg");
                    opensuccessdialog();
                    progress.setVisibility(View.GONE);
                } else {
                    Toast.makeText(InterNationalActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(InterNationalActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(InterNationalActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cat_id", cat_id);
                params.put("customer_name", customer_name);
                params.put("customer_address", customer_address);
                params.put("phone", mobile);
                params.put("email", email);
                params.put("website", website);
                params.put("goods_desc", description);
                params.put("origin_city", origin_address);
                params.put("destination_city", destination_address);
                params.put("readyness_date", shipment_readyness_date);
                params.put("deadline_date", deadline_for_quote);
                params.put("decission_date", customer_decision_date);
                params.put("instruction", special_istructions);
                params.put("box_data", String.valueOf(jsonObject1));
                params.put("membership", quotation_type);
                params.put("box_data_file", filePath1);
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
        RequestQueue requestQueue = Volley.newRequestQueue(InterNationalActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_DOC && resultCode == RESULT_OK && data.getData() != null) {
                Uri fileuri = data.getData();
                filePath1 = getStringPdf(fileuri);
                Uri fileuri1 = data.getData();
                String fPath = getPath(fileuri1);
                File f = new File(fPath);
                String imageName = f.getName();
                file_name.setText(imageName);
                String filePathh1 = getFilePathFromURI(InterNationalActivity.this, fileuri);
                File file = new File(filePathh1);
                file_size2 = Integer.parseInt(String.valueOf(file.length() / 1024));
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            path = uri.getPath();
            file_name.setText(path);
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }
        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    public String getStringPdf(Uri filepath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = getContentResolver().openInputStream(filepath);
            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void opensuccessdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InterNationalActivity.this);
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.enquiry_form_dialog, null);
        builder.setView(convertview);
        TextView go_home = convertview.findViewById(R.id.go_home);
        go_home.setOnClickListener(v -> {
            Intent intent = new Intent(InterNationalActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        final AlertDialog b = builder.show();
    }
}
