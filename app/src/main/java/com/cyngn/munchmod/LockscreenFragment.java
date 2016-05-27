package com.cyngn.munchmod;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;


/**
 * Full page view
 */
public class LockscreenFragment extends Fragment {

    private static final boolean DEBUG = true;
    private static final String TAG = "LockscreenFragment";


    public static LockscreenFragment newInstance(int position) {
        if (DEBUG) {
            Log.d(TAG, "newInstance position=" + position);
        }
        LockscreenFragment fragment = new LockscreenFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int position = getArguments().getInt("position");
        Resources res = getResources();
        ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.lockscreen_listitem, container, false);

        if (position == 0) {
            View v = vg.findViewById(R.id.data_container);
            v.setVisibility(View.GONE);
        }
        else {

            LockscreenPagerAdapter.PageId pageId = LockscreenPagerAdapter.PageId.values()[
                    position % LockscreenPagerAdapter.PageId.values().length
                    ];

            final Business business = ((MunchApp) getActivity().getApplication()).
                    getYelpApiClient().getBusinessAt(position - 1);


            ImageView img = (ImageView) vg.findViewById(R.id.background_img);
            img.setImageResource(pageId.imgResId);

            img = (ImageView) vg.findViewById(R.id.rating_img);
            String url = business.ratingImgUrlLarge();
            if (url == null) {
                img.setVisibility(View.GONE);
            } else {
                Picasso.with(getContext()).load(url).into(img);
            }

            TextView tv = (TextView) vg.findViewById(R.id.name_text);
            tv.setText(business.name());

            tv = (TextView) vg.findViewById(R.id.category_text);
            tv.setText(business.categories().get(0).name());

            tv = (TextView) vg.findViewById(R.id.snippet_text);
            tv.setText(business.snippetText());
        }

        return vg;
    }

}
