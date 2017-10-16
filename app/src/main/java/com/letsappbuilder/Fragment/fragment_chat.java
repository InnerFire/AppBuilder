package com.letsappbuilder.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.letsappbuilder.FCM_Chat.Constants;
import com.letsappbuilder.FCM_Chat.Message;
import com.letsappbuilder.FCM_Chat.ThreadAdapter;
import com.letsappbuilder.R;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Savaliya Imfotech on 16-12-2016.
 */
public class fragment_chat extends Fragment implements View.OnClickListener {
    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private RelativeLayout rootChat;
    //Recyclerview objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //ArrayList of messages to store the thread messages
    private ArrayList<Message> messages;

    //Button to send new message on the thread
    private ImageView buttonSend;

    //EditText to send new message on the thread
    private EditText editTextMessage;
    AppPrefs appPrefs;
    Common common;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chat_room, container, false);

        //Initializing recyclerview
        appPrefs = new AppPrefs(getActivity());
        common = new Common(this.getActivity());
        rootChat = (RelativeLayout) v.findViewById(R.id.root_fragment_chat);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Initializing message arraylist
        messages = new ArrayList<>();
        adapter = new ThreadAdapter(getActivity(), messages, appPrefs.getUserId());
        recyclerView.setAdapter(adapter);

        //Calling function to fetch the existing messages on the thread
        fetchMessages();

        //initializing button and edittext
        buttonSend = (ImageView) v.findViewById(R.id.buttonSend);
        editTextMessage = (EditText) v.findViewById(R.id.editTextMessage);

        //Adding listener to button
        buttonSend.setOnClickListener(this);

        //Creating broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    //Getting message data
                    String name = intent.getStringExtra("name");
                    String message = intent.getStringExtra("message");
                    String id = intent.getStringExtra("id");
                  //  Log.e("###123=>", name + message + id);
                    //processing the message to add it in current thread
                    processMessage(name, message, id);
                }
            }
        };


        return v;
    }

    //  ####################   Token Register Api   ###################
    AsyncHttpClient callTokenRegisterAPIRequest;

    public void CallFetchApi(String appid) {
        if (callTokenRegisterAPIRequest != null) {
            callTokenRegisterAPIRequest.cancelRequests(getActivity(), true);
        }
        callTokenRegisterAPIRequest = new AsyncHttpClient();
        callTokenRegisterAPIRequest.post("http://fadootutorial.com/FcmExample/Chat_fetch_messages.php", RequestFetchMessage(appid), new RequestToken_result());
    }

    public RequestParams RequestFetchMessage(String appid) {
        RequestParams params = new RequestParams();

        params.put("appid", appid);

        return params;
    }


    public class RequestToken_result extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
           // Log.e("$$$", "On Success");
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
              //  Log.e("***********", "response is" + str);

                if (str != null) {
                    JSONObject res = new JSONObject(str);
                    JSONArray thread = res.getJSONArray("messages");
                    for (int i = 0; i < thread.length(); i++) {
                        JSONObject obj = thread.getJSONObject(i);
                        String userId = obj.getString("userid");
                      //  Log.e("&&&", userId);
                        String message = obj.getString("message");
                        String name = obj.getString("name");
                        String sentAt = obj.getString("sentat");
                        Message messagObject = new Message(userId, message, sentAt, name);
                        messages.add(messagObject);
                    }

                    adapter = new ThreadAdapter(getActivity(), messages, appPrefs.getUserId());
                    recyclerView.setAdapter(adapter);
                    scrollToBottom();

                }
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
          //  Log.e("###", "Something went wrong");
            common.hideProgressDialog();

        }


    }


    //  ####################   Send Messages Api   ###################  //
    AsyncHttpClient callSendMessageAPIRequest;

    public void CallSendMessageApi(String uid, String name, String email, String message, String appid) {
        if (callSendMessageAPIRequest != null) {
            callSendMessageAPIRequest.cancelRequests(getActivity(), true);
        }
        callSendMessageAPIRequest = new AsyncHttpClient();
        callSendMessageAPIRequest.post("http://fadootutorial.com/FcmExample/Chat_SendMessages.php", RequestSendMessage(uid, name, email, message, appid), new RequestSendMessages_result());
    }

    public RequestParams RequestSendMessage(String uid, String name, String email, String message, String appid) {
        RequestParams params = new RequestParams();

        params.put("users_id", uid);
        params.put("name", name);
        params.put("email", email);
        params.put("message", message);
        params.put("app_id", appid);

        return params;
    }


    public class RequestSendMessages_result extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
          //  Log.e("$$$", "On Success");
            try {
                String str = new String(responseBody, "UTF-8");
              //  Log.e("***********", "response is" + str);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
          //  Log.e("###", "Something went wrong");
        }


    }

    //This method will fetch all the messages of the thread
    private void fetchMessages() {
        if (common.isConnected()) {
            common.showProgressDialog(getString(R.string.progress_getting));
            CallFetchApi(appPrefs.getCHAT_APP_ID());
        } else {
            Snackbar snackbar = Snackbar.make(rootChat, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
            snackbar.show();
        }
    }

    //Processing message to add on the thread
    private void processMessage(String name, String message, String id) {
        Message m = new Message(id, message, getTimeStamp(), name);
        messages.add(m);
        scrollToBottom();
    }

    //This method will send the new message to the thread
    private void sendMessage() {
        final String message = editTextMessage.getText().toString().trim();
        String name = appPrefs.getNAME();
        String sentAt = getTimeStamp();

        Message m = new Message(appPrefs.getUserId(), message, sentAt, name);
        messages.add(m);
        scrollToBottom();
        editTextMessage.setText("");
        if (common.isConnected())
            CallSendMessageApi(appPrefs.getUserId(), name, appPrefs.getMAIL(), message, appPrefs.getCHAT_APP_ID());
        else {
            Snackbar snackbar = Snackbar.make(rootChat, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
            snackbar.show();
        }
    }

    //method to scroll the recyclerview to bottom
    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }

    //This method will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //Registering broadcast receivers
    @Override
    public void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }


    //Unregistering receivers
    @Override
    public void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    //Sending message onclick
    @Override
    public void onClick(View v) {
        if (v == buttonSend) {
            if (!editTextMessage.getText().toString().isEmpty())
                sendMessage();
        }
    }

}
