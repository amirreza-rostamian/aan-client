package ir.amin.HaftTeen.vasni.utils.grid;

import android.os.Parcelable;

public interface AsymmetricItem extends Parcelable {
    int getColumnSpan();

    int getRowSpan();
}
