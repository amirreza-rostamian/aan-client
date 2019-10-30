package ir.amin.HaftTeen.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.amin.HaftTeen.messenger.support.widget.RecyclerView;
import ir.amin.HaftTeen.ui.ActionBar.Theme;
import ir.amin.HaftTeen.R;

public class BotsListAdapter extends RecyclerView.Adapter<BotsListAdapter.ViewHolder> {

    private ArrayList<String> mBots = new ArrayList<>();
    private ArrayList<String> mBotsName = new ArrayList<>();
    private ArrayList<String> mBotsLogo = new ArrayList<>();
    private Context mContext;

    public BotsListAdapter(Context context, ArrayList<String> bots) {
        if (mBotsLogo.size() > 0)
            mBotsLogo.clear();
        if (mBotsName.size() > 0)
            mBotsName.clear();
        mBotsName.add("Shoot");
        mBotsName.add("TomAAN");
        mBotsName.add("Pantomim");
        mBotsName.add("QuizBaz");
        mBotsName.add("AANhang");
        mBotsName.add("Khargoosh");
        mBotsName.add("MovafaghAAN");
        mBotsName.add("AmoozAAN");
        mBotsName.add("Begardan");
        mBotsName.add("Adiyeh");
        mBotsName.add("News");
        mBotsName.add("AANiCharge");
        mBotsName.add("Support");
        mBotsName.add("TeenTaak");

        mBotsLogo.add("bot_shoot");
        mBotsLogo.add("bot_tomaan");
        mBotsLogo.add("bot_pantomim");
        mBotsLogo.add("bot_quizbaz");
        mBotsLogo.add("bot_aanhang");
        mBotsLogo.add("bot_khargoosh");
        mBotsLogo.add("bot_movafaghaan");
        mBotsLogo.add("bot_amoozaan");
        mBotsLogo.add("bot_begardaan");
        mBotsLogo.add("bot_adeiyeh");
        mBotsLogo.add("bot_khabar");
        mBotsLogo.add("bot_aannichage");
        mBotsLogo.add("bot_support");
        mBotsLogo.add("bot_teentaak");
        mBots = bots;
        mContext = context;
    }

    public void onItemClick(int position) {
    }

    @Override
    public BotsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bot, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        BotsListAdapter.ViewHolder vh = new BotsListAdapter.ViewHolder(v);
        vh.bot_name = v.findViewById(R.id.bot_name);
        vh.bot_avatar = v.findViewById(R.id.bot_avatar);
        vh.bot_name.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
        return vh;
    }

    @Override
    public void onBindViewHolder(BotsListAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bot_name.setText(mBotsName.get(position));

        holder.bot_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });

        int logo_id = mContext.getResources().getIdentifier(mBotsLogo.get(position), "drawable", mContext.getPackageName());
        holder.bot_avatar.setImageDrawable(mContext.getResources().getDrawable(logo_id));

    }

    @Override
    public int getItemCount() {
        return mBots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bot_name;
        //        public CardView cvRoomCard;
        public ImageView bot_avatar;

        public ViewHolder(View vCard) {
            super(vCard);
//            bot_name = (CardView) vCard;
            bot_name = (TextView) vCard.findViewById(R.id.bot_name);
            bot_avatar = (ImageView) vCard.findViewById(R.id.bot_avatar);
        }
    }
}
