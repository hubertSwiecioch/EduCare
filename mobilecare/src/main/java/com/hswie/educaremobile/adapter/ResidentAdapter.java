package com.hswie.educaremobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.DrawableConverter;
import com.hswie.educaremobile.helper.ImageHelper;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Residents ListView's adapter.
 */
public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ViewHolder> {
    private static final String TAG = "ResidentAdapter";


//    public static AnimationDrawable frameAnimation;

    public interface ResidentAdapterCallbacks {
        void onListItemClick(int position);
    }

    int itemSelected = -1;
    private ArrayList<Resident> items = new ArrayList<>();
    private Context context;
    private ResidentAdapterCallbacks residentAdapterCallbacks;

    public ResidentAdapter(ResidentAdapterCallbacks residentAdapterCallbacks) {
        this.residentAdapterCallbacks = residentAdapterCallbacks;
        items = ResidentsModel.get().getResidents();

    }

    @Override
    public ResidentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.resident_list_item, null);

        context = parent.getContext();


        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Resident resident = items.get(position);

        viewHolder.parentLayout.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        residentAdapterCallbacks.onListItemClick(position);
                    }
                });

        viewHolder.firstnameView.setText(resident.getFirstName());
        viewHolder.lastnameView.setText(resident.getLastName());

        Bitmap bitmap;
        if(resident.getPhotoCache() == null || resident.getPhotoCache().isEmpty()){

            Log.d(TAG, "loadPhotoFromByteArray");
            String cachePath = ImageHelper.cacheImageOnDisk(context, resident.getPhotoByte(),
                    "resident_" + resident.getID() + ".jpg",
                    ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_QUALITY);
            resident.setPhotoCache(cachePath);

        }
        Log.d(TAG, "loadPhotoFromCache:"  + resident.getPhotoCache());
        bitmap = BitmapFactory.decodeFile(resident.getPhotoCache());
        viewHolder.photoView.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Resident getItem(int position){
        return items.get(position);
    }

    public void selectItem(int position){
        itemSelected = position;
    }

    public int getSelectedItem(){
        return itemSelected;
    }

    public void filterItems(String query){
        ArrayList<Resident> filteredItems = new ArrayList<>();
        Log.d(TAG, "items size: " + items.size());
        for (Resident item : ResidentsModel.get().getResidents()) {
            if (item.contains(query))
                filteredItems.add(item);
        }

        Log.d(TAG, "itemsFiltered size: " + filteredItems.size());
        items = filteredItems;
        notifyItemRangeChanged(0, items.size());
        notifyDataSetChanged();
    }

    public void resetItems(){
        items = ResidentsModel.get().getResidents();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView firstnameView;
        public TextView lastnameView;
        public ImageView photoView;
        public LinearLayout imageBorderLayout;
        public RelativeLayout parentLayout;



        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            parentLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.resident_row);
            firstnameView = (TextView) itemLayoutView.findViewById(R.id.text_firstname);
            lastnameView = (TextView) itemLayoutView.findViewById(R.id.text_lastname);
            photoView = (ImageView) itemLayoutView.findViewById(R.id.image_photo);
            imageBorderLayout = (LinearLayout) itemLayoutView.findViewById(R.id.image_border);

        }


    }

}