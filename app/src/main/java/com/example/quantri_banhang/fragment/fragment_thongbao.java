package com.example.quantri_banhang.fragment;

import static java.security.AccessController.getContext;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.Adapter.NotificationAdapter;
import com.example.quantri_banhang.DTO.NotificationDTO;
import com.example.quantri_banhang.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class fragment_thongbao extends Fragment {
    private RecyclerView recyclerView;

    private ArrayList<NotificationDTO> notificationsList;

    private NotificationAdapter notificationAdapter;

    public fragment_thongbao() {
    }

    public static fragment_thongbao newInstance() {
        fragment_thongbao fragment = new fragment_thongbao();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thongbao, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewThongBao);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationsList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationsList, getContext());
        recyclerView.setAdapter(notificationAdapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}