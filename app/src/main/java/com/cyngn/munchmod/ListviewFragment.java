package com.cyngn.munchmod;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cyngn.munchmod.data.MunchyAdapter;

import java.util.List;

/**
 * Created by jzhang on 5/25/16.
 */
public class ListviewFragment extends Fragment {

    private static final String FRAGMENT_TAG = ListviewFragment.class.getSimpleName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMunchy();
    }

    private void loadMunchy() {
        MunchApp app = (MunchApp)getActivity().getApplication();
//        app.getYelpApiClient().loadPlaces();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.munchy_listview, null);
        rootView.setTag(FRAGMENT_TAG);

        RecyclerView recylerView = (RecyclerView)rootView.findViewById(R.id.recylerlist);

        recylerView.setAdapter(new MunchyAdapter());
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
