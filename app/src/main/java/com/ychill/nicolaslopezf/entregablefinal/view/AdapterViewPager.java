package com.ychill.nicolaslopezf.entregablefinal.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ychill.nicolaslopezf.entregablefinal.view.YouTube.YouTubeFragment;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsParaSeries.FragmentRecycleSeries;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class AdapterViewPager extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentsDelAdapter;

    public void agregameUnFragment(Fragment fragment){
        fragmentsDelAdapter.add(fragment);
    }

    public AdapterViewPager(FragmentManager fm) {
        super(fm);
        ArrayList<Fragment> fragments = new ArrayList<>();
        FragmentRecyclerSoloImagen fragmentRecyclerSoloImagen = new FragmentRecyclerSoloImagen();
        fragments.add(new FragmentRecyclerSoloImagen());
//        fragments.add(new FragmentRecycleSeries());
        fragments.add(new FragmentRecycleGridFavoritas());
        fragments.add(new FragmentRecyclerUsuario());
        fragments.add(new FragmentRecyclerUsuariosConection());

        this.fragmentsDelAdapter = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentsDelAdapter.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsDelAdapter.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle = null;

        switch (position){
            case 0:
                tabTitle = "Peliculas";
                break;
            case 1:
                tabTitle = "Favoritos";
                break;
            case 2:
                tabTitle = "Cerca Tuyo";
                break;
            case 3:
                tabTitle = "Matches";
        }
        return tabTitle;
    }

    public ArrayList<Fragment> getFragmentsDelAdapter() {
        return fragmentsDelAdapter;
    }

    public void setFragmentsDelAdapter(ArrayList<Fragment> fragmentsDelAdapter) {
        this.fragmentsDelAdapter = fragmentsDelAdapter;
    }
}
