package com.cyngn.munchmod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView image = (ImageView) vg.findViewById(R.id.item_icon);
        Picasso.with(container.getContext()).load(business.imageUrl()).into(image);

        TextView tv = (TextView) vg.findViewById(R.id.name_text);
        tv.setText(business.name());

        /*
        tv = (TextView) vg.findViewById(R.id.category_text);
        tv.setText(business.categories().get(0).name());

        tv = (TextView) vg.findViewById(R.id.snippet_text);
        tv.setText(business.snippetText());
        */

        return vg;
    }


}
