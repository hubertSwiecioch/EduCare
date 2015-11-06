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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.ImageHelper;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Residents ListView's adapter.
 */
public class CarerAdapter extends RecyclerView.Adapter<CarerAdapter.ViewHolder> {
    private static final String TAG = "ResidentAdapter";


//    public static AnimationDrawable frameAnimation;

    public interface CarerAdapterCallbacks {
        void onListItemClick(int position);
    }

    int itemSelected = -1;
    private ArrayList<Carer> items = new ArrayList<>();
    private Context context;
    private CarerAdapterCallbacks carerAdapterCallbacks;

    public CarerAdapter(CarerAdapterCallbacks residentAdapterCallbacks) {
        this.carerAdapterCallbacks = residentAdapterCallbacks;
        items = CarerModel.get().getCarers();

    }

    @Override
    public CarerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.carer_list_item, null);

        context = parent.getContext();


        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Carer carer = items.get(position);

        viewHolder.parentLayout.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        carerAdapterCallbacks.onListItemClick(position);
                    }
                });

        viewHolder.firstnameView.setText(carer.getFullName());

        viewHolder.callView.setBackgroundResource(R.drawable.icon_call);
        viewHolder.callView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "clickCall");
            }
        });



        Bitmap bitmap;
        if(carer.getPhotoCache() == null || carer.getPhotoCache().isEmpty()){

            Log.d(TAG, carer.getPhoto());
            bitmap =BitmapFactory.decodeByteArray(carer.getPhotoByte() , 0, carer.getPhotoByte().length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, ImageHelper.AVATAR_QUALITY, stream);

            String cachePath = ImageHelper.cacheImageOnDisk(context, stream.toByteArray(),
                    "resident_" + carer.getID() + ".jpg",
                    ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_QUALITY);
            carer.setPhotoCache(cachePath);
            try {
                stream.close();
                stream = null;
            }
            catch(IOException e){
                Log.e(TAG, "IOException e = ", e);
            }
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeFile(carer.getPhotoCache());
        viewHolder.photoView.setImageBitmap(bitmap);
//        ImageManager.get().displayImage("file://" + resident.getPhotoCache(), viewHolder.photoView);




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Carer getItem(int position){
        return items.get(position);
    }

    public void selectItem(int position){
        itemSelected = position;
    }

    public int getSelectedItem(){
        return itemSelected;
    }

    public void filterItems(String query){
        ArrayList<Carer> filteredItems = new ArrayList<>();
        Log.d(TAG, "items size: " + items.size());
        for (Carer item : CarerModel.get().getCarers()) {
            if (item.contains(query))
                filteredItems.add(item);
        }

        Log.d(TAG, "itemsFiltered size: " + filteredItems.size());
        items = filteredItems;
        notifyItemRangeChanged(0, items.size());
        notifyDataSetChanged();
    }

    public void resetItems(){
        items = CarerModel.get().getCarers();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public Button callView;
        public TextView firstnameView;
        public TextView lastnameView;
        public ImageView photoView;
        public LinearLayout imageBorderLayout;
        public RelativeLayout parentLayout;



        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            parentLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.resident_row);
            firstnameView = (TextView) itemLayoutView.findViewById(R.id.text_firstname);
            callView = (Button) itemLayoutView.findViewById(R.id.button_call);
            lastnameView = (TextView) itemLayoutView.findViewById(R.id.text_lastname);
            photoView = (ImageView) itemLayoutView.findViewById(R.id.image_photo);
            imageBorderLayout = (LinearLayout) itemLayoutView.findViewById(R.id.image_border);

        }


    }

}