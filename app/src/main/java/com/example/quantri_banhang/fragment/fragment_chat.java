package com.example.quantri_banhang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.Adapter.ListChatAdapter;
import com.example.quantri_banhang.DTO.ChatDTO;
import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_chat extends Fragment {
    String TAG = "fragment_loai";

    FirebaseDatabase database;
    DatabaseReference myRef;
    List<String> userIds;
    ArrayList<UserDTO> listUser;
    ListChatAdapter adapter;
    RecyclerView rcv_listChat;
    private ValueEventListener chatEventListener;
    SearchView sv_chat;
    ArrayList<UserDTO> filteredList;
    public fragment_chat() {
        // Required empty public constructor
    }

    public static fragment_chat newInstance() {
        fragment_chat fragment = new fragment_chat();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        database = FirebaseDatabase.getInstance();
        rcv_listChat = view.findViewById(R.id.rcv_listchat);
        sv_chat = view.findViewById(R.id.sv_chat);
        filteredList = new ArrayList<>();

        userIds = new ArrayList<>();
        listUser = new ArrayList<>();

        adapter = new ListChatAdapter(getContext(),listUser);

        rcv_listChat.setAdapter(adapter);


        sv_chat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList.clear();
                for (UserDTO user : listUser) {
                    if (user.getFullname().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(user);
                    }
                }

                adapter.updateList(filteredList);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    private void getListUserChat() {
        myRef = database.getReference("chats");

        chatEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                userIds.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String roomId = chatSnapshot.getKey();
                    if (roomId != null && roomId.endsWith("admin")) {
                        String userId = roomId.replace("admin", "");
                        if (!userIds.contains(userId)) {
                            userIds.add(userId);
                        }
                        Log.d(TAG, "onDataChange: " + userId);

                        DataSnapshot messagesSnapshot = chatSnapshot.child("messages");
                        ChatDTO lastMessage = null;
                        long latestTimestamp = -1;
                        for (DataSnapshot messageSnapshot : messagesSnapshot.getChildren()) {
                            long currentTimestamp = messageSnapshot.child("timeStamp").getValue(Long.class);
                            if (currentTimestamp > latestTimestamp) {
                                latestTimestamp = currentTimestamp;
                                lastMessage = messageSnapshot.getValue(ChatDTO.class);
                            }
                        }

                        DatabaseReference usersRef = database.getReference("Users");
                        ChatDTO finalLastMessage = lastMessage;
                        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserDTO user = dataSnapshot.getValue(UserDTO.class);
                                if (finalLastMessage != null) {
                                    user.setLastMess(finalLastMessage.getMessage());
                                    user.setLastMessageSenderId(finalLastMessage.getSenderid());
                                }
                                listUser.add(user);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };

        myRef.addValueEventListener(chatEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listUser.clear();
        if (myRef != null && chatEventListener != null) {
            myRef.removeEventListener(chatEventListener);

        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listUser.clear();
        if (myRef != null && chatEventListener != null) {
            myRef.removeEventListener(chatEventListener);

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getListUserChat();
    }
}
