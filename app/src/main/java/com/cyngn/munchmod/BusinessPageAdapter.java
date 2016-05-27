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
public class BusinessPageAdapter extends FragmentStatePagerAdapter
                                    implements YelpApiClient.ResultCallback{

    List<Business> mBusiness;
    public BusinessPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void onBusinessesLoaded(List<Business> business) {
        mBusiness = business;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new MunchyBusinessPageFragment();
        Bundle b = new Bundle();
        b.putString("url", mBusiness.get(position).imageUrl());
        f.setArguments(b);
        return  f;
    }

    public int getPositionForBusiness(Business b){
        for(int i = 0; i< mBusiness.size();i++){
            if(b.equals(mBusiness.get(i))) return i;
        }
        return 0;
    }

    @Override
    public int getCount() {
        return mBusiness == null ? 0 : mBusiness.size();
    }
}
