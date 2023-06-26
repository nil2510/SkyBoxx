package x.skyboxx.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import x.skyboxx.R;

public class Detailsadapter extends RecyclerView.Adapter<Detailsadapter.ListViewHolder> {

    Context context;
    ArrayList<HashMap<String, Object>> service_details_list;

    public Detailsadapter(Context context, ArrayList<HashMap<String, Object>> details_list) {
        this.context = context;
        this.service_details_list = details_list;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_listing_adapter, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {


        Object key = service_details_list.get(position).get("key");
        Object valuee = service_details_list.get(position).get("value");

        holder.type_name.setText((CharSequence) key);
        holder.type_value.setText((CharSequence) valuee);


    }

    @Override
    public int getItemCount() {

        return service_details_list.size();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView type_name,type_value;

        ListViewHolder(View _view) {
            super(_view);
            type_name = _view.findViewById(R.id.type_name);
            type_value = _view.findViewById(R.id.type_value);
        }
    }
}
