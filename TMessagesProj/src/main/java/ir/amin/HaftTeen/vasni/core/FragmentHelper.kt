package ir.amin.HaftTeen.vasni.core

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.util.Log

object FragmentHelper {

    val NO_STACK = "NO_STACK"
    val NULL_TAG = "Null Tag"
    var isInSecondFragment = true

    fun loadFragment(activity: FragmentActivity, frag: Fragment?, frameId: Int, tag: String) {
        if (frag != null) {
            try {
                val fm = activity.supportFragmentManager
                //Fragment curr = fm.findFragmentById(frameId);
                //if (curr==null || (curr!=null && !curr.getTag().equals(tag))) {

                val ft = fm.beginTransaction()
                ft.replace(frameId, frag, tag)
                if (tag != NO_STACK) {
                    ft.addToBackStack(tag)
                }
                ft.commit()
                isInSecondFragment = false
                //}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun removeFragment(activity: FragmentActivity, frag: Fragment?) {
        var freagments = frag
        if (freagments != null) {
            val ft = activity.supportFragmentManager.beginTransaction()
            ft.remove(freagments).commit()
            activity.supportFragmentManager.popBackStack()
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            val count = activity.supportFragmentManager.backStackEntryCount
            if (count == 1) {
                isInSecondFragment = true
            }

        } else
            Log.d("fragment helper", "null fragment wants to be removed!!")
    }


    fun getCurrentFragmentTag(activity: FragmentActivity, frame_container: Int): String? {
        val currentFragment = activity.supportFragmentManager.findFragmentById(frame_container)
        return if (currentFragment != null)
            currentFragment.tag
        else
            NULL_TAG
    }

    fun getCurrentFragment(activity: FragmentActivity, frame_container: Int): Fragment {
        return activity.supportFragmentManager.findFragmentById(frame_container)!!

    }

    fun removeCurrentFragment(activity: FragmentActivity, frag: Fragment?): Boolean {
        var freagments = frag
        if (freagments != null) {
            val ft = activity.supportFragmentManager.beginTransaction()
            ft.remove(freagments).commit()
            activity.supportFragmentManager.popBackStack()
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            val count = activity.supportFragmentManager.backStackEntryCount
            if (count == 1) {
                isInSecondFragment = true
            }
            return true
        } else {
            Log.d("fragment helper", "null fragment wants to be removed!!")
            return false
        }
    }

}
