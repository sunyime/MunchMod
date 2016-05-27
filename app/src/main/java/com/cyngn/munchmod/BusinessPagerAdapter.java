package com.cyngn.munchmod;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.cyngn.munchmod.data.YelpApiClient;
import com.yelp.clientlib.entities.Business;

import java.util.List;

/**
 * Created by jzhang on 5/26/16.
 */
public class BusinessPagerAdapter extends FragmentStatePagerAdapter
                                    implements YelpApiClient.ResultCallback{

    private int mDataCount = 0;

    public BusinessPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void onBusinessesLoaded(List<Business> business) {
        mDataCount = business.size();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new MunchyBusinessPageFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return  f;
    }

    @Override
    public int getCount() {
        return mDataCount;
    }
}
