package com.cyngn.munchmod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyngn.munchmod.utils.AndroidUtils;
import com.cyngn.munchmod.utils.LocationUtils;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

/**
 * Created by jzhang on 5/26/16.
 */
public class MunchyBusinessPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int position = getArguments().getInt("position");
        final Business business = ((MunchApp)getActivity().getApplication()).getYelpApiClient().getBusinessAt(position);
        final ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.munchy_listitem_new, container, false);

        // Populate view
        ImageView img = (ImageView) vg.findViewById(R.id.item_icon);
        Picasso.with(container.getContext()).load(business.imageUrl()).into(img);

        img = (ImageView) vg.findViewById(R.id.rating_img);
        if (img != null) {
            String url = business.ratingImgUrlLarge();
            if (url == null) {
                img.setVisibility(View.GONE);
            } else {
                Picasso.with(getContext()).load(url).into(img);
            }
        }

        TextView tv = (TextView) vg.findViewById(R.id.name_text);
        tv.setText(business.name());

        tv = (TextView) vg.findViewById(R.id.category_text);
        if (tv != null) {
            tv.setText(business.categories().get(0).name());
        }

        tv = (TextView) vg.findViewById(R.id.snippet_text);
        if (tv != null) {
            tv.setText(business.snippetText());
        }

        tv = (TextView) vg.findViewById(R.id.map_action);
        final String distanceStr = LocationUtils.formatDistanceTimeText(
                getActivity(),
                LocationUtils.coordinateToLatLng(business.location().coordinate()),
                LocationUtils.locationToLatLng(((MunchApp) getActivity().getApplication()).getCurrentLocationClient().getLastLocation())
        );
        tv.setText(distanceStr);
        final String geoUri = "geo:0,0?q=" + business.location().coordinate().latitude()
                + ","
                + business.location().coordinate().longitude()
                + "("
                + business.location().displayAddress().get(0)
                + ")";

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.startUriActivity(getActivity(), geoUri);
            }
        });

        tv = (TextView) vg.findViewById(R.id.takeout_action);
        if (business.eat24Url() != null) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidUtils.startUriActivity(getActivity(), business.eat24Url());
                }
            });
        }
        else {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidUtils.startCallActivity(getActivity(), business.phone());
                }
            });
        }

        tv = (TextView) vg.findViewById(R.id.reserve_action);
        if (business.reservationUrl() != null) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidUtils.startUriActivity(getActivity(), business.reservationUrl());
                }
            });
        }
        else {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidUtils.startCallActivity(getActivity(), business.phone());
                }
            });
        }
        /*
        tv = (TextView) vg.findViewById(R.id.call_action);
        //tv.setText(business.displayPhone());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.startCallActivity(getActivity(), business.phone());
            }
        });*/
        return vg;
    }


}
