package x.skyboxx.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import x.skyboxx.Activity.ServiceListActivity;
import x.skyboxx.R;

public class ServiceAdapter extends RecyclerView.Adapter <ServiceAdapter.ListViewHolder>{

    Context context;
    ArrayList<HashMap<String, String>> service_list = new ArrayList<>();

    public ServiceAdapter(Context context, ArrayList<HashMap<String, String>> service_list) {

        this.context = context;
        this.service_list = service_list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_service, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceAdapter.ListViewHolder holder, int position) {


        String cat_id = service_list.get(position).get("cat_id");
        String catname = service_list.get(position).get("catname");
        String catimage = service_list.get(position).get("catimage");
        String service_charge = service_list.get(position).get("service_charge");

        if(Objects.equals(cat_id, "1")){
            Picasso.get().load(R.drawable.a0).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "2")){
            Picasso.get().load(R.drawable.a1).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "3")){
            Picasso.get().load(R.drawable.a2).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "4")){
            Picasso.get().load(R.drawable.a3).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "5")){
            Picasso.get().load(R.drawable.a4).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "6")){
            Picasso.get().load(R.drawable.a5).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "7")){
            Picasso.get().load(R.drawable.a6).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "8")){
            Picasso.get().load(R.drawable.a7).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "9")){
            Picasso.get().load(R.drawable.a8).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }
        else if(Objects.equals(cat_id, "10")){
            Picasso.get().load(R.drawable.a9).placeholder(R.drawable.logoapp).into(holder.catgory_img);
        }

//        Picasso.get().load(catimage).placeholder(R.drawable.logoapp).into(holder.catgory_img);

        holder.catgoryName.setText(catname);

        holder.catgory_img.setOnClickListener(v -> {

            Intent intent = new Intent(context, ServiceListActivity.class);
            intent.putExtra("cat_id",cat_id);
            intent.putExtra("catname",catname);
            intent.putExtra("service_charge",service_charge);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return service_list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView catgoryName;
        ImageView catgory_img;

        public ListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            catgory_img = itemView.findViewById(R.id.catgory_img);
            catgoryName = itemView.findViewById(R.id.catgory_name);
        }
    }
}
