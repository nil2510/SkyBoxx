package x.skyboxx.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.skyboxx.Activity.AboutUsActivity;
import x.skyboxx.Activity.LoginActivity;
import x.skyboxx.Activity.MainActivity;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.MyViewHolder> {

    Context context;
    String[] title;
    int[] icon;
    SharedPreferenceClass sharedPreferenceClass;
    DrawerLayout drawer;
    private int selectedItem;
    ProgressDialog progressDialog;


    public NavigationAdapter(Context context, String[] title, int[] icon, DrawerLayout drawer) {
        this.context = context;
        this.title = title;
        this.icon = icon;
        this.drawer = drawer;
        sharedPreferenceClass = new SharedPreferenceClass(context);
        selectedItem = -1;
        progressDialog = new ProgressDialog(context);

    }

    public static boolean isValidPassword(String password) {

        String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.adapter_navigationdrawer, parent, false);
        MyViewHolder holder = new MyViewHolder(view1);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String user_id = sharedPreferenceClass.getValue_string("user_id");
        holder.imageview.setImageResource(icon[position]);
        if (user_id.equals("")){
            if (title[position].equals("Logout")){
                holder.text.setText("Login");
                holder.imageview.setImageResource(R.drawable.baseline_login_24);
            }else {
                holder.text.setText(title[position]);
            }
        }else {
            holder.text.setText(title[position]);
        }
        /*if (selectedItem == position) {
            holder.imageview.setColorFilter(context.getResources().getColor(R.color.blue)); // Add tint color
            holder.text.setTextColor(context.getResources().getColor(R.color.blue));
        }*/

        holder.layout.setOnClickListener(view -> {

            int previousItem = selectedItem;
            selectedItem = position;
            notifyItemChanged( previousItem );
            notifyItemChanged( position );


            if (title[position].equals("Home")) {

                context.startActivity(new Intent(context, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            }else if (title[position].equals("Change Password")) {

                if (user_id.equals("")){

                    Toast.makeText(context, "please login to change the password", Toast.LENGTH_LONG).show();

                }else {

                    ChangePassword(user_id);
                }

            }
            else if (title[position].equals("About Us")) {

                context.startActivity(new Intent(context, AboutUsActivity.class) .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            } else if (title[position].equals("Share App")) {

                ShareApp();

            } else if (title[position].equals("Logout")) {

                if (user_id.equals("")){

                    context.startActivity(new Intent(context, LoginActivity.class));

                }else {

                    LogoutNow();
                }

            }

            drawer.closeDrawer(GravityCompat.START);

        });
    }

    private void ChangePassword(String user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertview = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setView(convertview);

        //final EditText reasons_text = (EditText) convertview.findViewById(R.id.other);

        final TextView Ok = (TextView) convertview.findViewById(R.id.update);
        TextView cancel = (TextView) convertview.findViewById(R.id.cancel);
        EditText old_Password = (EditText) convertview.findViewById(R.id.old_password);
        EditText new_confirm_password = (EditText) convertview.findViewById(R.id.new_confirm_password);

        final AlertDialog b = builder.show();

        Ok.setOnClickListener(v -> {

            String Old_passwordd = old_Password.getText().toString();
            String New_confirmpassword = new_confirm_password.getText().toString();

            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Ok.getWindowToken(), 0);

            if (Old_passwordd.equals("")) {
                Toast.makeText(context, "Please enter your old password", Toast.LENGTH_SHORT).show();

            } else if (New_confirmpassword.equals("")) {

                Toast.makeText(context, "Please enter your new password", Toast.LENGTH_SHORT).show();

            }else if (!isValidPassword(New_confirmpassword)){

                Toast.makeText(context, "Password must be 6 digit with Upercase,lowercase,special character and number", Toast.LENGTH_SHORT).show();

            } else {
                Updatechangepassword(user_id, Old_passwordd, New_confirmpassword, b);
            }
        });

        cancel.setOnClickListener(view -> b.dismiss());
    }

    private void Updatechangepassword(String user_id, String old_passwordd, String new_confirmpassword, AlertDialog b) {

        progressDialog.show();
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.changePassword,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {

                            progressDialog.hide();
                            b.dismiss();

                            Toast.makeText(context, "password changed succesfully", Toast.LENGTH_SHORT).show();

                        } else {
                            progressDialog.hide();
                            Toast.makeText(context, "incorrect old password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException jsn) {
                        jsn.printStackTrace();
                        Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();

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
                params.put("old_password", old_passwordd);
                params.put("new_password", new_confirmpassword);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
    }



    private void ShareApp() {

//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "To download Realogist App click here! https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
//        sendIntent.setType("text/plain");
//        context.startActivity(sendIntent);

    }
    private void LogoutNow() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Are you sure to logout Realogist from this device?");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.logosquablack);
        dialog.setPositiveButton("Yes", (dialog1, i) -> {


            new SharedPreferenceClass(context).clearData();
            context.startActivity(new Intent(context, LoginActivity.class));
            dialog1.dismiss();
        })
                .setNegativeButton("No ", (dialog12, which) -> dialog12.dismiss());

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView imageview;
        LinearLayout layout;

        public MyViewHolder(View view1) {
            super(view1);

            text = view1.findViewById(R.id.tv);
            imageview = view1.findViewById(R.id.imageview);
            layout = view1.findViewById(R.id.layout);

        }
    }

}
