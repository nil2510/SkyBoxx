package x.skyboxx.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import x.skyboxx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ListViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> banner_list = new ArrayList<>();

    public BannerAdapter(Context context, ArrayList<HashMap<String, String>> banner_list1) {

        this.context = context;
        this.banner_list = banner_list1;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_banner, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;

    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {


        final String image = banner_list.get(position).get("image");
        final String id = banner_list.get(position).get("id");
        final String title = banner_list.get(position).get("title");

        Picasso.get().load(image).placeholder(R.drawable.logorecttrans).into(holder.bimage);
    }

    @Override
    public int getItemCount() {
        return banner_list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        CardView ecard;
        ImageView bimage;

        ListViewHolder(View view) {
            super(view);
            ecard = view.findViewById(R.id.banner_cardView);
            bimage = view.findViewById(R.id.banner_img);

        }
    }
}
