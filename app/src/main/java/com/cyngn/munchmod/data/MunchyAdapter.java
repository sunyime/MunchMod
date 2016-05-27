package com.cyngn.munchmod.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import com.cyngn.munchmod.MunchApp;
import com.cyngn.munchmod.R;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by jzhang on 5/25/16.
 */
public class MunchyAdapter  extends RecyclerView.Adapter<MunchyAdapter.MunchyItemHoldler>
                            implements YelpApiClient.ResultCallback{


    private static final String TAG = MunchyAdapter.class.getSimpleName();



    List<Business> mBusiness;

    public MunchyAdapter(Context c){
        MunchApp app = (MunchApp)c.getApplicationContext();
       // app.getYelpApiClient().loadPlaces(null, "food,lunch");

    }
    @Override
    public MunchyItemHoldler onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.munchy_listitem, parent, false);
        MunchyItemHoldler holder = new MunchyItemHoldler(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(MunchyItemHoldler holder, int position) {
        Business b = mBusiness.get(position);

        Picasso.with(holder.itemView.getContext()).load(b.imageUrl()).into(holder.imageView);
        runEnterAnimation(holder.itemView);
    }

    private void runEnterAnimation(View view) {
        view.setTranslationY(320);
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    @Override
    public int getItemCount() {
        return  mBusiness == null ? 0 : mBusiness.size();
    }

    @Override
    public void onBusinessesLoaded(List<Business> business) {
        //TODO: get data from yelp sdk.
        Log.d(TAG, "onBusinessesLoaded() called with " + "business = [" + business + "], items returned" + business.size() );
        mBusiness = business;
        notifyDataSetChanged();
    }

    public static class MunchyItemHoldler extends  RecyclerView.ViewHolder{

        ImageView imageView;
        @Override
        public String toString() {
            return super.toString();
        }

        public MunchyItemHoldler(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.item_icon);
        }
    }
}
