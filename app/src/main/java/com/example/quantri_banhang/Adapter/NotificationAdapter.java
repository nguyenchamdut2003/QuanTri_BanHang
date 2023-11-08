package com.example.quantri_banhang.Adapter;
import static java.security.AccessController.getContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quantri_banhang.DTO.NotificationDTO;
import com.example.quantri_banhang.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationDTO> notificationList;
    private Context context;

    public NotificationAdapter(List<NotificationDTO> notificationList,Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongbao, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationDTO notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        if (notificationList != null) {
            return notificationList.size();
        } else {
            return 0; // Hoặc giá trị mặc định tùy thuộc vào trường hợp của bạn
        }
    }
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImageView;
        private TextView tieudeTextView;
        private TextView noidungTextView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            tieudeTextView = itemView.findViewById(R.id.tieude_id);
            noidungTextView = itemView.findViewById(R.id.noidung_id);

        }

        public void bind(NotificationDTO notification) {
            // Truy cập trực tiếp các trường trong NotificationDTO
            tieudeTextView.setText("Tiêu đề: " + notification.getTitle());
            noidungTextView.setText("noi dung"+notification.getContent());


        }
    }


}