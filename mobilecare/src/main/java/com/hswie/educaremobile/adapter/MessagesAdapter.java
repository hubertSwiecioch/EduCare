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


import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.CarerMessage;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DateTimeConvert;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private static final String TAG = "MessagesAdapter";

    public interface MessagesAdapterCallbacks {
        public void onListItemClick(int position);
    }

    private MessagesAdapterCallbacks messagesAdapterCallbacks;
    private Context context;


    public MessagesAdapter(MessagesAdapterCallbacks messagesAdapterCallbacks){
        this.messagesAdapterCallbacks = messagesAdapterCallbacks;

    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.messages_list_item, null);

        context = parent.getContext();

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CarerMessage messageItem = CarerModel.get().getCurrentCarer().getCarerMessages().get(position);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesAdapterCallbacks.onListItemClick(position);
            }
        });

        int substringEnd = 15;
        if(messageItem.getTitle().length() < substringEnd)
            substringEnd = messageItem.getTitle().length();

        String text = messageItem.getTitle().substring(0, substringEnd);
        if(substringEnd == 15)
            text += "...";

        viewHolder.titleView.setText(text);

        viewHolder.sentByView.setText(CarerModel.get().getCarerByID(messageItem.getSentBy()).getFullName());

        substringEnd = 45;
        if(messageItem.getMessage().length() < substringEnd)
            substringEnd = messageItem.getMessage().length();

        text = messageItem.getMessage().substring(0, substringEnd);
        if(substringEnd == 45)
            text += "...";

        viewHolder.messageView.setText(text);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        String messageDate = DateTimeConvert.getDate(context, messageItem.getSendDate());
        String systemDate = currentDateandTime;

        if(messageDate.equals(systemDate))
            viewHolder.dateView.setText(DateTimeConvert.getTime(context, messageItem.getSendDate()));
        else
            viewHolder.dateView.setText(DateTimeConvert.getDate(context, messageItem.getSendDate()));

        if(!messageItem.isRead()){
            viewHolder.titleView.setTypeface(null, Typeface.BOLD);
            viewHolder.titleView.setTextColor(context.getResources().getColor(R.color.accent));
            viewHolder.sentByView.setTypeface(null, Typeface.BOLD);
            viewHolder.messageView.setTypeface(null, Typeface.BOLD);
            viewHolder.dateView.setTypeface(null, Typeface.BOLD);
        }
        else{
            viewHolder.titleView.setTypeface(null, Typeface.NORMAL);
            viewHolder.titleView.setTextColor(context.getResources().getColor(R.color.text_primary));
            viewHolder.sentByView.setTypeface(null, Typeface.NORMAL);
            viewHolder.messageView.setTypeface(null, Typeface.NORMAL);
            viewHolder.dateView.setTypeface(null, Typeface.NORMAL);
        }

        if(position == CarerModel.get().getCurrentCarer().getCarerMessages().size() - 1)
            viewHolder.dividerView.setVisibility(View.INVISIBLE);
        else
            viewHolder.dividerView.setVisibility(View.VISIBLE);
    }

    public CarerMessage getItem(int position){
        return CarerModel.get().getCurrentCarer().getCarerMessages().get(position);
    }


    public void resetItems(){
        Log.d(TAG, "resetItems");
        CarerModel.get().getCurrentCarer().setCarerMessages(CarerModel.get().getCurrentCarer().getCarerMessages());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return CarerModel.get().getCurrentCarer().getCarerMessages().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parentLayout;

        public TextView titleView;
        public TextView dateView;
        public TextView messageView;
        public TextView sentByView;
        public ImageView dividerView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            parentLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.item_layout);
            titleView = (TextView) itemLayoutView.findViewById(R.id.item_title);
            dateView = (TextView) itemLayoutView.findViewById(R.id.item_date);
            messageView = (TextView) itemLayoutView.findViewById(R.id.item_message);
            sentByView = (TextView) itemLayoutView.findViewById(R.id.item_sentby);
            dividerView = (ImageView) itemLayoutView.findViewById(R.id.item_divider);
        }
    }
}
