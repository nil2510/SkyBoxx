package x.skyboxx.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import x.skyboxx.Activity.AboutUsActivity;
import x.skyboxx.Activity.EditProfileActivity;
import x.skyboxx.Activity.LoginActivity;
import x.skyboxx.Activity.OrderActivity;
import x.skyboxx.R;
import x.skyboxx.Utils.ServerLinks;
import x.skyboxx.Utils.SharedPreferenceClass;

public class ProfileFragment extends Fragment {
    ImageView profile_image;
    String user_id;
    SharedPreferenceClass sharedPreferenceClass;
    TextView namee, emaill, mobilee;
    ConstraintLayout order, next, about, terms, share, privacy, logout, feedback;
    TextView edit_profile_details;
    String name, email, mobile, profile_pic;
    NestedScrollView parentlayout;
    FrameLayout progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        parentlayout = view.findViewById(R.id.parentlayout);
        progressbar = view.findViewById(R.id.progressframe);
        progressbar.setVisibility(View.VISIBLE);

        profile_image = view.findViewById(R.id.edit_profile_image);
        namee = view.findViewById(R.id.namee);
        emaill = view.findViewById(R.id.email);
        mobilee = view.findViewById(R.id.mobile);
        edit_profile_details = view.findViewById(R.id.edit_profile_details);
        order = view.findViewById(R.id.constraint_booking);
        next = view.findViewById(R.id.constraint_next);
        about = view.findViewById(R.id.constraint_about);
        terms = view.findViewById(R.id.constraint_terms);
        share = view.findViewById(R.id.constraint_share);
        privacy = view.findViewById(R.id.constraint_privacy);
        logout = view.findViewById(R.id.constraint_logout);
        feedback = view.findViewById(R.id.constraint_feedback);

        sharedPreferenceClass = new SharedPreferenceClass(getActivity());
        user_id = sharedPreferenceClass.getValue_string("user_id");
        Toast.makeText(getContext(), user_id, Toast.LENGTH_SHORT).show();

        if (!user_id.equals("")) {

            getProfiledata(user_id, progressbar);
            edit_profile_details.setEnabled(true);
            order.setEnabled(true);
            privacy.setEnabled(true);
            logout.setEnabled(true);

        } else {

            openlogindialog(progressbar);
            edit_profile_details.setEnabled(false);
            order.setEnabled(false);
            privacy.setEnabled(false);
            logout.setEnabled(false);
        }

        order.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });

        next.setOnClickListener(v -> {

        });

        edit_profile_details.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("mobile", mobile);
            intent.putExtra("profile_pic", profile_pic);
            startActivity(intent);
        });

        privacy.setOnClickListener(v -> opendialog());
        terms.setOnClickListener(v -> opendialog2());

        about.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), AboutUsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        logout.setOnClickListener(v -> LogoutNow());
        share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "To download SkyBoxx App click here! https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
        feedback.setOnClickListener(v -> {

        });

        return view;
    }

    private void opendialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.activity_privacy, null);
        builder.setView(convertview);

        TextView terms_condition = convertview.findViewById(R.id.terms_condition);
        setcontent(terms_condition);
        final AlertDialog b = builder.show();
    }

    private void opendialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertview = inflater.inflate(R.layout.dialog_terms1, null);
        builder.setView(convertview);

        TextView terms_condition = convertview.findViewById(R.id.terms_condition);
        setcontent2(terms_condition);
        final AlertDialog b = builder.show();
    }

    private void setcontent(TextView terms_condition) {
        terms_condition.setText("Privacy Policy\n" +
                "x built the SkyBoxx app as a Free app. This SERVICE is provided by x at no cost and is intended for use as is.\n" +
                "\n" +
                "This page is used to inform visitors regarding our policies with the collection, use, and disclosure of Personal Information if anyone decided to use our Service.\n" +
                "\n" +
                "If you choose to use our Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that we collect is used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy.\n" +
                "\n" +
                "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at SkyBoxx unless otherwise defined in this Privacy Policy.\n" +
                "\n" +
                "Information Collection and Use\n" +
                "\n" +
                "For a better experience, while using our Service, we may require you to provide us with certain personally identifiable information, including but not limited to 1234567890. The information that we request will be retained by us and used as described in this privacy policy.\n" +
                "\n" +
                "The app does use third-party services that may collect information used to identify you.\n" +
                "\n" +
                "Link to the privacy policy of third-party service providers used by the app\n" +
                "\n" +
                "Google Play Services\n" +
                "Log Data\n" +
                "\n" +
                "We want to inform you that whenever you use our Service, in a case of an error in the app we collect data and information (through third-party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing our Service, the time and date of your use of the Service, and other statistics.\n" +
                "\n" +
                "Cookies\n" +
                "\n" +
                "Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.\n" +
                "\n" +
                "This Service does not use these “cookies” explicitly. However, the app may use third-party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n" +
                "\n" +
                "Service Providers\n" +
                "\n" +
                "We may employ third-party companies and individuals due to the following reasons:\n" +
                "\n" +
                "To facilitate our Service;\n" +
                "To provide the Service on our behalf;\n" +
                "To perform Service-related services; or\n" +
                "To assist us in analyzing how our Service is used.\n" +
                "We want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n" +
                "\n" +
                "Security\n" +
                "\n" +
                "We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.\n" +
                "\n" +
                "Links to Other Sites\n" +
                "\n" +
                "This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by us. Therefore, we strongly advise you to review the Privacy Policy of these websites. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n" +
                "\n" +
                "Children’s Privacy\n" +
                "\n" +
                "These Services do not address anyone under the age of 13. We do not knowingly collect personally identifiable information from children under 13 years of age. In the case we discover that a child under 13 has provided us with personal information, we immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact us so that we will be able to do the necessary actions.\n" +
                "\n" +
                "Changes to This Privacy Policy\n" +
                "\n" +
                "We may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page.\n" +
                "\n" +
                "This policy is effective as of 2023-04-23\n" +
                "\n" +
                "Contact Us\n" +
                "\n" +
                "If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us at sp33190242gmail.com.");
    }

    private void setcontent2(TextView terms_condition) {
        terms_condition.setText(
                "By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages or make derivative versions. The app itself, and all the trademarks, copyright, database rights, and other intellectual property rights related to it, still belong to SkyBoxx.\n" +
                        "\n" +
                        "SkyBoxx is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.\n" +
                        "\n" +
                        "The SkyBoxx app stores and processes personal data that you have provided to us, to provide our Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the SkyBoxx app won’t work properly or at all.\n" +
                        "\n" +
                        "The app does use third-party services that declare their Terms and Conditions.\n" +
                        "\n" +
                        "Link to Terms and Conditions of third-party service providers used by the app\n" +
                        "\n" +
                        "Google Play Services\n" +
                        "You should be aware that there are certain things that SkyBoxx will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi or provided by your mobile network provider, but SkyBoxx cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.\n" +
                        "\n" +
                        "If you’re using the app outside of an area with Wi-Fi, you should remember that the terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third-party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.\n" +
                        "\n" +
                        "Along the same lines, SkyBoxx cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, SkyBoxx cannot accept responsibility.\n" +
                        "\n" +
                        "With respect to SkyBoxx’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavor to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. SkyBoxx accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.\n" +
                        "\n" +
                        "At some point, we may wish to update the app. The app is currently available on Android – the requirements for the system(and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. SkyBoxx does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.\n" +
                        "\n" +
                        "Changes to This Terms and Conditions\n" +
                        "\n" +
                        "We may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Terms and Conditions on this page.\n" +
                        "\n" +
                        "These terms and conditions are effective as of 2023-04-20\n" +
                        "\n" +
                        "Contact Us\n" +
                        "\n" +
                        "If you have any questions or suggestions about our Terms and Conditions, do not hesitate to contact us at sp3319024@gmail.com.\n" +
                        "\n" +
                        "This Terms and Conditions page was generated by App Privacy Policy Generator");
    }

    private void openlogindialog(FrameLayout progressbar) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertview = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(convertview);

        edit_profile_details.setClickable(false);
        order.setClickable(false);
        privacy.setClickable(false);
        logout.setClickable(false);

        TextView lets_go = convertview.findViewById(R.id.login);

        lets_go.setOnClickListener((View.OnClickListener) view -> {

            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        });

        final AlertDialog b = builder.show();
    }

    private void LogoutNow() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Log Out");
        dialog.setMessage("Are you sure?");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.logosquatrans);
        dialog.setPositiveButton("Yes", (dialog1, i) -> {
            new SharedPreferenceClass(getActivity()).clearData();
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            dialog1.dismiss();
        }).setNegativeButton("No ", (dialog12, which) -> dialog12.dismiss());
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void getProfiledata(String user_id, FrameLayout progressbar) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.profile, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    name = data.getString("name");
                    email = data.getString("email");
                    mobile = data.getString("mobile");
                    profile_pic = data.getString("profile_pic");

                    namee.setText(name);
                    emaill.setText(email);
                    mobilee.setText(mobile);

                    if (!profile_pic.equals("")) {
                        Picasso.get().load(profile_pic).placeholder(R.drawable.logosquatrans).into(profile_image);
                    }
                    progressbar.setVisibility(View.GONE);

                    sharedPreferenceClass.setValue_string("name", name);
                    sharedPreferenceClass.setValue_string("email", email);
                    sharedPreferenceClass.setValue_string("mobile", mobile);
                    sharedPreferenceClass.setValue_string("profile_pic", profile_pic);
                } else {
                }
            } catch (Exception e) {
                progressbar.setVisibility(View.GONE);
                e.printStackTrace();
            }
        },
                error -> {
                    progressbar.setVisibility(View.GONE);
                    error.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!user_id.equals("")) {
            getProfiledata(user_id, progressbar);
        }
    }
}