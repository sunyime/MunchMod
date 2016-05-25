package com.cyngn.munchmod.data;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.yelp.clientlib.entities.Business;

import java.util.List;

/**
 * Created by jzhang on 5/25/16.
 */
public class MunchyAdapter  extends RecyclerView.Adapter<MunchyAdapter.MunchyItemHoldler>
                            implements YelpApiClient.ResultCallback{


    private static final String TAG = MunchyAdapter.class.getSimpleName();

    @Override
    public MunchyItemHoldler onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MunchyItemHoldler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBusinessesLoaded(List<Business> business) {
        //TODO: get data from yelp sdk.
        Log.d(TAG, "onBusinessesLoaded() called with " + "business = [" + business + "], items returned" + business.size() );
    }

    public static class MunchyItemHoldler extends  RecyclerView.ViewHolder{

        @Override
        public String toString() {
            return super.toString();
        }

        public MunchyItemHoldler(View itemView) {
            super(itemView);
        }
    }
}
