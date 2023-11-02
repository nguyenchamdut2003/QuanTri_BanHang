package com.example.quantri_banhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;
import com.example.quantri_banhang.actitvity.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.viewholder> {
    Context context;
    ArrayList<UserDTO> usersArrayList;
    public ListChatAdapter(Context context, ArrayList<UserDTO> usersArrayList) {
        this.context=context;
        this.usersArrayList=usersArrayList;
    }
    public void updateList(ArrayList<UserDTO> userList) {
        this.usersArrayList = userList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ListChatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_chat,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.viewholder holder, int position) {

        UserDTO userDTO = usersArrayList.get(position);
        holder.username.setText(userDTO.getFullname());
        holder.userstatus.setText(userDTO.getLastMess()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("iduser",userDTO.getId());
                Log.d("chuongdk", "onClick: "+userDTO.getId());
                intent.putExtra("nameU",userDTO.getFullname());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
