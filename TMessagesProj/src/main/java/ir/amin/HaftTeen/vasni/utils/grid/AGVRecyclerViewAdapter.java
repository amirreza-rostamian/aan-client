package ir.amin.HaftTeen.vasni.utils.grid;

import android.support.v7.widget.RecyclerView;

public abstract class AGVRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public abstract AsymmetricItem getItem(int position);
}
