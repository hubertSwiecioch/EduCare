package com.hswie.educaremobile.resident;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.CarerTasksAdapter;
import com.hswie.educaremobile.adapter.ResidentAdapter;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.dialog.TaskDialog;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DrawableConverter;
import com.hswie.educaremobile.helper.ImageHelper;
import com.hswie.educaremobile.helper.PreferencesManager;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class OverviewFragment extends Fragment implements CarerTasksAdapter.CarerTasksAdapterCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OverviewFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView nameTV, addressTV,emptyTV;
    private ImageView photoIV;

    private Handler handler;
    private Context context;
    private RecyclerView messagesRV;
    private CarerTasksAdapter adapter;
    private TaskDialog taskDialog;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean asyncTaskWorking = false;


    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d(TAG, "CurrnetResidentID: " + ResidentsModel.get().getCurrentResident().getID());
        return fragment;
    }

    public OverviewFragment() {
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

        adapter = new CarerTasksAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity();
        View rootView =  inflater.inflate(R.layout.fragment_overview, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });

        nameTV = (TextView)rootView.findViewById(R.id.nameTV);
        addressTV = (TextView)rootView.findViewById(R.id.text_address);
        emptyTV = (TextView)rootView.findViewById(R.id.text_empty_list);
        photoIV = (ImageView)rootView.findViewById(R.id.overviewResidentPhoto);

        setData(rootView);

        return rootView;
    }

    private void checkAdapterIsEmpty() {
        if (adapter.getItemCount() == 0) {
            emptyTV.setVisibility(View.VISIBLE);
            messagesRV.setVisibility(View.GONE);
        } else {
            emptyTV.setVisibility(View.GONE);
            messagesRV.setVisibility(View.VISIBLE);
        }

        emptyTV.setText(R.string.tasks_empty);
    }


    private void setData(View rootView) {
        Log.d(TAG, "setData");
        try {
            Resident resident = ResidentsModel.get().getCurrentResident();


            Bitmap bitmap;
            if(resident.getPhotoCache() == null || resident.getPhotoCache().isEmpty()){
                bitmap = DrawableConverter.drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_person_black_48dp));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, ImageHelper.AVATAR_QUALITY, stream);

                String cachePath = ImageHelper.cacheImageOnDisk(context, stream.toByteArray(),
                        "resident_" + resident.getID() + ".jpg",
                        ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_QUALITY);
                resident.setPhotoCache(cachePath);
                try {
                    stream.close();
                    stream = null;
                }
                catch(IOException e){
                    Log.e(TAG, "IOException e = ", e);
                }
                bitmap = null;
            }

            bitmap = BitmapFactory.decodeFile(resident.getPhotoCache());
            photoIV.setImageBitmap(bitmap);
//            ImageManager.get().displayImage("file://" + resident.getPhotoCache(), photoIV);


            nameTV.setText(resident.getFirstName() + " " + resident.getLastName());
            addressTV.setText(resident.getAddress() + ", " + resident.getCity());

            messagesRV = (RecyclerView) rootView.findViewById(R.id.tasksRV);
            messagesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new CarerTasksAdapter(this);
            Log.d(TAG, "AdapterItemCount: " +  adapter.getItemCount());
            messagesRV.setAdapter(adapter);
            messagesRV.setItemAnimator(new DefaultItemAnimator());

            resetTasks();
            checkAdapterIsEmpty();

        }
        catch(NullPointerException e){
            Log.e(TAG, "NullPointerException ", e);
        }
    }

    public void refreshData() {
        if (!asyncTaskWorking) {
            asyncTaskWorking = true;
            new GetCarerTasks().execute();
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

    public void resetTasks(){
        adapter.resetItems();
    }

    @Override
    public void onListItemClick(int position) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        taskDialog = TaskDialog.newInstance(adapter.getItem(position));
        taskDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.EduCareAppDialog);
        taskDialog.callback  = new TaskDialog.DismissCallback() {
            @Override
            public void dismissTaskDialog() {

                Log.d(TAG, "DISMISS");
                refreshData();

            }
        };
        taskDialog.show(fragmentTransaction, "taskDialog");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestory");

//        if(taskDialog != null)
//            taskDialog.dismiss();

    }

    private class GetCarerTasks extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            asyncTaskWorking = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                CarerModel.get().setCurrentCarrerTasks();

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
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            asyncTaskWorking = false;
            adapter.resetItems();
            adapter.notifyDataSetChanged();
            checkAdapterIsEmpty();
            swipeRefreshLayout.setRefreshing(false);

        }
    }


}



