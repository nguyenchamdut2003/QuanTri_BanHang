package com.example.quantri_banhang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.Adapter.ListChatAdapter;
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
        View viewok = inflater.inflate(R.layout.fragment_chat, container, false);
        database = FirebaseDatabase.getInstance();
        rcv_listChat = viewok.findViewById(R.id.rcv_listchat);



        userIds = new ArrayList<>();
        listUser = new ArrayList<>();
        getListUserChat();
        adapter = new ListChatAdapter(getContext(),listUser);

        rcv_listChat.setAdapter(adapter);

        return viewok;
    }

    private void getListUserChat() {
        myRef = database.getReference("chats");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String roomId = chatSnapshot.getKey();
                    if (roomId != null && roomId.endsWith("admin")) {
                        // Trích xuất userId từ roomId
                        String userId = roomId.replace("admin", "");
                        userIds.add(userId);
                        Log.d(TAG, "onDataChange: " + userId);
                    }
                }
                DatabaseReference usersRef = database.getReference("Users");
                for (String userId : userIds) {
                    usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserDTO user = dataSnapshot.getValue(UserDTO.class);
                            listUser.add(user);

                            Log.d(TAG, "onDataChange: " + user.getFullname());
                            Log.d(TAG, "onDataChange: "+user.getId());
                            // Làm gì đó với thông tin người dùng tại đây
                            // Ví dụ: hiển thị thông tin lên giao diện
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi ở đây
                            Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                        }
                    });
                }

                // Dựa vào userIds, bạn có thể truy vấn thêm thông tin của mỗi user và hiển thị
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
