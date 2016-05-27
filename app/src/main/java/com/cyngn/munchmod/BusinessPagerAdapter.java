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

    List<Business> mBusinesses;
    public BusinessPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void onBusinessesLoaded(List<Business> business) {
        mBusinesses = business;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new MunchyBusinessPageFragment();
        Bundle b = new Bundle();
        b.putString("url", mBusinesses.get(position).imageUrl());
        f.setArguments(b);
        return  f;
    }

    public int getPositionForBusiness(Business b){
        for(int i = 0; i< mBusinesses.size(); i++){
            if(b.equals(mBusinesses.get(i))) return i;
        }
        return 0;
    }

    public Business getBusinessForPosition(int position){
        if (position > 0 && position < mBusinesses.size()) {
            return mBusinesses.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mBusinesses == null ? 0 : mBusinesses.size();
    }
}
