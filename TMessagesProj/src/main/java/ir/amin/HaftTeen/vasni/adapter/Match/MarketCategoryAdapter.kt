package ir.amin.HaftTeen.vasni.adapter.Match

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.row_category_media.view.*
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.vasni.core.BaseFragment
import ir.amin.HaftTeen.R


class MarketCategoryAdapter(
        manager: FragmentManager,
        frgList: ArrayList<BaseFragment>,
        categoryList: ArrayList<String>
) :

        FragmentPagerAdapter(manager) {
    private var frgList: ArrayList<BaseFragment> = frgList
    private var categoryList: ArrayList<String> = categoryList

    override fun getItem(position: Int): Fragment {
        return frgList.get(position)
    }

    override fun getCount(): Int {
        return frgList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position]
    }

    fun getTabView(position: Int): View {
        val view = LayoutInflater.from(ApplicationLoader.applicationContext).inflate(R.layout.row_category_media_market, null)
        try {
            view.txtCategoryName.setText(categoryList[position])
        } catch (e: Exception) {

        }
        return view
    }

}
