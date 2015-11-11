package com.hswie.educaremobile.resident;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.DrawableConverter;
import com.hswie.educaremobile.helper.ImageHelper;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OverviewFragment extends Fragment {
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


    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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

        setData();

        return rootView;
    }

    private void setData() {
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

        }
        catch(NullPointerException e){
            Log.e(TAG, "NullPointerException ", e);
        }
    }


}



