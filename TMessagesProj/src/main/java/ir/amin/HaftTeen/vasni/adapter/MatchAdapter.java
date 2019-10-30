package ir.amin.HaftTeen.vasni.adapter;


import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.messenger.support.widget.RecyclerView;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetQuestionData;
import ir.amin.HaftTeen.vasni.utils.Function;
import ir.amin.HaftTeen.R;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private ArrayList<ResponseGetQuestionData> matches = new ArrayList<>();
    private OnClickListenerItemMatch listenerItemMatch;

    public MatchAdapter(ArrayList<ResponseGetQuestionData> matches) {
        this.matches = matches;
        this.listenerItemMatch = listenerItemMatch;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_match_name.setText(matches.get(position).getName());
        if (matches.get(position).getThumbnail().endsWith(".gif")) {
            Function.LoadGif(ApplicationLoader.applicationContext, matches.get(position).getThumbnail(), holder.img_logo);
        } else {
            Function.loadImage(holder.img_logo, matches.get(position).getThumbnail());
        }
        holder.txt_match_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public interface OnClickListenerItemMatch {
        void onClick(ResponseGetQuestionData data);
//    void onClick(ArrayList<ResponseGetQuestionDataQuestions> question, boolean isOnline);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_logo;
        private TextView txt_match_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.img_match_adapter_logo);
            txt_match_name = itemView.findViewById(R.id.txt_match_adapter_name);
        }
    }

}