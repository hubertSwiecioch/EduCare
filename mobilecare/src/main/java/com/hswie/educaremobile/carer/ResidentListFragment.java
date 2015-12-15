package com.hswie.educaremobile.carer;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.ResidentAdapter;
import com.hswie.educaremobile.api.dao.ResidentRDH;
import com.hswie.educaremobile.helper.PreferencesManager;
import com.hswie.educaremobile.helper.ResidentsModel;
import com.hswie.educaremobile.resident.ResidentActivity;


public class ResidentListFragment extends Fragment implements ResidentAdapter.ResidentAdapterCallbacks {

   
    private ResidentAdapter residentAdapter;

    private Handler handler;
    private static final String TAG = "ResidentListFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean asyncTaskWorking = false;



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

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(residentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        resetResidents();

        return rootView;
    }

    public void refreshData() {
        if (!asyncTaskWorking) {
            asyncTaskWorking = true;
            new DownloadResidentsList().execute();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler = null;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        refreshData();

    }

    public void resetResidents(){
        residentAdapter.resetItems();
    }

    private void checkAdapterIsEmpty() {
        if (residentAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(int position) {

        Log.d(TAG, "Click: " + residentAdapter.getItem(position).getFirstName());
        PreferencesManager.setCurrentResidentIndex(Integer.parseInt(residentAdapter.getItem(position).getID()));
        ResidentsModel.get().setCurrentResidentIndex((position));
        //ResidentsModel.get().setCurrentResident(residentAdapter.getItem(position));

        Intent myIntent = new Intent(getActivity(), ResidentActivity.class);
        getActivity().startActivity(myIntent);
    }

    private class DownloadResidentsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            asyncTaskWorking = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
           ResidentRDH residentRDH = new ResidentRDH();

            ResidentsModel.get().setResidents(residentRDH.getAllResidents());

            ResidentsModel.get().getResidentsImages();

            return null;
        }

        @Override
        protected void onPostExecute(Void bitmap) {
            residentAdapter.notifyDataSetChanged();

            asyncTaskWorking = false;
            checkAdapterIsEmpty();
            residentAdapter.resetItems();
            swipeRefreshLayout.setRefreshing(false);
        }
    }




}
