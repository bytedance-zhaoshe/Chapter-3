package com.example.chapter3.homework;


import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    public static String OPTIONS_SHOW_LOADING = "show_loading_option";

    public static int ITEM_COUNT = 30;

    public static final PlaceholderFragment getInstance(boolean showLoading) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(OPTIONS_SHOW_LOADING, showLoading);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean showLoading = false;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LottieAnimationView animationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading = getArguments().getBoolean(OPTIONS_SHOW_LOADING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        animationView = view.findViewById(R.id.animation_view);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        animationView.setVisibility(showLoading ? View.VISIBLE : View.GONE);
        if (showLoading) {
            animationView.setRepeatCount(LottieDrawable.INFINITE);
            animationView.setRepeatMode(LottieDrawable.REVERSE);
            animationView.playAnimation();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (showLoading) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 这里会在 5s 后执行
                    // ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                    ObjectAnimator animator = ObjectAnimator.ofFloat(animationView, "alpha", 1f, 0f);
                    animator.setDuration(1000);
                    animator.start();
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }, 5000);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.bind(i);
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(ViewGroup parent) {
            super(new TextView(parent.getContext()));
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        }

        public void bind(int position) {
            TextView view = ((TextView) itemView);
            view.setTextSize(24);
            view.setTextColor(Color.parseColor("#0288D1"));
            view.setGravity(Gravity.CENTER);
            view.setText("Item" + (position + 1));
        }
    }
}
