package x.skyboxx.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class JobDetailActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int REQUEST_CODE_DOC = 1;
    NestedScrollView nested_parent;
    FrameLayout progress;
    ImageView job_img;
    TextView company_name, designation, slary, location, gradutaion, p_gradutaion, skill, job_time, job_descp;
    TextView apply_now;
    String job_id, job_name, job_image, user_id, job_company_name, job_experience, job_country, salary, job_city, job_designation, job_educations,
            description, job_posted_date;
    SharedPreferenceClass sharedPreferenceClass;
    String filePath1 = "";
    int file_size2;
    TextView file_name;
    String checkterms = "0";
    String checkemail = "0";

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
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        sharedPreferenceClass = new SharedPreferenceClass(JobDetailActivity.this);
        user_id = sharedPreferenceClass.getValue_string("user_id");

        job_id = getIntent().getStringExtra("job_id");
        job_name = getIntent().getStringExtra("job_name");
        job_image = getIntent().getStringExtra("job_image");
        job_company_name = getIntent().getStringExtra("job_company_name");
        job_experience = getIntent().getStringExtra("job_experience");
        job_country = getIntent().getStringExtra("job_country");
        job_city = getIntent().getStringExtra("job_city");
        salary = getIntent().getStringExtra("salary");
        job_designation = getIntent().getStringExtra("job_designation");
        job_educations = getIntent().getStringExtra("job_educations");
        job_posted_date = getIntent().getStringExtra("job_posted_date");
        description = getIntent().getStringExtra("description");

        nested_parent = findViewById(R.id.nested_parent);
        progress = findViewById(R.id.progress);
        job_img = findViewById(R.id.job_img);
        company_name = findViewById(R.id.company_name);
        designation = findViewById(R.id.designation);
        slary = findViewById(R.id.slary);
        location = findViewById(R.id.location);
        gradutaion = findViewById(R.id.gradutaion);
        p_gradutaion = findViewById(R.id.p_gradutaion);
        skill = findViewById(R.id.skill);
        job_time = findViewById(R.id.job_time);
        job_descp = findViewById(R.id.job_descp);
        apply_now = findViewById(R.id.apply_now);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        Picasso.get().load(job_image).placeholder(R.drawable.logosquatrans).into(job_img);

        company_name.setText(job_company_name);
        designation.setText(job_name);
        slary.setText(salary);
        location.setText(job_city);
        gradutaion.setText(job_educations);
        p_gradutaion.setText(job_educations);
        skill.setText(job_designation);
        job_time.setText(job_posted_date);

        job_descp.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        progress.setVisibility(View.GONE);
        apply_now.setOnClickListener(view -> opensignupdialog(job_id, user_id));
    }

    private void opensignupdialog(String c, String user_id) {
        final Dialog convertview = new Dialog(JobDetailActivity.this);
        convertview.setContentView(R.layout.dialog_job_signup);
        convertview.setCancelable(false);
        convertview.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        convertview.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        convertview.setCanceledOnTouchOutside(false);

        final EditText email = convertview.findViewById(R.id.email);
        final EditText first_name = convertview.findViewById(R.id.first_name);
        final EditText last_name = convertview.findViewById(R.id.last_name);
        final EditText number = convertview.findViewById(R.id.number);
        TextView salary = convertview.findViewById(R.id.salary);
        TextView select_cv = convertview.findViewById(R.id.select_cv);
        file_name = convertview.findViewById(R.id.file_name);
        CheckBox terms = convertview.findViewById(R.id.terms);
        TextView terms_text = convertview.findViewById(R.id.terms_text);
        CheckBox email_check = convertview.findViewById(R.id.email_check);
        Button apply_now = convertview.findViewById(R.id.apply_now);
        FrameLayout progress = convertview.findViewById(R.id.progress);
        ImageView close = convertview.findViewById(R.id.close);

        requestStoragePermission();
        requestWritePermission();

        terms_text.setOnClickListener(v -> opendialog());

        select_cv.setOnClickListener(v -> {
            try {
                Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
                intentPDF.setType("application/pdf");
                intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intentPDF, "Select Picture"), REQUEST_CODE_DOC);
                filePath1 = "selected";
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        });

        apply_now.setOnClickListener(v -> {
            if (terms.isChecked()) {
                checkterms = "1";
            } else {
                checkterms = "0";
            }
            if (email_check.isChecked()) {
                checkemail = "1";
            } else {
                checkemail = "0";
            }
            String Email = email.getText().toString();
            String First_name = first_name.getText().toString();
            String Last_name = last_name.getText().toString();
            String Number = number.getText().toString();
            String Salary = salary.getText().toString();

            InputMethodManager imm = (InputMethodManager) JobDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(apply_now.getWindowToken(), 0);

            if (Email.equals("")) {
                Snackbar.make(convertview.findViewById(android.R.id.content), "Enter your email", Snackbar.LENGTH_LONG).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                Snackbar.make(convertview.findViewById(android.R.id.content), "Enter valid email", Snackbar.LENGTH_LONG).show();
            } else if (First_name.equals("")) {
                Snackbar.make(convertview.findViewById(android.R.id.content), "Enter your first name", Snackbar.LENGTH_LONG).show();
            } else if (Last_name.equals("")) {
                Snackbar.make(convertview.findViewById(android.R.id.content), "Enter your last name", Snackbar.LENGTH_LONG).show();
            } else if (Number.equals("")) {
                Snackbar.make(convertview.findViewById(android.R.id.content), "Enter your number", Snackbar.LENGTH_LONG).show();
            } else if (Number.length() < 10) {
                Snackbar.make(convertview.findViewById(android.R.id.content), "Enter valid mobile number", Snackbar.LENGTH_LONG).show();
            } else if (filePath1.equals("")) {
                Snackbar snackbar1 = Snackbar.make(v, "Upload your Cv", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            } else if (checkterms.equals("0")) {
                Snackbar snackbar1 = Snackbar.make(v, "Accept terms and condition", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            } else {
                progress.setVisibility(View.VISIBLE);
                applyforjob(job_id, user_id, Email, First_name, Last_name, Number, Salary, filePath1, checkterms, checkemail, convertview);
            }
        });
        close.setOnClickListener(view -> convertview.dismiss());
        convertview.show();
    }

    private void opendialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JobDetailActivity.this);
        builder.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) JobDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.dialog_terms, null);
        builder.setView(convertview);
        TextView terms_condition = convertview.findViewById(R.id.terms_condition);
        ProgressBar progress = convertview.findViewById(R.id.progress);
        setcontent(terms_condition, progress);
        final AlertDialog b = builder.show();
    }

    private void setcontent(TextView terms_condition, ProgressBar progress) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.job_terms, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    String content = jsonObject.getString("content");
                    terms_condition.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
                    progress.setVisibility(View.GONE);
                } else {
                    Toast.makeText(JobDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                progress.setVisibility(View.GONE);
            }
        }, error -> error.printStackTrace()) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(JobDetailActivity.this);
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
                String filePathh1 = getFilePathFromURI(JobDetailActivity.this, fileuri);
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

    private void applyforjob(String jobid, String user_id, String email, String first_name, String last_name, String mobile,
                             String salary, String filePath1, String checkterms, String checkemail, Dialog convertview) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.jobApply, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    progress.setVisibility(View.GONE);
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(JobDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                    opensuccessdialog();
                    convertview.dismiss();
                } else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(JobDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(JobDetailActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(JobDetailActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_id", jobid);
                params.put("email", email);
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("phone_no", mobile);
                params.put("current_salary", salary);
                params.put("resume", filePath1);
                params.put("notify_email", checkemail);
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
        RequestQueue requestQueue = Volley.newRequestQueue(JobDetailActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void opensuccessdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JobDetailActivity.this);
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.dialog_job_apply, null);
        builder.setView(convertview);
        TextView go_home = convertview.findViewById(R.id.go_home);
        go_home.setOnClickListener(v -> {
            Intent intent = new Intent(JobDetailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        final AlertDialog b = builder.show();
    }
}