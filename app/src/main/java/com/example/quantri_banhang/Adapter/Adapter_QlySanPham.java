package com.example.quantri_banhang.Adapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quantri_banhang.DTO.DTO_QlySanPham;
import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;

import java.util.List;
public class Adapter_QlySanPham extends RecyclerView.Adapter<Adapter_QlySanPham.SanPhamViewHolder>{


    private List<DTO_QlySanPham> mlist;
    Context context;
    public Adapter_QlySanPham(List<DTO_QlySanPham> mlist,Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
      DTO_QlySanPham dtoQlySanPham = mlist.get(position);
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

    public class SanPhamViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItemsp;
        private TextView tvItemtensp;
        private TextView tvItemgiasp;
        private TextView tvItemthongtinsp;
        private TextView tvItemsoluongsp;
        private TextView tvItemloaisp;
        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemsp=itemView.findViewById(R.id.imgItemsp);
            tvItemtensp=itemView.findViewById(R.id.tvItemtensp);
            tvItemgiasp=itemView.findViewById(R.id.tvItemgiasp);
            tvItemthongtinsp=itemView.findViewById(R.id.tvItemthongtinsp);
            tvItemsoluongsp=itemView.findViewById(R.id.tvItemsoluongsp);
            tvItemloaisp=itemView.findViewById(R.id.tvItemloaisp);
        }
    }
}
