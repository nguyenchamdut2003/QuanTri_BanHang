package com.example.quantri_banhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quantri_banhang.DTO.CategoryDTO;
import com.example.quantri_banhang.R;
import com.example.quantri_banhang.fragment.fragment_loai;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterCategory extends BaseAdapter {
    String TAG = "adaptercategory";
    ArrayList<CategoryDTO> list;
    Context context;

    OnClick onClick;

    public AdapterCategory(ArrayList<CategoryDTO> list, Context context, OnClick onClick) {
        this.list = list;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        CategoryDTO categoryDTO = list.get(i);
        return categoryDTO;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewok;
        if (view == null){
            viewok = View.inflate(viewGroup.getContext(), R.layout.item_addcategory, null);
        }else{
            viewok = view;
        }
        CategoryDTO categoryDTO = list.get(i);

        TextView tvname = viewok.findViewById(R.id.tv_tenloai);
        tvname.setText(categoryDTO.getName());

        ImageView btnsua = viewok.findViewById(R.id.img_sualoai);
        ImageView btnxoa = viewok.findViewById(R.id.img_xoaloai);

        String id = categoryDTO.getIdCate();
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickUpdate(categoryDTO);
            }
        });
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickDelete(categoryDTO);
            }
        });
        return viewok;
    }

     public interface OnClick{
        void onClickUpdate(CategoryDTO categoryDTO);

        void onClickDelete(CategoryDTO categoryDTO);
     }
}
