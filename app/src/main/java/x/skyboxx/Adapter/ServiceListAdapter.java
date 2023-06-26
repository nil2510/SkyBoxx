package x.skyboxx.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import x.skyboxx.Activity.ServiceDetailActivity;
import x.skyboxx.R;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ListViewHolder> {

    Context context;
    String Serviceid;
    ArrayList<HashMap<String, String>> cat_service_list = new ArrayList<>();

    public ServiceListAdapter(Context context, ArrayList<HashMap<String, String>> cat_service_list, String Serviceid) {
        this.context = context;
        this.cat_service_list = cat_service_list;
        this.Serviceid = Serviceid;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_list_adapter, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {

        final String service_id = cat_service_list.get(position).get("service_id");
        final String vendor_id = cat_service_list.get(position).get("vendor_id");
        final String service_type_id = cat_service_list.get(position).get("service_type_id");
        final String origin_country = cat_service_list.get(position).get("origin_country");
        final String origin_city = cat_service_list.get(position).get("origin_city");
        final String destination_country = cat_service_list.get(position).get("destination_country");
        final String destination_city = cat_service_list.get(position).get("destination_city");
        final String truck_type = cat_service_list.get(position).get("truck_type");
        final String payload_capacity = cat_service_list.get(position).get("payload_capacity");
        final String dimension = cat_service_list.get(position).get("dimension");
        final String rate_one_way = cat_service_list.get(position).get("rate_one_way");
        final String other_charges = cat_service_list.get(position).get("other_charges");
        final String transition_time = cat_service_list.get(position).get("transition_time");
        final String image = cat_service_list.get(position).get("image");
        final String vendor_name = cat_service_list.get(position).get("vendor_name");
        final String vendor_type = cat_service_list.get(position).get("vendor_type");
        final String service_cat = cat_service_list.get(position).get("service_cat");
        final String service_charge = cat_service_list.get(position).get("service_charge");
        final String origin_city_name = cat_service_list.get(position).get("origin_city_name");
        final String destination_city_name = cat_service_list.get(position).get("destination_city_name");
        final String location_name = cat_service_list.get(position).get("location_name");
        final String buildup_area = cat_service_list.get(position).get("buildup_area");
        final String rental_sqft_month = cat_service_list.get(position).get("rental_sqft_month");

        if (Serviceid.equals("10")) {
            Picasso.get().load(image).placeholder(R.drawable.logoapp).into(holder.service_img);

            holder.city_name.setText(location_name);
            holder.company_name.setText(vendor_name);
            holder.member_name.setText(vendor_type);
            holder.truck_type_text.setText("Booking Charge : ");
            holder.rate_trip_text.setText("Rental Sqft Month : ");
            holder.rate_trip.setText("₹" + rental_sqft_month);
            holder.truck_name.setText("₹" + service_charge);
        } else {
            holder.truck_type_text.setText("Booking Charge : ");
            holder.rate_trip_text.setText("Transition Time : ");

            Picasso.get().load(image).placeholder(R.drawable.logoapp).into(holder.service_img);

            holder.city_name.setText(origin_city_name + " to " + destination_city_name);
            holder.company_name.setText(vendor_name);
            holder.member_name.setText(vendor_type);
            holder.truck_name.setText("₹" + service_charge);
            holder.rate_trip.setText(transition_time);
        }
        holder.view_more.setOnClickListener(view -> {
            Intent intent = new Intent(context, ServiceDetailActivity.class);
            intent.putExtra("service_id", service_id);
            intent.putExtra("service_type_id", service_type_id);
            intent.putExtra("service_cat", service_cat);
            intent.putExtra("service_charge", service_charge);
            intent.putExtra("image", image);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cat_service_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView service_img;
        TextView city_name, company_name, member_name, truck_name, rate_trip;
        Button view_more;
        TextView truck_type_text, rate_trip_text;

        ListViewHolder(View view) {
            super(view);
            service_img = view.findViewById(R.id.service_img);
            city_name = view.findViewById(R.id.city_name);
            company_name = view.findViewById(R.id.company_name);
            member_name = view.findViewById(R.id.member_name);
            truck_name = view.findViewById(R.id.truck_name);
            rate_trip = view.findViewById(R.id.rate_trip);
            view_more = view.findViewById(R.id.view_more);
            truck_type_text = view.findViewById(R.id.truck_type_text);
            rate_trip_text = view.findViewById(R.id.rate_trip_text);
        }
    }
}
