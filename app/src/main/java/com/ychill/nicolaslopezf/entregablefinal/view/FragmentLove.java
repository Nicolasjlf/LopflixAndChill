package com.ychill.nicolaslopezf.entregablefinal.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Nicolas Lopez F on 12/6/2016.
 */

public class FragmentLove extends Fragment{

    private ImageView imagenLove;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewADevolverInflado = inflater.inflate(R.layout.fragment_love, container, false);

        imagenLove = (ImageView) viewADevolverInflado.findViewById(R.id.fragmentLove_imageview);

        Picasso.with(getActivity()).load("http://4.bp.blogspot.com/-PEl-ZiKzpZk/Uzn3hDhswPI/AAAAAAAAKDI/eRqHgR_wwak/s1600/Movie.png")
                .into(imagenLove);



        return viewADevolverInflado;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.rotate_and_translate);
        animator.setTarget(imagenLove);
        animator.start();
        super.onViewCreated(view, savedInstanceState);
    }
}
