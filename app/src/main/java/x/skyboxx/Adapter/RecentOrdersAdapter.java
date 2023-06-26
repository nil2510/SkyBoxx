package x.skyboxx.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

import x.skyboxx.R;

public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.MyViewHolder> {

    ArrayList<HashMap<String, String>> order_list;
    private Context context;

    public RecentOrdersAdapter(Context context, ArrayList<HashMap<String, String>> order_list) {
        this.context = context;
        this.order_list = order_list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_recent_orders,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int i) {



        String booking_id = order_list.get(i).get("booking_id");
        String book_date = order_list.get(i).get("book_date");
        String price = order_list.get(i).get("price");
        String company_name = order_list.get(i).get("company_name");
        String tag = order_list.get(i).get("tag");
        String image = order_list.get(i).get("image");
        String service_name = order_list.get(i).get("service_name");


        holder.orderDate.setText(book_date);
        holder.price.setText(" ₹ " +price);
        holder.status.setText(tag);
        holder.service_name.setText(service_name);

    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView service_name, orderDate,price,status;
        ImageView product_pic;
        CardView orderparent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            service_name = itemView.findViewById(R.id.service_name);
            product_pic = itemView.findViewById(R.id.product_pic);
            orderDate = itemView.findViewById(R.id.orderdate);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            orderparent = itemView.findViewById(R.id.orderparent);

        }
    }
}
