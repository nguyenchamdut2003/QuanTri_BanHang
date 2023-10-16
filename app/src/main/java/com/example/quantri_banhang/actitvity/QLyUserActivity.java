package com.example.quantri_banhang.actitvity;

import static com.facebook.share.widget.ShareDialog.show;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.Adapter.AdapterUser;
import com.example.quantri_banhang.DTO.DTO_QlySanPham;
import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QLyUserActivity extends AppCompatActivity {
    private RecyclerView rcvUsers;
    private AdapterUser mAdapterUser;
    private List<UserDTO> mListUsers;

    private SearchView sv_searchUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qly_user);

        ImageView imgback = findViewById(R.id.img_backuser);
        sv_searchUser = findViewById(R.id.sv_searchUser);
        sv_searchUser.clearFocus();


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rcvUsers = findViewById(R.id.rcv_qlyuser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUsers.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvUsers.addItemDecoration(dividerItemDecoration);

        mListUsers = new ArrayList<>();
        mAdapterUser = new AdapterUser(mListUsers);

        rcvUsers.setAdapter(mAdapterUser);

        getListUsers("");
        sv_searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getListUsers(s);
                return true;
            }
        });



    }

    public void getListUsers(String key){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("Users");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);

                       mListUsers.add(userDTO);
             
                }
                mAdapterUser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLyUserActivity.this, "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });


    }




}