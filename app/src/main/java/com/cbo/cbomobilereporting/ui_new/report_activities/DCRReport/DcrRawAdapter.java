
package com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import com.cbo.cbomobilereporting.MyCustumApplication;


public class DcrRawAdapter extends RecyclerView.Adapter<DcrRawAdapter.MyViewHolder> {

    private ArrayList<mDcrGrid> moviesList=new ArrayList<mDcrGrid> ();
    Context context;
   public  interface itemclciked
   {

     void   OnCallclicked(String msg);
   }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre,year;
       //Button year;

        public MyViewHolder(View view) {
            super(view);
    title = (TextView) view.findViewById(R.id.title);
           // genre = (TextView) view.findViewById(R.id.genre);
 year = (TextView) view.findViewById(R.id.year);
           // year = (Button) view.findViewById(R.id.year);
        }
    }


    public DcrRawAdapter(Context context, ArrayList<mDcrGrid> moviesList) {
        this.context=context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dcr_grifd_raw, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mDcrGrid movie = moviesList.get(position);

        holder.title.setText(movie.getTitle());
       // holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getCall());
        holder.year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyCustumApplication.getInstance(), "No.of Call"+movie.getCall(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}