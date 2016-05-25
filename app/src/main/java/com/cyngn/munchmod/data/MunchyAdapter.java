package com.cyngn.munchmod.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jzhang on 5/25/16.
 */
public class MunchyAdapter  extends RecyclerView.Adapter<MunchyAdapter.MunchyItemHoldler>{


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
