package com.example.atd.infoway;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atd on 9/20/2016.
 */

public class PendingFrag extends Fragment {

    UsersDBHelper helpher;
    List<Item> dbList;
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_pending, container, false);

        helpher = UsersDBHelper.getInstance(getContext());
        dbList= new ArrayList<Item>();
        dbList = helpher.getUserItems(User.getInstance());


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ItemsAdapter(getContext(),dbList);

        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }



}