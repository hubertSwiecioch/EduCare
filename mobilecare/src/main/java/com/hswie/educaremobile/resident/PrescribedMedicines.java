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
import com.hswie.educaremobile.adapter.MedicineAdapter;
import com.hswie.educaremobile.api.dao.ResidentRDH;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.ResidentsModel;


public class PrescribedMedicines extends Fragment implements MedicineAdapter.MedicineAdapterCallbacks {


    private static final String TAG = "PrescribedMedicines";

    private RecyclerView medicineRV;
    private TextView emptyTV;
    private MedicineAdapter adapter;


    private Context context;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean asyncTaskWorking = false;

    private Handler handler;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static PrescribedMedicines newInstance(String param1, String param2) {
        PrescribedMedicines fragment = new PrescribedMedicines();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PrescribedMedicines() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "msg = " + msg.toString());
                adapter.notifyDataSetChanged();
            }
        };

        adapter = new MedicineAdapter(context, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        context = getActivity().getApplicationContext();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });

        emptyTV = (TextView)rootView.findViewById(R.id.text_empty_list);
        emptyTV.setText(R.string.medicine_empty);

        medicineRV = (RecyclerView) rootView.findViewById(R.id.list);
        medicineRV.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MedicineAdapter(context, this);
        medicineRV.setAdapter(adapter);
        medicineRV.setItemAnimator(new DefaultItemAnimator());

        checkAdapterIsEmpty();

        return rootView;
    }

    public void refreshData() {
        Log.d(TAG, "refreshData");
        if (!asyncTaskWorking) {
            asyncTaskWorking = true;
            new DownloadMedicines().execute();
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
        if (adapter.getItemCount() == 0) {
            emptyTV.setVisibility(View.VISIBLE);
            medicineRV.setVisibility(View.GONE);
        } else {
            emptyTV.setVisibility(View.GONE);
            medicineRV.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onListItemClick(int position) {

        new RemoveMedicine().execute(ResidentsModel.get().getCurrentResident().getMedicines().get(position).getId());
        ResidentsModel.get().getCurrentResident().getMedicines().remove(position);
        adapter.notifyDataSetChanged();

    }


    private class RemoveMedicine extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {

            ResidentRDH residentRDH = new ResidentRDH();
            residentRDH.removeResidentMedicine(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            asyncTaskWorking = true;

        }

    }

    private class DownloadMedicines extends AsyncTask<Void, Void, Void> {
        private Resident resident;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            asyncTaskWorking = true;
            resident = ResidentsModel.get().getCurrentResident();
        }

        @Override
        protected Void doInBackground(Void... params) {


                ResidentRDH residentRDH = new ResidentRDH();
                resident.setMedicines(residentRDH.getResidentMedicines(resident.getID()));
                ResidentsModel.get().getCurrentResident().setMedicines(resident.getMedicines());

            return null;
        }

        @Override
        protected void onPostExecute(Void bitmap) {
            if (isCancelled()){
                Log.d(TAG, "DownloadPills isCancelled");
                return;
            }

            ResidentsModel.get().setCurrentResident(resident);

            asyncTaskWorking = false;
            adapter.resetItems();
            adapter.notifyDataSetChanged();
            checkAdapterIsEmpty();
            swipeRefreshLayout.setRefreshing(false);

        }
    }


}
