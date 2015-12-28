package com.hswie.educaremobile.carer;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.MessagesAdapter;
import com.hswie.educaremobile.api.dao.CarerMessageRDH;
import com.hswie.educaremobile.api.pojo.CarerMessage;
import com.hswie.educaremobile.dialog.MessageDialog;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.PreferencesManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements MessagesAdapter.MessagesAdapterCallbacks {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    private RecyclerView messagesRV;
    private TextView emptyTV;
    private MessagesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean asyncTaskWorking = false;

    private Context context;

    public static Handler handler;

    private static ArrayList<CarerMessage> messages;
    private final static String TAG = "MessagesFragment";
    private MessageDialog messageDialog;


    public static MessagesFragment newInstance(int page, String title) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "msg = " + msg.toString());
                adapter.notifyDataSetChanged();
            }
        };

        adapter = new MessagesAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });


        emptyTV = (TextView)rootView.findViewById(R.id.text_empty_list);

        messagesRV = (RecyclerView) rootView.findViewById(R.id.list);
        messagesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessagesAdapter(this);
        messagesRV.setAdapter(adapter);
        messagesRV.setItemAnimator(new DefaultItemAnimator());

        checkAdapterIsEmpty();
        resetMessages();

        return rootView;
    }

    public void refreshData() {
        if (!asyncTaskWorking) {
            asyncTaskWorking = true;
            new DownloadMessages().execute();
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

    public void resetMessages(){
        adapter.resetItems();
    }



    public void onListItemClick(int position) {
        confirmMessage(position);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        messageDialog = MessageDialog.newInstance(adapter.getItem(position));
        messageDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.EduCareAppDialog);
        messageDialog.callback = new MessageDialog.DismissCallback(){

            @Override
            public void dismissMessageDialog() {

                Log.d(TAG, "DISMISS");
                refreshData();
            }
        };
        messageDialog.show(fragmentTransaction, "taskDialog");


    }

    @Override
    public void onListItemLongClick(final int  position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(adapter.getItem(position).getTitle());
        alertDialogBuilder.setMessage(getString(R.string.remove_question));
        alertDialogBuilder.setIcon(R.drawable.ic_delete_black_24dp );

        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d(TAG, "removeItem " + adapter.getItem(position).getId());
                if (!asyncTaskWorking) {
                    asyncTaskWorking = true;

                    new RemoveMessage().execute(adapter.getItem(position).getId());
                }

            }
        });


        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void checkAdapterIsEmpty() {
        if (adapter.getItemCount() == 0) {
            emptyTV.setVisibility(View.VISIBLE);
            messagesRV.setVisibility(View.GONE);
        } else {
            emptyTV.setVisibility(View.GONE);
            messagesRV.setVisibility(View.VISIBLE);
        }

        emptyTV.setText(R.string.message_empty);
    }





    private void confirmMessage(int position){
        if (!asyncTaskWorking){
            CarerMessage message = adapter.getItem(position);

            if(!message.isRead()){
                new ConfirmMessage().execute(adapter.getItem(position).getId());
                message.setIsRead(true);
            }
        }
    }

    private class DownloadMessages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            asyncTaskWorking = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            CarerModel.get().setCurrentCarrerMessages();

            return null;
        }

        @Override
        protected void onPostExecute(Void bitmap) {
            adapter.notifyDataSetChanged();

            asyncTaskWorking = false;
            checkAdapterIsEmpty();
            adapter.resetItems();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class ConfirmMessage extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            asyncTaskWorking = true;
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CarerMessageRDH carerMessagesRDH = new CarerMessageRDH();

            try{
                carerMessagesRDH.setIsRead(params[0]);
                Log.d(TAG, "ConfirmMessage doInBackground params[0] = " + params[0]);
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
        }

        @Override
        protected void onPostExecute(Boolean result) {
            asyncTaskWorking = false;
            adapter.resetItems();
            adapter.notifyDataSetChanged();
            checkAdapterIsEmpty();
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private class RemoveMessage extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            asyncTaskWorking = true;
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CarerMessageRDH carerMessagesRDH = new CarerMessageRDH();

            try{
                carerMessagesRDH.removeMessage(params[0]);
                Log.d(TAG, "RemoveMessage doInBackground params[0] = " + params[0]);
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
        }

        @Override
        protected void onPostExecute(Boolean result) {
            asyncTaskWorking = false;
            swipeRefreshLayout.setRefreshing(false);
            refreshData();

        }
    }

    public static ArrayList<CarerMessage> getMessages() {
        return messages;
    }


}
