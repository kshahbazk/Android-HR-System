package momenify.proconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.parse.ParseFile;
import com.parse.ParseUser;


import java.net.URI;
import java.net.URL;
import java.util.List;

import momenify.proconnect.activities.AgentProfileView;
import momenify.proconnect.navigationviewpagerliveo.R;


/**
 * Created by shahbazkhan on 5/16/15.
 */
public class OrionUniversalAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<ParseUser> universalUsers;

    public OrionUniversalAdapter(Context context,
                                 List<ParseUser> universalUsers) {

        this.context = context;
        this.universalUsers = universalUsers;
        inflater = LayoutInflater.from(context);

    }

    public class ViewHolder {
        TextView name;
        TextView timeIn;
        TextView role;
        TextView status;
        TextView timeout;


    }

       @Override
    public int getCount() {
        return universalUsers.size();
    }

       @Override
    public Object getItem(int position) {
        return universalUsers.get(position);
    }

       @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.dashboard_list_item, null);

            // Locate the TextViews in dashboard_list_item.xml
            holder.name = (TextView) view.findViewById(R.id.empInfo);
            holder.timeIn = (TextView) view.findViewById(R.id.puttimein);
            holder.role= (TextView) view.findViewById(R.id.putRole);
            holder.status = (TextView) view.findViewById(R.id.putStatus);
            holder.timeout = (TextView) view.findViewById(R.id.puttimeout);



            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(universalUsers.get(position).getString("name"));
        holder.timeIn.setText(universalUsers.get(position).getString("time_in"));
        holder.role.setText(universalUsers.get(position).getString("role"));
        holder.status.setText(universalUsers.get(position).getString("status"));
        holder.timeout.setText(universalUsers.get(position).getString("time_out"));


        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
               //  Send single item click data to UserProfileView Class
                Intent intent = new Intent(context, AgentProfileView.class);


                // Pass all data name
                intent.putExtra("name",
                        (universalUsers.get(position).getString("name")));
                // Pass all data email
                intent.putExtra("email",
                        (universalUsers.get(position).getEmail()));
                // Pass all data status
                intent.putExtra("status",
                        (universalUsers.get(position).getString("status")));
                // Pass all data lunch
                intent.putExtra("lunch",
                        (universalUsers.get(position).getString("lunch")));
                // Pass all data time_in
                intent.putExtra("time_in",
                        (universalUsers.get(position).getString("time_in")));
                // Pass all data time_out
                intent.putExtra("time_out",
                        (universalUsers.get(position).getString("time_out")));

                // Pass all data role
                intent.putExtra("role",
                        (universalUsers.get(position).getString("role")));
                // Pass all data team lead
                intent.putExtra("team_lead",
                        (universalUsers.get(position).getString("team_lead")));
                // Pass all data wave
                intent.putExtra("wave",
                        (universalUsers.get(position).getString("wave")));
                // Pass all data shift
                intent.putExtra("shift",
                        (universalUsers.get(position).getString("shift")));
                //pass objectId
                intent.putExtra("objectId", universalUsers.get(position).getObjectId());







                context.startActivity(intent);



            }
        });
        return view;
    }





}
