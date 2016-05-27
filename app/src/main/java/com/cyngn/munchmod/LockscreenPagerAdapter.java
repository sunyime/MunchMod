package com.cyngn.munchmod;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cyngn.munchmod.data.YelpApiClient;
import com.yelp.clientlib.entities.Business;

import java.util.List;

/**
 * Lockscreen Pager
 * Simulates 
 */
public class LockscreenPagerAdapter extends FragmentStatePagerAdapter
    implements YelpApiClient.ResultCallback
{
    private static final boolean DEBUG = true;
    private static final String TAG = "LockscreenPagerAdapter";

    /**
     * Hard-coded pages
     */
    public enum PageId {
        BUSINESS1(R.drawable.evvia),
        BUSINESS2(R.drawable.als_place_grits),
        BUSINESS3(R.drawable.als_place_salad),
        COFFEE(R.drawable.blue_bottle);

        public final int imgResId;
        PageId(int imgResId) {
            this.imgResId = imgResId;
        }

    }
    private int mDataCount = 1;

    public LockscreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return LockscreenFragment.newInstance(position);
    }

    @Override
    public void onBusinessesLoaded(List<Business> businesses) {
        mDataCount = Math.min(businesses.size() + 1, PageId.values().length);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataCount;
    }
}
