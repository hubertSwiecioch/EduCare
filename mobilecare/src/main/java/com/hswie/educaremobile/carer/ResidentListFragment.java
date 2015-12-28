package com.hswie.educaremobile.carer;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.ResidentAdapter;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.dao.ResidentRDH;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.NetworkHelper;
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

        Intent myIntent = new Intent(getActivity(), ResidentActivity.class);
        getActivity().startActivity(myIntent);
    }

    @Override
    public void onListItemLongClick(final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(residentAdapter.getItem(position).getFirstName() +
                " " + residentAdapter.getItem(position).getLastName() );
        alertDialogBuilder.setMessage(getString(R.string.remove_question));
        alertDialogBuilder.setIcon(R.drawable.ic_delete_black_24dp );

        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d(TAG, "removeItem " + residentAdapter.getItem(position).getID());
                if (!asyncTaskWorking) {
                    asyncTaskWorking = true;

                    new RemoveResident().execute(residentAdapter.getItem(position).getID());
                }

            }
        });


        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class RemoveResident extends AsyncTask<String, Void, Void>{
        ResidentRDH residentRDH = new ResidentRDH();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncTaskWorking = true;

        }

        @Override
        protected Void doInBackground(String... params) {

            if(NetworkHelper.isConnectedToNetwork(getContext())) {
                try {
                    residentRDH.removeResident(params[0]);
                } catch (Exception e) {

                    cancel(true);
                }
            }else
                cancel(true);


            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            asyncTaskWorking = false;
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            asyncTaskWorking = false;
            refreshData();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), getString(R.string.done), Toast.LENGTH_LONG).show();
        }
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

            try {
                ResidentsModel.get().setResidents(residentRDH.getAllResidents());
                ResidentsModel.get().getResidentsImages(getContext());
            }catch (Exception e){

                cancel(true);
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            asyncTaskWorking = false;
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPostExecute(Void bitmap) {

            asyncTaskWorking = false;
            residentAdapter.resetItems();
            residentAdapter.notifyDataSetChanged();
            checkAdapterIsEmpty();
            swipeRefreshLayout.setRefreshing(false);
        }
    }




}
