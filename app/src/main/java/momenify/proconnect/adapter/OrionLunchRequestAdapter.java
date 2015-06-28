package momenify.proconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.List;

import momenify.proconnect.activities.OrionLunchAcceptance;
import momenify.proconnect.navigationviewpagerliveo.R;

/**
 * Created by shahbazkhan on 5/20/15.
 */
public class OrionLunchRequestAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<ParseObject> lunchRequests;

    public OrionLunchRequestAdapter(Context context,
                                 List<ParseObject> lunchRequests) {

        this.context = context;
        this.lunchRequests = lunchRequests;
        inflater = LayoutInflater.from(context);

    }

    public class ViewHolder {
        TextView name;
        TextView shift;
        TextView time;
        TextView accept;
        TextView deny;
    }

    @Override
    public int getCount() {
        return lunchRequests.size();
    }

    @Override
    public Object getItem(int position) {
        return lunchRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.orion_lunch_request_item, null);

            // Locate the TextViews in dashboard_list_item.xml
            holder.name = (TextView) view.findViewById(R.id.putname);
            holder.shift = (TextView) view.findViewById(R.id.putshift);
            holder.time= (TextView) view.findViewById(R.id.puttime);
            holder.accept = (TextView) view.findViewById(R.id.acceptbutton);
            holder.deny = (TextView) view.findViewById(R.id.denybutton);




            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(lunchRequests.get(position).getString("name"));
        holder.time.setText(lunchRequests.get(position).getString("time"));
        holder.shift.setText(lunchRequests.get(position).getString("shift"));

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchRequests.get(position).put("active", false);
                lunchRequests.get(position).put("accepted", true);


                lunchRequests.get(position).saveInBackground();

                Toast.makeText(context, "Accepted lunch request",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, OrionLunchAcceptance.class);
                context.startActivity(i);
            }
        });

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchRequests.get(position).put("active", false);
                lunchRequests.get(position).put("accepted", false);

                lunchRequests.get(position).saveInBackground();

                Toast.makeText(context, "Denied lunch request",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, OrionLunchAcceptance.class);
                context.startActivity(i);
            }
        });



        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {




            }
        });
        return view;
    }


}
