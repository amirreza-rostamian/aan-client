package ir.amin.HaftTeen.vasni.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ir.amin.HaftTeen.vasni.model.grid.TileModel;
import ir.amin.HaftTeen.vasni.utils.Function;
import ir.amin.HaftTeen.R;

public class AsymmetricListAdapter extends BaseAdapter {

    private static final String TAG = AsymmetricListAdapter.class.getSimpleName();
    private ArrayList<TileModel> listData;
    private LayoutInflater layoutInflater;
    private Context cntx;

    public AsymmetricListAdapter(Context context, ArrayList<TileModel> listData) {
        this.listData = listData;
        this.cntx = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View container, ViewGroup parent) {
        View view = container;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.adapter_item_asymmetric, null, false);
        }
        final TileModel item = listData.get(position);
        ProgressBar pb_grid_asymetric = view.findViewById(R.id.pb_grid_asymetric);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Function.LoadImage(cntx, item.getIcon(), pb_grid_asymetric, imageView);
        return view;
    }


}
