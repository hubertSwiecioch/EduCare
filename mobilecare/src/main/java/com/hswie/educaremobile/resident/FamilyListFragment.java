package com.hswie.educaremobile.resident;


import android.content.Context;
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
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.FamilyAdapter;
import com.hswie.educaremobile.api.dao.FamilyRDH;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.ResidentsModel;


public class FamilyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FamilyListFragment" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private FamilyAdapter familyAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Context context;

    public static Handler handler;

    private RecyclerView familyListView;
    private TextView emptyTV;

    public boolean asyncTaskWorking = false;


    public static FamilyListFragment newInstance(String param1, String param2) {
        FamilyListFragment fragment = new FamilyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FamilyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                familyAdapter.notifyDataSetChanged();
            }
        };

        setHasOptionsMenu(true);
        familyAdapter = new FamilyAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_list,
                container, false);

        context = getActivity();

//        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData();
//            }
//        });

        emptyTV = (TextView) rootView.findViewById(R.id.text_empty_list);
        emptyTV.setText(R.string.empty_family_list);

        familyListView = (RecyclerView) rootView.findViewById(R.id.list);
        familyListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        familyListView.setAdapter(familyAdapter);
        familyListView.setItemAnimator(new DefaultItemAnimator());

//        new ResidentRDH(context);
        resetfamily();
        checkAdapterIsEmpty();

        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void checkAdapterIsEmpty() {
        if (familyAdapter.getItemCount() == 0) {
            emptyTV.setVisibility(View.VISIBLE);
            familyListView.setVisibility(View.GONE);
        } else {
            emptyTV.setVisibility(View.GONE);
            familyListView.setVisibility(View.VISIBLE);
        }
    }


    public void resetfamily(){
        familyAdapter.resetItems();
    }


    private void downloadFamily() {
        if (!asyncTaskWorking)
            new DownloadFamily().execute();
    }


    private class DownloadFamily extends AsyncTask<Void, Void, Void> {
        private Resident resident;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            asyncTaskWorking = true;
            resident = ResidentsModel.get().getCurrentResident();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FamilyRDH familyRDH = new FamilyRDH();

            resident.setFamilies(familyRDH.getResidentFamily(resident.getID()));

            return null;
        }

        @Override
        protected void onPostExecute(Void bitmap) {
            if (isCancelled()){
                Log.d(TAG, "DownloadPills isCancelled");
                return;
            }

            ResidentsModel.get().setCurrentResident(resident);

            familyAdapter.notifyDataSetChanged();

            asyncTaskWorking = false;
            checkAdapterIsEmpty();

            //swipeRefreshLayout.setRefreshing(false);
        }
    }



    public void refreshData() {

        downloadFamily();
    }




}
