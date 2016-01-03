package com.hswie.educaremobile.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hswie.educaremobile.BuildConfig;
import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DateTimeConvert;
import com.hswie.educaremobile.helper.FamilyModel;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Adapter containing MessageItem list.
 */
public class CarerTasksAdapter extends RecyclerView.Adapter<CarerTasksAdapter.ViewHolder> {

    private static final String TAG = "CarerTasksAdapter";
    private  boolean isFamily;

    public interface CarerTasksAdapterCallbacks {
        public void onListItemClick(int position);
    }

    private CarerTasksAdapterCallbacks carerTasksAdapterCallbacks;
    private Context context;
    private ArrayList<CarerTask> residentTasks;


    public CarerTasksAdapter(CarerTasksAdapterCallbacks carerTasksAdapterCallbacks){
        this.carerTasksAdapterCallbacks = carerTasksAdapterCallbacks;
        isFamily = BuildConfig.IS_FAMILY;
        resetItems();
        Log.d(TAG, "ResidentTasksSize: "  + residentTasks.size());
    }

    @Override
    public CarerTasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.carer_tasks_list_item, null);

        context = parent.getContext();

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CarerTask carerTask = residentTasks.get(position);
        //Log.d(TAG, "CarerTasksSize: " + CarerModel.get().getCurrentCarer().getCarerTasks().size() );


        if(!isFamily) {
            viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carerTasksAdapterCallbacks.onListItemClick(position);
                }
            });
        }

        int substringEnd = 15;
        if(carerTask.getHeader().length() < substringEnd)
            substringEnd = carerTask.getHeader().length();

        String text = carerTask.getHeader().substring(0, substringEnd);
        if(substringEnd == 15)
            text += "...";

        viewHolder.titleView.setText(text);

        substringEnd = 45;
        if(carerTask.getDescription().length() < substringEnd)
            substringEnd = carerTask.getDescription().length();

        text = carerTask.getDescription().substring(0, substringEnd);
        if(substringEnd == 45)
            text += "...";

        viewHolder.messageView.setText(text);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        String messageDate = DateTimeConvert.getDate(context, carerTask.getDate());
        String systemDate = currentDateandTime;

        if(messageDate.equals(systemDate))
            viewHolder.dateView.setText(DateTimeConvert.getTime(context, carerTask.getDate()));
        else
            viewHolder.dateView.setText(DateTimeConvert.getDate(context, carerTask.getDate()));

        if(!carerTask.getIs_done()){
            viewHolder.titleView.setTypeface(null, Typeface.BOLD);
            viewHolder.titleView.setTextColor(context.getResources().getColor(R.color.accent));
            viewHolder.messageView.setTypeface(null, Typeface.BOLD);
            viewHolder.dateView.setTypeface(null, Typeface.BOLD);
        }
        else{
            viewHolder.titleView.setTypeface(null, Typeface.NORMAL);
            viewHolder.titleView.setTextColor(context.getResources().getColor(R.color.text_primary));
            viewHolder.messageView.setTypeface(null, Typeface.NORMAL);
            viewHolder.dateView.setTypeface(null, Typeface.NORMAL);
        }

        if(position == residentTasks.size() - 1)
            viewHolder.dividerView.setVisibility(View.INVISIBLE);
        else
            viewHolder.dividerView.setVisibility(View.VISIBLE);
    }

    public void resetItems(){
        residentTasks = new ArrayList<>();

        if(!isFamily) {
            if (CarerModel.get().getCurrentCarer().getCarerTasks() != null) {
                for (CarerTask carerTask : CarerModel.get().getCurrentCarer().getCarerTasks()) {

                    if (carerTask.getTargetResidentID().equals(ResidentsModel.get().getCurrentResident().getID()))
                        residentTasks.add(carerTask);

                    Log.d(TAG, "getTargetResidentID: " + carerTask.getTargetResidentID());
                    Log.d(TAG, "getCurrentResident: " + ResidentsModel.get().getCurrentResident().getID());
                }
            }
        } else if (isFamily){

            if (ResidentsModel.get().getCurrentResident().getCarerTasks() != null) {
                for (CarerTask carerTask : ResidentsModel.get().getCurrentResident().getCarerTasks()) {

                    if (carerTask.getTargetResidentID().equals(ResidentsModel.get().getCurrentResident().getID()))
                        residentTasks.add(carerTask);

                    Log.d(TAG, "getTargetResidentID: " + carerTask.getTargetResidentID());
                    Log.d(TAG, "getCurrentResident: " + ResidentsModel.get().getCurrentResident().getID());
                }
            }

        }
        notifyDataSetChanged();
    }

    public CarerTask getItem(int position){
        return residentTasks.get(position);
    }

    @Override
    public int getItemCount() {
        return residentTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parentLayout;

        public TextView titleView;
        public TextView dateView;
        public TextView messageView;
        public ImageView dividerView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            parentLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.item_layout);
            titleView = (TextView) itemLayoutView.findViewById(R.id.item_title);
            dateView = (TextView) itemLayoutView.findViewById(R.id.item_date);
            messageView = (TextView) itemLayoutView.findViewById(R.id.item_message);
            dividerView = (ImageView) itemLayoutView.findViewById(R.id.item_divider);
        }
    }
}
