package com.example.quantri_banhang.Adapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quantri_banhang.DTO.DTO_QlySanPham;
import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;
import com.example.quantri_banhang.actitvity.QLySanPhamActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
public class Adapter_QlySanPham extends RecyclerView.Adapter<Adapter_QlySanPham.SanPhamViewHolder>{


    private List<DTO_QlySanPham> mlist;
    Context context;
    updateSanPham updateSanPham;






    public Adapter_QlySanPham(List<DTO_QlySanPham> mlist,Context context,updateSanPham updateSanPham) {
        this.mlist = mlist;
        this.context = context;
        this.updateSanPham=updateSanPham;

    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        DTO_QlySanPham dtoQlySanPham= mlist.get(position);
        if (dtoQlySanPham == null){
            return;
        }
        Glide.with(context).load(Uri.parse(dtoQlySanPham.getImage())).into(holder.imgItemsp);
        holder.tvItemtensp.setText(""+ dtoQlySanPham.getName());
        holder.tvItemgiasp.setText(""+  dtoQlySanPham.getPrice());
        holder.tvItemthongtinsp.setText(""+  dtoQlySanPham.getInformation());
        holder.tvItemsoluongsp.setText(""+  dtoQlySanPham.getNumber());
        holder.tvItemloaisp.setText(""+ dtoQlySanPham.getCategory());

    }

    @Override
    public int getItemCount() {
        if (mlist != null){
            return mlist.size();
        }
        return 0;
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private ImageView imgItemsp;
        private TextView tv_menubam;
        private TextView tvItemtensp;
        private TextView tvItemgiasp;
        private TextView tvItemthongtinsp;
        private TextView tvItemsoluongsp;
        private TextView tvItemloaisp;
        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemsp = itemView.findViewById(R.id.imgItemsp);
            tvItemtensp = itemView.findViewById(R.id.tvItemtensp);
            tvItemgiasp = itemView.findViewById(R.id.tvItemgiasp);
            tvItemthongtinsp = itemView.findViewById(R.id.tvItemthongtinsp);
            tvItemsoluongsp = itemView.findViewById(R.id.tvItemsoluongsp);
            tvItemloaisp = itemView.findViewById(R.id.tvItemloaisp);

            tv_menubam = itemView.findViewById(R.id.tv_menubam);
            tv_menubam.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            ShowPopupmenu(v);
        }
        private void ShowPopupmenu(View view){

            PopupMenu popupMenu= new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item1) {
            int position = getAdapterPosition();
            DTO_QlySanPham dtoQlySanPham = mlist.get(position);
                if (item1.getItemId() == R.id.action_popup_edit) {
                    updateSanPham.update1(dtoQlySanPham);
                    return true;
                } else if (item1.getItemId() == R.id.action_popup_delete) {
                    // Xử lý khi người dùng chọn action_popup_delete
                    updateSanPham.delete1(dtoQlySanPham);
                    return true;
                } else {
                    return false;
                }



        }


    }
    public interface updateSanPham {
        void update1(DTO_QlySanPham dto_qlySanPham);
        void delete1(DTO_QlySanPham dto_qlySanPham);
    }

}
