package ir.amin.HaftTeen.vasni.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.amin.HaftTeen.messenger.support.widget.RecyclerView;
import ir.amin.HaftTeen.vasni.model.api.playerchart.ResponseChartData;
import ir.amin.HaftTeen.R;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.MyViewHolder> {

    private ArrayList<ResponseChartData> lessonDataArrayList;

    public ChartAdapter(ArrayList<ResponseChartData> moviesList) {
        this.lessonDataArrayList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_chart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            ResponseChartData movie = lessonDataArrayList.get(position);
            holder.title.setText(movie.getUser());
            holder.star.setText("" + movie.getScore() + " ستاره ");
            holder.user_rank.setText("" + movie.getRank());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //holder.desc.setText(movie.getDescription());
    }

    @Override
    public int getItemCount() {
        return lessonDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, star, user_rank;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            star = (TextView) view.findViewById(R.id.star);
            user_rank = view.findViewById(R.id.user_rank);
        }
    }
}

