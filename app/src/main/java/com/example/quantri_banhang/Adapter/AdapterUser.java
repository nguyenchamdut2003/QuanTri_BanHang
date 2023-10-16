package com.example.quantri_banhang.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserViewHolder>{
    private List<UserDTO> listUserDTO;



    public AdapterUser(List<UserDTO> listUserDTO) {
        this.listUserDTO = listUserDTO;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qlyuser, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDTO userDTO = listUserDTO.get(position);
        if (userDTO == null){
            return;
        }
        holder.tv_email.setText("Email: "+ userDTO.getEmail());
        holder.tv_fullname.setText("Fullname: "+ userDTO.getFullname());
    }

    @Override
    public int getItemCount() {
        if (listUserDTO != null){
            return listUserDTO.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_email, tv_fullname;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRefId1 = database.getReference("Users");
            myRefId1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Dữ liệu người dùng được tìm thấy
                        UserDTO user = snapshot.getValue(UserDTO.class);
                        if (user != null) {
                            tv_email.setText("Email:"+user.getEmail());

                            if (user.getFullname() != null) {
                                String fullname = user.getFullname();
                                String fullnameText = "FullName: " + fullname;
                                tv_fullname.setText(fullnameText);
                                tv_fullname.setVisibility(View.VISIBLE); // Hiển thị TextView
                            } else {
                                // Dữ liệu full name chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                                tv_fullname.setText("FullName: chưa điền");
                                tv_fullname.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
