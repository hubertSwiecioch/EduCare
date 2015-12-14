package com.hswie.educaremobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.Medicine;
import com.hswie.educaremobile.helper.DateTimeConvert;
import com.hswie.educaremobile.helper.ResidentsModel;


/**
 *  hswie
 */
public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    public interface MedicineAdapterCallbacks {
        public void onListItemClick(int position);
    }
    private MedicineAdapterCallbacks medicineAdapterCallbacks;



    private Context context;

    public MedicineAdapter(Context context, MedicineAdapterCallbacks medicineAdapterCallbacks) {
        this.context = context;
        this.medicineAdapterCallbacks = medicineAdapterCallbacks;
    }

    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.medicine_list_item, null);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                medicineAdapterCallbacks.onListItemClick(position);
                return false;
            }
        });

        int medicineSize = ResidentsModel.get().getCurrentResident().getMedicines().size();
        Medicine medicine = ResidentsModel.get().getCurrentResident().getMedicines().get(position);

        viewHolder.titleView.setText(medicine.getName());
        String dateTime = context.getString(R.string.from) + " " + DateTimeConvert.getDate(context, medicine.getStartDate());
        viewHolder.startDateView.setText(dateTime);
        dateTime = context.getString(R.string.medicine_to) + " " + DateTimeConvert.getDate(context, medicine.getEndDate());
        viewHolder.endDateView.setText(dateTime);
        viewHolder.bodyView.setText(context.getString(R.string.medicine_dose) + ": " + medicine.getDose());

        if(position == medicineSize - 1)
            viewHolder.dividerView.setVisibility(View.INVISIBLE);
        else
            viewHolder.dividerView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        try{
            return ResidentsModel.get().getCurrentResident().getMedicines().size();
        } catch (NullPointerException e){
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parentLayout;
        public TextView titleView;
        public TextView startDateView;
        public TextView endDateView;
        public TextView bodyView;
        public ImageView dividerView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            parentLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.item_layout);
            titleView = (TextView) itemLayoutView.findViewById(R.id.item_title);
            startDateView = (TextView) itemLayoutView.findViewById(R.id.item_start_date);
            endDateView = (TextView) itemLayoutView.findViewById(R.id.item_end_date);
            bodyView = (TextView) itemLayoutView.findViewById(R.id.item_body);
            dividerView = (ImageView) itemLayoutView.findViewById(R.id.item_divider);
        }
    }
}
