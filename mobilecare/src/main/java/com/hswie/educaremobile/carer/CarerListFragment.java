package com.hswie.educaremobile.carer;


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
import com.hswie.educaremobile.adapter.CarerAdapter;
import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.helper.CarerModel;


public class CarerListFragment extends Fragment implements CarerAdapter.CarerAdapterCallbacks {

   
    private CarerAdapter carerAdapter;

    private Handler handler;
    private static final String TAG = "CarerListFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean asyncTaskWorking = false;


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
        recyclerView.setAdapter(carerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        resetCarers();

        return rootView;
    }

    public void refreshData() {
        if (!asyncTaskWorking) {
            asyncTaskWorking = true;
            new DownloadCarersList().execute();
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

    private void checkAdapterIsEmpty() {
        if (carerAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    private class DownloadCarersList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            asyncTaskWorking = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CarerRDH carerRDH = new CarerRDH();

            CarerModel.get().setCarers(carerRDH.getAllCarers());
            CarerModel.get().setCurrentCarrerMessages();
            CarerModel.get().setCurrentCarrerTasks();
            CarerModel.get().getCarerImages();

            return null;
        }

        @Override
        protected void onPostExecute(Void bitmap) {
            asyncTaskWorking = false;
            carerAdapter.resetItems();
            carerAdapter.notifyDataSetChanged();
            checkAdapterIsEmpty();
            swipeRefreshLayout.setRefreshing(false);
        }
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
