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
import com.hswie.educaremobile.adapter.ResidentAdapter;


public class ResidentListFragment extends Fragment implements ResidentAdapter.ResidentAdapterCallbacks {

   
    private ResidentAdapter residentAdapter;

    private Handler handler;
    private static final String TAG = "ResidentListFragment";
    private RecyclerView recyclerView;


    public static ResidentListFragment newInstance(int page, String title) {
        ResidentListFragment residentListFragment = new ResidentListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        residentListFragment.setArguments(args);
        return residentListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "msg = " + msg.toString());
                residentAdapter.notifyDataSetChanged();
            }
        };

        setHasOptionsMenu(true);
        residentAdapter = new ResidentAdapter(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_persons_list,
                container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(residentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        resetResidents();

        return rootView;
    }

    public void resetResidents(){
        residentAdapter.resetItems();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(int position) {

        Log.d(TAG, "Click: " + residentAdapter.getItem(position).getFirstName());
    }




}
