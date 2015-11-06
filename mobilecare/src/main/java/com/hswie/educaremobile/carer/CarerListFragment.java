package com.hswie.educaremobile.carer;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.CarerAdapter;



public class CarerListFragment extends Fragment implements CarerAdapter.CarerAdapterCallbacks {

   
    private CarerAdapter carerAdapter;

    private Handler handler;
    private static final String TAG = "CarerListFragment";
    private RecyclerView carersListView;


    public static CarerListFragment newInstance(int page, String title) {
        CarerListFragment carerListFragment = new CarerListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        carerListFragment.setArguments(args);
        return carerListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "msg = " + msg.toString());
                carerAdapter.notifyDataSetChanged();
            }
        };

        setHasOptionsMenu(true);
        carerAdapter = new CarerAdapter(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_carer_list,
                container, false);

        carersListView = (RecyclerView) rootView.findViewById(R.id.list);
        carersListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        carersListView.setAdapter(carerAdapter);
        carersListView.setItemAnimator(new DefaultItemAnimator());

        resetCarers();

        return rootView;
    }

    public void resetCarers(){
        carerAdapter.resetItems();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(int position) {

        Log.d(TAG, "Click: " + carerAdapter.getItem(position).getFullName());
    }




}
