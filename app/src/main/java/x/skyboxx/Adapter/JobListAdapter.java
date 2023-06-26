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

import x.skyboxx.Activity.JobDetailActivity;
import x.skyboxx.R;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ListViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> job_list = new ArrayList<>();

    public JobListAdapter(Context context, ArrayList<HashMap<String, String>> cat_service_list) {

        this.context = context;
        this.job_list = cat_service_list;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_job_list, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;

    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {


        final String job_id = job_list.get(position).get("job_id");
        final String job_name = job_list.get(position).get("job_name");
        final String job_image = job_list.get(position).get("job_image");
        final String job_company_name = job_list.get(position).get("job_company_name");
        final String job_experience = job_list.get(position).get("job_experience");
        final String job_country = job_list.get(position).get("job_country");
        final String job_city = job_list.get(position).get("job_city");
        final String salary = job_list.get(position).get("salary");
        final String job_designation = job_list.get(position).get("job_designation");
        final String job_educations = job_list.get(position).get("job_educations");
        final String job_posted_date = job_list.get(position).get("job_posted_date");
        final String description = job_list.get(position).get("description");

        Picasso.get().load(job_image).placeholder(R.drawable.logoapp).into(holder.job_img);

        holder.designation.setText(job_designation);
        holder.location.setText(job_country);
        holder.date.setText(job_posted_date);
        holder.expeience.setText(job_experience);

        holder.view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("job_id",job_id);
                intent.putExtra("job_name",job_name);
                intent.putExtra("job_image",job_image);
                intent.putExtra("job_company_name",job_company_name);
                intent.putExtra("job_experience",job_experience);
                intent.putExtra("job_country",job_country);
                intent.putExtra("job_city",job_city);
                intent.putExtra("salary",salary);
                intent.putExtra("job_designation",job_designation);
                intent.putExtra("job_educations",job_educations);
                intent.putExtra("job_posted_date",job_posted_date);
                intent.putExtra("description",description);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return job_list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView job_img;
        TextView designation,location,date,expeience;
        Button view_more;

        ListViewHolder(View view) {
            super(view);

            job_img = view.findViewById(R.id.job_img);
            designation = view.findViewById(R.id.designation);
            location = view.findViewById(R.id.location);
            date = view.findViewById(R.id.date);
            expeience = view.findViewById(R.id.expeience);
            view_more = view.findViewById(R.id.view_more);

        }
    }
}
