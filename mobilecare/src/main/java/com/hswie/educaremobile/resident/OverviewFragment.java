package com.hswie.educaremobile.resident;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.CarerTasksAdapter;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.dao.ResidentRDH;
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

    private Context context;
    private RecyclerView messagesRV;
    private CarerTasksAdapter adapter;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity();
        View rootView =  inflater.inflate(R.layout.fragment_overview, container, false);

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
                bitmap = DrawableConverter.drawableToBitmap(context.getResources().getDrawable(R.drawable.noavatar));
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

            checkAdapterIsEmpty();

        }
        catch(NullPointerException e){
            Log.e(TAG, "NullPointerException ", e);
        }
    }

    @Override
    public void onListItemClick(int position) {

        TaskDialog newFragment = TaskDialog.newInstance(adapter.getItem(position));
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent, newFragment).commit();
        fragmentManager.beginTransaction().show(newFragment).commit();

    }

    private class GetCarerTasks extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... params) {

            CarerTasksRDH carerTasksRDH = new CarerTasksRDH();
            Log.d(TAG, "CurrentCarerPreferencesManager: " + PreferencesManager.getCurrentCarerID());
            Log.d(TAG, "CurrentCarerCarerModel: " + CarerModel.get().getCurrentCarer().getID());

            ArrayList<CarerTask> carerTasks = carerTasksRDH.getCarerTasks(String.valueOf(PreferencesManager.getCurrentCarerID()));
            CarerModel.get().getCurrentCarer().setCarerTasks(carerTasks);
            Log.d(TAG, "CurrentCarerTasksL: " + CarerModel.get().getCurrentCarer().getCarerTasks().get(0).getHeader());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


}



