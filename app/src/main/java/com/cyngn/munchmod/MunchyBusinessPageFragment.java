package com.cyngn.munchmod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by jzhang on 5/26/16.
 */
public class MunchyBusinessPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String url = getArguments().getString("url");
        ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.munchy_listitem, container, false);
        ImageView image = (ImageView)vg.findViewById(R.id.item_icon);
        Picasso.with(container.getContext()).load(url).into(image);
        return vg;
    }


}
