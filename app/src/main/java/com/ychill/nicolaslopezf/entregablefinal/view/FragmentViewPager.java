package com.ychill.nicolaslopezf.entregablefinal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ychill.nicolaslopezf.entregablefinal.R;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class FragmentViewPager extends Fragment {

    private ViewPager viewPager;
    private AdapterViewPager adapterViewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vistaADevolver = inflater.inflate(R.layout.fragment_view_pager,container,false);

        viewPager = (ViewPager) vistaADevolver.findViewById(R.id.viewpager);

        TabLayout tabLayout= (TabLayout) vistaADevolver.findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);

        adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(adapterViewPager);

        return vistaADevolver;
    }

    @Override
    public void onResume() {
        viewPager.setAdapter(new AdapterViewPager(getActivity().getSupportFragmentManager()));
        super.onResume();
    }

    public AdapterViewPager getAdapterViewPager() {
        return adapterViewPager;
    }

    public void setAdapterViewPager(AdapterViewPager adapterViewPager) {
        this.adapterViewPager = adapterViewPager;
    }
}
