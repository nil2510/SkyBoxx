package x.skyboxx.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import x.skyboxx.Fragment.ProfileFragment;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;

public class EditProfileActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    String base64Rpic = "N", user_id;
    String name, email, profile_pic, mobile;
    ImageView edit_profile_image;
    EditText Name, mobile_number;
    EditText Emaill;
    MaterialButton save_details;
    FrameLayout progressbar;
    TextView change_password;

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        edit_profile_image = findViewById(R.id.edit_profile_image);
        Name = findViewById(R.id.name);
        Emaill = findViewById(R.id.email);
        mobile_number = findViewById(R.id.mobile);
        save_details = findViewById(R.id.save_details);
        progressbar = findViewById(R.id.progress);
        change_password = findViewById(R.id.change_password);

        user_id = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        mobile = getIntent().getStringExtra("mobile");
        profile_pic = getIntent().getStringExtra("profile_pic");

        getProfiledata(user_id);

        edit_profile_image.setOnClickListener(v -> {
            requestStoragePermission();
            requestWritePermission();

            final CharSequence[] options = {"Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setTitle("Update Photo!");
            builder.setItems(options, (dialog, item) -> {

                if (options[item].equals("Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (EditProfileActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(EditProfileActivity.this.getPackageManager()) != null) {
                                startActivityForResult(intent, CAMERA_REQUEST);
                            }
                        }
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_REQUEST);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });


        change_password.setOnClickListener(v -> ChangePasswordDialog());

        save_details.setOnClickListener(v -> {
            String Namee = Name.getText().toString();
            String Mobile_number = mobile_number.getText().toString();

            if (Namee.equals(name) && Mobile_number.equals(mobile)) {
                Snackbar.make(EditProfileActivity.this.findViewById(android.R.id.content), "No changes detected", Snackbar.LENGTH_LONG).show();
            } else if (Namee.equals("")) {
                Snackbar.make(EditProfileActivity.this.findViewById(android.R.id.content), "Enter name to update", Snackbar.LENGTH_LONG).show();
            } else if (Mobile_number.equals("")) {
                Snackbar.make(EditProfileActivity.this.findViewById(android.R.id.content), "Enter mobile number to update", Snackbar.LENGTH_LONG).show();
            } else if (Mobile_number.length() < 10) {
                Snackbar.make(EditProfileActivity.this.findViewById(android.R.id.content), "Invalid Number!", Snackbar.LENGTH_LONG).show();
            } else {
                change_profile_details(Namee, Mobile_number);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(EditProfileActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(EditProfileActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                base64Rpic = encodeTobase64(photo);
                if (!base64Rpic.equals(""))
                    UpdateProfilePic(user_id, base64Rpic);
                else
                    Toast.makeText(EditProfileActivity.this, "", Toast.LENGTH_SHORT).show();

            } else if (requestCode == GALLERY_REQUEST && data.getData() != null) {

                Uri fileuri = data.getData();
                String fPath = getPath(fileuri);

                InputStream imageStream = null;
                try {
                    imageStream = EditProfileActivity.this.getContentResolver().openInputStream(fileuri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                base64Rpic = encodeTobase64(yourSelectedImage);

                File f = new File(base64Rpic);
                String imageName = f.getName();

                if (!base64Rpic.equals(""))
                    UpdateProfilePic(user_id, base64Rpic);
                else
                    Toast.makeText(EditProfileActivity.this, "", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void getProfiledata(String user_id) {
        progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.profile,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            progressbar.setVisibility(View.GONE);
                            JSONObject data = jsonObject.getJSONObject("data");

                            name = data.getString("name");
                            email = data.getString("email");
                            mobile = data.getString("mobile");
                            profile_pic = data.getString("profile_pic");
                            Name.setText(name);
                            Emaill.setText(email);
                            mobile_number.setText(mobile);

                            if (!profile_pic.equals("")) {
                                Picasso.get().load(profile_pic).placeholder(R.drawable.logosquatrans).into(edit_profile_image);
                            }
                        } else {
                            progressbar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        progressbar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },

                error -> {
                    if (error.getMessage() == null) {
                        error.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        requestQueue.add(stringRequest);
    }

    private void UpdateProfilePic(String user_id, String profile_photo) {
        progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.updateProfilePic, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    String message = jsonObject.getString("message");
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    getProfiledata(user_id);
                    progressbar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(EditProfileActivity.this, "profile pic upload failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException js) {
                js.printStackTrace();
                Toast.makeText(EditProfileActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(EditProfileActivity.this, "Something went wrong!try again later", Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
            if (error.getMessage() == null) {
                int i = 0;
                if (i < 3) {
                    i++;
                    UpdateProfilePic(user_id, profile_photo);
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", user_id);
                param.put("profile_pic", profile_photo);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Auth", ServerLinks.Authkey);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        requestQueue.add(stringRequest);
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    public String getPath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = EditProfileActivity.this.getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }
        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void ChangePasswordDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(EditProfileActivity.this).create();
        dialogBuilder.setCancelable(false);
        View dialogView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.dialog_change_password, null);

        final EditText old_password = dialogView.findViewById(R.id.old_password);
        final EditText confirm_password = dialogView.findViewById(R.id.new_confirm_password);
        final TextView cancel_dialog = dialogView.findViewById(R.id.cancel);
        final TextView forgot_password_button = dialogView.findViewById(R.id.update);
        final ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        forgot_password_button.setOnClickListener(view -> {

            InputMethodManager imm = (InputMethodManager) EditProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cancel_dialog.getWindowToken(), 0);
            String Old_password = old_password.getText().toString();
            String Confirm_password = confirm_password.getText().toString();

            if (Old_password.equals("")) {
                Toast.makeText(EditProfileActivity.this, "Please enter old password", Toast.LENGTH_SHORT).show();
            } else if (Confirm_password.equals("")) {
                Toast.makeText(EditProfileActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();
            } else {
                ChangePassword(user_id, Old_password, Confirm_password, progressBar, dialogBuilder);
            }
        });
        cancel_dialog.setOnClickListener(view -> {
            dialogBuilder.dismiss();
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void change_profile_details(String namee, String Mobile_number) {
        progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.updateProfile, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    progressbar.setVisibility(View.GONE);
                    String message = jsonObject.getString("message");
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.commit();
                    finish();

                } else {
                    progressbar.setVisibility(View.GONE);
                    String message = jsonObject.getString("message");
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressbar.setVisibility(View.GONE);
            }
        }, error -> {
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
                params.put("user_id", user_id);
                params.put("name", namee);
                params.put("mobile", Mobile_number);
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
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }

    private void ChangePassword(String user_id, String old_password, String confirm_password, ProgressBar progressBar, AlertDialog dialogBuilder) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.changePassword, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    progressBar.setVisibility(View.GONE);
                    dialogBuilder.dismiss();
                    String message = jsonObject.getString("message");
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    String message = jsonObject.getString("message");
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        }, error -> {
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
                params.put("user_id", user_id);
                params.put("old_password", old_password);
                params.put("new_password", confirm_password);
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
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        requestQueue.add(stringRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }
}