//package com.hswie.educaremobile.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.hswie.educaremobile.api.pojo.Resident;
//import com.hswie.educaremobile.helper.ResidentsModel;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
///**
// * Residents ListView's adapter.
// */
//public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ViewHolder> {
//    private static final String TAG = "ResidentAdapter";
//    private ViewHolder.ResidentNotificationClick notificationClick;
////    public static AnimationDrawable frameAnimation;
//
//    public interface ResidentAdapterCallbacks {
//        void onListItemClick(int position);
//    }
//
//    int itemSelected = -1;
//    private ArrayList<Resident> items = new ArrayList<>();
//    private Context context;
//    private ResidentAdapterCallbacks residentAdapterCallbacks;
//
//    public ResidentAdapter(ResidentAdapterCallbacks residentAdapterCallbacks, ViewHolder.ResidentNotificationClick notificationClick) {
//        this.residentAdapterCallbacks = residentAdapterCallbacks;
//        items = ResidentsModel.get().getResidents();
//        this.notificationClick = notificationClick;
//    }
//
//    @Override
//    public ResidentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.resident_list_item, null);
//
//        context = parent.getContext();
//
//
//        return new ViewHolder(itemLayoutView, notificationClick);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        final Resident resident = items.get(position);
//
//        viewHolder.parentLayout.setOnClickListener(
//                new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        residentAdapterCallbacks.onListItemClick(position);
//                    }
//                });
//
//        viewHolder.firstnameView.setText(resident.getFirstName());
//        viewHolder.lastnameView.setText(resident.getLastName());
//
//
//        Bitmap bitmap;
//        if(resident.getPhotoCache() == null || resident.getPhotoCache().isEmpty()){
//            bitmap = DrawableConverter.drawableToBitmap(context.getResources().getDrawable(R.drawable.noavatar));
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, ImageHelper.AVATAR_QUALITY, stream);
//
//            String cachePath = ImageHelper.cacheImageOnDisk(context, stream.toByteArray(),
//                    "resident_" + resident.getIdTerminalUser() + ".jpg",
//                    ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_SIZE, ImageHelper.AVATAR_QUALITY);
//            resident.setPhotoCache(cachePath);
//            try {
//                stream.close();
//                stream = null;
//            }
//            catch(IOException e){
//                Log.e(TAG, "IOException e = ", e);
//            }
//            bitmap = null;
//        }
//
//        bitmap = BitmapFactory.decodeFile(resident.getPhotoCache());
//        viewHolder.photoView.setImageBitmap(bitmap);
////        ImageManager.get().displayImage("file://" + resident.getPhotoCache(), viewHolder.photoView);
//
//        if(itemSelected == position){
//            viewHolder.parentLayout.setBackgroundResource(R.color.primary_light);
//            viewHolder.firstnameView.setTextColor(context.getResources().getColor(R.color.menu_text_primary));
//            viewHolder.lastnameView.setTextColor(context.getResources().getColor(R.color.menu_text_primary));
//            //viewHolder.notificationView.setColorFilter(context.getResources().getColor(R.color.window));
//
//            if(viewHolder.addressView != null){
//                viewHolder.addressView.setTextColor(context.getResources().getColor(R.color.menu_text_primary));
//                viewHolder.cityView.setTextColor(context.getResources().getColor(R.color.menu_text_primary));
//            }
//
//            if(viewHolder.callView != null)
//                viewHolder.callView.setBackgroundResource(R.drawable.icon_call_light);
//        }
//        else{
//            viewHolder.parentLayout.setBackgroundResource(R.color.indigo_light);
//            viewHolder.firstnameView.setTextColor(context.getResources().getColor(R.color.primary));
//            viewHolder.lastnameView.setTextColor(context.getResources().getColor(R.color.primary));
//            //viewHolder.notificationView.clearColorFilter();
//
//            if(viewHolder.addressView != null){
//                viewHolder.addressView.setTextColor(context.getResources().getColor(R.color.primary));
//                viewHolder.cityView.setTextColor(context.getResources().getColor(R.color.primary));
//            }
//
//            if(viewHolder.callView != null)
//                viewHolder.callView.setBackgroundResource(R.drawable.icon_call);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public Resident getItem(int position){
//        return items.get(position);
//    }
//
//    public void selectItem(int position){
//        itemSelected = position;
//    }
//
//    public int getSelectedItem(){
//        return itemSelected;
//    }
//
//    public void filterItems(String query){
//        ArrayList<Resident> filteredItems = new ArrayList<>();
//        Log.d(TAG, "items size: " + items.size());
//        for (Resident item : ResidentsModel.get().getResidents()) {
//            if (item.contains(query))
//                filteredItems.add(item);
//        }
//
//        Log.d(TAG, "itemsFiltered size: " + filteredItems.size());
//        items = filteredItems;
//        notifyItemRangeChanged(0, items.size());
//        notifyDataSetChanged();
//    }
//
//    public void resetItems(){
//        items = ResidentsModel.get().getResidents();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        public TextView firstnameView;
//        public TextView lastnameView;
//        public TextView addressView;
//        public TextView cityView;
//        public ImageView photoView;
//        public ImageView notificationView;
//        public Button callView;
//        public LinearLayout callLayout;
//        public LinearLayout imageBorderLayout;
//        public RelativeLayout parentLayout;
//        public ResidentNotificationClick mListener;
//
//
//        public ViewHolder(View itemLayoutView, ResidentNotificationClick listener) {
//            super(itemLayoutView);
//
//            mListener = listener;
//
//            parentLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.resident_row);
//            firstnameView = (TextView) itemLayoutView.findViewById(R.id.text_firstname);
//            lastnameView = (TextView) itemLayoutView.findViewById(R.id.text_lastname);
//            addressView = (TextView) itemLayoutView.findViewById(R.id.text_address);
//            cityView = (TextView) itemLayoutView.findViewById(R.id.text_city);
//            photoView = (ImageView) itemLayoutView.findViewById(R.id.image_photo);
//            notificationView = (ImageView) itemLayoutView.findViewById(R.id.image_type);
//            callView = (Button) itemLayoutView.findViewById(R.id.button_call);
//            callLayout = (LinearLayout) itemLayoutView.findViewById(R.id.layout_call);
//            imageBorderLayout = (LinearLayout) itemLayoutView.findViewById(R.id.image_border);
//
//
//            notificationView.setOnClickListener(this);
//            notificationView.setBackgroundResource(R.drawable.animation_notifications_red );
////            frameAnimation = (AnimationDrawable) notificationView.getBackground();
//
//
//        }
//
//        @Override
//        public void onClick(View view) {
//
//            mListener.showNavigateToNotification(getPosition());
//
//        }
//
//        public static interface ResidentNotificationClick {
//
//            public void showNavigateToNotification(int position);
//
//        }
//    }
//
//}